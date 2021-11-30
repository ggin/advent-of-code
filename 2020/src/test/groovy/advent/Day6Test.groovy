package advent

class Day6Test extends DailyChallengeTest {
    {
        input = "abc\n" +
                "\n" +
                "a\n" +
                "b\n" +
                "c\n" +
                "\n" +
                "ab\n" +
                "ac\n" +
                "\n" +
                "a\n" +
                "a\n" +
                "a\n" +
                "a\n" +
                "\n" +
                "b"
        expectedResultPuzzle1 = 11
        expectedResultPuzzle2 = 6
        classToTest = Day6
    }
}
