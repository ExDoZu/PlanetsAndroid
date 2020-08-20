package exdo.planets

import android.app.Application
val MYTAG = "ZUEV"
class App: Application() {

    override fun onCreate() {
        super.onCreate()
        System.loadLibrary("native-lib")
    }
}