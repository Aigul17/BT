package com.example.bt.activities




import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.webkit.WebView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.example.bt.*
import com.example.bt.R
import com.example.bt.fragments.User
import com.google.android.gms.common.util.CollectionUtils.listOf
import me.zhanghai.android.materialprogressbar.MaterialProgressBar
import java.lang.Exception

abstract class BaseActivity : AppCompatActivity() {
    private var lastBackPressedAt = 0L
    private val LOGIN_REQ_CODE = 111
    private var t: Toast? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        t = Toast.makeText(this, R.string.press_again_to_exit, Toast.LENGTH_SHORT)
    }


    fun toast(msg: String, time: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(this, msg, time).show()
    }

    fun toast(res: Int, time: Int = Toast.LENGTH_SHORT) {
        toast(getString(res), time)
    }

    fun logout() {
        confirm(this, R.string.confirm_logout) {
            AuthUI.getInstance().signOut(this)
            mUser = User()
            finish()
        }
    }

    protected fun confirmBeforeExit() {
        val now = System.currentTimeMillis()
        if (now - lastBackPressedAt <= 1000) {
            t?.cancel()
            super.onBackPressed()
        } else {
            t?.show()
            lastBackPressedAt = now
        }
    }

    fun showAbout() {
        val view = LayoutInflater.from(this).inflate(R.layout.fragment_about, null)
        AlertDialog.Builder(this).setView(view).setPositiveButton(R.string.ok, null).create().show()
        view.findViewById<WebView>(R.id.aboutWebView).apply {
            loadUrl("file:///android_asset/about.html")
        }
    }

    fun loginUser() {
        if (this is LoginActivity) {
            findViewById<MaterialProgressBar>(R.id.progressBar).visibility = View.INVISIBLE
        }
        startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder()
            .setAvailableProviders(listOf(
                AuthUI.IdpConfig.GoogleBuilder().build(),
                AuthUI.IdpConfig.FacebookBuilder().build(),
                AuthUI.IdpConfig.EmailBuilder().build(),
                AuthUI.IdpConfig.PhoneBuilder().build())).build(), LOGIN_REQ_CODE)

    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == LOGIN_REQ_CODE) {
            if (this is LoginActivity) {
                findViewById<MaterialProgressBar>(R.id.progressBar).visibility = View.VISIBLE
            }
            if (resultCode == Activity.RESULT_OK) {
                cUser = FirebaseAuth.getInstance().currentUser
                if (cUser != null) {
                    val instance = FirebaseFirestore.getInstance()
                    instance.firestoreSettings = fireSettings
                    val query = instance.collection("users").document(cUser?.uid + "").get()
                    query.addOnSuccessListener { snapshot ->
                        mUser = snapshot.toObject(User::class.java) ?: User()
                        val role = mUser.role
                        if (role.isEmpty()) {
                            startActivity(Intent(baseContext, LoginActivity::class.java))
                        } else when (role) {
                            "admin" -> startActivity(Intent(baseContext, TransportControllerActivity::class.java))
                            "passenger" -> startActivity(Intent(baseContext, PassengerActivity::class.java))
                            "driver" -> startActivity(Intent(baseContext, BusDriverActivity::class.java))
                            else -> startActivity(Intent(baseContext, LoginActivity::class.java))
                        }
                        finish()
                    }.addOnFailureListener { e ->
                        e.printStackTrace()
                        finish()
                    }
                } else finish()
            } else {
                System.out.println("Here are result $resultCode")
                confirm(this, R.string.error_login,
                    { finish() }, { finish() })
            }
        }
    }

    fun delayed(ms: Long = 1000, task: () -> Unit) {
        Handler().postDelayed({
            task.invoke()
        }, ms)
    }

    protected open fun startServices() {}
    protected open fun stopServices() {}

    override fun onStart() {
        super.onStart()
        try {
            startServices()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    override fun onStop() {
        super.onStop()
        try {
            stopServices()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    override fun onRestart() {
        super.onRestart()
        try {
            startServices()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }
}
