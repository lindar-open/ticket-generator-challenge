package data

import bingo.service.exception.EmptyColumnException
import bingo.service.exception.TicketContentException
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

class BingoTicketTest {

    @Test
    fun `should create valid ticket`() {
        // given
        val row1 = listOf(1, 11, 21, 31, 0, 0, 0, 0, 81)
        val row2 = listOf(2, 12, 22, 0, 42, 0, 0, 0, 90)
        val row3 = listOf(3, 0, 0, 0, 0, 57, 67, 77, 87)

        // when
        val bingoTicket = BingoTicket(row1, row2, row3)

        // then
        assertThat(bingoTicket.row1).containsExactly(1, 11, 21, 31, 0, 0, 0, 0, 81)
        assertThat(bingoTicket.row2).containsExactly(2, 12, 22, 0, 42, 0, 0, 0, 90)
        assertThat(bingoTicket.row3).containsExactly(3, 0, 0, 0, 0, 57, 67, 77, 87)
    }

    @Test
    fun `should fail to create ticket with empty column`() {
        val row1 = listOf(1, 11, 21, 31, 0, 0, 0, 0, 81)
        val row2 = listOf(2, 12, 22, 0, 42, 0, 0, 0, 90)
        val row3 = listOf(3, 15, 0, 0, 0, 57, 0, 77, 87)

        assertThatThrownBy { BingoTicket(row1, row2, row3) }
            .isInstanceOf(EmptyColumnException::class.java)
    }

    @Test
    fun `should fail to create ticket value 3x elsewhere than column 4`() {
        val row1 = listOf(1, 11, 21, 31, 0, 0, 0, 0, 81)
        val row2 = listOf(2, 12, 22, 0, 32, 0, 0, 0, 90)
        val row3 = listOf(3, 0, 0, 0, 47, 57, 67, 77, 0)

        assertThatThrownBy { BingoTicket(row1, row2, row3) }
            .isInstanceOf(TicketContentException::class.java)
    }

    @Test
    fun `should fail to create ticket more than 15 values`() {
        val row1 = listOf(1, 11, 21, 31, 41, 51, 0, 0, 81)
        val row2 = listOf(2, 12, 22, 0, 42, 0, 0, 0, 90)
        val row3 = listOf(3, 0, 0, 0, 0, 57, 67, 77, 87)

        assertThatThrownBy { BingoTicket(row1, row2, row3) }
            .isInstanceOf(TicketContentException::class.java)
    }

    @Test
    fun `should fail to create ticket more less 15 values`() {
        val row1 = listOf(1, 11, 21, 0, 0, 0, 0, 0, 81)
        val row2 = listOf(2, 12, 22, 0, 42, 0, 0, 0, 90)
        val row3 = listOf(3, 0, 0, 0, 0, 57, 67, 77, 87)

        assertThatThrownBy { BingoTicket(row1, row2, row3) }
            .isInstanceOf(TicketContentException::class.java)
    }

    @Test
    fun `should fail to create ticket with 15 values distributed other way than 3 rows 5 vals each`() {
        val row1 = listOf(1, 11, 21, 0, 0, 0, 0, 0, 81)
        val row2 = listOf(2, 12, 22, 32, 42, 0, 0, 0, 90)
        val row3 = listOf(3, 0, 0, 0, 0, 57, 67, 77, 87)

        assertThatThrownBy { BingoTicket(row1, row2, row3) }
            .isInstanceOf(TicketContentException::class.java)
    }

    @Test
    fun `should fail to create ticket with value exceeding 90`() {
        val row1 = listOf(1, 11, 21, 0, 0, 0, 0, 71, 81)
        val row2 = listOf(2, 12, 22, 0, 42, 0, 0, 0, 91)
        val row3 = listOf(3, 0, 0, 0, 0, 57, 67, 77, 87)

        assertThatThrownBy { BingoTicket(row1, row2, row3) }
            .isInstanceOf(TicketContentException::class.java)
    }

    @Test
    fun `should fail to create ticket with value exceeding below 0`() {
        val row1 = listOf(-1, 11, 21, 0, 0, 0, 0, 0, 81)
        val row2 = listOf(2, 12, 22, 0, 42, 0, 0, 0, 91)
        val row3 = listOf(3, 0, 0, 0, 0, 57, 67, 77, 87)

        assertThatThrownBy { BingoTicket(row1, row2, row3) }
            .isInstanceOf(TicketContentException::class.java)
    }
}
