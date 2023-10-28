package fixtures

import data.BingoTicket

class StripeFixture {
    companion object {

        private val DUPLICATE_CLASH_WITH_VALID_TICKET_2 = BingoTicket(
            listOf(1, 13, 0, 37, 0, 52, 0, 0, 83),
            listOf(9, 0, 20, 0, 48, 58, 64, 0, 0),
            listOf(0, 0, 28, 39, 0, 0, 65, 73, 84)
        )
        private val VALID_TICKET_1 = BingoTicket(
            listOf(7, 13, 0, 37, 0, 52, 0, 0, 83),
            listOf(9, 0, 20, 0, 48, 58, 64, 0, 0),
            listOf(0, 0, 28, 39, 0, 0, 65, 73, 84)
        )
        private val VALID_TICKET_2 = BingoTicket(
            listOf(1, 0, 22, 0, 40, 0, 67, 0, 80),
            listOf(0, 15, 0, 36, 0, 0, 69, 71, 82),
            listOf(0, 19, 27, 0, 47, 56, 0, 78, 0)
        )
        private val VALID_TICKET_3 = BingoTicket(
            listOf(2, 11, 0, 0, 46, 0, 62, 0, 81),
            listOf(0, 0, 26, 31, 49, 0, 0, 74, 87),
            listOf(0, 17, 29, 0, 0, 55, 68, 76, 0)
        )
        private val VALID_TICKET_4 = BingoTicket(
            listOf(8, 0, 24, 34, 0, 57, 0, 75, 0),
            listOf(0, 12, 0, 35, 42, 0, 63, 0, 89),
            listOf(0, 18, 0, 0, 44, 59, 0, 79, 90)
        )
        private val VALID_TICKET_5 = BingoTicket(
            listOf(5, 10, 21, 0, 0, 51, 0, 72, 0),
            listOf(0, 14, 0, 33, 41, 0, 66, 0, 86),
            listOf(6, 0, 0, 38, 43, 54, 0, 77, 0)
        )
        private val VALID_TICKET_6 = BingoTicket(
            listOf(0, 0, 23, 30, 0, 50, 60, 0, 85),
            listOf(3, 16, 0, 32, 45, 0, 0, 0, 88),
            listOf(4, 0, 25, 0, 0, 53, 61, 70, 0)
        )

        val VALID_STRIPE_CONTENT = listOf(
            VALID_TICKET_1, VALID_TICKET_2, VALID_TICKET_3,
            VALID_TICKET_4, VALID_TICKET_5, VALID_TICKET_6
        )

        val STRIPE_CONTENT_WITH_DUPLICATE = listOf(
            DUPLICATE_CLASH_WITH_VALID_TICKET_2, VALID_TICKET_2, VALID_TICKET_3,
            VALID_TICKET_4, VALID_TICKET_5, VALID_TICKET_6
        )

        /**
         * This will fail, even data class copy method won't be able to bypass init{} block validation
         */
        fun invalidStripeContent(): MutableList<BingoTicket> {
            val ticketContent = VALID_STRIPE_CONTENT
            val invalidContent = ticketContent[0].row1.toMutableList()
            invalidContent[0] = 91
            val invalidTicket = ticketContent[0].copy(row1 = invalidContent)
            val invalidStripeContent = ticketContent.toMutableList()
            invalidStripeContent[0] = invalidTicket
            return invalidStripeContent
        }

    }
}
