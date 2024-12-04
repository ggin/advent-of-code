package common

fun combineIntoPairs(r: IntRange) = combineIntoPairs(r, r)
fun combineIntoPairs(r1: IntRange, r2: IntRange) = r1.flatMap { x -> r2.map { Pair(x, it) } }
