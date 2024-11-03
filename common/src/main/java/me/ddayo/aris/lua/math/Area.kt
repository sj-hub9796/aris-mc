package me.ddayo.aris.lua.math

import me.ddayo.aris.ILuaStaticDecl
import me.ddayo.aris.lua.glue.LuaGenerated
import me.ddayo.aris.lua.math.Point.Companion.with
import me.ddayo.aris.luagen.LuaFunction
import me.ddayo.aris.luagen.LuaProvider


@LuaProvider
class AreaBuilder: ILuaStaticDecl by LuaGenerated.AreaBuilder_LuaGenerated {
    private val points = mutableListOf<Point>()
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
    fun intoString() = "$maxX $maxY $minX $minY"
}