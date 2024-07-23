package com.calimoto


class Graph(
    val nodes: List<Node> = emptyList(),
    val edges: List<Edge> = emptyList(),
) {
    companion object Factory {
        fun createExampleInstance(): Graph {
            val nodes: MutableList<Node> = mutableListOf()
            for (i in 1..9) {
                nodes.add(Node(id = i))
            }

            val edges: MutableList<Edge> = mutableListOf()
            edges.add(Edge(nodesPair = Pair(nodes.first { it.id == 1 }, nodes.first { it.id == 2 }), weight = 2))
            edges.add(Edge(nodesPair = Pair(nodes.first { it.id == 1 }, nodes.first { it.id == 6 }), weight = 7))
            edges.add(Edge(nodesPair = Pair(nodes.first { it.id == 1 }, nodes.first { it.id == 7 }), weight = 3))
            edges.add(Edge(nodesPair = Pair(nodes.first { it.id == 2 }, nodes.first { it.id == 3 }), weight = 4))
            edges.add(Edge(nodesPair = Pair(nodes.first { it.id == 2 }, nodes.first { it.id == 7 }), weight = 6))
            edges.add(Edge(nodesPair = Pair(nodes.first { it.id == 3 }, nodes.first { it.id == 4 }), weight = 2))
            edges.add(Edge(nodesPair = Pair(nodes.first { it.id == 3 }, nodes.first { it.id == 9 }), weight = 2))
            edges.add(Edge(nodesPair = Pair(nodes.first { it.id == 4 }, nodes.first { it.id == 5 }), weight = 1))
            edges.add(Edge(nodesPair = Pair(nodes.first { it.id == 4 }, nodes.first { it.id == 9 }), weight = 8))
            edges.add(Edge(nodesPair = Pair(nodes.first { it.id == 5 }, nodes.first { it.id == 6 }), weight = 6))
            edges.add(Edge(nodesPair = Pair(nodes.first { it.id == 5 }, nodes.first { it.id == 8 }), weight = 2))
            edges.add(Edge(nodesPair = Pair(nodes.first { it.id == 6 }, nodes.first { it.id == 8 }), weight = 5))
            edges.add(Edge(nodesPair = Pair(nodes.first { it.id == 7 }, nodes.first { it.id == 8 }), weight = 1))
            edges.add(Edge(nodesPair = Pair(nodes.first { it.id == 7 }, nodes.first { it.id == 9 }), weight = 3))
            edges.add(Edge(nodesPair = Pair(nodes.first { it.id == 8 }, nodes.first { it.id == 9 }), weight = 4))

            return Graph(nodes = nodes, edges = edges)
        }
    }

    /**
     * Calculates all connections to all nodes in the network
     */
    fun dijkstra(start: Node): DijkstraResult {
        val shortestDistancesFromStartToNode: MutableMap<Node, Int> = mutableMapOf()
        val previous: HashMap<Node, Node?> = hashMapOf()

        nodes.forEach {
            shortestDistancesFromStartToNode[it] = Int.MAX_VALUE
            previous[it] = null
        }
        // we know that the start to start distance must be 0
        shortestDistancesFromStartToNode[start] = 0

        val queue: MutableList<Pair<Node, Int>> = mutableListOf()
        val settled: MutableList<Node> = mutableListOf()

        // add source node e.g. start value to queue
        queue.add(shortestDistancesFromStartToNode.minBy { it.value }.toPair())

        while (queue.isNotEmpty()) {
            // find the node with the lowest cost
            val active: Pair<Node, Int> = queue.minBy { it.second }
            // remove the active Node from the queue
            queue.remove(active)

            val directNeighboursEdgesWithoutSettled: List<Edge> =
                getAllNeighboursEdges(active.first).filter {
                    settled.contains(it.nodesPair.first).not() && settled.contains(it.nodesPair.second)
                        .not()
                }

            directNeighboursEdgesWithoutSettled.forEach { aNeighbourEdge ->
                aNeighbourEdge.nodesPair.getNeighbourNode(active.first)?.let { theNeighbourNode ->
                    val totalDistance = aNeighbourEdge.weight + active.second
                    val currentlyBestKnownShortestDistance: Int? =
                        shortestDistancesFromStartToNode[theNeighbourNode]
                    if (currentlyBestKnownShortestDistance == null || totalDistance < currentlyBestKnownShortestDistance) {
                        // update, we found a shorter route
                        shortestDistancesFromStartToNode[theNeighbourNode] = totalDistance
                        previous[theNeighbourNode] = active.first
                    }
                    queue.add(Pair(theNeighbourNode, totalDistance))
                }
            }
            settled.add(active.first)
        }

        return DijkstraResult(
            source = start,
            distances = shortestDistancesFromStartToNode.filter { it.key.id != start.id },
            precursors = previous,
        )
    }

    fun reconstructFullPathFromPreviousNode(
        target: Node,
        precursors: HashMap<Node, Node?>,
        path: ArrayDeque<Node>
    ): List<Node> {
        val precursor = precursors[target]
        precursor?.let {
            path.addFirst(target)
        }
        if (precursor == null || target == precursors[target]) {
            path.addFirst(target)
            return path
        }
        return reconstructFullPathFromPreviousNode(precursor, precursors, path)
    }

    private fun getAllNeighboursEdges(node: Node): List<Edge> {
        return edges.filter {
            it.nodesPair.first.id == node.id || it.nodesPair.second.id == node.id
        }
    }
}