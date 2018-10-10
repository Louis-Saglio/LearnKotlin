import java.util.*

fun main(args: Array<String>) {
    PlusOrLessMaster(100).plusOrLess()
    solve(100)
}

enum class Ratio(val value: String) {
    TOO_BIG("Too big"),
    TOO_SMALL("Too small"),
    EQUALS("Equals")
}

class PlusOrLessMaster(max: Int) {

    private val number: Int = Random().nextInt(max)

    fun ask(test: Int): Ratio {
        return when {
            test < number -> Ratio.TOO_SMALL
            test > number -> Ratio.TOO_BIG
            else -> Ratio.EQUALS
        }
    }

    fun plusOrLess() {
        var input: String?
        var guess: Int?
        do {
            input = readLine()
            guess = try {
                input?.toInt()
            } catch (e: NumberFormatException) {
                null
            }
            println(if (guess == null) "Bad entry" else ask(guess).value)
        } while (number != guess)
    }
}

fun printStepSummaryLine(step: Int, next: Int, max: Int) {
    val lineSize = 190
    val char = "*"
    println(step.toString() + " " + char.repeat((next * lineSize) / max) + " " + next.toString())
}

fun solve(max: Int) {
    printStepSummaryLine(0, max, max)
    val master = PlusOrLessMaster(max)
    var mini = 0
    var maxi = max
    var next = mini + (mini + maxi) / 2
    var step = 0
    loop@ while (true) {
        step++
        val answer = master.ask(next)
        printStepSummaryLine(step, next, max)
        when (answer) {
            Ratio.TOO_BIG -> maxi = next
            Ratio.TOO_SMALL -> mini = next
            Ratio.EQUALS -> break@loop
        }
        next = (mini + maxi) / 2
    }
}

