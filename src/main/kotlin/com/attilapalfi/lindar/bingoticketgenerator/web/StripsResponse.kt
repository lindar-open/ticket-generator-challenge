package com.attilapalfi.lindar.bingoticketgenerator.web

data class StripsResponse(val tickets: List<TicketResponse>) {
    override fun toString(): String {
        val stringBuilder = StringBuilder()
        tickets.forEach {
            stringBuilder.append(it.toString())
                .append("\n-----------------------------------\n")
        }
        return stringBuilder.toString()
    }
}

data class TicketResponse(
    val rows: List<List<Int>>
) {
    override fun toString(): String {
        val stringBuilder = StringBuilder()
        rows.forEach { row ->
            stringBuilder.append(row.toString()).append("\n")
        }
        return stringBuilder.toString()
    }
}