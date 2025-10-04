package hoc.unam

class AdjacencyList<T>: Graph<T>{
    private val adjacencies: HashMap<Vertex<T>, ArrayList<Edge<T>>> = HashMap()
    private val vertexMap: HashMap<T, Vertex<T>> = HashMap()
    
    override fun createVertex(data: T): Vertex<T> {
        // Si el vértice ya existe, devolverlo
        vertexMap[data]?.let { return it }
        
        // Si no existe, crearlo y almacenarlo
        val vertex = Vertex(adjacencies.count(), data)
        adjacencies[vertex] = ArrayList()
        vertexMap[data] = vertex // Almacenar el mapeo
        return vertex
    }

    override fun addDirectedEdge(source: Vertex<T>, destination: Vertex<T>, weight: Double?) {
        val edge = Edge(source, destination, weight)
        adjacencies[source]?.add(edge)
    }

    override fun addUndirectedEdge(source: Vertex<T>, destination: Vertex<T>, weight: Double?) {
        addDirectedEdge(source, destination, weight)
        addDirectedEdge(destination, source, weight)
    }

    override fun add(edge: EdgeType, source: Vertex<T>, destination: Vertex<T>, weight: Double?) {
        when (edge) {
            EdgeType.DIRECTED -> addDirectedEdge(source, destination, weight)
            EdgeType.UNDIRECTED -> addUndirectedEdge(source, destination, weight)
        }
    }

    override fun edges(source: Vertex<T>) = adjacencies[source] ?: arrayListOf()
    override fun weight(source: Vertex<T>, destination: Vertex<T>): Double? {
        return edges(source).firstOrNull { it.destination == destination }?.weight
    }

    fun vertexExistence(source: Vertex<T>): Boolean{
        return adjacencies.containsKey(source)
    }


    override fun toString(): String {
        return buildString {
            adjacencies.forEach { (vertex, edges) -> 
                
                // 1. Formateamos la lista de aristas para mostrar SOLO el dato del destino.
                val edgeString = edges.joinToString { it.destination.data.toString() }
                
                // 2. Usamos append() una sola vez, incluyendo la nueva línea (\n)
                // Esto garantiza que todos los vecinos estén en una sola línea.
                append("${vertex.data} ---> [ $edgeString ]\n")
            }
        }   
    }

}