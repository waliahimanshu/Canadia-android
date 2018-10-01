package com.waliahimanshu.canadia.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.common.SignInButton
import com.waliahimanshu.canadia.ui.R
import com.waliahimanshu.canadia.ui.home.ExpressEntryActivity
import dagger.android.AndroidInjection
import javax.inject.Inject


class LoginActivity : AppCompatActivity(), LoginContract.View {
    private lateinit var signInButton: SignInButton

    @Inject
    lateinit var presenter: LoginContract.Presenter

    companion object {
        private const val RC_SIGN_IN = 1
        fun getLaunchIntent(context: Context): Intent {
            return Intent(context, LoginActivity::class.java)
        }
    }

    override fun onStart() {
        super.onStart()
        presenter.onStart()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        AndroidInjection.inject(this)
        initSignInButton()

        presenter.init()
        signInButton.setOnClickListener {
            presenter.signIn()

        }
    }

    override fun startActivityForResult(signInIntent: Intent?) {
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        presenter.handleGoogleSignInResult(data, requestCode)
    }


    override fun launchMainActivity() {
        startActivity(ExpressEntryActivity.getLaunchIntent(this))
        finish()
    }

    override fun showNetworkFailureError() {
        Toast.makeText(this, R.string.network_failure_error, Toast.LENGTH_SHORT).show()
    }

    override fun showGenericError() {
        Toast.makeText(this, R.string.error_google_sign_in, Toast.LENGTH_SHORT).show()
    }

    private fun initSignInButton() {
        signInButton = findViewById(R.id.activity_login_google_button)
        signInButton.setSize(SignInButton.SIZE_STANDARD)
        val textView = signInButton.getChildAt(0) as TextView
        textView.setText(R.string.signup_with_google)
    }
}

