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