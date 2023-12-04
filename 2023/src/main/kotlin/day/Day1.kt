package day

import common.DailyChallenge
import common.Input

class Day1 : DailyChallenge {

    private val words = listOf("one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "1", "2", "3", "4", "5", "6", "7", "8", "9")
    private val regex = "(one|two|three|four|five|six|seven|eight|nine|1|2|3|4|5|6|7|8|9)".toRegex()
    private val map = mapOf(
        "one" to "1",
        "two" to "2",
        "three" to "3",
        "four" to "4", "five" to "5", "six" to "6", "seven" to "7", "eight" to "8", "nine" to "9"
    )

    override fun puzzle1(input: Input) = input.values
        .map { Regex("[^0-9]").replace(it, "") }
        .map { "${it.first()}${it.last()}" }
        .sumOf { it.toLong() }

    override fun puzzle2(input: Input) = input.values
//        .map {
//            val groups = regex.findAll(it).map { x -> x.groupValues[1] }
//            "${map.getOrDefault(groups.first(), groups.first())}${map.getOrDefault(groups.last(), groups.last())}"
//        }
        .map {
            it.replace("one", "o1ne")
                .replace("two", "t2wo")
                .replace("three", "t3hree")
                .replace("four", "f4our")
                .replace("five", "f5ive")
                .replace("six", "s6ix")
                .replace("seven", "s7even")
                .replace("eight", "e8ight")
                .replace("nine", "n9ine")
        }
        .also{println(it)}
        .map { Regex("[^0-9]").replace(it, "") }
        .map { "${it.first()}${it.last()}" }
        .also{println(it)}
        .sumOf { it.toLong() }

}
