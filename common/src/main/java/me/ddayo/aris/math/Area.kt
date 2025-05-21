package me.ddayo.aris.math

import me.ddayo.aris.luagen.ILuaStaticDecl
import me.ddayo.aris.lua.glue.LuaGenerated
import me.ddayo.aris.math.Point.Companion.with
import me.ddayo.aris.luagen.LuaFunction
import me.ddayo.aris.luagen.LuaProvider
import kotlin.math.abs
import kotlin.math.min


@LuaProvider
object AreaFunctions {
    /**
     * @param x x of left-top point
     * @param y y of left-top point
     * @param width width of the area
     * @param height height of the area
     * @return Area(x with y, x with (y + height), (x + width) with (y + height), (x + width) with y)
     */
    @LuaFunction("create_rect_area")
    fun createRect(x: Double, y: Double, width: Double, height: Double) = Area(x with y, x with (y + height), (x + width) with (y + height), (x + width) with y)

    /**
     * @return createRect(min(p1.x, p2.x), min(p1.y, p2.y), abs(p1.x - p2.x), abs(p1.y - p2.y))
     */
    @LuaFunction("create_rect_area")
    fun createRect(p1: Point, p2: Point) = createRect(min(p1.x, p2.x), min(p1.y, p2.y), abs(p1.x - p2.x), abs(p1.y - p2.y))
}

@LuaProvider
class AreaBuilder: ILuaStaticDecl by LuaGenerated.AreaBuilder_LuaGenerated {
    private val points = mutableListOf<Point>()

    /**
     * Append the point that constructs node of the area
     */
    @LuaFunction
    fun append(p: Point) {
        points.add(p)
    }

    @LuaFunction
    fun build() = Area(*points.toTypedArray())
}

@LuaProvider
class Area(private vararg val points: Point): ILuaStaticDecl by LuaGenerated.Area_LuaGenerated {
    @LuaFunction("is_in")
    fun isIn(p: Point): Boolean {
        var inside = false

        var j = points.size - 1
        for (i in points.indices) {
            if ((points[i].y > p.y) != (points[j].y > p.y) &&
                (p.x < (points[j].x - points[i].x) * (p.y - points[i].y) / (points[j].y - points[i].y) + points[i].x)
            )
                inside = !inside
            j = i
        }
        return inside
    }

    val minX get() = points.minOf { it.x }
    val minY get() = points.minOf { it.y }
    val maxX get() = points.maxOf { it.x }
    val maxY get() = points.maxOf { it.y }
    val centerX get() = (minX + maxX) / 2
    val centerY get() = (minY + maxY) / 2

    @LuaFunction
    fun times(x: Double): Area {
        return Area(*points.map { (it.x * x) with (it.y * x) }.toTypedArray())
    }

    @LuaFunction
    fun center() = centerX with centerY

    @LuaFunction("into_string")
    fun intoString() = "Area ($minX, $minY)..($maxX, $maxY)"
}