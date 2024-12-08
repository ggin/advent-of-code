package common

fun combineIntoPairs(r: IntRange) = combineIntoPairs(r, r)
fun combineIntoPairs(r1: IntRange, r2: IntRange) = r1.flatMap { x -> r2.map { Pair(x, it) } }

// from https://gist.github.com/trygvea/a2d9cdbc19ceff3df7eb64ccef3c0597
fun <T> permute(list: List<T>): List<List<T>> = when {
    list.size > 10 -> throw Exception("You probably dont have enough memory to keep all those permutations")
    list.size <= 1 -> listOf(list)
    else ->
        permute(list.drop(1)).map { perm ->
            (list.indices).map { i ->
                perm.subList(0, i) + list.first() + perm.drop(i)
            }
        }.flatten()
}

fun Input.toMatrix() = Matrix(this.toStringList())

data class Matrix<T>(val matrix: List<List<T>>) {

    val xCount = matrix.size
    val yCount = matrix[0].size

    fun at(xy: Pair<Int, Int>) = at(xy.first, xy.second)

    fun at(x: Int, y: Int): Value<T>? {
        if (x < 0 || y < 0 || x >= xCount || y >= yCount) return null
        return Value(matrix[x][y], Pair(x,y))
    }

    fun values() : List<Value<T>> {
        return matrix.flatMapIndexed { x, l -> l.mapIndexed { y, v -> Value(v, Pair(x, y)) } }
    }

    fun contains(it: Pair<Int, Int>): Boolean {
        return it.first in 0..<xCount && it.second in 0 ..< yCount
    }

    data class Value<T>(val value: T, val xy: Pair<Int, Int>) {
        val x = xy.first
        val y = xy.second
    }
}