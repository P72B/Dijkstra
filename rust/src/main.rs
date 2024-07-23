use std::collections::{HashMap, HashSet};
use std::ops::{Index, Not};
use crate::model::{Graph, Node, NodeId, ResultSet};

mod model;

fn main() {
    println!("FINISH");
}

fn dijkstra(graph: Graph, start: Node) -> ResultSet {
    let mut result = ResultSet::default();
    let mut queue : Vec<NodeId> = Vec::new();

    graph.nodes.iter().for_each( |node| {
        result.distances.insert(node.id, Some(u32::MAX));
        result.prev.insert(node.id, None);
        queue.push(node.id);
    });
    result.distances.insert(start.id, Some(0));

    while queue.is_empty().not() {
        let (index, active) = {
            let (index, active) = queue.iter().enumerate()
                .min_by_key(|(index, active)| result.distances.get(active)).unwrap().clone();
            (index, active.clone())
        };
        queue.remove(index);

        let all_neighbours_edge = graph.get_all_neighbour_edges(active);

        for neighbour_edge in all_neighbours_edge {
            let distance: u32 = neighbour_edge.weight + result.distances.get(&active).unwrap().unwrap();
            let the_neighbour_node_id = neighbour_edge.get_neighbour_node_id(active);
            if distance < result.get_distance(the_neighbour_node_id) {
                result.distances.insert(the_neighbour_node_id, Some(distance));
                result.prev.insert(the_neighbour_node_id, Some(active.clone()));
            }
        }
    }
    return result;
}



#[cfg(test)]
mod test {
    use std::collections::VecDeque;
    use std::ops::Index;
    use std::path::Display;
    use crate::dijkstra;
    use crate::model::{Graph, Node, NodeId, VcWrapper};

    #[test]
    fn three_equals_1() {
        let routing_graph = Graph::default();
        let start = routing_graph.nodes.iter().find(|node| node.id == 1).cloned().unwrap();
        let result = dijkstra(routing_graph, start);
        println!("{:?}", result);
        assert_eq!(result.distances.len(), 9);
        assert_eq!(result.get_distance(1), 0);
        assert_eq!(result.get_distance(2), 2);
        assert_eq!(result.get_distance(7), 3);
        assert_eq!(result.get_distance(4), 7);
        assert_eq!(result.get_distance(7), 3);
        assert_eq!(result.get_distance(8), 4);
        assert_eq!(result.get_distance(5), 6);
        assert_eq!(result.get_distance(9), 6);
        assert_eq!(result.get_distance(3), 6);
        assert_eq!(result.get_distance(4), 7);

        let mut path = VecDeque::new();
        result.get_path(4, &mut path);
        assert_eq!(path.len(), 5);
        //assert_eq!(path.index(0), 1);
        println!("{}", VcWrapper(path));

        path = VecDeque::new();
        result.get_path(3, &mut path);
        assert_eq!(path.len(), 3);

        path = VecDeque::new();
        result.get_path(6, &mut path);
        assert_eq!(path.len(), 2);

        path = VecDeque::new();
        result.get_path(9, &mut path);
        assert_eq!(path.len(), 3);
    }
}
