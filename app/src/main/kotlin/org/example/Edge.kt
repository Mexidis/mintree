package org.example

/**
 * Representa una arista en la gráfica.
 * Es una clase de datos para almacenar de forma concisa los dos vértices y el peso.
 *
 * @param V el tipo de dato para los vértices
 * @property u Uno de los vértices de la arista.
 * @property v El otro vértice de la arista.
 * @property weight El peso o costo asociado a la arista.
 */
data class Edge<V>(val u: V, val v: V, val weight: Double)
