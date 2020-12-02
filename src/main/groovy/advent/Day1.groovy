package advent

class Day1 {
    def static VALUES = Day1.class.getResource("/day1-input.txt").readLines().collect { it -> Integer.parseInt(it) }
    def static MAGIC_NUMBER = 2020

    static long puzzle1() {
        def complements = new HashSet()
        long n = VALUES.stream()
                .filter {
                    complements.add(MAGIC_NUMBER - it)
                    return complements.remove(it)
                }
                .findFirst().get()
        return n * (MAGIC_NUMBER - n)
    }

    static long puzzle2() {
        def complements = [VALUES, VALUES].combinations()
                .findAll { a, b -> a < b && a + b < MAGIC_NUMBER }
                .collectEntries { a, b -> [(MAGIC_NUMBER - a - b): [a, b]] }

        def n = VALUES.find { complements.containsKey(it) }

        return [n, *complements[n]].inject { a, b -> a * b } as long
    }

    static void main(String[] args) {
        println puzzle1()
        println puzzle2()
    }
}
