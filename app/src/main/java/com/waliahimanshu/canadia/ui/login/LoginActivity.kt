package com.waliahimanshu.canadia.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.waliahimanshu.canadia.ui.R
import com.waliahimanshu.canadia.ui.home.ExpressEntryActivity


class LoginActivity : AppCompatActivity(), GoogleApiClient.OnConnectionFailedListener {


    private val tag = LoginActivity::class.java.simpleName
    private var googleApiClient: GoogleApiClient? = null
    private var firebaseAuth: FirebaseAuth? = null
    private lateinit var signInButton: SignInButton


    companion object {
        private const val RC_SIGN_IN = 1

        fun getLaunchIntent(context: Context): Intent {
            return Intent(context, LoginActivity::class.java)
        }
    }


    /**
     *  Check for existing Google Sign In account, if the user is already signed in
        the GoogleSignInAccount will be non - null.
     */
    override fun onStart() {
        super.onStart()
        val account = GoogleSignIn.getLastSignedInAccount(this)
        if (account != null) {
            startActivity(ExpressEntryActivity.getLaunchIntent(this))
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        signInButton = findViewById(R.id.activity_login_google_button)
        signInButton.setSize(SignInButton.SIZE_STANDARD)

        val textView = signInButton.getChildAt(0) as TextView
        textView.setText(R.string.signup_with_google)
        signInButton.setOnClickListener(
                {
                    signIn()
                })

        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(
                        getString(R.string.default_web_client_id))
                .requestEmail().build()

        googleApiClient = GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso).build()

        // Initialize FirebaseAuth
        firebaseAuth = FirebaseAuth.getInstance()

    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        Log.d(tag, "onConnectionFailed:" + connectionResult.errorMessage)
        Toast.makeText(this, connectionResult.errorMessage, Toast.LENGTH_SHORT).show()
    }

    private fun signIn() {
        val signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient)
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            handleGoogleSignInResult(result)
        }
    }

    private fun handleGoogleSignInResult(result: GoogleSignInResult) {
        if (result.isSuccess) {
            // Google Sign In was successful, authenticate with Firebase
            val account = result.signInAccount
            Toast.makeText(this, "You are signed in as ${account?.email}", Toast.LENGTH_SHORT).show()

            if (account != null) {
                firebaseAuthWithGoogle(account)
            }
        } else {

            Toast.makeText(this, R.string.error_google_sign_in, Toast.LENGTH_SHORT).show()
        }
    }


    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        Log.d(tag, "firebaseAuthWithGoogle:" + acct.id!!)

        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        firebaseAuth!!.signInWithCredential(credential)
                .addOnCompleteListener { task: Task<AuthResult> ->
                    // If sign in fails, display a message to the user. If sign in succeeds
                    // the auth state listener will be notified and logic to handle the
                    // signed in user can be handled in the listener.
                    if (!task.isSuccessful) {
                        Log.w(tag, "signInWithCredential", task.exception)
                        Toast.makeText(this, task.exception?.message, Toast.LENGTH_SHORT).show()
                    } else {
                        startActivity(ExpressEntryActivity.getLaunchIntent(this))
                        finish()
                    }
                }
    }


}
