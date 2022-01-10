package ru.arina.graphics

import ru.arina.CartesianPlane
import ru.arina.Painter
import java.awt.Color
import java.awt.Graphics

class ExplFunct (private val plane: CartesianPlane, val f:(Double)->Double): Painter {
    override fun paint(g: Graphics) {
        if (g!=null){
            PaintFucntion(g)
        }
    }
    fun PaintFucntion(g: Graphics){
        for (i in 0..plane.width) {
            g.color = Color.GREEN
            val y1 = plane.yCrt2Scr(f(plane.xScr2Crt(i)))
            val y2 = plane.yCrt2Scr(f(plane.xScr2Crt(i+1)))
            g.drawLine(i,y1,i+1,y2)
        }
    }


}