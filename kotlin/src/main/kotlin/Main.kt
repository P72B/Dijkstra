package com.calimoto

fun main() {
    val graph: Graph = Graph.createExampleInstance()
    val result: DijkstraResult = graph.dijkstra(start = graph.nodes.first())

    println("Nodes: ${graph.nodes.size}\nEdges: ${graph.edges.size}\nDistances: ${result.distances.size}")
}