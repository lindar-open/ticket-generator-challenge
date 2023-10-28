package data

import bingo.service.exception.StripeContentException
import bingo.service.exception.StripeLengthException
import bingo.service.exception.TicketContentException
import fixtures.StripeFixture
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

class TicketStripeTest {

    @Test
    fun `should create valid TicketStripe`() {
        // given
        val validStripeContent = StripeFixture.VALID_STRIPE_CONTENT

        // when
        val ticketStripe = TicketStripe(validStripeContent)

        // then
        val stripeData = ticketStripe.tickets
            .flatMapTo(mutableListOf()) { it.row1 + it.row2 + it.row3 }
            .filter { it != 0 }

        assertThat(stripeData).hasSize(90)
        assertThat(stripeData).containsExactlyInAnyOrderElementsOf((1..90))
    }

    @Test
    fun `should fail to create TicketStripe if any value is duplicated`() {
        val duplicatedStripeContent = StripeFixture.STRIPE_CONTENT_WITH_DUPLICATE

        assertThatThrownBy { TicketStripe(duplicatedStripeContent) }
            .isInstanceOf(StripeContentException::class.java)
    }

    @Test
    fun `should fail to create TicketStripe if any value exceeds 90`() {

        assertThatThrownBy { TicketStripe(StripeFixture.invalidStripeContent()) }
            .isInstanceOf(TicketContentException::class.java)
    }

    @Test
    fun `should fail when stripe is too short`() {
        // given
        val row1 = listOf(1, 11, 21, 31, 0, 0, 0, 0, 81)
        val row2 = listOf(2, 12, 22, 0, 42, 0, 0, 0, 90)
        val row3 = listOf(3, 0, 0, 0, 0, 57, 67, 77, 87)
        val bingoTicket = BingoTicket(row1, row2, row3)

        // when
        assertThatThrownBy { TicketStripe(listOf(bingoTicket)) }
            .isInstanceOf(StripeLengthException::class.java)
    }

    @Test
    fun `should fail when stripe is too long`() {
        // given
        val row1 = listOf(1, 11, 21, 31, 0, 0, 0, 0, 81)
        val row2 = listOf(2, 12, 22, 0, 42, 0, 0, 0, 90)
        val row3 = listOf(3, 0, 0, 0, 0, 57, 67, 77, 87)
        val bingoTicket = BingoTicket(row1, row2, row3)
        val tickets = (1..7).map { bingoTicket }

        // when
        assertThatThrownBy { TicketStripe(tickets) }
            .isInstanceOf(StripeLengthException::class.java)
    }
}
