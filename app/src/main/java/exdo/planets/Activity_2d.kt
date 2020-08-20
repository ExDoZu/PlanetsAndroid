package exdo.planets


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.graphics.drawable.VectorDrawable
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import kotlin.math.pow
import kotlin.math.sqrt


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class Activity_2d : Activity() {
    //val drawView = DrawView(this)
    lateinit var planets: Array<Planet>
    var countOfPlanets: Short = 0
    var drawPath = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(DrawView(this))

        planets = Array(20, { Planet() })
    }

    override fun onResume() {
        super.onResume()
        drawPath = getSharedPreferences("PlanetsSettings", Context.MODE_PRIVATE).getBoolean(
            "drawPath",
            false
        )
    }

    inner class DrawView(context: Context) : View(context) {
        val p: Paint
        val bp: Paint
        val tp: Paint


        var drawPointX: Float
        var drawPointY: Float
        var redraw: Boolean
        var myScale: Float
        var lastDistance: Float
        var xBias: Float
        var yBias: Float
        var lastX: Float
        var lastY: Float
        val displayWidth: Float
        val displayHeight: Float

        val bttnPause: MyButton
        val bttnSettings: MyButton

        init {

            p = Paint()
            p.setColor(Color.RED)
            // толщина линии = 10
            p.setStrokeWidth(10.0f)

            bp = Paint()
            bp.setColor(Color.YELLOW)
            bp.setStrokeWidth(10.0f)

            tp = Paint()
            tp.color = Color.BLACK
            bp.strokeWidth = 1f
            tp.textSize = 15f


            drawPointX = 0f
            drawPointY = 0f
            redraw = false
            myScale = 1f
            lastDistance = 0f
            xBias = 0f
            yBias = 0f
            lastX = 0f
            lastY = 0f
            val rect: Rect = Rect()
            getWindowVisibleDisplayFrame(rect)
            displayHeight = rect.height().toFloat()
            displayWidth = rect.width().toFloat()

            bttnPause = MyButton(
                BitmapFactory.decodeResource(resources, R.drawable.pausebttn),
                90f,
                5f,
                97f,
                15f,
                displayWidth.toInt(),
                displayHeight.toInt()
            )
            bttnSettings = MyButton(
                BitmapFactory.decodeResource(resources, R.drawable.setbttn),
                90f,
                20f,
                97f,
                30f,
                displayWidth.toInt(),
                displayHeight.toInt()
            )
        }

        override fun onDraw(canvas: Canvas?) {

            if (redraw) recalculation(planets, countOfPlanets)
            //canvas?.drawColor(Color.GREEN)
            // заливка канвы цветом

            canvas?.drawARGB(80, 102, 204, 255);
            bttnSettings.draw(canvas, tp)
            bttnPause.draw(canvas, tp)

            for (i in 0..countOfPlanets - 1) {
                drawPointX = width / 2f + myScale * (xBias + planets[i].x - planets[i].radius / 2f)
                drawPointY = height / 2f + myScale * (yBias + planets[i].y - planets[i].radius / 2f)
                canvas?.drawCircle(drawPointX, drawPointY, myScale * planets[i].radius, p)
                canvas?.drawText(
                    "X: ${planets[i].x}, Y: ${planets[i].y}",
                    drawPointX,
                    drawPointY,
                    tp
                )
                if (drawPath) {
                    for (j in 1999 downTo 3 step 2) {
                        canvas?.drawPoint(
                            width / 2f + myScale * (xBias + planets[i].line[j - 1] - planets[i].radius / 2f),
                            height / 2f + myScale * (yBias + planets[i].line[j] - planets[i].radius / 2f),
                            tp
                        )
                        planets[i].line[j] = planets[i].line[j - 2]
                        planets[i].line[j - 1] = planets[i].line[j - 3]
                    }
                    planets[i].line[0] = planets[i].x
                    planets[i].line[1] = planets[i].y
                }


            }
            invalidate()
        }

        override fun onTouchEvent(event: MotionEvent?): Boolean {
            //Log.d("ZUEV", "ea: ${event?.actionMasked}")
            when (event?.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    lastX = event.x / myScale
                    lastY = event.y / myScale

                    if (bttnPause.inBttn(event, displayWidth, displayHeight)) {
                        redraw = !redraw
                    } else if (bttnSettings.inBttn(event, displayWidth, displayHeight)) {
                        val myIntent = Intent(context, Settings2D::class.java)
                        startActivityForResult(myIntent, 0)

                    }
                }
                MotionEvent.ACTION_POINTER_DOWN -> {
                    //Log.d("ZUEV", "more fingers")
                    lastDistance = getDistance(event)

                }
                MotionEvent.ACTION_MOVE -> {
                    when (event.pointerCount) {
                        1 -> {
                            xBias += event.x / myScale - lastX
                            yBias += event.y / myScale - lastY
                            lastX = event.x / myScale
                            lastY = event.y / myScale
                        }
                        2 -> {
                            var diff = getDistance(event) - lastDistance
                            lastDistance += diff
                            diff /= 600f
                            if (myScale + diff > 0 && myScale + diff < 10) {
                                myScale += diff
                            }
                            //Log.d("ZUEV", "scale: ${myScale}")
                        }
                    }
                }
                MotionEvent.ACTION_POINTER_UP -> {
                    Log.d(
                        "ZUEV",
                        "ACTION_POINTER_UP ind ${event.actionIndex}  id ${event.getPointerId(event.actionIndex)}"
                    )
                    when (event.actionIndex) {
                        0 -> {
                            lastX = event.getX(1) / myScale
                            lastY = event.getY(1) / myScale
                        }
                        1 -> {
                            lastX = event.getX(0) / myScale
                            lastY = event.getY(0) / myScale
                        }
                    }

                }
                MotionEvent.ACTION_UP -> {
//                    Log.d(
//                        "ZUEV",
//                        "ACTION_UP ind ${event.actionIndex}  id ${event.getPointerId(event.actionIndex)}"
//                    )
                }
            }
            return true
        }


        external fun recalculation(planets: Array<Planet>, length: Short)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            planets[countOfPlanets.toInt()].x = data.getFloatExtra("X", 0f)
            planets[countOfPlanets.toInt()].y = data.getFloatExtra("Y", 0f)
            planets[countOfPlanets.toInt()].xspeed =
                data.getFloatExtra("XSpeed", 0f)
            planets[countOfPlanets.toInt()].yspeed =
                data.getFloatExtra("YSpeed", 0f)
            planets[countOfPlanets.toInt()].weight =
                data.getFloatExtra("Weight", 100f)
            planets[countOfPlanets.toInt()].radius =
                data.getFloatExtra("Radius", 20f)
            countOfPlanets++
        }
    }

}

