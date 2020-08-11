package exdo.planets


import android.app.Activity
import android.content.Context
import android.graphics.*
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class Activity_2d : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(DrawView(this))
    }

}
class Planet(
    var xspeed: Float = 0f,
    var yspeed: Float = 0f,
    var x: Float = 0f,
    var y: Float = 0f,
    var weight:Float = 10f,
    var radius: Float = 10f
)

class DrawView(context: Context) : View(context) {
    var p: Paint
    var planet: Planet
    var planets: Array<Planet>
    var countOfPlanets:Short
    init {
        p = Paint()
        // настройка кисти
        // красный цвет
        p.setColor(Color.RED);
        // толщина линии = 10
        p.setStrokeWidth(10.0f);

        planet = Planet()
        planets = Array(20){ Planet() }
        planets[0].x = 300f
        planets[0].y = 300f
        planets[1].x = 600f
        planets[1].y = 600f
        countOfPlanets =2
    }

    override fun onDraw(canvas: Canvas?) {
        //canvas?.drawColor(Color.GREEN)
        // заливка канвы цветом
        canvas?.drawARGB(80, 102, 204, 255);
        for(i in 0..countOfPlanets-1){
            canvas?.drawCircle(planets[i].x, planets[i].y, planets[i].radius, p)
            Log.i("coord", "planet $i:  X:${planets[i].x}, Y:${planets[i].y}, SX:${planets[i].xspeed}, SY:${planets[i].yspeed}")
        }
        invalidate()
        recalculation(planets, countOfPlanets)

    }
    external fun recalculation(planets: Array<Planet>, length:Short)
}
