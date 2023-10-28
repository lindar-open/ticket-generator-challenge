package bingo.service

class TicketTemplate {

    val row1 = mutableListOf(0, 0, 0, 0, 0, 0, 0, 0, 0)
    val row2 = mutableListOf(0, 0, 0, 0, 0, 0, 0, 0, 0)
    val row3 = mutableListOf(0, 0, 0, 0, 0, 0, 0, 0, 0)

    fun add(values: List<Int>) {

        val numberOfValuesToBeAdded = values.filterNot { it == 0 }.size
        if (numberOfValuesToBeAdded > 15 || numberOfValuesToBeAdded > emptySlotsCount()) throw IllegalStateException("Ticket can contain only 15 entries")

        val dataCanBeProcessed = values.all {
            row1.canAccept(it) || row2.canAccept(it) || row3.canAccept(it)
        }

        if (!dataCanBeProcessed) throw IllegalStateException("given values cannot be added to this Ticket")

        values.forEach {
            when {
                row1.canAccept(it) -> row1.addValue(it)
                row2.canAccept(it) -> row2.addValue(it)
                row3.canAccept(it) -> row3.addValue(it)
            }
        }
    }

    private fun MutableList<Int>.addValue(value: Int) {
        val column = valueToColumn(value)
        if (this[column] == 0) {
            this[column] = value
        }
    }


    private fun List<Int>.canAccept(value: Int): Boolean {
        if (!this.hasEmptySlots()) {
            return false
        }
        val column = valueToColumn(value)
        return this[column] == 0
    }

    private fun List<Int>.hasEmptySlots() = this.filter { it != 0 }.size < 5

    private fun List<Int>.emptySlotsCount() = 5 - this.filter { it != 0 }.size

    private fun emptySlotsCount() =
        this.row1.emptySlotsCount() + this.row2.emptySlotsCount() + this.row3.emptySlotsCount()

    fun addAsMayAsPossible(datasource: MutableList<MutableList<Int>>) {
        val usedValues = mutableListOf<Pair<MutableList<Int>, Int>>()
        datasource.forEach { dataRow ->
            for (value in dataRow) {
                if (row1.canAccept(value)) {
                    row1.addValue(value)
                    usedValues.add(dataRow to value)
                    continue
                }
                if (row2.canAccept(value)) {
                    row2.addValue(value)
                    usedValues.add(dataRow to value)
                    continue
                }
                if (row3.canAccept(value)) {
                    row3.addValue(value)
                    usedValues.add(dataRow to value)
                    continue
                }
            }
        }
        usedValues.forEach { it.first.remove(it.second) }
        datasource.removeIf { it.isEmpty() }
    }

    fun canAccept(value: Int) = row1.canAccept(value) || row2.canAccept(value) || row3.canAccept(value)

    fun canTransfer(brokenTicket: TicketTemplate, data: Int): Boolean {
        val index = valueToColumn(data)

        val row = brokenTicket.brokenRow() ?: return false

        val returnSlots = row.unusedIndexes()

        return (row1[index] == 0 && returnSlots.any { row1[it] != 0 && (row2[it] != 0 || row3[it] != 0) }) ||
                (row2[index] == 0 && returnSlots.any { row2[it] != 0 && (row1[it] != 0 || row3[it] != 0) }) ||
                (row3[index] == 0 && returnSlots.any { row3[it] != 0 && (row1[it] != 0 || row2[it] != 0) })
    }

    private fun brokenRow() = row3.takeIf { it.hasEmptySlots() } ?: row2.takeIf { it.hasEmptySlots() }
    ?: row1.takeIf { it.hasEmptySlots() }

    fun isValid(): Boolean = !row1.hasEmptySlots() && !row2.hasEmptySlots() && !row3.hasEmptySlots()

    private fun MutableList<Int>.swap(other: MutableList<Int>, index: Int) {
        val temp = this[index]
        this[index] = other[index]
        other[index] = temp
    }

    fun sortColumns() {
        (0..8).forEachIndexed { index, _ ->
            if (row1[index] != 0 && row2[index] != 0 && row1[index] > row2[index]) {
                row1.swap(row2, index)
            }
            if (row1[index] != 0 && row3[index] != 0 && row1[index] > row3[index]) {
                row1.swap(row3, index)
            }
            if (row2[index] != 0 && row3[index] != 0 && row2[index] > row3[index]) {
                row2.swap(row3, index)
            }
        }
    }

    /**
     * If `otherTicket` can receive data then one compatible datapoint will be migrated to `this` to keep both tickets valid.
     * `otherTicket` will receive data.
     */
    fun transferData(otherTicket: TicketTemplate, data: Int) {
        val column = valueToColumn(data)
        val brokenRow = this.brokenRow() ?: return
        var unusedIndexes = brokenRow.unusedIndexes()
        val receivableIndexesRow1: List<Int> =
            unusedIndexes.filter { otherTicket.row1[it] != 0 && (otherTicket.row2[it] != 0 || otherTicket.row3[it] != 0) }
        if (receivableIndexesRow1.isNotEmpty() && otherTicket.row1[column] == 0) {
            val receivableIndex = receivableIndexesRow1.first()
            brokenRow.swap(otherTicket.row1, receivableIndex)
            otherTicket.row1[column] = data
            return
        }

        unusedIndexes = brokenRow.unusedIndexes()
        val receivableIndexesRow2: List<Int> =
            unusedIndexes.filter { otherTicket.row2[it] != 0 && (otherTicket.row1[it] != 0 || otherTicket.row3[it] != 0) }
        if (receivableIndexesRow2.isNotEmpty() && otherTicket.row2[column] == 0) {
            val receivableIndex = receivableIndexesRow2.first()
            brokenRow.swap(otherTicket.row2, receivableIndex)
            otherTicket.row2[column] = data
            return
        }

        unusedIndexes = brokenRow.unusedIndexes()
        val receivableIndexesRow3: List<Int> =
            unusedIndexes.filter { otherTicket.row3[it] != 0 && (otherTicket.row1[it] != 0 || otherTicket.row2[it] != 0) }
        if (receivableIndexesRow3.isNotEmpty() && otherTicket.row3[column] == 0) {
            val receivableIndex = receivableIndexesRow3.first()
            brokenRow.swap(otherTicket.row2, receivableIndex)
            otherTicket.row2[column] = data
            return
        }
    }
}

private fun List<Int>.unusedIndexes(): List<Int> =
    this.mapIndexedNotNull { index, value -> index.takeIf { value == 0 } }

fun valueToColumn(value: Int): Int =
    when (value) {
        90 -> 8
        else -> value / 10
    }
