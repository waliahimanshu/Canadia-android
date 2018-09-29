package com.waliahimanshu.firebase

import com.google.firebase.auth.FirebaseAuth

class FirebaseApiWrapper {

    fun getFireBaseAuthInstance(): FirebaseAuth? = FirebaseAuth.getInstance()

}
