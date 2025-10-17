package hoc.unam

import kotlin.math.min

open class Graph (private val nodes: Int, private val nodesList: MutableSet<String>) {
    private val adjMatrix: Array<DoubleArray> =
        Array(nodes) { rowIndex ->         // We get the current row index
            DoubleArray(nodes) { colIndex -> // We get the current column index
                // Apply the logic: 0 on the diagonal, Infinity otherwise
                if (rowIndex == colIndex) 0.0 else Double.POSITIVE_INFINITY
            }
        }

    private lateinit var adjMatrixWarshalled: Array<DoubleArray>
    private lateinit var completedGraph: Array<DoubleArray>

    private var diameter: Double? = null
    private val indexedValues: MutableMap<String, Int> = mutableMapOf()

    init {
        var i = 0
        for (node in nodesList) {
            indexedValues[node] = i
            i++
        }
    }

    fun completeGraph(){
        calculateShortestPaths()
        completedGraph = adjMatrixWarshalled.map { it.clone() }.toTypedArray()
        for (i in 0 until nodes) {
            for (j in 0 until nodes) {

                // si no existía la arista
                if (adjMatrix[i][j] == Double.POSITIVE_INFINITY) {

                    val shortestPath = adjMatrixWarshalled[i][j]

                    // los nodos deberían estar conectados
                    if (shortestPath != Double.POSITIVE_INFINITY) {
                        val newWeight = shortestPath * this.diameter!!
                        completedGraph[i][j] = newWeight
                    }
                }
            }
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

    fun getNormalizer(k: Int): Double {
        val edgeWeights = mutableListOf<Double>()
        for (i in 0 until nodes) {
            for (j in i + 1 until nodes) {
                val weight = adjMatrix[i][j]
                if (weight > 0 && weight != Double.POSITIVE_INFINITY) {
                    edgeWeights.add(weight)
                }
            }
        }

        return edgeWeights.sortedDescending().take(k - 1).sum()

    }

    private fun calculateShortestPaths(){
        adjMatrixWarshalled = adjMatrix.map { it.clone() }.toTypedArray()
        val v = adjMatrix.size

        for (k in 0 until v) {
            for (i in 0 until v) {
                for (j in 0 until v) {
                    val pathViaK = adjMatrixWarshalled[i][k] + adjMatrixWarshalled[k][j]

                    // Compara la distancia actual con la nueva ruta a través de k
                    if (pathViaK < adjMatrixWarshalled[i][j]) {
                        adjMatrixWarshalled[i][j] = pathViaK
                    }
                }
            }
        }

        var maxDistance = 0.0

        // Itera sobre la matriz de resultados de Floyd-Warshall
        for (i in 0 until nodes) {
            for (j in 0 until nodes) {
                val distance = adjMatrixWarshalled[i][j]

                if (distance > maxDistance && distance != Double.POSITIVE_INFINITY) {
                    maxDistance = distance
                }
            }
        }

        this.diameter = maxDistance
    }

    fun getDiameter(): Double? {
        return this.diameter
    }



    fun getAdjMatShortestPaths(): Array<DoubleArray>{
        return this.adjMatrixWarshalled
    }

    fun getCompletedGraph(): Array<DoubleArray>{
        return this.completedGraph
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
