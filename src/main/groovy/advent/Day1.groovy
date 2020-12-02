package advent

class Day1 implements DailyChallenge {
    def static MAGIC_NUMBER = 2020

    long puzzle1(List<String> values) {
        def complements = new HashSet()
        long n = toIntValues(values).stream()
                .filter {
                    complements.add(MAGIC_NUMBER - it)
                    return complements.remove(it)
                }
                .findFirst().get()
        return n * (MAGIC_NUMBER - n)
    }

    long puzzle2(List<String> values) {
        def intValues = toIntValues(values)
        def complements = [intValues , intValues ].combinations()
                .findAll { a, b -> a < b && a + b < MAGIC_NUMBER }
                .collectEntries { a, b -> [(MAGIC_NUMBER - a - b): [a, b]] }

        def n = intValues .find { complements.containsKey(it) }
        return [n, *complements[n]].inject { a, b -> a * b } as long
    }

}
