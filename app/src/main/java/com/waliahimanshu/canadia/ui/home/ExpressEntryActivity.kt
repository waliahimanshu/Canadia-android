package com.waliahimanshu.canadia.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.animation.AnimationUtils
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import android.widget.Toast.LENGTH_SHORT
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.waliahimanshu.canadia.ui.R
import com.waliahimanshu.canadia.ui.login.LoginActivity
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_item_detail.*
import kotlinx.android.synthetic.main.activity_item_list.*
import kotlinx.android.synthetic.main.item_list.*
import kotlinx.android.synthetic.main.menu_filter_layout.*
import javax.inject.Inject


class ExpressEntryActivity : AppCompatActivity(), ExpressEntryContract.View, GoogleApiClient.OnConnectionFailedListener {

    override fun setToolbarTitle(year: String) {
    }

    private var twoPane: Boolean = false
    private lateinit var expressEntryAdapter: ExpressEntryAdapter
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var mGoogleApiClient: GoogleApiClient
    private var currentUser: FirebaseUser? = null
    private var googleApiAvailability: GoogleApiAvailability = GoogleApiAvailability.getInstance()

    @Inject
    lateinit var expressEntryPresenter: ExpressEntryContract.Presenter

    companion object {
        fun getLaunchIntent(context: Context): Intent {
            return Intent(context, ExpressEntryActivity::class.java)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_list)
        firebaseAuth = FirebaseAuth.getInstance()
        currentUser = firebaseAuth.currentUser
        googleApiAvailability.makeGooglePlayServicesAvailable(this)
        mGoogleApiClient = GoogleApiClient.Builder(baseContext)
                .enableAutoManage(this, this).addApi(Auth.GOOGLE_SIGN_IN_API).build()

        AndroidInjection.inject(this)
        setSupportActionBar(toolbar)

        if (item_detail_container != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            twoPane = true
        }

        sign_out.setOnClickListener {
            currentUser?.photoUrl
            currentUser?.displayName
            firebaseAuth.signOut()
            Auth.GoogleSignInApi.signOut(mGoogleApiClient)
            startActivity(LoginActivity.getLaunchIntent(this))
            Toast.makeText(this, "You are signed out from you account ${currentUser?.email} ", LENGTH_SHORT).show()
            this.finish()
        }
    }

    override fun onStart() {
        super.onStart()
        expressEntryPresenter.loadData();
    }

    override fun setPresenter(presenter: ExpressEntryContract.Presenter) {
        expressEntryPresenter = presenter
    }

    override fun onDestroy() {
        super.onDestroy()
        expressEntryPresenter.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        googleApiAvailability.makeGooglePlayServicesAvailable(this)
    }

    override fun loadInitCurrentYearData(dataSet: ArrayList<ExpressEntryModel>) {
        if (dataSet.isEmpty()) {
            showEmptyState()

        } else {
            recyler_view.visibility = VISIBLE
            no_result.visibility = GONE
            setupRecyclerView(dataSet)
            runLayoutAnimation(recyler_view)
        }
    }

    override fun showProgressBar(show: Boolean) {
        progressBar?.visibility = View.GONE
    }

    override fun handleDatabaseLoadError(message: String?) {
        Toast.makeText(baseContext, message, LENGTH_LONG).show()
    }

    override fun showEmptyState() {
        recyler_view.visibility = GONE
        no_result.visibility = VISIBLE

    }

    private fun setupRecyclerView(dataSet: ArrayList<ExpressEntryModel>) {
        val linearLayoutManager = LinearLayoutManager(baseContext);
        expressEntryAdapter = ExpressEntryAdapter(this, twoPane, dataSet)

//        linearLayoutManager.reverseLayout = true
//        linearLayoutManager.stackFromEnd = true
        recyler_view.layoutManager = linearLayoutManager
        recyler_view.adapter = expressEntryAdapter

    }

    private fun runLayoutAnimation(recyclerView: RecyclerView) {
        val context = recyclerView.context
        val loadLayoutAnimation =
                AnimationUtils.loadLayoutAnimation(context, R.anim.layout_fall_down)

        recyclerView.layoutAnimation = loadLayoutAnimation
        recyclerView.adapter?.notifyDataSetChanged()
        recyclerView.scheduleLayoutAnimation()
    }


    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        Log.d("ExpressEntryActivity", "onConnectionFailed:" + connectionResult.errorMessage)
        Toast.makeText(this, connectionResult.errorMessage, Toast.LENGTH_SHORT).show()
    }
}
