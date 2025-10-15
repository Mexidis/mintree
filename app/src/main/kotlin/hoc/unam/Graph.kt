package hoc.unam

open class Graph (private val nodes: Int, private val nodesList: MutableSet<String>) {
    private val adjMatrix: Array<DoubleArray> =
        Array(nodes) { DoubleArray(nodes) }

    private val indexedValues: MutableMap<String, Int> = mutableMapOf()

    init {
        var i = 0
        for (node in nodesList) {
            indexedValues[node] = i
            i++
        }
    }

    fun addEdge(source: String, destination: String, weight: Double) {
        val u = indexedValues[source]!!
        val v = indexedValues[destination]!!
        adjMatrix[u][v] = weight
        adjMatrix[v][u] = weight
    }

    fun getDistance(source: String, destination: String): Double {
        val u = indexedValues[source]!!
        val v = indexedValues[destination]!!
        return adjMatrix[u][v]
    }


    fun printMatrix() {
        val sortedNodes = nodesList.sorted()
        println("\n--- Adjacency Matrix ---")
        print("      ")
        sortedNodes.forEach { print("%-5s ".format(it)) }
        println("\n" + "------".repeat(nodes + 1))

        for (sourceNode in sortedNodes) {
            print("%-5s|".format(sourceNode))
            for (destNode in sortedNodes) {
                val weight = getDistance(sourceNode, destNode)
                print("%-5.1f ".format(weight))
            }
            println()
        }
        println("------------------------------")
    }
}
