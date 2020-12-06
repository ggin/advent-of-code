package advent

import static java.lang.System.lineSeparator

class Day6 implements DailyChallenge {

    long puzzle1(String text) {
        text.split(lineSeparator() * 2)
                .collect { it.replaceAll(lineSeparator(), "").split("").toUnique().size() }
                .inject { a, b -> a + b } as long
    }

    long puzzle2(List<String> values) {
        def sum = 0
        def m = null
        values.each { value ->
            if (value.isEmpty()) {
                sum += m.size()
                m = null
            } else {
                m = m != null ? m.intersect(value.toList()) : value.toList()
            }
        }
        sum += m.size()
        sum
    }

}
