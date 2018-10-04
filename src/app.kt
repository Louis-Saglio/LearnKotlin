import java.util.*

fun main(args: Array<String>) {
    PlusOrLessAsker().plusOrLess(100)
}

class PlusOrLessAsker {

    fun plusOrLess(max: Int=100) {
        val number = Random().nextInt(max)
        var input: String?
        var guess: Int?
        do {
            input = readLine()
            guess = try {input?.toInt()} catch (e: NumberFormatException) {null}
            when {
                guess == null -> println("Bad entry")
                guess < number -> println(TOO_SMALL)
                guess > number -> println(TOO_BIG)
                guess == number -> println("Win $guess")
            }
        } while (number != guess)
    }

    companion object {
        const val TOO_BIG = "Too big"
        const val TOO_SMALL = "To small"
    }
}

