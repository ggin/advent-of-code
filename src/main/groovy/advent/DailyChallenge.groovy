package advent

trait DailyChallenge {

    def dayNumber() {
        (getClass().simpleName =~ /\d+/).findAll().first()
    }

    List<String> parseInputFile() {
        getClass().getResource("/day${dayNumber()}-input.txt").readLines()
    }

    long puzzle1(List<String> values) { 0 }

    long puzzle2(List<String> values) { 0 }

    def toIntValues(l) { l.collect { it -> Integer.parseInt(it) } }

    def run() {
        println puzzle1(parseInputFile())
        println puzzle2(parseInputFile())
    }

}
