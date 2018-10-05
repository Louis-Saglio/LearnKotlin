import java.util.*

fun main(args: Array<String>) {
    PlusOrLessAsker().plusOrLess(100)
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
            guess = try {input?.toInt()} catch (e: NumberFormatException) {null}
            println(if (guess == null) "Bad entry" else ask(guess).value)
        } while (number != guess)
    }
}

