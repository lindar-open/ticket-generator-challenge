package com.attilapalfi.lindar.bingoticketgenerator.ticketgenerator

import java.util.stream.IntStream

class SixTickets {
    val tickets: ArrayList<Ticket> = ArrayList(6)

    init {
        IntStream.range(0, 6).forEach {
            tickets.add(Ticket())
        }
    }

    override fun toString(): String {
        val stringBuilder = StringBuilder()
        tickets.forEach { stringBuilder.append(it).append("\n") }
        return stringBuilder.toString()
    }
}