import common.DailyChallenge

interface DailyChallengeTest {

    fun puzzle1(challenge: DailyChallenge) =
        challenge.puzzle1(challenge.getInput("test-input"))

    fun puzzle2(challenge: DailyChallenge) =
        challenge.puzzle2(challenge.getInput("test-input"))
}
