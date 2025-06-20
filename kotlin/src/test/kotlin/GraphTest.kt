import com.calimoto.DijkstraResult
import com.calimoto.Graph
import com.calimoto.Node
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals
import kotlin.test.assertNull

/**
 * Class should unit test the Graph class
 */
class GraphTest {

    // subject under test (sut, e.g. the graph) holds the network relations e.g. nodes, edges with weight
    // setup the example graph from helper factory method
    private val sutGraph: Graph = Graph.createExampleInstance()

    // result data class holds the shortest distances and path relations from a given start node to all other nodes
    private lateinit var result: DijkstraResult

    /**
     * Helper function to get the distance from the dijkstra result to a node in the graph network or null when
     * the node is not found.
     */
    private fun getDistanceToNode(id: Int): Int? {
        runCatching { sutGraph.getNode(id = id) }.getOrNull()?.let {
            return result.distances[it]
        }
        return null
    }

    @BeforeEach
    fun init() {
        // pick the node with id 1 from the routing network to set as start point for the dijkstra algorithm
        val startNode: Node = sutGraph.getNode(id = 1)

        // calculate the shortest distances from the start node to all other nodes
        result = sutGraph.dijkstra(start = startNode)
    }

    @Test
    fun `can calculate shortest distance from default start node to all other nodes in graph using dijkstra`() {
        assertEquals(expected = 8, actual = result.distances.size)
        assertNull(getDistanceToNode(id = 10)) // there are only 9 nodes in the graph and 10 should not exist
        assertNull(getDistanceToNode(id = 1)) // obvious start node to start node should have distance null
        assertEquals(expected = 7, actual = getDistanceToNode(id = 6))
        assertEquals(expected = 3, actual = getDistanceToNode(id = 7))
        assertEquals(expected = 2, actual = getDistanceToNode(id = 2))
        assertEquals(expected = 4, actual = getDistanceToNode(id = 8))
        assertEquals(expected = 6, actual = getDistanceToNode(id = 5))
        assertEquals(expected = 6, actual = getDistanceToNode(id = 9))
        assertEquals(expected = 6, actual = getDistanceToNode(id = 3))
        assertEquals(expected = 7, actual = getDistanceToNode(id = 4))
    }

    @Test
    fun `can route from node 1 to node 1`() {
        val destinationNode = sutGraph.getNode(id = 1)
        val detailedPathFromStartToDestination: List<Node> = sutGraph.reconstructFullPathFromPreviousNode(
            target = destinationNode,
            precursors = result.precursors,
            path = ArrayDeque()
        )
        assertThrows<IndexOutOfBoundsException> { detailedPathFromStartToDestination[1] }
        assertEquals(1, detailedPathFromStartToDestination.size)
        assertEquals(1, detailedPathFromStartToDestination.first().id)
        assertEquals(1, detailedPathFromStartToDestination.last().id)
    }

    @Test
    fun `can route from node 1 to node 2`() {
        val destinationNode = sutGraph.getNode(id = 2)
        val detailedPathFromStartToDestination: List<Node> = sutGraph.reconstructFullPathFromPreviousNode(
            target = destinationNode,
            precursors = result.precursors,
        )
        assertEquals(2, detailedPathFromStartToDestination.size)
        assertEquals(1, detailedPathFromStartToDestination.first().id)
        assertEquals(2, detailedPathFromStartToDestination.last().id)
    }

    @Test
    fun `can route from node 1 to node 3`() {
        val destinationNode = sutGraph.getNode(id = 3)
        val detailedPathFromStartToDestination: List<Node> = sutGraph.reconstructFullPathFromPreviousNode(
            target = destinationNode,
            precursors = result.precursors,
        )
        assertEquals(3, detailedPathFromStartToDestination.size)
        assertEquals(1, detailedPathFromStartToDestination.first().id)
        assertEquals(2, detailedPathFromStartToDestination[1].id)
        assertEquals(3, detailedPathFromStartToDestination.last().id)
    }

    @Test
    fun `can route from node 1 to node 4`() {
        val destinationNode = sutGraph.getNode(id = 4)
        val detailedPathFromStartToDestination: List<Node> = sutGraph.reconstructFullPathFromPreviousNode(
            target = destinationNode,
            precursors = result.precursors,
        )
        assertEquals(5, detailedPathFromStartToDestination.size)
        assertEquals(1, detailedPathFromStartToDestination.first().id)
        assertEquals(7, detailedPathFromStartToDestination[1].id)
        assertEquals(8, detailedPathFromStartToDestination[2].id)
        assertEquals(5, detailedPathFromStartToDestination[3].id)
        assertEquals(4, detailedPathFromStartToDestination.last().id)
    }

    @Test
    fun `can route from node 1 to node 5`() {
        val destinationNode = sutGraph.getNode(id = 5)
        val detailedPathFromStartToDestination: List<Node> = sutGraph.reconstructFullPathFromPreviousNode(
            target = destinationNode,
            precursors = result.precursors,
        )
        assertEquals(4, detailedPathFromStartToDestination.size)
        assertEquals(1, detailedPathFromStartToDestination.first().id)
        assertEquals(7, detailedPathFromStartToDestination[1].id)
        assertEquals(8, detailedPathFromStartToDestination[2].id)
        assertEquals(5, detailedPathFromStartToDestination.last().id)
    }

    @Test
    fun `can route from node 1 to node 6`() {
        val destinationNode = sutGraph.getNode(id = 6)
        val detailedPathFromStartToDestination: List<Node> = sutGraph.reconstructFullPathFromPreviousNode(
            target = destinationNode,
            precursors = result.precursors,
        )
        assertEquals(2, detailedPathFromStartToDestination.size)
        assertEquals(1, detailedPathFromStartToDestination.first().id)
        assertEquals(6, detailedPathFromStartToDestination.last().id)
    }

    @Test
    fun `can route from node 1 to node 7`() {
        val destinationNode = sutGraph.getNode(id = 7)
        val detailedPathFromStartToDestination: List<Node> = sutGraph.reconstructFullPathFromPreviousNode(
            target = destinationNode,
            precursors = result.precursors,
        )
        assertEquals(2, detailedPathFromStartToDestination.size)
        assertEquals(1, detailedPathFromStartToDestination.first().id)
        assertEquals(7, detailedPathFromStartToDestination.last().id)
    }

    @Test
    fun `can route from node 1 to node 8`() {
        val destinationNode = sutGraph.getNode(id = 8)
        val detailedPathFromStartToDestination: List<Node> = sutGraph.reconstructFullPathFromPreviousNode(
            target = destinationNode,
            precursors = result.precursors,
        )
        assertEquals(3, detailedPathFromStartToDestination.size)
        assertEquals(1, detailedPathFromStartToDestination.first().id)
        assertEquals(7, detailedPathFromStartToDestination[1].id)
        assertEquals(8, detailedPathFromStartToDestination.last().id)
    }

    @Test
    fun `can route from node 1 to node 9`() {
        val destinationNode = sutGraph.getNode(id = 9)
        val detailedPathFromStartToDestination: List<Node> = sutGraph.reconstructFullPathFromPreviousNode(
            target = destinationNode,
            precursors = result.precursors,
        )
        assertEquals(3, detailedPathFromStartToDestination.size)
        assertEquals(1, detailedPathFromStartToDestination.first().id)
        assertEquals(7, detailedPathFromStartToDestination[1].id)
        assertEquals(9, detailedPathFromStartToDestination.last().id)
    }
}