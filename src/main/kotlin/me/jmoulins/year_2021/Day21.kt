package me.jmoulins.year_2021

fun main() {
    // Part 1
    println("Part 1")
    playWithDeterministicDice(4, 8)
    println()
    playWithDeterministicDice(2, 8)
    println()

    // Part 2
    println("Part 2")
    playWithDiracDice(4, 8)
    println()
    playWithDiracDice(2, 8)
}

fun playWithDeterministicDice(player1StartingPosition: Int, player2StartingPosition: Int) {
    val player1 = Player(player1StartingPosition)
    val player2 = Player(player2StartingPosition)
    val dice = DeterministicDice()
    var turn = 0
    while (!player1.hasWon(1000) && !player2.hasWon(1000)) {
        turn++
        val sum = dice.roll() + dice.roll() + dice.roll()
        if (turn % 2 == 0) {
            player2.move(sum)
        } else {
            player1.move(sum)
        }
    }
    val losingPlayer = if (player1.score >= 1000) player2 else player1
    println("Dice has been rolled ${dice.value} times")
    println("Losing player has a score of ${losingPlayer.score}")
    println("Result: ${dice.value * losingPlayer.score}")
}

fun playWithDiracDice(player1StartingPosition: Int, player2StartingPosition: Int) {
    val dieFrequency: Map<Int, Long> = mapOf(3 to 1, 4 to 3, 5 to 6, 6 to 7, 7 to 6, 8 to 3, 9 to 1)
    val games: MutableMap<Game, Pair<Long, Long>> = mutableMapOf()
    val player1 = Player(player1StartingPosition)
    val player2 = Player(player2StartingPosition)
    val wins = playWithDiracDice(Game(player1, player2, true, 21), games, dieFrequency)
    println("Player 1 wins in ${wins.first} universes and player 2 wins in ${wins.second} universe")
}

fun playWithDiracDice(
    game: Game,
    games: MutableMap<Game, Pair<Long, Long>>,
    dieFrequency: Map<Int, Long>
): Pair<Long, Long> {
    return when {
        game.isFinished() -> if (game.player1.hasWon(game.scoreToReach)) 1L to 0L else 0L to 1L
        game in games -> games.getValue(game)
        else -> {
            dieFrequency
                .map { (die, frequency) ->
                    val newGameResults = playWithDiracDice(game.next(die), games, dieFrequency)
                    newGameResults.first * frequency to newGameResults.second * frequency
                }
                .reduce { results, gameResult -> (results.first + gameResult.first) to (results.second + gameResult.second) }
                .also { games[game] = it }
        }
    }
}

class DeterministicDice {
    var value = 0

    fun roll(): Int {
        value++
        return (value - 1) % 100 + 1
    }
}

data class Player(var position: Int, var score: Int = 0) {
    fun move(die: Int) {
        this.position = (this.position + die - 1) % 10 + 1
        this.score += this.position
    }

    fun hasWon(scoreToReach: Int): Boolean {
        return this.score >= scoreToReach
    }

    fun next(die: Int): Player {
        val next = (this.position + die - 1) % 10 + 1
        return Player(next, next + score)
    }
}

class Game(val player1: Player, private val player2: Player, var player1Plays: Boolean, val scoreToReach: Int) {

    fun isFinished(): Boolean {
        return player1.hasWon(scoreToReach) || player2.hasWon(scoreToReach)
    }

    fun next(die: Int): Game {
        return Game(
            if (player1Plays) player1.next(die) else player1,
            if (player1Plays) player2 else player2.next(die),
            !player1Plays,
            scoreToReach
        )
    }
}