import com.calimoto.DijkstraResult
import com.calimoto.Graph
import com.calimoto.Node
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GraphTest {

    private lateinit var graph: Graph
    private lateinit var result: DijkstraResult

    private lateinit var startNode: Node

    private fun getNodeFromId(idOfTargetNode: Int): Node {
        return graph.nodes.first { it.id == idOfTargetNode }
    }

    @BeforeEach
    fun init() {
        graph = Graph.createExampleInstance()
        startNode = getNodeFromId(1)
        result = graph.dijkstra(start = startNode)
    }

    @Test
    fun canCalculateDijkstra() {
        assertEquals(8, result.distances.size)
        assertEquals(7, result.distances[getNodeFromId(6)])
        assertEquals(3, result.distances[getNodeFromId(7)])
        assertEquals(2, result.distances[getNodeFromId(2)])
        assertEquals(4, result.distances[getNodeFromId(8)])
        assertEquals(6, result.distances[getNodeFromId(5)])
        assertEquals(6, result.distances[getNodeFromId(9)])
        assertEquals(6, result.distances[getNodeFromId(3)])
        assertEquals(7, result.distances[getNodeFromId(4)])
    }

    @Test
    fun `can route from node 1 to node 1`() {
        val destinationNode = getNodeFromId(1)
        val pathToNode: List<Node> = graph.reconstructFullPathFromPreviousNode(
            destinationNode,
            result.precursors,
            ArrayDeque()
        )
        assertEquals(1, pathToNode.size)
        assertEquals(1, pathToNode.first().id)
    }

    @Test
    fun `can route from node 1 to node 2`() {
        val destinationNode = getNodeFromId(2)
        val pathToNode: List<Node> = graph.reconstructFullPathFromPreviousNode(
            destinationNode,
            result.precursors,
            ArrayDeque()
        )
        assertEquals(2, pathToNode.size)
        assertEquals(1, pathToNode.first().id)
        assertEquals(2, pathToNode[1].id)
    }

    @Test
    fun `can route from node 1 to node 3`() {
        val destinationNode = getNodeFromId(3)
        val pathToNode: List<Node> = graph.reconstructFullPathFromPreviousNode(
            destinationNode,
            result.precursors,
            ArrayDeque()
        )
        assertEquals(3, pathToNode.size)
        assertEquals(1, pathToNode.first().id)
        assertEquals(2, pathToNode[1].id)
        assertEquals(3, pathToNode[2].id)
    }

    @Test
    fun `can route from node 1 to node 4`() {
        val destinationNode = getNodeFromId(4)
        val pathToNode: List<Node> = graph.reconstructFullPathFromPreviousNode(
            destinationNode,
            result.precursors,
            ArrayDeque()
        )
        assertEquals(5, pathToNode.size)
        assertEquals(1, pathToNode.first().id)
        assertEquals(7, pathToNode[1].id)
        assertEquals(8, pathToNode[2].id)
        assertEquals(5, pathToNode[3].id)
        assertEquals(4, pathToNode[4].id)
    }

    @Test
    fun `can route from node 1 to node 5`() {
        val destinationNode = getNodeFromId(5)
        val pathToNode: List<Node> = graph.reconstructFullPathFromPreviousNode(
            destinationNode,
            result.precursors,
            ArrayDeque()
        )
        assertEquals(4, pathToNode.size)
        assertEquals(1, pathToNode.first().id)
        assertEquals(7, pathToNode[1].id)
        assertEquals(8, pathToNode[2].id)
        assertEquals(5, pathToNode[3].id)
    }

    @Test
    fun `can route from node 1 to node 6`() {
        val destinationNode = getNodeFromId(6)
        val pathToNode: List<Node> = graph.reconstructFullPathFromPreviousNode(
            destinationNode,
            result.precursors,
            ArrayDeque()
        )
        assertEquals(2, pathToNode.size)
        assertEquals(1, pathToNode.first().id)
        assertEquals(6, pathToNode[1].id)
    }

    @Test
    fun `can route from node 1 to node 7`() {
        val destinationNode = getNodeFromId(7)
        val pathToNode: List<Node> = graph.reconstructFullPathFromPreviousNode(
            destinationNode,
            result.precursors,
            ArrayDeque()
        )
        assertEquals(2, pathToNode.size)
        assertEquals(1, pathToNode.first().id)
        assertEquals(7, pathToNode[1].id)
    }

    @Test
    fun `can route from node 1 to node 8`() {
        val destinationNode = getNodeFromId(8)
        val pathToNode: List<Node> = graph.reconstructFullPathFromPreviousNode(
            destinationNode,
            result.precursors,
            ArrayDeque()
        )
        assertEquals(3, pathToNode.size)
        assertEquals(1, pathToNode.first().id)
        assertEquals(7, pathToNode[1].id)
        assertEquals(8, pathToNode[2].id)
    }

    @Test
    fun `can route from node 1 to node 9`() {
        val destinationNode = getNodeFromId(9)
        val pathToNode: List<Node> = graph.reconstructFullPathFromPreviousNode(
            destinationNode,
            result.precursors,
            ArrayDeque()
        )
        assertEquals(3, pathToNode.size)
        assertEquals(1, pathToNode.first().id)
        assertEquals(7, pathToNode[1].id)
        assertEquals(9, pathToNode[2].id)
    }
}