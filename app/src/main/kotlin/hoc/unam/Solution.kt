package hoc.unam

class Solution(
    val selectedNodes: Set<String>,
    val graph: Graph
) {
    var cost: Double = Double.POSITIVE_INFINITY
        private set

    var violation: Double = Double.POSITIVE_INFINITY
        private set

    var mst: List<Edge> = emptyList()
        private set

    init {
        // Al crear una solución, debe autoevaluarse
        evaluateCostAndViolation()
    }

    private fun evaluateCostAndViolation() {
        //TODO
    }

}