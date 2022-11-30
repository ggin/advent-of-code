import common.DailyChallenge

interface DailyChallengeTest {

    fun puzzle1(challenge: DailyChallenge) =
        challenge.puzzle1(challenge.parseInputFile("test-input").lines())

    fun puzzle2(challenge: DailyChallenge) =
        challenge.puzzle2(challenge.parseInputFile("test-input").lines())
}
