package advent

trait DailyChallenge {

    def dayNumber() {
        (getClass().simpleName =~ /\d+/).findAll().first()
    }

    String parseInputFile() {
        getClass().getResource("/day${dayNumber()}-input.txt").text
    }

    long puzzle1(List<String> values) { 0 }

    long puzzle2(List<String> values) { 0 }

    long puzzle1(String text) {
        puzzle1(text.readLines())
    }

    long puzzle2(String text) {
        puzzle2(text.readLines())
    }

    def toIntValues(l) { l.collect { it -> Integer.parseInt(it) } }

    def toLongValues(l) { l.collect { it -> Long.parseLong(it) } }

    def run() {
        println puzzle1(parseInputFile())
        println puzzle2(parseInputFile())
    }

}
