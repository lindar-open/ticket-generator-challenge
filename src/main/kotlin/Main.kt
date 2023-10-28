import bingo.service.BingoTicketGenerator

fun main() {

    val bingoTicketGenerator = BingoTicketGenerator()
    //warmup
    println("Start Warmup")
    val warmupResult = (1..6).map {
        (1..10000).map {
            bingoTicketGenerator.generateBingoTicketStripe()
        }
    }
    println("WARMUP completed (${warmupResult.size} * 10K stripes) ")
    println("----")
    val numberOfStripes = 10000
    println("Start Generate $numberOfStripes stripes")
    val start = System.currentTimeMillis()
    val result = (1..numberOfStripes).map {
        bingoTicketGenerator.generateBingoTicketStripe()
    }
    val end = System.currentTimeMillis()
    println("${result.size} stripes generated in ${end - start}ms")
}
