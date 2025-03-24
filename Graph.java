import java.util.*;

class Graph {
    private class Node {
        private final String label;

        public Node(String label) {
            this.label = label;
        }

        @Override
        public String toString() {
            return label;
        }
    }

    private final Map<String, Node> nodes = new HashMap<>();
    private final Map<Node, List<Node>> adjacencyList = new HashMap<>();

    public void addNode(String label) {
        var node = new Node(label);
        nodes.putIfAbsent(label, node);
        adjacencyList.putIfAbsent(node, new ArrayList<>());
    }

    public void addEdge(String from, String to) {
        var fromNode = nodes.get(from);
        if (fromNode == null)
            throw new IllegalArgumentException("Node not found");

        var toNode = nodes.get(to);
        if (toNode == null)
            throw new IllegalArgumentException("Node not found");

        adjacencyList.get(fromNode).add(toNode);
    }

    public void removeNode(String label) {
        var node = nodes.get(label);
        if (node == null)
            return;

        for (var n : adjacencyList.keySet())
            adjacencyList.get(n).remove(node);

        adjacencyList.remove(node);
        nodes.remove(node);
    }

    public void removeEdge(String from, String to) {
        var fromNode = nodes.get(from);
        var toNode = nodes.get(to);

        if (fromNode == null || toNode == null)
            return;

        adjacencyList.get(fromNode).remove(toNode);
    }

    public void traverseDepthFirst(String root) {
        traverseDepthFirst(nodes.get(root), new HashSet<>());
    }

    private void traverseDepthFirst(Node root, Set<Node> visited) {
        System.out.println(root);
        visited.add(root);

        for (var n : adjacencyList.get(root)) {
            if (!visited.contains(n))
                traverseDepthFirst(n, visited);
        }
    }

    // Stack
    public void traverseDepthFirstIterative(String root) {
        var node = nodes.get(root);

        Set<Node> visited = new HashSet<>();
        Stack<Node> stack = new Stack<>();
        stack.push(node);

        while (!stack.isEmpty()) {
            var current = stack.pop();

            if (visited.contains(current))
                continue;

            visited.add(current);
            System.out.println(current);

            for (var neighbor : adjacencyList.get(current)) {
                if (!visited.contains(neighbor))
                    stack.push(neighbor);
            }
        }
    }

    // Queue
    public void traverseBreadthFirst(String root) {
        var node = nodes.get(root);
        if (node == null)
            return;

        Set<Node> visited = new HashSet<>();
        Queue<Node> queue = new ArrayDeque<>();
        queue.add(node);

        while (!queue.isEmpty()) {
            var current = queue.remove();

            if (visited.contains(current))
                continue;

            visited.add(current);
            System.out.println(current);

            for (var neighbor : adjacencyList.get(current)) {
                if (!visited.contains(neighbor))
                    queue.add(neighbor);
            }
        }
    }

    public List<String> topologicalSort() {
        Stack<Node> stack = new Stack<>();
        Set<Node> visited = new HashSet<>();

        for (var node : nodes.values())
            topologicalSort(node, visited, stack);

        List<String> sorted = new ArrayList<>();

        while (!stack.isEmpty())
            sorted.add(stack.pop().label);

        return sorted;
    }

    private void topologicalSort(Node root, Set<Node> visited, Stack<Node> stack) {
        if (visited.contains(root))
            return;

        visited.add(root);

        for (var neighbor : adjacencyList.get(root)) {
            if (!visited.contains(neighbor))
                topologicalSort(neighbor, visited, stack);
        }

        stack.push(root);
    }

    public boolean hasCycle() {
        Set<Node> all = new HashSet<>();
        Set<Node> visiting = new HashSet<>();
        Set<Node> visited = new HashSet<>();

        all.addAll(nodes.values());

        while (!all.isEmpty()) {
            var current = all.iterator().next();

            if (hasCycle(current, all, visiting, visited))
                return true;
        }

        return false;
    }

    private boolean hasCycle(Node root, Set<Node> all, Set<Node> visiting, Set<Node> visited) {
        all.remove(root);
        visiting.add(root);

        for (var neighbor : adjacencyList.get(root)) {
            if (visited.contains(neighbor))
                continue;

            if (visiting.contains(neighbor))
                return true;

            if (hasCycle(neighbor, all, visiting, visited))
                return true;
        }

        visiting.remove(root);
        visited.add(root);

        return false;
    }

    public void print(){
        for (var source : adjacencyList.keySet()) {
            var targets = adjacencyList.get(source);
            if (!targets.isEmpty())
                System.out.println(source + " is connect to " + targets);
        }
    }
}