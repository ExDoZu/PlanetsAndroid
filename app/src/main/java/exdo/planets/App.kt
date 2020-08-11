package exdo.planets

import android.app.Application

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        System.loadLibrary("native-lib")
    }
}