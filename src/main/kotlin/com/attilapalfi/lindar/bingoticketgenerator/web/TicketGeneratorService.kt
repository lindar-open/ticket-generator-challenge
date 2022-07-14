package com.attilapalfi.lindar.bingoticketgenerator.web

import com.attilapalfi.lindar.bingoticketgenerator.random.RandomProvider
import com.attilapalfi.lindar.bingoticketgenerator.ticketgenerator.ArrayBasedTicketGenerator
import com.attilapalfi.lindar.bingoticketgenerator.ticketgenerator.TicketGenerator
import org.springframework.stereotype.Service

@Service
class TicketGeneratorService(private val simpleRandomProvider: RandomProvider) {

    fun generateStripOfSix(): StripsResponse {
        val ticketGenerator: TicketGenerator = ArrayBasedTicketGenerator(simpleRandomProvider)
        val sixTickets = ticketGenerator.generateTickets()

        return StripsResponse(
            tickets = sixTickets.tickets.map { ticket -> TicketResponse(ticket.getRowsOfTicket()) }
        )
    }
}
