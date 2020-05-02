package com.example.rescalc

class FromColorNameError(message:String): Exception(message)
class GetResourceError(message:String): Exception(message)
class GetResistanceError(message:String): Exception(message)
class GetMultiplicandError(message:String): Exception(message)
class GetToleranceError(message:String): Exception(message)
class GetCoefficientError(message:String): Exception(message)

enum class Colors {
    BLACK, BROWN, RED, ORANGE, YELLOW, GREEN, BLUE, VIOLET, GREY, WHITE, SILVER, GOLD, NONE;

    companion object {
        fun fromColorName(colorName: String): Colors {
            return when(colorName.toLowerCase()) {
                "none" -> Colors.NONE
                "silver" -> Colors.SILVER
                "gold" -> Colors.GOLD
                "black" -> Colors.BLACK
                "brown" -> Colors.BROWN
                "red" -> Colors.RED
                "orange" -> Colors.ORANGE
                "yellow" -> Colors.YELLOW
                "green" -> Colors.GREEN
                "blue" -> Colors.BLUE
                "violet" -> Colors.VIOLET
                "grey" -> Colors.GREY
                "white" -> Colors.WHITE
                else -> throw FromColorNameError("")
            }
        }

        fun getResource(color: Colors): Int {
            return when(color) {
                Colors.NONE -> R.drawable.resistor_color_none
                Colors.SILVER -> R.drawable.resistor_color_silver
                Colors.GOLD -> R.drawable.resistor_color_gold
                Colors.BLACK -> R.drawable.resistor_color_black
                Colors.BROWN -> R.drawable.resistor_color_brown
                Colors.RED -> R.drawable.resistor_color_red
                Colors.ORANGE -> R.drawable.resistor_color_orange
                Colors.YELLOW -> R.drawable.resistor_color_yellow
                Colors.GREEN-> R.drawable.resistor_color_green
                Colors.BLUE -> R.drawable.resistor_color_blue
                Colors.VIOLET -> R.drawable.resistor_color_violet
                Colors.GREY -> R.drawable.resistor_color_grey
                Colors.WHITE -> R.drawable.resistor_color_white
                else -> throw GetResourceError("")
            }
        }

        fun getResistance(color: Colors): Int {
            return when(color) {
                Colors.BLACK -> 0
                Colors.BROWN -> 1
                Colors.RED -> 2
                Colors.ORANGE -> 3
                Colors.YELLOW -> 4
                Colors.GREEN-> 5
                Colors.BLUE -> 6
                Colors.VIOLET -> 7
                Colors.GREY -> 8
                Colors.WHITE -> 9
                else -> throw GetResistanceError("")
            }
        }

        fun getMultiplicand(color: Colors): Float {
            return when(color) {
                Colors.SILVER -> 0.01f
                Colors.GOLD -> 0.1f
                Colors.BLACK -> 1f
                Colors.BROWN -> 10f
                Colors.RED -> 100f
                Colors.ORANGE -> 1000f
                Colors.YELLOW -> 10000f
                Colors.GREEN -> 100000f
                Colors.BLUE -> 1000000f
                Colors.VIOLET -> 10000000f
                Colors.GREY -> 100000000f
                Colors.WHITE -> 1000000000f
                else -> throw GetMultiplicandError("")
            }
        }

        fun getTolerance(color: Colors): Float {
            return when(color) {
                Colors.SILVER -> 10f
                Colors.GOLD -> 5f
                Colors.BROWN -> 1f
                Colors.RED -> 2f
                Colors.GREEN -> 0.5f
                Colors.BLUE -> 0.25f
                Colors.VIOLET -> 0.1f
                Colors.GREY -> 0.05f
                else -> throw GetToleranceError("")
            }
        }

        fun getCoefficient(color: Colors): Float {
            return when(color) {
                Colors.BROWN -> 100f
                Colors.RED -> 50f
                Colors.ORANGE -> 15f
                Colors.YELLOW -> 25f
                Colors.BLUE -> 10f
                Colors.VIOLET -> 5f
                Colors.WHITE -> 1f
                else -> throw GetCoefficientError("")
            }
        }
    }
}
