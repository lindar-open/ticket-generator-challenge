package data

import bingo.service.exception.EmptyColumnException
import bingo.service.exception.TicketContentException
import bingo.service.valueToColumn


data class BingoTicket(val row1: List<Int>, val row2: List<Int>, val row3: List<Int>) {

    init {
        row1.validate()
        row2.validate()
        row3.validate()
        validateColumns(row1, row2, row3)
    }

    private fun validateColumns(row1: List<Int>, row2: List<Int>, row3: List<Int>) {
        for (column in (0..8)) {
            if (row1.hasNoData(column) && row2.hasNoData(column) && row3.hasNoData(column)) {
                throw EmptyColumnException("Empty ticket column is not allowed")
            }
        }
    }

    private fun List<Int>.hasNoData(index: Int) = this[index] == 0

    private fun List<Int>.validate() {

        if (this.any { it > 90 || it < 0 })
            throw TicketContentException("Ticket min value is 1 max value is 90, and empty space is represented by 0 $this")

        this.takeIf { it.size == 9 }
            ?.filterNot { it == 0 }
            ?.takeIf { it.size == 5 }
            ?: throw TicketContentException("Each ticket row must contain 5 numbers and 4 empty fields, but was: $this")
        this.forEachIndexed { index, value ->
            if (value != 0 && index != valueToColumn(value)) throw TicketContentException(
                "Cannot create valid ticket with value: $value in ticket column: ${index + 1}"
            )
        }
    }
}
