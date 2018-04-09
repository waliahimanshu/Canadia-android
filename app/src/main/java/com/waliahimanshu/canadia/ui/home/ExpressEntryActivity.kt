package com.waliahimanshu.canadia.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.CheckedTextView
import android.widget.LinearLayout
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.waliahimanshu.canadia.ui.R
import com.waliahimanshu.canadia.ui.login.LoginActivity
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_item_detail.*
import kotlinx.android.synthetic.main.activity_item_list.*
import kotlinx.android.synthetic.main.item_list.*
import kotlinx.android.synthetic.main.menu_filter_layout.*
import javax.inject.Inject


class ExpressEntryActivity : AppCompatActivity(), ExpressEntryContract.View, GoogleApiClient.OnConnectionFailedListener {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private var twoPane: Boolean = false
    private lateinit var database: DatabaseReference
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
            Toast.makeText(this, "You are signed out from you account ${currentUser?.email} ", LENGTH_LONG).show()
            this.finish()
        }

        setUpFilters()
        setupRecyclerView()
    }

    override fun setPresenter(presenter: ExpressEntryContract.Presenter) {
        expressEntryPresenter = presenter
    }

    override fun onStart() {
        super.onStart()
        expressEntryPresenter.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        expressEntryPresenter.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        googleApiAvailability.makeGooglePlayServicesAvailable(this)
    }

    override fun showData(itemList: HashMap<String, ArrayList<ExpressEntryModel>>) {
        if (itemList.isEmpty()) {
            showEmptyState()

        } else {
            recyler_view.visibility = VISIBLE
            no_result.visibility = GONE
            expressEntryAdapter.showData(itemList)
        }
    }

    override fun showProgressBar(show: Boolean) {
        progressBar?.visibility = View.GONE
    }

    private fun loadDataByCRS(string: String): ArrayList<ExpressEntryModel> {
        val itemList: ArrayList<ExpressEntryModel> = java.util.ArrayList()

        database = database.child("ee_crs").child("2018")
        val queryRef = database.orderByKey()


        queryRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for (item: DataSnapshot in dataSnapshot.children) {
                    val invitationModel: ExpressEntryModel? = item.getValue(ExpressEntryModel::class.java)
                    if (invitationModel != null) {
                        itemList.add(invitationModel)
                    }
                }
                if (itemList.isNotEmpty()) {
                    progressBar?.visibility = View.GONE
                }
            }

            override fun onCancelled(dataSnapshot: DatabaseError?) {
                TODO("logging db fetch failed")
            }
        })
        return itemList
    }

    override fun handleDatabaseLoadError(message: String?) {
        Toast.makeText(baseContext, message, LENGTH_LONG).show()
    }

    override fun showEmptyState() {
        recyler_view.visibility = GONE
        no_result.visibility = VISIBLE

    }

    private fun setupRecyclerView() {
        expressEntryAdapter = ExpressEntryAdapter(this, twoPane)
        recyler_view.adapter = expressEntryAdapter
        val dividerItemDecoration = DividerItemDecoration(recyler_view.context, LinearLayout.VERTICAL)
        recyler_view.addItemDecoration(dividerItemDecoration)

        val linearLayoutManager = LinearLayoutManager(baseContext);
        linearLayoutManager.reverseLayout = true;
        linearLayoutManager.stackFromEnd = true;
        recyler_view.layoutManager = linearLayoutManager
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_menu_filter, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_filter -> {
                drawerLayout.openDrawer(GravityCompat.END)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setUpFilters() {

        year_2018.setOnClickListener {
            if (year_2018.isChecked) {
                disable(year_2018)
                expressEntryPresenter.removeDataFor("2018")

            } else {
                enabled(year_2018)
                expressEntryPresenter.loadDataFor("2018")

            }
        }

        year_2017.setOnClickListener {
            if (year_2017.isChecked) {
                disable(year_2017)
                expressEntryPresenter.removeDataFor("2017")


            } else {
                enabled(year_2017)
                expressEntryPresenter.loadDataFor("2017")

            }
        }

        year_2016.setOnClickListener {
            if (year_2016.isChecked) {
                disable(year_2016)
                expressEntryPresenter.removeDataFor("2016")


            } else {
                enabled(year_2016)
                expressEntryPresenter.loadDataFor("2016")


            }
        }

        year_2015.setOnClickListener {
            if (year_2015.isChecked) {
                disable(year_2015)
                expressEntryPresenter.removeDataFor("2015")


            } else {
                enabled(year_2015)
                expressEntryPresenter.loadDataFor("2015")

            }
        }
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        Log.d("ExpressEntryActivity", "onConnectionFailed:" + connectionResult.errorMessage)
        Toast.makeText(this, connectionResult.errorMessage, Toast.LENGTH_SHORT).show()
    }

    private fun enabled(checkedTextView: CheckedTextView) {
        checkedTextView.isChecked = true
        checkedTextView.setCheckMarkDrawable(R.drawable.ic_check_black_24dp)
    }

    private fun disable(checkedTextView: CheckedTextView) {
        checkedTextView.isChecked = false
        checkedTextView.setCheckMarkDrawable(R.drawable.ic_check_transparent_24dp)
        checkedTextView.setTextColor(resources.getColor(R.color.grey_500))
    }
}