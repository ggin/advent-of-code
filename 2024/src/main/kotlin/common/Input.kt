package common

class Input(filepath: String) {

    val content = DailyChallenge::class.java.getResource(filepath)!!.readText().trimEnd()
    val values = content.lines()


    fun toInt() = values.map { it.toInt() }
    fun toLong() = values.map { it.toLong() }
    fun toLongList(sep: String = " ") = values.map { it.split(sep).map { s -> s.toLong() } }.toList()
    fun toStringPairs() = values.map { it.split(" ") }.map { Pair(it[0], it[1]) }
    fun toCharPairs() = values.map { it.toCharArray() }.map { Pair(it[0], it[2]) }
    fun <T> toPairs(sep: String = " ", transform: (line: String) -> T) =
        values.map { it.split(sep) }.map { Pair(transform(it[0]), transform(it[1])) }

    val singleValue = values.first()

    fun <T> split(transform: (line: String) -> T) = values.fold(mutableListOf(mutableListOf()))
    { p: MutableList<MutableList<T>>, s: String ->
        if (s.isEmpty()) p.apply { p.add(mutableListOf()) }
        else p.apply { p.last().add(transform(s)) }
    }
        .map { it.toList() }
        .filter { it.isNotEmpty() }

}
