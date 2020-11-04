package com.androdude.githubtaskapp.UI

import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.androdude.githubtaskapp.DATA.Repository
import com.androdude.githubtaskapp.InternetBroadCast
import com.androdude.githubtaskapp.R
import com.androdude.githubtaskapp.UI.Fragments.CommitListViewFragment
import com.androdude.githubtaskapp.ViewModel.MainViewModel
import com.androdude.githubtaskapp.ViewModel.MainViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var repository: Repository
    private lateinit var mainViewModelFactory: MainViewModelFactory
    lateinit var mainViewModel: MainViewModel
    private lateinit var mNetworkReceiver : InternetBroadCast

    private lateinit var alertDialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        repository = Repository()
       mainViewModelFactory = MainViewModelFactory(repository)
        mainViewModel = ViewModelProvider(this,mainViewModelFactory).get(MainViewModel::class.java)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.newNavHostFragment) as NavHostFragment
        val navController = navHostFragment.navController

        mNetworkReceiver = InternetBroadCast()
        registerNetworkBroadcastForNougat()
        noInternetDialog()
        checkInternet()

    }

    private fun checkInternet() {
        mNetworkReceiver.isInternetAvaible.observe(this, Observer {
            if (it) {
                alertDialog.dismiss()
                mainViewModel.getAllCommitDetails()
            } else {
                alertDialog.show()
            }
        })
    }


    private fun noInternetDialog()
    {
        alertDialog = MaterialAlertDialogBuilder(this)
            .setTitle("You are disconnected")
            .setMessage("Please enable mobile data or Wifi")
            .setCancelable(false)
            .setNegativeButton("Quit") { dialog, which ->
                finish()
            }
            .setPositiveButton("Retry") { dialog, which ->
                MainScope().launch {
                    delay(500L)
                    checkInternet()
                }
            }
            .show()
    }

    private fun registerNetworkBroadcastForNougat() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            registerReceiver(
                mNetworkReceiver,
                IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
            )
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            registerReceiver(
                mNetworkReceiver,
                IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
            )
        }
    }

    protected fun unregisterNetworkChanges() {
        try {
            unregisterReceiver(mNetworkReceiver)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterNetworkChanges()
    }





}