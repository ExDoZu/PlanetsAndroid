package exdo.planets

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_settings2_d.*
import java.lang.Exception

class Settings2D : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings2_d)

        switchDrawPath.isChecked =
            getSharedPreferences("PlanetsSettings", Context.MODE_PRIVATE).getBoolean(
                "drawPath",
                false
            )
        switchDrawPath.setOnCheckedChangeListener { compoundButton, b ->
            getSharedPreferences("PlanetsSettings", Context.MODE_PRIVATE).edit()
                .putBoolean("drawPath", switchDrawPath.isChecked).apply()
        }

        val getpref = getSharedPreferences("PlanetsSettings", Context.MODE_PRIVATE)
        xEditText.setText(getpref.getFloat("X", 0f).toString())
        yEditText.setText(getpref.getFloat("Y", 0f).toString())
        xspeedEditText.setText(getpref.getFloat("XSpeed", 0f).toString())
        yspeedEditText.setText(getpref.getFloat("YSpeed", 0f).toString())
        weightEditText.setText(getpref.getFloat("Weight", 50f).toString())
        radiusEditText.setText(getpref.getFloat("Radius", 20f).toString())

        createPlanetButton.setOnClickListener {
            try {
                val backIntent = Intent()
                backIntent.putExtra("X", xEditText.text.toString().toFloat())
                backIntent.putExtra("Y", yEditText.text.toString().toFloat())
                backIntent.putExtra("XSpeed", xspeedEditText.text.toString().toFloat())
                backIntent.putExtra("YSpeed", yspeedEditText.text.toString().toFloat())
                backIntent.putExtra("Weight", weightEditText.text.toString().toFloat())
                backIntent.putExtra("Radius", radiusEditText.text.toString().toFloat())
                setResult(Activity.RESULT_OK, backIntent)
                finish()
            } catch (e: Exception) {
                Toast.makeText(this, "Incorrect data", Toast.LENGTH_LONG).show()
            }
        }
        setAsDefaultButton.setOnClickListener {
            try {
                val preferences =
                    getSharedPreferences("PlanetsSettings", Context.MODE_PRIVATE).edit()
                preferences.putFloat("X", xEditText.text.toString().toFloat())
                preferences.putFloat("Y", yEditText.text.toString().toFloat())
                preferences.putFloat("XSpeed", xspeedEditText.text.toString().toFloat())
                preferences.putFloat("YSpeed", yspeedEditText.text.toString().toFloat())
                preferences.putFloat("Weight", weightEditText.text.toString().toFloat())
                preferences.putFloat("Radius", radiusEditText.text.toString().toFloat())
                preferences.apply()

            } catch (e: Exception) {
                Toast.makeText(this, "Incorrect data", Toast.LENGTH_LONG).show()
            }
        }
    }
}
