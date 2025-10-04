package hoc.unam

/**
 * Implementa el algoritmo de Kruskal para encontrar el Árbol Generador de Peso Mínimo (MST).
 */
class KruskalMST<V> {

    // Clase interna para la estructura de datos Union-Find (Conjuntos Disjuntos)
    private class UnionFind<V>(vertices: Set<V>) {
        private val parent = mutableMapOf<V, V>()
        private val rank = mutableMapOf<V, Int>()

        init {
            // Inicialmente, cada vértice es su propio padre (está en su propio conjunto).
            vertices.forEach {
                parent[it] = it
                rank[it] = 0
            }
        }

        /**
         * Encuentra el representante (raíz) del conjunto al que pertenece el vértice v.
         * Aplica compresión de caminos para optimizar búsquedas futuras.
         */
        fun find(v: V): V {
            if (parent[v] != v) {
                parent[v] = find(parent[v]!!)
            }
            return parent[v]!!
        }

        /**
         * Une los conjuntos que contienen a los vértices u y v.
         * Utiliza la unión por rango para mantener el árbol lo más plano posible.
         */
        fun union(u: V, v: V) {
            val rootU = find(u)
            val rootV = find(v)

            if (rootU != rootV) {
                val rankU = rank.getOrDefault(rootU, 0)
                val rankV = rank.getOrDefault(rootV, 0)

                if (rankU < rankV) {
                    parent[rootU] = rootV
                } else if (rankU > rankV) {
                    parent[rootV] = rootU
                } else {
                    parent[rootV] = rootU
                    rank[rootU] = rankU + 1
                }
            }
        }
    }

    /**
     * Ejecuta el algoritmo de Kruskal en la gráfica proporcionada.
     *
     * @param graph La gráfica de entrada.
     * @return Una nueva gráfica que representa el MST.
     */
    // fun findMST(graph: AdjacencyList<V>): AdjacencyList<V> {
    //     val mst = AdjacencyList<V>()
    //     val unionFind = UnionFind(graph.vertices)

    //     // 1. Ordenar todas las aristas por peso de forma ascendente.
    //     val sortedEdges = graph.edges.sortedBy { it.weight }

    //     // 2. Iterar sobre las aristas ordenadas.
    //     for (edge in sortedEdges) {
    //         // 3. Si los vértices de la arista no forman un ciclo (están en diferentes conjuntos)...
    //         if (unionFind.find(edge.u) != unionFind.find(edge.v)) {
    //             // ...añadir la arista al MST.
    //             mst.addEdge(edge.u, edge.v, edge.weight)
    //             // Y unir los conjuntos de los vértices.
    //             unionFind.union(edge.u, edge.v)
    //         }
    //     }
    //     return mst
    // }
}