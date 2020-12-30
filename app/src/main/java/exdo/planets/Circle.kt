package exdo.planets

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.sqrt
import kotlin.math.abs


class Circle : AppCompatActivity() {
    lateinit var sensorManager: SensorManager
    lateinit var sensorAccel: Sensor
    lateinit var sensorLinAccel: Sensor
    lateinit var sensorGravity: Sensor

    var valuesAccel = FloatArray(3)

    lateinit var listener: SensorEventListener
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(CircleView(this))

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        sensorAccel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);


        listener = object : SensorEventListener {
            override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
            override fun onSensorChanged(event: SensorEvent) {
                when (event.sensor.type) {
                    Sensor.TYPE_ACCELEROMETER -> {
                        for (i in 0..2) {
                            valuesAccel[i] = event.values[i]
                        }
                    }
                }
            }
        }

    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(listener);
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(
            listener, sensorAccel,
            SensorManager.SENSOR_DELAY_NORMAL
        )
    }

    inner class CircleView(context: Context) : View(context) {

        val screenWidth: Int
        val screenHeight: Int
        lateinit var line: Array<FloatArray>
        val p: Paint


        init {
            val rect: Rect = Rect()
            getWindowVisibleDisplayFrame(rect)
            screenWidth = rect.width()
            screenHeight = rect.height()
            p = Paint()
            line = Array(screenHeight / 2, { floatArrayOf(screenWidth / 2f, screenHeight / 2f) })


        }

        override fun onDraw(canvas: Canvas?) {
            //Log.d(MYTAG, "w $fieldWidth   h $fieldHeight\n                        $screenWidth   ${(screenWidth / fieldWidth )}")
            canvas?.drawARGB(80, 102, 204, 255)
            p.color = Color.RED
            canvas?.drawCircle(
                line[screenHeight / 2 - 1][0],
                line[screenHeight / 2 - 1][1],
                screenWidth / 25f,
                p
            )
            p.color = Color.BLACK
            for (i in 0..screenHeight / 2 - 2) {
                canvas?.drawCircle(line[i][0], line[i][1], 2f, p)
            }

            recalculate()
            invalidate()

        }

        fun recalculate() {
//            Log.d(
//                "ZUEV",
//                valuesAccel[0].toString() + '\t' + valuesAccel[1].toString() + '\t' + valuesAccel[2].toString() + "\n\t\t\t\t\t\t\t\t\t\t\t\t"
//                        + valuesLinAccel[0].toString() + '\t' + valuesLinAccel[1].toString() + '\t' + valuesLinAccel[2].toString() + "\n\t\t\t\t\t\t\t\t\t\t\t\t"
//                        + valuesGravity[0].toString() + '\t' + valuesGravity[1].toString() + '\t' + valuesGravity[2].toString() + ' '
//            )

            if (sqrt(
                    Math.pow(
                        line[screenHeight / 2 - 1][0] + valuesAccel[1] - screenWidth / 2.0,
                        2.0
                    ) +
                            Math.pow(
                                line[screenHeight / 2 - 1][1] + valuesAccel[0] - screenHeight / 2.0,
                                2.0
                            )
                )
                > screenHeight / 2.0
            ) {
                val bvx = line[screenHeight / 2 - 1][0] - screenWidth / 2.0 + valuesAccel[1]
                val bvy = line[screenHeight / 2 - 1][1] - screenHeight / 2.0 + valuesAccel[0]
                var cosa = bvx / sqrt(bvx * bvx + bvy * bvy)
                //Log.d("ZUEV", "cos = " + cosa.toString())
                if (cosa < -1) {
                    cosa = -1.0
                } else if (cosa > 1) {
                    cosa = 1.0
                }

                var sina = sqrt(1 - cosa * cosa)
                if (line[screenHeight / 2 - 1][1] + valuesAccel[0] < screenHeight / 2) {
                    sina = -sina
                }
                //Log.d("ZUEV", "sin = " + sina.toString())
                line[screenHeight / 2 - 1][0] =
                    ((screenHeight / 2.0) * cosa).toFloat() + screenWidth / 2.0f
                line[screenHeight / 2 - 1][1] =
                    ((screenHeight / 2.0) * sina).toFloat() + screenHeight / 2.0f
//                Log.d(
//                    "ZUEV",
//                    line[screenHeight - 1][0].toString() + line[screenHeight - 1][1].toString()
//                )

            } else {
                line[screenHeight / 2 - 1][0] += valuesAccel[1]
                line[screenHeight / 2 - 1][1] += valuesAccel[0]
            }


            for (i in screenHeight / 2 - 2  downTo 1) {
                val vx = line[i + 1][0] - line[i-1][0]
                val vy = line[i + 1][1] - line[i-1][1]
                if (sqrt(vx * vx + vy * vy) >= 1f) {
                    val dy2 = vy * vy / (vx * vx + vy * vy)

                    var dx = sqrt(1 - dy2)
                    var dy = sqrt(dy2)
                    if (line[i-1][0] > line[i + 1][0]) {
                        dx = -dx
                    }
                    if (line[i-1][1] > line[i + 1][1]) {
                        dy = -dy
                    }
//                    Log.d(
//                        "ZUEV",
//                        sqrt(dx * dx + dy * dy).toString()
//                    )
                    //if(sqrt(Math.pow((line[i + 1][0] - dx -screenWidth/2).toDouble(), 2.0)+Math.pow((line[i + 1][1] - dy -screenHeight/2).toDouble(), 2.0))<=i){
                    line[i][0] = line[i + 1][0] - dx
                    line[i][1] = line[i + 1][1] - dy
                    //}

                }

            }

            for (i in 1..screenHeight / 2 - 2) {
                val vx = -line[i - 1][0] + line[i+1][0]
                val vy = -line[i - 1][1] + line[i+1][1]
                if (sqrt(vx * vx + vy * vy) >= 1f) {
                    val dy2 = vy * vy / (vx * vx + vy * vy)

                    var dx = sqrt(1 - dy2)
                    var dy = sqrt(dy2)
                    if (line[i+1][0] < line[i - 1][0]) {
                        dx = -dx
                    }
                    if (line[i+1][1] < line[i - 1][1]) {
                        dy = -dy
                    }
//                    Log.d(
//                        "ZUEV",
//                        sqrt(dx * dx + dy * dy).toString()
//                    )
                    //if(sqrt(Math.pow((line[i + 1][0] - dx -screenWidth/2).toDouble(), 2.0)+Math.pow((line[i + 1][1] - dy -screenHeight/2).toDouble(), 2.0))<=i){
                    line[i][0] = line[i - 1][0] + dx
                    line[i][1] = line[i - 1][1] + dy
                    //}

                }

            }


        }
    }
}