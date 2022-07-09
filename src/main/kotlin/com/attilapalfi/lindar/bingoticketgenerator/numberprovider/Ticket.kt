package com.attilapalfi.lindar.bingoticketgenerator.numberprovider

class Ticket {

    private val ticket: IntArray = IntArray(27)

    fun setValue(row: Int, column: Int, value: Int) {
        ticket[row * 9 + column] = value
    }

    fun getValue(row: Int, column: Int): Int {
        return ticket[row * 9 + column]
    }

    fun canBeBlankByColumn(row: Int, column: Int): Boolean {
        val otherRow1 = (row + 1) % 3
        val otherRow2 = (row + 2) % 3
        return getValue(otherRow1, column) != BLANK || getValue(otherRow2, column) != BLANK
    }

    override fun toString(): String {
        val stringBuilder = StringBuilder()
        for (row in 0 until 3) {
            for (column in 0 until 9) {
                stringBuilder.append(getValue(row, column)).append(" ")
            }
            stringBuilder.append("\n")
        }
        stringBuilder.append("\n---------------------\n")
        return stringBuilder.toString()
    }
}