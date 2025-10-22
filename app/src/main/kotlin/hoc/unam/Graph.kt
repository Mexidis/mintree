package hoc.unam

import kotlin.math.min

open class Graph (private val nodes: Int, private val nodesList: MutableSet<String>, private val edges: MutableList<Edge>) {
    private val adjMatrix: Array<DoubleArray> =
        Array(nodes) { rowIndex ->         // We get the current row index
            DoubleArray(nodes) { colIndex -> // We get the current column index
                // Apply the logic: 0 on the diagonal, Infinity otherwise
                if (rowIndex == colIndex) 0.0 else Double.POSITIVE_INFINITY
            }
        }

    private lateinit var adjMatrixWarshalled: Array<DoubleArray> // matriz con distancias mínimas
    private lateinit var completedGraph: Array<DoubleArray> //matriz completada

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

    fun primsAlgorithm(k: Int): Double{
        val inMST = BooleanArray(this.nodes)
        val keyValues = FloatArray(this.nodes) { Float.POSITIVE_INFINITY }
        val parents = IntArray(this.nodes) {-1}
        var mstWeight = 0.0

        keyValues[0] = 0.0F  // Starting vertex
        for (i in 0 until  k) {
            val u: Int = (0 until this.nodes)
                .filter { v -> !inMST[v] } // 1. Filtra los índices que NO están en el MST
                .minByOrNull { v -> keyValues[v] } // 2. Encuentra el índice 'v' con el menor keyValues[v]
                ?: break // Si el subgrafo no está conectado o ya se seleccionaron todos, rompe.

            inMST[u] = true

            if (parents[u] != -1){
                // Usamos la lista de nodos para obtener los nombres
                val parentName = nodesList.find { indexedValues[it] == parents[u] } ?: "???"
                val childName = nodesList.find { indexedValues[it] == u } ?: "???"
                // El peso está en la matriz adjMatrix
                val weight = adjMatrix[u][parents[u]]

                mstWeight += weight

                println("$parentName-$childName \t$weight")
            }

            //ciclo interior: Actualizar keyValues y parents para todos los vecinos de 'u'
            for (v in 0 until this.nodes) {
                val weight = adjMatrix[u][v]

                // Condición de actualización:
                // a) La arista (u, v) tiene un peso positivo (0 < weight)
                // b) El peso es menor que el keyValues actual de v (weight < keyValues[v])
                // c) El nodo v aún no está en el MST (not in_mst[v])
                if (weight > 0 && weight < keyValues[v] && !inMST[v]) {
                    // Actualiza el keyValues (costo para conectar v al MST)
                    keyValues[v] = weight.toFloat() // Convertimos Double a Float para el FloatArray

                    // Actualiza el padre de v (la arista más barata que conecta v al MST)
                    parents[v] = u
                }
            }
        }
        return mstWeight
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

    fun printGraphMatrix(type: MatrixType) {
        val matrixToPrint = when (type) {
            MatrixType.ADJACENCY -> adjMatrix
            MatrixType.SHORTEST_PATHS -> adjMatrixWarshalled
            MatrixType.COMPLETED -> completedGraph
        }

        val title = when (type) {
            MatrixType.ADJACENCY -> "Adjacency Matrix (Original)"
            MatrixType.SHORTEST_PATHS -> "Shortest Paths (Floyd-Warshall)"
            MatrixType.COMPLETED -> "Completed Graph (Diam. Weighted)"
        }

        val sortedNodes = nodesList.sorted()
        println("\n--- $title ---")

        print("      ")
        sortedNodes.forEach { print("%-6s ".format(it)) }
        println("\n" + "------".repeat(nodes + 1))

        for (sourceNode in sortedNodes) {
            print("%-5s|".format(sourceNode))
            val u = indexedValues[sourceNode]!!

            for (destNode in sortedNodes) {
                val v = indexedValues[destNode]!!

                // Usar la matriz seleccionada
                val distance = matrixToPrint[u][v]

                val displayValue = if (distance == Double.POSITIVE_INFINITY) "Inf" else "%.1f".format(distance)
                print("%-6s ".format(displayValue))
            }
            println()
        }
        println("------------------------------")
    }
}