class Planet(
    var xspeed: Float = 0f,
    var yspeed: Float = 0f,
    var x: Float = 0f,
    var y: Float = 0f,
    var weight: Float = 300f,
    var radius: Float = 10f,
    var line: FloatArray = FloatArray(2000, { 0f })
)

class MyButton(
    var pic: Bitmap,
    var x: Float,
    var y: Float,
    var x2: Float,
    var y2: Float,
    width: Int,
    height: Int
) {
    val rectsrc: Rect
    val rectdraw: Rect

    init {
        rectsrc = Rect(0, 0, pic.width, pic.height)
        rectdraw = Rect(
            (width / 100f * x).toInt(), (height / 100f * y).toInt(),
            (width / 100f * x2).toInt(), (height / 100f * y2).toInt()
        )
    }

    fun draw(canvas: Canvas?, tp: Paint) {
        canvas?.drawBitmap(pic, rectsrc, rectdraw, tp)

    }

    fun inBttn(event: MotionEvent?, displayWidth: Float, displayHeight: Float): Boolean =
        event!!.x > displayWidth / 100f * x && event.x < displayWidth / 100f * x2 && event.y > displayHeight / 100f * y && event.y < displayHeight / 100f * y2

}

fun getDistance(event: MotionEvent?): Float =
    sqrt((event!!.getX(0) - event.getX(1)).pow(2f) + (event.getY(0) - event.getY(1)).pow(2f))