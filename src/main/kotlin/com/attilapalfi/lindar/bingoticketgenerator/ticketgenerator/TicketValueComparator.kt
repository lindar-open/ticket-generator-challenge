package com.attilapalfi.lindar.bingoticketgenerator.ticketgenerator

class TicketValueComparator : Comparator<Int> {

    override fun compare(o1: Int, o2: Int): Int {
//        return o2.compareTo(o1)
        return if (o1 == BLANK || o2 == BLANK) {
            0
        } else {
//            o2.compareTo(o1)
            o1.compareTo(o2)
        }
    }
}