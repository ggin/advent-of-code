package common

fun combineIntoPairs(r: IntRange) = combineIntoPairs(r, r)
fun combineIntoPairs(r1: IntRange, r2: IntRange) = r1.flatMap { x -> r2.map { Pair(x, it) } }

operator fun <T> Iterable<T>.times(other: Iterable<T>): Iterable<Iterable<T>> {
    val otherSeq = other.asSequence()
    return flatMap{ a -> otherSeq.map { b -> listOf(a, b) }}
}


fun <T> cartesianProduct(iterables: Iterable<Iterable<T>>, reduce: (Iterable<T>) -> T): Iterable<T> {
    return iterables.reduce { acc, i -> (acc * i).map { reduce(it) }}
}

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

fun Input.toStringMatrix() = Matrix(this.toStringList())
fun Input.toLongMatrix() = Matrix(this.toLongList())
fun Input.toIntMatrix() = Matrix(this.toIntList())

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

    fun findFirst(t: T): Value<T>? {
        return matrix.mapIndexedNotNull { x, l ->
            val y = l.mapIndexedNotNull { y, s -> if (s == t) y else null }.firstOrNull()
            if (y != null) Value(t, Pair(x, y)) else null
        }.firstOrNull()
    }

    fun findAll(t: T): List<Value<T>> {
        return matrix.flatMapIndexed { x, l ->
            l.mapIndexedNotNull { y, s -> if (s == t) Value(s, Pair(x, y)) else null }
        }
    }

    fun set(xy: Pair<Int, Int>, value: T): Matrix<T> {
        val newMatrix = matrix.mapIndexed { x, l ->
            if (x == xy.first) l.mapIndexed { y, v -> if (y == xy.second) value else v }
            else l
        }
        return Matrix(newMatrix)
    }

    fun allValidDirections(v: Value<T>) : List<DirectionalValue<T>> {
        return Direction.allDirections(v.xy).mapNotNull { d -> at(d.first)?.let { DirectionalValue(it.value, it.xy, d.second) } }
    }

    fun set(x: Int, y: Int, value: T) = set(Pair(x, y), value)

    override fun toString(): String {
        return matrix.joinToString("\n") { it.joinToString("") }
    }

    fun print() {
        for (x in 0 until xCount) {
            for (y in 0 until yCount) {
                print(at(x, y)?.value)
            }
            println()
        }
    }

    open class Value<T>(val value: T, val xy: Pair<Int, Int>) {
        val x = xy.first
        val y = xy.second
        override fun toString(): String {
            return "Value(x=$x, y=$y, value=$value)"
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is Value<*>) return false

            if (value != other.value) return false
            if (xy != other.xy) return false
            if (x != other.x) return false
            if (y != other.y) return false

            return true
        }

        override fun hashCode(): Int {
            var result = value?.hashCode() ?: 0
            result = 31 * result + xy.hashCode()
            result = 31 * result + x
            result = 31 * result + y
            return result
        }


    }

    class DirectionalValue<T>(value: T, xy: Pair<Int, Int>, val direction: Direction) : Value<T>(value, xy)

    enum class Direction(val move: (Pair<Int, Int>) -> Pair<Int, Int>) {
        UP({ p -> Pair(p.first - 1, p.second) }),
        LEFT({ p -> Pair(p.first, p.second - 1) }),
        DOWN({ p -> Pair(p.first + 1, p.second) }),
        RIGHT({ p -> Pair(p.first, p.second + 1) });

        fun moveNTimes(p: Pair<Int, Int>, n: Int) : Pair<Int, Int> {
            return (1..n).fold(p) { acc, _ -> move(acc) }
        }

        companion object {
            fun allDirections(p: Pair<Int, Int>) = entries.map { Pair(it.move(p), it) }
        }
    }
}

fun <E> Iterable<E>.indexesOf(e: E) = mapIndexedNotNull{ index, elem -> index.takeIf{ elem == e } }