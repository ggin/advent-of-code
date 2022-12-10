package common

interface DailyChallenge {

    fun dayNumber() = """\d+""".toRegex().find(javaClass.simpleName)!!.value.toInt()

    fun filepath(suffix: String) = "/day${dayNumber()}-${suffix}.txt"

    fun getInput(suffix: String) = Input(filepath(suffix))
    fun puzzle1(input: Input): Long
    fun puzzle2(input: Input): Long
    fun puzzle2S(input: Input): String = ""

    fun run() {
        val input = getInput("input")
        println("Running day ${dayNumber()} challenge")
        println("Puzzle 1: ${puzzle1(input)}")
        println("Puzzle 2: ${puzzle2S(input)}")
    }

}
