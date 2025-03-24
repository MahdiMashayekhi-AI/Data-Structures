import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Trie {
    public static int ALPHABET_SIZE = 26;

    private class Node {
        private char value;
        private HashMap<Character, Node> children = new HashMap<>();
        private boolean isEndOfWord;

        public Node(char value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return "value=" + value;
        }

        private boolean hasChild(char ch){
            return children.containsKey(ch);
        }

        private void addChild(char ch){
            children.put(ch, new Node(ch));
        }

        private Node getChild(char ch){
            return children.get(ch);
        }

        private Node[] getChildren(){
            return children.values().toArray(new Node[0]);
        }

        private boolean hasChildren(){
            return !children.isEmpty();
        }

        private void removeChild(char ch){
            children.remove(ch);
        }
    }

    Node root = new Node(' ');

    public void insert(String word) {
        var current = root;
        for (var ch : word.toCharArray()) {
            if (!current.hasChild(ch))
                current.addChild(ch);
            current = current.children.get(ch);
        }
        current.isEndOfWord = true;
    }

    public boolean contains(String word) {
        if (word == null || word.isEmpty())
            return false;

        var current = root;
        for (var ch : word.toCharArray()) {
            if (!current.hasChild(ch))
                return false;
            current = current.children.get(ch);
        }
        return current.isEndOfWord;
    }

    // Traversing the nodes
    public void traverse() {
        traverse(root);
    }

    private void traverse(Node root){
        for (var child : root.getChildren())
            traverse(child);

        // Post-order traversal
        System.out.println(root.value);
    }

    // Removing an item
    public void remove(String word) {
        if (word == null || word.isEmpty())
            return;

        remove(root, word, 0);
    }

    private void remove(Node root, String word, int index){
        if (word.length() == index) {
            root.isEndOfWord = false;
            return;
        }

        var ch = word.charAt(index);
        var child = root.getChild(ch);
        if (child == null)
            return;

        remove(child, word, index + 1);

        if (!root.hasChildren() && !root.isEndOfWord)
            root.removeChild(ch);
    }

    // Auto Completion
    public List<String> findWords(String prefix){
        List<String> words = new ArrayList<>();
        var lastNode = findLastNodeOf(prefix);
        findWords(lastNode, prefix, words);

        return words;
    }

    private void findWords(Node root, String prefix, List<String> words){
        if (root.isEndOfWord)
            words.add(prefix);

        for (var child : root.getChildren())
            findWords(child, prefix + child.value, words);
    }

    private Node findLastNodeOf(String prefix){
        var current = root;
        for (var ch : prefix.toCharArray()) {
            var child = current.getChild(ch);
            if (child == null)
                return null;

            current = child;
        }
        return current;
    }
}