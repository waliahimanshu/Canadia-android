package com.waliahimanshu.canadia.ui

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {
    private val TAG = "MyFirebaseMsgService"


    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        Log.d(TAG, "From: " + remoteMessage?.from)
        super.onMessageReceived(remoteMessage)
    }
}
