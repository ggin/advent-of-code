package common

interface DailyChallenge {

    fun dayNumber() = """\d+""".toRegex().find(javaClass.simpleName)!!.value.toInt()

    fun parseInputFile(suffix: String) =
        DailyChallenge::class.java.getResource("/day${dayNumber()}-${suffix}.txt")!!.readText().trimEnd()

    fun puzzle1(values: List<String>): Long

    fun puzzle2(values: List<String>): Long

    fun toIntValues(values: List<String>) = values.map { it.toInt() }

    fun toLongValues(values: List<String>) = values.map { it.toLong() }

    fun run() {
        val inputFile = parseInputFile("input")
        val values = inputFile.lines()
        println("Running day ${dayNumber()} challenge")
        println("Puzzle 1: ${puzzle1(values)}")
        println("Puzzle 2: ${puzzle2(values)}")
    }

}
