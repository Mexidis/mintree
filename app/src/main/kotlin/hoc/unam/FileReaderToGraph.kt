package hoc.unam

import java.io.File
import java.io.IOException

class FileReaderToGraph(private val filePath: String) {

    fun createGraphFromFile(): Graph {
        val file = File(filePath)
        if (!file.exists()) {
            throw IOException("File does not exist: $filePath")
        }

        val nodesList = mutableSetOf<String>()
        val edgesList = mutableListOf<Edge>()

        //get relevant info about the file
        file.forEachLine { line ->
            if (line.isNotBlank() && !line.startsWith("#")) {
                val parts = line.split(",").map { it.trim() }
                if (parts.size == 3) {
                    try {
                        val u = parts[0]
                        val v = parts[1]
                        val weight = parts[2].toDouble()

                        nodesList.add(u)
                        nodesList.add(v)
                        edgesList.add(Edge(u, v, weight))
                    } catch (e: NumberFormatException) {
                        System.err.println("Warning, wrong line size: $line, $e")
                    }
                } else {
                    System.err.println("Warning, wrong line format: $line")
                }
            }
        }

        // create the graph
        val graph = Graph(nodesList.size, nodesList)

        // fill the graph
        for (edge in edgesList) {
            graph.addEdge(edge.source, edge.destination, edge.weight)
        }

        return graph
    }
}
