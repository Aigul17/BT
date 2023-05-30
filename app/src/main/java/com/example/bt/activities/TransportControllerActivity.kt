package com.example.bt.activities

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.widget.TextView
import com.example.bt.ACTION_HANDLE_PENDING_REQUEST
import com.example.bt.EXTRA_TEACHER_ID
import com.example.bt.R
import com.example.bt.adapters.SimpleBusAdapter
import com.example.bt.fragments.AllBusesFragment
import com.example.bt.fragments.ChangeRegKeyFragment
import com.example.bt.fireSettings
import com.example.bt.fragments.PendingRequestsFragment
import com.example.bt.fragments.PendingUsersFragment
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.navigation.NavigationView
import com.google.firebase.firestore.FirebaseFirestore
import com.example.bt.adapters.PendingBusAdapter
import com.example.bt.adapters.PendingPassengerAdapter
import com.example.bt.fragments.*
import kotlinx.android.synthetic.main.activity_home.*

class TransportControllerActivity : HomeActivity(), NavigationView.OnNavigationItemSelectedListener {

    // fragments we need
    lateinit var approvedBusesFragment: AllBusesFragment
    lateinit var approvedPasengersFragment: PassengerListFragment
    lateinit var pendingBusesFragment: PendingUsersFragment
    lateinit var pendingPassengersFragment: PendingUsersFragment
    lateinit var pendingRequestsFragment: PendingRequestsFragment

    lateinit var pendingBusesAdapter: SimpleBusAdapter
    lateinit var pendingTeacherAdapter: PendingPassengerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideWaiting()
        nav_view.inflateMenu(R.menu.menu_transport_controller)
        nav_view.menu.findItem(R.id.menu_pending_requests).actionView = TextView(this).also {
            it.gravity = Gravity.CENTER
        }
        nav_view.menu.findItem(R.id.menu_online_buses).actionView = TextView(this).also {
            it.gravity = Gravity.CENTER
        }
        if (savedInstanceState == null) {
            showOnlineBuses()
            nav_view.menu.findItem(R.id.menu_online_buses).isChecked = true
        }

        // create fragments
        approvedBusesFragment = AllBusesFragment()
        approvedPassengersFragment = PassengerListFragment()

        pendingBusesAdapter = PendingBusAdapter(FirestoreRecyclerOptions.Builder<Any>().setQuery(FirebaseFirestore.getInstance()
            .collection("users").whereEqualTo("role", "driver")
            .whereEqualTo("approved", false), Any::class.java)
//                .setLifecycleOwner(this)
            .build(), { bus ->
            deleteBus(bus)
        }, { bus ->
            // approve the bus driver
            FirebaseFirestore.getInstance().document("users/${bus["uid"]?.toString()
                ?: "NA"}").update(mapOf("approved" to true)).addOnCompleteListener { task ->
                if (!task.isSuccessful) task.exception?.printStackTrace()
            }
        })

        pendingBusesFragment = PendingUsersFragment().also {
            it.setAdapter(pendingBusesAdapter)
        }

        pendingTeacherAdapter = PendingPassengerAdapter(
            FirestoreRecyclerOptions.Builder<Any>().setQuery(FirebaseFirestore.getInstance()
                .also { it.firestoreSettings = fireSettings }
                .collection("pending"), Any::class.java)
//                        .setLifecycleOwner(this)
                .build(), { approveTeacher(it) }, { teacher ->
                // Start details activity
                val intent = Intent(this, PassengerDetailsActivity::class.java)
                intent.action = ACTION_HANDLE_PENDING_REQUEST
                intent.putExtra(EXTRA_TEACHER_ID, teacher["uid"]?.toString())
                this.startActivity(intent)
            })
        pendingPassengersFragment = PendingUsersFragment().also {
            it.setAdapter(pendingTeacherAdapter)
        }

        pendingRequestsFragment = PendingRequestsFragment()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_change_reg_key -> {
                ChangeRegKeyFragment().also {
                    it.isCancelable = false
                }.show(supportFragmentManager, "ShowInputDialog")
            }
            R.id.menu_all_passengers -> supportFragmentManager.beginTransaction().replace(R.id.fragment_container, approvedPassengersFragment).commit()
            R.id.menu_all_buses -> supportFragmentManager.beginTransaction().replace(R.id.fragment_container, approvedBusesFragment).commit()
            R.id.menu_pending_requests -> supportFragmentManager.beginTransaction().replace(R.id.fragment_container, pendingRequestsFragment).commit()
        }
        return super.onNavigationItemSelected(item)
    }

    override fun setMenuBadges() {
        super.setMenuBadges()
        val count = pendingBusesAdapter.itemCount + pendingPassengerAdapter.itemCount
        (nav_view.menu.findItem(R.id.menu_pending_requests).actionView as TextView).text = if (count == 0) "" else count.toString()
    }

    private fun deleteBus(bus: Map<*, *>) {
        // Delete this bus
        FirebaseFirestore.getInstance().document("users/${bus["uid"]
            ?: "NA"}").delete().addOnCompleteListener { task ->
            if (!task.isSuccessful)
                task.exception?.printStackTrace()
        }
    }

    override fun onResume() {
        super.onResume()
        // listen data changes
        pendingPassengerAdapter.startListening()
        pendingBusesAdapter.startListening()

        // observer
        pendingPassengerAdapter.registerAdapterDataObserver(observer)
        pendingBusesAdapter.registerAdapterDataObserver(observer)
    }

    override fun onPause() {
        super.onPause()
        pendingpassengerAdapter.unregisterAdapterDataObserver(observer)
        pendingBusesAdapter.unregisterAdapterDataObserver(observer)
    }

    private fun approvePassenger(passenger: Map<*, *>) {
        // Start approval operation
        val batch = FirebaseFirestore.getInstance().batch()
        // set user as approved
        batch.update(FirebaseFirestore.getInstance().collection("users")
            .document(passenger["uid"]?.toString() ?: "NA"), "approved", true)
        batch.delete(FirebaseFirestore.getInstance().document("pending/${passenger["uid"]?.toString()
            ?: "NA"}"))
        // Get all buses
        if (passenger["buses"] is List<*>) {
            FirebaseFirestore.getInstance().collection("users").whereEqualTo("role", "driver")
                .whereEqualTo("approved", true).get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful)
                        for (document in task.result.documents) {
                            val myList = arrayListOf<Any?>()
                            val subscribers = document["subscribers"]
                            System.out.println("Here are the subscribers : $subscribers ${subscribers?.javaClass}")
                            if (subscribers is ArrayList<*>) {
                                subscribers.remove(passenger["uid"])
                                System.out.println("Here are the results : $subscribers ${passenger}")
                                if ((passenger["buses"] as ArrayList<*>).contains(document["uid"]))
                                    myList.add(passenger["uid"])
                                myList.addAll(subscribers)
                                System.out.println("Here are the My Lists : $myList")

                            }
                            // Update the bus
                            FirebaseFirestore.getInstance().document("users/${document["uid"]}")
                                .update(mapOf("subscribers" to myList))
                        }
                }
        }

        // Commit batch operation.
        batch.commit().addOnCompleteListener { task ->
            if (!task.isSuccessful)
                task.exception?.printStackTrace()
        }
    }
}

class PassengerListFragment {

}
