package data

import bingo.service.exception.StripeContentException
import bingo.service.exception.StripeLengthException

data class TicketStripe(val tickets: List<BingoTicket>) {
    init {
        tickets.takeIf { it.size == 6 }
            ?: throw StripeLengthException("Invalid TicketStripe size.  Stripe should consists of 6 tickets.")

        tickets.flatMapTo(mutableSetOf()) { it.row1 + it.row2 + it.row3 }
            .takeIf {
                it.containsAll((0..90).toSet()) && it.size == 91
            }
            ?: throw StripeContentException(
                "Ticket Stripe must Contain all and only numbers in the range [1:90]. Duplicates are not allowed."
            )
    }
}
