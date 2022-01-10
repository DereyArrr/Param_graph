package ru.arina.graphics

import ru.arina.CartesianPlane
import ru.arina.Painter
import java.awt.Color
import java.awt.Graphics

class ParamFunct (private val plane: CartesianPlane,
                  val x: (Double) -> Double,
                  val y: (Double) -> Double,
                  val tMin: Double,
                  val tMax: Double
) : Painter {
    override fun paint(g: Graphics) {
        if (g != null) {
            PaintFucntion(g)
        }
    }

    fun PaintFucntion(g: Graphics) {
        g.color = Color.PINK
        var t = tMin
        val d = (tMax - tMin) / 1000
        while (t <= tMax) {
            val x1 = plane.xCrt2Scr(x(t))
            val y1 = plane.yCrt2Scr(y(t))
            val x2 = plane.xCrt2Scr(x(t + d))
            val y2 = plane.yCrt2Scr(y(t + d))
            g.drawLine(x1, y1, x2, y2)
            t+=d
        }
    }


}