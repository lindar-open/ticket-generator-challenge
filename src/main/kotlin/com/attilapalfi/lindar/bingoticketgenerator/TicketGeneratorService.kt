package com.attilapalfi.lindar.bingoticketgenerator

import com.attilapalfi.lindar.bingoticketgenerator.ticketgenerator.TicketGenerator
import com.attilapalfi.lindar.bingoticketgenerator.ticketgenerator.SixTickets
import org.springframework.stereotype.Service

@Service
class TicketGeneratorService(private val ticketGenerator: TicketGenerator) {

    fun generateStripOfSix(): SixTickets {
        ticketGenerator.generateTickets()
        for (ticketIndex in 0 until 6) {

        }
        TODO()
    }
}
