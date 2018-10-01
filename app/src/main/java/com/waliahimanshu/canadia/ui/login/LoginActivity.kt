package com.waliahimanshu.canadia.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.common.SignInButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.waliahimanshu.canadia.ui.R
import com.waliahimanshu.canadia.ui.home.ExpressEntryActivity
import dagger.android.AndroidInjection
import javax.inject.Inject


class LoginActivity : AppCompatActivity(), LoginContract.View {

    private val tag = LoginActivity::class.java.simpleName
    private var firebaseAuth: FirebaseAuth? = null
    private lateinit var signInButton: SignInButton

    @Inject
    lateinit var presenter: LoginContract.Presenter

    companion object {
        private const val RC_SIGN_IN = 1
        fun getLaunchIntent(context: Context): Intent {
            return Intent(context, LoginActivity::class.java)
        }
    }

    /**
     *  Check for existing Google Sign In account, if the user is already signed in
     *  the GoogleSignInAccount will be non - null.
     */
    override fun onStart() {
        super.onStart()
        presenter.onStart()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        AndroidInjection.inject(this)
        signInButton = findViewById(R.id.activity_login_google_button)
        signInButton.setSize(SignInButton.SIZE_STANDARD)

        val textView = signInButton.getChildAt(0) as TextView
        textView.setText(R.string.signup_with_google)
        signInButton.setOnClickListener {
            presenter.signIn()

        }
        presenter.init()
    }

    override fun startActivityForResult(signInIntent: Intent?) {
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
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
            Log.w(tag, "GoogleSignInResult +" + result.status.statusCode, null)

            if (result.status.statusCode == 7) {
                Toast.makeText(this, "Google login failed, check your internet connections", Toast.LENGTH_SHORT).show()

            } else {
                Toast.makeText(this, R.string.error_google_sign_in, Toast.LENGTH_SHORT).show()

            }
        }
    }


    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        Log.d(tag, "firebaseAuthWithGoogle:" + acct.id)

        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)

        firebaseAuth?.signInWithCredential(credential)?.addOnCompleteListener {
            if (it.isSuccessful) {
                Log.d(tag, "signInWithCredential:success")
                launchMainActivity()
            } else {
                Log.w(tag, "signInWithCredential:failure", it.exception)
                Toast.makeText(this, "signInWithCredential failed ${it.exception?.message}", Toast.LENGTH_SHORT).show()

            }
        }
    }

    @Override
    override fun launchMainActivity() {
        startActivity(ExpressEntryActivity.getLaunchIntent(this))
        finish()
    }
}

