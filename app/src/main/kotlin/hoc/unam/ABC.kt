package hoc.unam

class ABC (
    private val graph: Graph,
    private val k: Int,

    private val SN: Int,  // population size
    private val MCN: Int, // maximun cycle number
    private val limit: Int
){
    private val hive = Hive(SN)
    private val restrictionManager: Nothing = TODO()
    private lateinit var bestGlobalSolution: Solution
}