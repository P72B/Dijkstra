package com.calimoto

fun main() {
    val graph = Graph.createExampleInstance()

    val result = graph.dijkstra(graph.nodes.first())

    println("Nodes: ${graph.nodes.size}, Edges: ${graph.edges.size}")
}