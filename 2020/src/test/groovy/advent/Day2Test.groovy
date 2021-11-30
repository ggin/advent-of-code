package advent

class Day2Test extends DailyChallengeTest {
    {
        input = "1-3 a: abcde\n" +
                "1-3 b: cdefg\n" +
                "2-9 c: ccccccccc"
        expectedResultPuzzle1 = 2
        expectedResultPuzzle2 = 1
        classToTest = Day2
    }
}
