package me.ddayo.aris.util

object ListExtensions {
    inline fun<reified T> List<T>.mutableForEach(f: (T) -> Unit) {
        var it = 0
        while(it < size) {
            f(get(it))
            it++
        }
    }
}