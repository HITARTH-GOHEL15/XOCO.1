package com.app.movieexplorer2.app

import android.app.Application
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MovieExplorerApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseAuth.getInstance()
    }
}