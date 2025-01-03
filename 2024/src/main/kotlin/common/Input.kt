package common

class Input(filepath: String) {

    val content = DailyChallenge::class.java.getResource(filepath)!!.readText().trimEnd()
    val values: List<String> = content.lines()


    fun toInt() = values.map { it.toInt() }
    fun toLong() = values.map { it.toLong() }
    fun toLongList(sep: String = "") = toList(sep, { it.toLong() })
    fun toIntList(sep: String = "") = toList(sep, { it.toInt() })
    fun toStringList(sep: String = "") = toList(sep, { it })
    fun <T> toList(sep: String = "", transform: (line: String) -> T) = values.map { it.split(sep).filter { s -> s.isNotBlank() }.map { s -> transform(s) } }.toList()
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
