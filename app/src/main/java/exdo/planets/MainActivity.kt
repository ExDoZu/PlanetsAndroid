package exdo.planets

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bttn_2d.setOnClickListener {
            startActivity(Intent(this, Activity_2d::class.java))
        }
        bttn_3d.setOnClickListener {
            startActivity(Intent(this, GameOfLifeActivity::class.java))
        }
        bttn_4d.setOnClickListener {
            startActivity(Intent(this, Circle::class.java ))
        }
        // Example of a call to a native method
        //sample_text.text = factorial(20).toString()//stringFromJNI()
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */


}
