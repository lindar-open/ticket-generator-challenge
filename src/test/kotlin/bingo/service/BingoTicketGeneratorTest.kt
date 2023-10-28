package bingo.service

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class BingoTicketGeneratorTest {

    @Test
    fun `should create valid tickets stripe`() {
        // given
        val bingoTicketGenerator = BingoTicketGenerator()
        // when
        val ticketStripe = bingoTicketGenerator.generateBingoTicketStripe()

        // then
        val stripeData = ticketStripe.tickets
            .flatMapTo(mutableListOf()) { it.row1 + it.row2 + it.row3 }
            .filter { it != 0 }

        Assertions.assertThat(stripeData).hasSize(90)
        Assertions.assertThat(stripeData).containsExactlyInAnyOrderElementsOf((1..90))
    }
}
