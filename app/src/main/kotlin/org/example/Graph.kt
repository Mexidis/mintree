package org.example

/**
 * Representa una gráfica con un conjunto de vértices y una lista de aristas.
 * La estructura es genérica para poder trabajar con cualquier tipo de identificador de vértice.
 *
 * @param V el tipo de dato para los vértices.
 */
class Graph<V> {
    val vertices = mutableSetOf<V>()
    val edges = mutableListOf<Edge<V>>()

    /**
     * Añade una arista a la gráfica.
     * También añade los vértices de la arista al conjunto de vértices si no existen.
     */
    fun addEdge(u: V, v: V, weight: Double) {
        vertices.add(u)
        vertices.add(v)
        edges.add(Edge(u, v, weight))
    }

    override fun toString(): String {
        val edgeStrings = edges.joinToString(separator = "\n  ") {
            "(${it.u} -- ${it.v}, w:${it.weight})"
        }
        return "Graph with ${vertices.size} vertices and ${edges.size} edges:\n  $edgeStrings"
    }
}
