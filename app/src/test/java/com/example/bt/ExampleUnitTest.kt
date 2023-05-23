package com.example.bt



/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

import com.example.bt.activities.LoginActivity
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.AuthResult

class ExampleUnitTest {

    // Mock FirebaseAuth
    @Mock
    private lateinit var firebaseAuth: FirebaseAuth

    // Mocked email and password for testing
    private val email = "test@example.com"
    private val password = "password123"

    // Test LoginActivity instance
    private lateinit var loginActivity: LoginActivity

    @Before
    fun setup() {
        // Initialize mocks
        MockitoAnnotations.openMocks(this)

        // Create LoginActivity instance with mocked FirebaseAuth
        loginActivity = LoginActivity()
        loginActivity.firebaseAuth = firebaseAuth
    }

    @Test
    fun testLoginWithEmailAndPassword_Successful() {
        // Mock successful login
        val authResult = AuthResult(null, null)
        `when`(firebaseAuth.signInWithEmailAndPassword(email, password)).thenReturn(authResult)

        // Call the login function
        val result = loginActivity.loginWithEmailAndPassword(email, password)

        // Check if login was successful
        assertEquals(true, result)
    }

    @Test
}