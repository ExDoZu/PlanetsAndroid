package exdo.planets

import kotlin.random.*
import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View

class GameOfLifeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(MyView(this))
    }

    inner class MyView(context: Context) : View(context) {
        val screenWidth: Int
        val screenHeight: Int
        val fieldWidth: Int
        val fieldHeight: Int
        var field: Array<Array<Boolean>>
        val p: Paint

        init {
            val rect: Rect = Rect()
            getWindowVisibleDisplayFrame(rect)
            screenWidth =rect.width()
            screenHeight = rect.height()
            fieldWidth=480
            fieldHeight = fieldWidth*screenHeight/screenWidth




            field =
//               Array(fieldHeight, { Array(fieldWidth, { false }) })
//
//            field[45][45]=true
//            field[46][45]=true
//            field[47][45]=true
//            field[47][44]=true
//            field[46][43]=true
                    Array(fieldHeight, { Array(fieldWidth, { Random.nextBoolean() }) })
            p = Paint()
            p.color = Color.BLACK

        }

        override fun onDraw(canvas: Canvas?) {
            //Log.d(MYTAG, "w $fieldWidth   h $fieldHeight\n                        $screenWidth   ${(screenWidth / fieldWidth )}")
            canvas?.drawARGB(80, 102, 204, 255);
            for (i in 0..fieldHeight - 1)
                for (j in 0..fieldWidth - 1) {
                    if (field[i][j]){
                        //Log.d("ZUEV", "X: $j, Y: $i")
                        canvas?.drawRect(
                            width.toFloat() / fieldWidth.toFloat()  * j.toFloat(),
                            width.toFloat() / fieldWidth.toFloat() * i.toFloat(),
                            width.toFloat() / fieldWidth.toFloat() * (j + 1).toFloat(),
                            width.toFloat() / fieldWidth.toFloat() * (i + 1).toFloat(),
                            p
                        )
                    }
                }
            //Log.d("ZUEV", "------------------------------------------------------")
            field = recalculate(field, fieldWidth, fieldHeight)
            //invalidate()

        }
        override fun onTouchEvent(event: MotionEvent?): Boolean {
            //Log.d("ZUEV", "ea: ${event?.actionMasked}")
            when (event?.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    invalidate()
                }
                MotionEvent.ACTION_MOVE->{
                    invalidate()
                }
            }
            return true
        }
    }

    fun recalculate(field: Array<Array<Boolean>>, widh: Int, height: Int): Array<Array<Boolean>> {

        val newField: Array<Array<Boolean>> = Array(height, { Array(widh, { false }) })
        var count: Int = 0
        if (field[height - 1][widh - 1] == true) count++
        if (field[height - 1][0] == true) count++
        if (field[height - 1][1] == true) count++
        if (field[0][widh - 1] == true) count++
        if (field[0][1] == true) count++
        if (field[1][widh - 1] == true) count++
        if (field[1][0] == true) count++
        if (field[1][1] == true) count++
        if (field[0][0]) {
            if (count == 2 || count == 3) newField[0][0] = true
        } else if (count == 3) {
            newField[0][0] = true
        }
        count = 0

        if (field[height - 1][widh - 2] == true) count++
        if (field[height - 1][widh - 1] == true) count++
        if (field[height - 1][0] == true) count++
        if (field[0][widh - 2] == true) count++
        if (field[0][0] == true) count++
        if (field[1][widh - 2] == true) count++
        if (field[1][widh - 1] == true) count++
        if (field[1][0] == true) count++
        if (field[0][widh - 1]) {
            if (count == 2 || count == 3) newField[0][widh - 1] = true
        } else if (count == 3) {
            newField[0][widh - 1] = true
        }
        count = 0

        if (field[height - 2][widh - 1] == true) count++
        if (field[height - 2][0] == true) count++
        if (field[height - 2][1] == true) count++
        if (field[height - 1][widh - 1] == true) count++
        if (field[height - 1][1] == true) count++
        if (field[0][widh - 1] == true) count++
        if (field[0][0] == true) count++
        if (field[0][1] == true) count++
        if (field[height - 1][0]) {
            if (count == 2 || count == 3) newField[height - 1][0] = true
        } else if (count == 3) {
            newField[height - 1][0] = true
        }
        count = 0

        if (field[height - 2][widh - 2] == true) count++
        if (field[height - 2][widh - 1] == true) count++
        if (field[height - 2][0] == true) count++
        if (field[height - 1][widh - 2] == true) count++
        if (field[height - 1][0] == true) count++
        if (field[0][widh - 2] == true) count++
        if (field[0][widh - 1] == true) count++
        if (field[0][0] == true) count++
        if (field[height - 1][widh - 1]) {
            if (count == 2 || count == 3) newField[height - 1][widh - 1] = true
        } else if (count == 3) {
            newField[height - 1][widh - 1] = true
        }
        count = 0

        for (i in 1..widh - 2) {
            if (field[height - 1][i - 1] == true) count++
            if (field[height - 1][i] == true) count++
            if (field[height - 1][i + 1] == true) count++
            if (field[0][i - 1] == true) count++
            if (field[0][i + 1] == true) count++
            if (field[1][i - 1] == true) count++
            if (field[1][i] == true) count++
            if (field[1][i + 1] == true) count++
            if (field[0][i]) {
                if (count == 2 || count == 3) newField[0][i] = true
            } else if (count == 3) {
                newField[0][i] = true
            }
            count = 0
        }
        for (i in 1..widh - 2) {
            if (field[height - 2][i - 1] == true) count++
            if (field[height - 2][i] == true) count++
            if (field[height - 2][i + 1] == true) count++
            if (field[height - 1][i - 1] == true) count++
            if (field[height - 1][i + 1] == true) count++
            if (field[0][i - 1] == true) count++
            if (field[0][i] == true) count++
            if (field[0][i + 1] == true) count++
            if (field[height - 1][i]) {
                if (count == 2 || count == 3) newField[height - 1][i] = true
            } else if (count == 3) {
                newField[height - 1][i] = true
            }
            count = 0
        }
        for (i in 1..height - 2) {
            if (field[i - 1][widh - 1] == true) count++
            if (field[i][widh - 1] == true) count++
            if (field[i + 1][widh - 1] == true) count++
            if (field[i - 1][0] == true) count++
            if (field[i + 1][0] == true) count++
            if (field[i - 1][1] == true) count++
            if (field[i][1] == true) count++
            if (field[i + 1][1] == true) count++
            if (field[i][0]) {
                if (count == 2 || count == 3) newField[i][0] = true
            } else if (count == 3) {
                newField[i][0] = true
            }
            count = 0
        }
        for (i in 1..height - 2) {
            if (field[i - 1][widh - 2] == true) count++
            if (field[i][widh - 2] == true) count++
            if (field[i + 1][widh - 2] == true) count++
            if (field[i - 1][widh - 1] == true) count++
            if (field[i + 1][widh - 1] == true) count++
            if (field[i - 1][0] == true) count++
            if (field[i][0] == true) count++
            if (field[i + 1][0] == true) count++
            if (field[i][widh - 1]) {
                if (count == 2 || count == 3) newField[i][widh - 1] = true
            } else if (count == 3) {
                newField[i][widh - 1] = true
            }
            count = 0
        }
        for (i in 1..height - 2) {
            for (j in 1..widh - 2) {
                if (field[i - 1][j - 1] == true) count++
                if (field[i - 1][j] == true) count++
                if (field[i - 1][j + 1] == true) count++
                if (field[i][j - 1] == true) count++
                if (field[i][j + 1] == true) count++
                if (field[i + 1][j - 1] == true) count++
                if (field[i + 1][j] == true) count++
                if (field[i + 1][j + 1] == true) count++
                if (field[i][j]) {
                    if (count == 2 || count == 3) newField[i][j] = true
                } else if (count == 3) {
                    newField[i][j] = true
                }
                count = 0
            }
        }
        return newField
    }
}
