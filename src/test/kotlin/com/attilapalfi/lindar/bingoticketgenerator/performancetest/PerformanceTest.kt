package com.attilapalfi.lindar.bingoticketgenerator.performancetest

import com.attilapalfi.lindar.bingoticketgenerator.random.SimpleRandomProvider
import com.attilapalfi.lindar.bingoticketgenerator.ticketgenerator.ArrayBasedTicketGenerator
import org.junit.jupiter.api.Test
import java.util.concurrent.Executors
import java.util.concurrent.Future

class PerformanceTest {

    private val randomProvider = SimpleRandomProvider()

    @Test
    fun `measure time for 10_000 strip generation`() {
        warmUpJvm()

        val strips = 10_000
        val startTime = System.currentTimeMillis()
        for (i in 0 until strips) {
            ArrayBasedTicketGenerator(randomProvider).generateTickets()
        }
        val executionTime = System.currentTimeMillis() - startTime
        println("Generated $strips strips in $executionTime ms.")
    }

    @Test
    fun `measure time for 100_000 strip generation in parallel`() {
        warmUpJvm()

        val strips = 10_000
        val startTime = System.currentTimeMillis()
        val threads = Runtime.getRuntime().availableProcessors()
        val executorService = Executors.newFixedThreadPool(threads)
        val chunkSize = strips / threads
        val futures = mutableListOf<Future<*>>()
        for (i in 0 until threads) {
            val future = executorService.submit {
                for (j in 0..chunkSize) {
                    ArrayBasedTicketGenerator(randomProvider).generateTickets()
                }
            }
            futures.add(future)
        }
        futures.forEach { it.get() }
        executorService.shutdown()
        val executionTime = System.currentTimeMillis() - startTime
        println("Generated $strips strips in parallel in $executionTime ms.")
    }

    private fun warmUpJvm() {
        for (i in 0 until 1000) {
            ArrayBasedTicketGenerator(randomProvider).generateTickets()
        }
    }
}