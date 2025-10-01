package hoc.unam

import java.io.File
import java.io.IOException

/**
 * Objeto para leer una gráfica desde un archivo de texto.
 */
object GraphReader {

    /**
     * Lee un archivo de texto y construye un objeto Graph.
     * El formato esperado es: vertice1,vertice2,peso por cada línea.
     *
     * @param filePath La ruta al archivo de la gráfica.
     * @return Un objeto Graph<String> con los datos leídos.
     * @throws IOException si el archivo no puede ser leído.
     * @throws NumberFormatException si el peso no es un número válido.
     */
    fun readFromFile(filePath: String): Graph<String> {
        val graph = Graph<String>()
        val file = File(filePath)

        if (!file.exists()) {
            throw IOException("El archivo no se encuentra en la ruta: $filePath")
        }

        file.forEachLine { line ->
            // Ignorar líneas en blanco o comentarios
            if (line.isNotBlank() && !line.startsWith("#")) {
                val parts = line.split(",")
                if (parts.size == 3) {
                    val u = parts[0].trim()
                    val v = parts[1].trim()
                    try {
                        val weight = parts[2].trim().toDouble()
                        graph.addEdge(u, v, weight)
                    } catch (e: NumberFormatException) {
                        System.err.println("Advertencia: Ignorando linea por peso invalido: $line")
                    }
                } else {
                    System.err.println("Advertencia: Ignorando linea mal formateada: $line")
                }
            }
        }
        return graph
    }
}
