package advent

def run(DailyChallenge challenge) {
    def values = challenge.parseInputFile()
    println "Running day ${challenge.dayNumber()} challenge"
    println "Puzzle 1: ${challenge.puzzle1(values)}"
    println "Puzzle 2: ${challenge.puzzle2(values)}"
}

run(new Day8
        ())
