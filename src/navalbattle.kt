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

fun inputPlace(message: String = ""): Place {
    println(message)
    return Place(inputInt("Choose x"), inputInt("Choose y"))
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

class Position(val place: Place, val direction: Direction) {
    override fun toString(): String {
        return "Position($place, $direction)"
    }
}

class Place(x: Int, y: Int) {
    val x = if (x in 0..MAX_X) x else throw Exception("$x is not between 0 and $MAX_X")
    val y = if (x in 0..MAX_Y) y else throw Exception("$y is not between 0 and $MAX_Y")

    override fun equals(other: Any?): Boolean {
        return when {
            other == null -> false
            other !is Place -> false
            other.x == this.x && other.y == this.y -> true
            else -> false
        }
    }

    override fun hashCode() = setOf(x, y).hashCode()
    override fun toString(): String {
        return "Place($x, $y)"
    }


}

enum class ShipPartState {
    INTACT, DESTROYED
}

class ShipPart(val place: Place, var state: ShipPartState = ShipPartState.INTACT) {
    override fun toString(): String {
        return "Part($place, $state)"
    }
}

class Ship(val name: String, val size: Int, private val position: Position? = null) {

    val parts = this.buildParts()

    private fun getPlaces(): Set<Place> {
        if (this.position == null) return setOf()
        val positions = mutableSetOf<Place>()
        var x = this.position.place.x
        var y = this.position.place.y
        for (i in 0 until this.size) {
            positions.add(Place(x, y))
            when (this.position.direction) {
                Direction.UP -> y += 1
                Direction.DOWN -> y -= 1
                Direction.RIGHT -> x += 1
                Direction.LEFT -> x -= 1
            }
        }
        return positions.toSet()
    }

    fun getPartByPlace(place: Place): ShipPart? {
        for (part in parts) {
            if (part.place == place) return part
        }
        return null
    }

    private fun buildParts(): Set<ShipPart> {
        val parts = mutableSetOf<ShipPart>()
        for (place in this.getPlaces()) {
            parts.add(ShipPart(place))
        }
        return parts.toSet()
    }

    fun doesNotInterpolate(ship: Ship): Boolean {
        if (this.position == null || ship.position == null) return false
        for (part in this.parts) {
            for (part_ in ship.parts) {
                if (part.place == part_.place) return false
            }
        }
        return true
    }

    override fun toString(): String {
        return "Ship(name='$name', size=$size, $position)"
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
            val position = Position(Place(x, y), direction)
            this.addShip(Ship(ship.name, ship.size, position))
        }
    }

    fun getMissileAttack(place: Place): Boolean {
        for (ship in ships) {
            val part = ship.getPartByPlace(place)
            if (part != null && part.state != ShipState.DESTROYED) {
                part.state = ShipState.DESTROYED
                return true
            }
        }
        return false
    }
}

fun main(args: Array<String>) {
    val players = listOf(Player("Player1"), Player("Player2"))
    for (player in players) {
        player.setFleet()
    }
}