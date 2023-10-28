package bingo.service

import data.BingoTicket
import data.TicketStripe
import transpose

class BingoTicketGenerator {

    fun generateBingoTicketStripe(): TicketStripe {
        val valuesTemplate = bingoStripeValuesTemplate()
        valuesTemplate.forEach { it.shuffle() }
        val bingoDataSource = valuesTemplate.transpose()
        bingoDataSource.forEach { it.shuffle() }

        val stripeTemplate = (1..6).map {
            val ticketRowsTemplate = TicketTemplate()
            ticketRowsTemplate.add(bingoDataSource.removeFirst())
            ticketRowsTemplate
        }

        while (stripeTemplate.any { ticket -> bingoDataSource.flatten().any { ticket.canAccept(it) } }) {
            stripeTemplate.filter { !it.isValid() }.forEach { ticketTemplate ->
                ticketTemplate.addAsMayAsPossible(bingoDataSource)
            }

            if (bingoDataSource.hasAvailableData()) {
                fixInvalidTickets(stripeTemplate, bingoDataSource)
            }
        }

        val bingoTickets = stripeTemplate.map { ticketDraft ->
            ticketDraft.sortColumns()
            BingoTicket(ticketDraft.row1, ticketDraft.row2, ticketDraft.row3)
        }
        return TicketStripe(bingoTickets)
    }

    private fun bingoStripeValuesTemplate(): List<MutableList<Int>> = listOf(
        (1..9).toMutableList(),
        (10..19).toMutableList(),
        (20..29).toMutableList(),
        (30..39).toMutableList(),
        (40..49).toMutableList(),
        (50..59).toMutableList(),
        (60..69).toMutableList(),
        (70..79).toMutableList(),
        (80..90).toMutableList(),
    )

    private fun fixInvalidTickets(stripe: List<TicketTemplate>, bingoValuesDataSource: List<MutableList<Int>>) {
        for (ticketIndex in stripe.indices.reversed()) {
            val ticketTemplate = stripe[ticketIndex]

            while (bingoValuesDataSource.hasAvailableData()) {
                if (ticketTemplate.isValid()) {
                    break
                }

                rebalanceWithOtherTicket(ticketTemplate, stripe, bingoValuesDataSource)

                if (!bingoValuesDataSource.hasAvailableData()) {
                    return
                }
            }

        }
    }

    private fun rebalanceWithOtherTicket(
        brokenTicket: TicketTemplate,
        stripeTemplate: List<TicketTemplate>,
        dataSource: List<MutableList<Int>>
    ) {
        val data = dataSource.find { it.isNotEmpty() }!!.removeFirst()
        val compatibleTicket = stripeTemplate.findCompatibleTicket(brokenTicket, data)

        brokenTicket.transferData(compatibleTicket, data)
    }

    private fun List<TicketTemplate>.findCompatibleTicket(
        brokenTicket: TicketTemplate,
        data: Int
    ): TicketTemplate {
        return this.first { brokenTicket.canTransfer(it, data) }
    }

    private fun List<MutableList<Int>>.hasAvailableData() = this.flatten().isNotEmpty()

}
