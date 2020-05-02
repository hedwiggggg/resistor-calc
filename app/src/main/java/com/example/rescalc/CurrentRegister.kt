package com.example.rescalc

class CurrentResistor {
    class Bands {
        companion object {
            var first = Colors.NONE
            var second = Colors.NONE
            var third = Colors.NONE
            var fourth = Colors.NONE
            var fifth = Colors.NONE
            var sixth = Colors.NONE

            fun getBand(type: Band): Colors {
                return when(type) {
                    Band.ONE -> first
                    Band.TWO -> second
                    Band.THREE -> third
                    Band.FOUR -> fourth
                    Band.FIVE -> fifth
                    Band.SIX -> sixth
                }
            }

            fun setBand(type: Band, color: Colors) {
                when(type) {
                    Band.ONE -> first = color
                    Band.TWO -> second = color
                    Band.THREE -> third = color
                    Band.FOUR -> fourth = color
                    Band.FIVE -> fifth = color
                    Band.SIX -> sixth = color
                }
            }
        }
    }

    companion object {
        val bands = Bands
    }
}