package com.calimoto

data class Node(
    val id: Int
)

data class Edge(
    val nodesPair: Pair<Node, Node>,
    val weight: Int,
)

fun Pair<Node, Node>.getNeighbourNode(node: Node): Node? {
    if (node.id != this.first.id && node.id != this.second.id) {
        return null
    }
    if (node.id == this.first.id) {
        return this.second
    }
    return this.first
}


/**
 * Contains distances from the given source node to all other vertices (nodes)
 */
data class DijkstraResult(
    val source: Node,
    val distances: Map<Node, Int> = hashMapOf(),
    val precursors: HashMap<Node, Node?>
)