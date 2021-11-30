package advent

import static java.lang.System.lineSeparator

class Day4 implements DailyChallenge {

    def passportFields = [
            "byr": (String s) -> between(s, 1920, 2002),
            "iyr": (String s) -> between(s, 2010, 2020),
            "eyr": (String s) -> between(s, 2020, 2030),
            "hgt": (String s) -> checkHeight(s, "cm", 150, 193) || checkHeight(s, "in", 59, 76),
            "hcl": (String s) -> s.matches("#[0-9a-f]{6}"),
            "ecl": (String s) -> s in ["amb", "blu", "brn", "gry", "grn", "hzl", "oth"],
            "pid": (String s) -> s.matches("[0-9]{9}")
    ]

    static def between(String s, int f, int t) {
        s.isNumber() && s.toInteger() >= f && s.toInteger() <= t
    }

    static def checkHeight(String s, String unit, int f, int t) {
        s.endsWith(s) && between(s.replaceAll(unit, ""), f, t)
    }

    long puzzle1(String text) {
        text.split(lineSeparator() * 2)
            .collect { it.replaceAll(lineSeparator(), " ") }
            .count {
                def keys = it.split(" ").collect { it.split(":")[0] }
                passportFields.keySet().every { it in keys }
            }
    }

    long puzzle2(String text) {
        text.split(lineSeparator() * 2)
            .collect { it.replaceAll(lineSeparator(), " ") }
            .count {
                def fields = it.split(" ").collectEntries { [it.split(":")[0], it.split(":")[1]] }
                passportFields.every {k, v ->
                    k in fields.keySet() && v.call(fields[k])
                }
            }
    }

}
