package common

class Input(filepath: String) {

    private val values = DailyChallenge::class.java.getResource(filepath)!!.readText().trimEnd().lines()

    fun toInt() = values.map { it.toInt() }
    fun toLong() = values.map { it.toLong() }

    fun <T> split(transform: (line: String) -> T) = values.fold(mutableListOf(mutableListOf()))
    { p: MutableList<MutableList<T>>, s: String ->
        if (s.isEmpty()) p.apply { p.add(mutableListOf()) }
        else p.apply { p.last().add(transform(s)) }
    }
        .map { it.toList() }
        .filter { it.isNotEmpty() }

}
