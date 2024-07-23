use std::collections::{HashMap, HashSet, VecDeque};
use std::fmt::{Display, Formatter};
use std::ops::Not;

pub type NodeId = u32;

pub struct VcWrapper(pub VecDeque<NodeId>);

#[derive(Eq, PartialEq, Hash, Clone)]
pub struct Node {
    pub id: NodeId,
}

/// A struct of edge
///
/// To show this nice rust doc type in the console:
/// cargo doc --open
///
/// # Example
/// ```
/// let edge = Edge {
///     nodes_ids: (1, 2),
///     weight: 2
/// }
///
/// assert_eq!(edge.weight == 2);
/// ```
///
/// # Fields
/// * nodes_ids - two [`NodeId`](NodeId) node ids describing the edge start and end
/// * weight - the [u32] weight of the edge
#[derive(Eq, Hash, PartialEq, Clone)]
pub struct Edge {
    pub nodes_ids: (NodeId, NodeId),
    pub weight: u32,
}

#[derive(Clone)]
pub struct Graph {
    pub nodes: HashSet<Node>,
    pub edges: HashSet<Edge>,
}

#[derive(Debug)]
pub struct ResultSet {
    pub distances: HashMap<NodeId, Option<u32>>,
    pub prev: HashMap<NodeId, Option<u32>>,
}

impl Edge {
    // 3 cases
    pub fn get_neighbour_node_id(&self, another_id: NodeId) -> NodeId {
        if self.contains(another_id).not() { return another_id }

        return  if self.nodes_ids.1 == another_id {
            self.nodes_ids.0
        } else {
            self.nodes_ids.1
        }
    }

    // contains
    fn contains(&self, id: NodeId) -> bool {
        return self.nodes_ids.0 == id || self.nodes_ids.1 == id;
    }
}

impl Graph {
    pub fn get_all_neighbour_edges(&self, node_id: NodeId) -> Vec<Edge> {
        let mut result = Vec::new();
        for item in &self.edges {
            if item.contains(node_id) {
                result.push(item.clone())
            }
        }
        return result
    }
}

impl Default for Graph {
    fn default() -> Self {
        Graph {
            nodes: generate_nodes(),
            edges: generate_edges(),
        }
    }
}

impl ResultSet {
    pub fn get_distance(&self, id: NodeId) -> u32 {
        let result_find = self.distances.get(&id);
        let unwrapped = result_find.unwrap();
        return match unwrapped {
            None => {
                u32::MAX
            }
            Some(distance) => {
                *distance
            }
        }
    }
    
    pub fn set_distance(&mut self, id: NodeId, distance: u32) {
        self.distances.insert(id, Some(distance));
    }
    
    pub fn set_prev(&mut self, id: NodeId, prev: u32) {
        self.prev.insert(id, Some(prev));
    }
    
    pub fn get_path(&self, target_id: NodeId, path: &mut VecDeque<NodeId>) {
        self.get_path_recursive(target_id, path);
        path.push_back(target_id)
    }
    
    fn get_path_recursive(&self, target_id: NodeId, path: &mut VecDeque<NodeId>) {
        match (self.prev[&target_id]) {
            None => { }
            Some(prev_id) => {
                path.push_front(prev_id);
                self.get_path_recursive(prev_id, path);
            }
        }
    }
}

impl Default for ResultSet {
    fn default() -> Self {
        ResultSet {
            distances: HashMap::new(),
            prev: HashMap::new()
        }
    }
}

impl Display for VcWrapper {
    fn fmt(&self, f: &mut Formatter<'_>) -> std::fmt::Result {
        let mut tmp = String::from("");
        self.0.iter().for_each(|x| {
            if tmp.is_empty() {
                tmp = format!("{}", x);
            } else {
                tmp = format!("{},{}", tmp, x);
            }
        });


        write!(f, "{}", &tmp)
    }
}

fn generate_nodes() -> HashSet<Node> {
    let mut hashset = HashSet::new();
    for i in 1..10 {
        hashset.insert(Node { id: i });
    }

    hashset
}

fn generate_edges() -> HashSet<Edge> {
    let mut hashset = HashSet::new();

    hashset.insert(Edge { nodes_ids: (1, 2), weight: 2 });
    hashset.insert(Edge { nodes_ids: (1, 6), weight: 7 });
    hashset.insert(Edge { nodes_ids: (1, 7), weight: 3 });
    hashset.insert(Edge { nodes_ids: (2, 3), weight: 4 });
    hashset.insert(Edge { nodes_ids: (2, 7), weight: 6 });
    hashset.insert(Edge { nodes_ids: (3, 4), weight: 2 });
    hashset.insert(Edge { nodes_ids: (3, 9), weight: 2 });
    hashset.insert(Edge { nodes_ids: (4, 5), weight: 1 });
    hashset.insert(Edge { nodes_ids: (4, 9), weight: 8 });
    hashset.insert(Edge { nodes_ids: (5, 6), weight: 6 });
    hashset.insert(Edge { nodes_ids: (5, 8), weight: 2 });
    hashset.insert(Edge { nodes_ids: (6, 8), weight: 5 });
    hashset.insert(Edge { nodes_ids: (7, 8), weight: 1 });
    hashset.insert(Edge { nodes_ids: (7, 9), weight: 3 });
    hashset.insert(Edge { nodes_ids: (8, 9), weight: 4 });

    hashset
}

#[cfg(test)]
mod test {
    #[test]
    fn three_equals_1() {
        assert!(3 == 3)
    }
}
