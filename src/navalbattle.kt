import java.lang.Exception

enum class Direction {
    UP, DOWN, RIGHT, LEFT
}

fun inputDirection(message: String): Direction {
    println(message)
    var input: Direction?
    do {
        input = when (readLine()) {
            "up" -> Direction.UP
            "down" -> Direction.DOWN
            "right" -> Direction.RIGHT
            "left" -> Direction.LEFT
            else -> {
                null
            }
        }
    } while (input == null)
    return input
}

fun inputInt(message: String = ""): Int {
    println(message)
    while (true) {
        // todo refactor with do loop
        val input = readLine()
        val number = try {
            input?.toInt()
        } catch (e: NumberFormatException) {
            null
        }
        if (number != null) return number
    }
}

val SHIP_CHOICES = setOf(
        Ship("avion", 2),
        Ship("sous-marin", 3),
        Ship("contre-torpilleur", 3),
        Ship("cuirassier", 4),
        Ship("porte-avion", 5)
)

const val MAX_X = 9
const val MAX_Y = 9

class Position(x: Int, y: Int, val direction: Direction) {
    val x = if (x in 0..MAX_X) x else throw Exception("$x is not between 0 and $MAX_X")
    val y = if (x in 0..MAX_Y) y else throw Exception("$y is not between 0 and $MAX_Y")
    override fun toString(): String {
        return "Position(direction=$direction, x=$x, y=$y)"
    }


}

class Ship(val name: String, val size: Int, private val position: Position? = null) {

    init {
        if (!this.isInBorder() && this.position != null) throw Exception("Not in border")
    }

    private fun isInBorder(): Boolean {
        if (this.position == null) return false
        var x = this.position.x
        var y = this.position.y
        for (i in 0 until this.size) {
            if (x !in 0..MAX_X || y !in 0..MAX_Y) return false
            when (this.position.direction) {
                Direction.UP -> y += 1
                Direction.DOWN -> y -= 1
                Direction.RIGHT -> x += 1
                Direction.LEFT -> x -= 1
            }
        }
        return true
    }

    fun doesNotInterpolate(ship: Ship): Boolean {
        if (this.position == null || ship.position == null) return false
        var x = this.position.x
        var y = this.position.y
        for (i in 0 until this.size) {
            var a = ship.position.x
            var b = ship.position.y
            for (n in 0 until ship.size) {
                if (x == a && y == b) {
                    return false
                }
                when (ship.position.direction) {
                    Direction.UP -> b += 1
                    Direction.DOWN -> b -= 1
                    Direction.RIGHT -> a += 1
                    Direction.LEFT -> a -= 1
                }
            }
            when (this.position.direction) {
                Direction.UP -> y += 1
                Direction.DOWN -> y -= 1
                Direction.RIGHT -> x += 1
                Direction.LEFT -> x -= 1
            }
        }
        return true
    }
}


class Player(private val name: String, private val ships: MutableSet<Ship> = mutableSetOf()) {

    private fun addShip(ship: Ship) {
        for (ship_ in ships) {
            if (!ship.doesNotInterpolate(ship_)) {
                throw Exception("$ship interpolate with $ship_")
            }
        }
        this.ships.add(ship)
    }

    fun setFleet(shipChoices: Set<Ship> = SHIP_CHOICES) {
        println("Set fleet of ${this.name}")
        for (ship in shipChoices) {
            val x = inputInt("Choose x for the ${ship.name} (${ship.size})")
            val y = inputInt("Choose y for the ${ship.name} (${ship.size})")
            val direction = inputDirection("Choose direction for the ${ship.name} (${ship.size})")
            val position = Position(x, y, direction)
            this.addShip(Ship(ship.name, ship.size, position))
        }
    }
}

fun main(args: Array<String>) {
    val players = listOf(Player("Player1"), Player("Player2"))
    for (player in players) {
        player.setFleet()
    }
}