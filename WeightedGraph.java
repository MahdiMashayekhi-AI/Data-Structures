import javax.swing.text.html.HTMLDocument;
import java.util.*;

public class WeightedGraph {
    private class Node {
        private String label;
        private List<Edge> edges = new ArrayList<>();

        public Node(String label) {
            this.label = label;
        }

        @Override
        public String toString() {
            return label;
        }

        public void addEdge(Node to, int weight) {
            edges.add(new Edge(this, to, weight));
        }

        public List<Edge> getEdges() {
            return edges;
        }
    }

    private class Edge {
        private Node from;
        private Node to;
        private int weight;

        public Edge(Node from, Node to, int weight) {
            this.from = from;
            this.to = to;
            this.weight = weight;
        }

        @Override
        public String toString() {
            return to + " -->(" + weight + ")";
        }
    }

    private class NodeEntry{
        private Node node;
        private int priority;

        public NodeEntry(Node node, int priority) {
            this.node = node;
            this.priority = priority;
        }
    }

    private final Map<String, Node> nodes = new HashMap<>();

    public void addNode(String label) {
        nodes.putIfAbsent(label, new Node(label));
    }

    public void addEdge(String from, String to, int weight) {
        var fromNode = nodes.get(from);
        var toNode = nodes.get(to);

        if (fromNode == null || toNode == null)
            throw new IllegalArgumentException("Node not found");

        fromNode.addEdge(toNode, weight);
        toNode.addEdge(fromNode, weight);
    }

    public Path getShortestPath(String from, String to) {
        var fromNode = nodes.get(from);
        var toNode = nodes.get(to);

        if (fromNode == null || toNode == null)
            throw new IllegalArgumentException("Node not found");

        Map<Node, Integer> distances = new HashMap<>();
        for (var node : nodes.values())
            distances.put(node, Integer.MAX_VALUE);
        distances.put(fromNode, 0);

        Map<Node, Node> previousNode = new HashMap<>();

        Set<Node> visited = new HashSet<>();

        PriorityQueue<NodeEntry> queue = new PriorityQueue<>(Comparator.comparingInt(en -> en.priority));
            queue.add(new NodeEntry(fromNode, 0));

        while (!queue.isEmpty()) {
            var current = queue.poll().node;
            visited.add(current);

            for (var edge : current.getEdges()) {
                if (visited.contains(edge.to))
                    continue;

                var newDistance = distances.get(current) + edge.weight;
                if (newDistance < distances.get(edge.to)) {
                    distances.put(edge.to, newDistance);
                    previousNode.put(edge.to, current);
                    queue.add(new NodeEntry(edge.to, newDistance));
                }
            }
        }

        return buildPath(previousNode, toNode);
    }

    private Path buildPath(Map<Node, Node> previousNode, Node toNode) {
        Stack<Node> stack = new Stack<>();
        stack.push(toNode);
        var previous = previousNode.get(toNode);
        while (previous != null) {
            stack.push(previous);
            previous = previousNode.get(previous);
        }

        var path = new Path();
        while (!stack.isEmpty())
            path.addNode(stack.pop().label);

        return path;
    }

    public boolean hasCycle() {
        Set<Node> visited = new HashSet<>();

        for (var node : nodes.values())
            if (!visited.contains(node) &&
                hasCycle(node, null, visited))
                    return true;

        return false;
    }

    private boolean hasCycle(Node node, Node parent, Set<Node> visited) {
        visited.add(node);

        for (var edge : node.getEdges()) {
            if (edge.to == parent)
                continue;

            if (visited.contains(edge.to) ||
                hasCycle(edge.to, node, visited))
                return true;
        }
        return false;
    }

    public WeightedGraph getMinimumSpanningTree() {
        var tree = new WeightedGraph();

        if (nodes.isEmpty())
            return tree;

        PriorityQueue<Edge> edges = new PriorityQueue<>(Comparator.comparingInt(e -> e.weight));
        var startNode = nodes.values().iterator().next();
        edges.addAll(startNode.getEdges());

        if (edges.isEmpty())
            return tree;

        while (tree.nodes.size() < nodes.size()) {
            var minEdge = edges.remove();
            var nextEdge = minEdge.to;

            if (tree.containsNode(nextEdge.label))
                continue;

            tree.addNode(nextEdge.label);
            tree.addEdge(minEdge.from.label,
                         minEdge.to.label,
                         minEdge.weight);

            for (var edge : nextEdge.getEdges())
                if (!tree.containsNode(edge.to.label))
                    edges.add(edge);
        }
        return tree;
    }

    public boolean containsNode(String label) {
        return nodes.containsKey(label);
    }

    public void print(){
        for (var node : nodes.values()) {
            var edges = node.getEdges();
            if (!edges.isEmpty())
                System.out.println(node + " is connected to " + edges);
        }
    }
}
