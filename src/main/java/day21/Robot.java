package day21;

import java.util.*;

public class Robot {
    int positionI;
    int positionJ;
    char[][] keypad;
    int notAllowedI = 0;
    int notAllowedJ = 0;

    public Robot(int positionI, int positionJ, char[][] keypad) {
        this.positionI = positionI;
        this.positionJ = positionJ;
        this.keypad = keypad;
        for (int i = 0; i < keypad.length; i++) {
            for (int j = 0; j < keypad[0].length; j++) {
                if (keypad[i][j] == '-') {
                    notAllowedI = i;
                    notAllowedJ = j;
                }
            }
        }

    }

    // moving is done with >, <, v, and ^
    // I cant move through '-'
    public List<String> getSequenceTo(Character c) {
        Queue<Node> queue = new ArrayDeque<>();
        queue.add(new Node(positionI, positionJ, ""));

        List<Node> endNodes = new ArrayList<>();
        int optimalCost = Integer.MAX_VALUE;
        while(!queue.isEmpty()) {
            Node node = queue.poll();

            if (keypad[node.i][node.j] == c) {
                // found sequence
                endNodes.add(node);
                optimalCost = node.getCost();
                continue;
            }
            if (node.getCost() >= optimalCost) continue; // can be greater or equal because newNodes are all cost + 1

            List<Node> newNodes = new ArrayList<>();
            newNodes.add(new Node(node.i + 1, node.j, node.sequence + "v"));
            newNodes.add(new Node(node.i - 1, node.j, node.sequence + "^"));
            newNodes.add(new Node(node.i, node.j + 1, node.sequence + ">"));
            newNodes.add(new Node(node.i, node.j - 1, node.sequence + "<"));

            for (Node newNode : newNodes) {
                if (newNode.i >= keypad.length || newNode.i < 0 || newNode.j < 0 || newNode.j>= keypad[0].length) continue;
                if (keypad[newNode.i][newNode.j] == '-') continue;
                queue.add(newNode);
            }
        }
        positionI = endNodes.get(0).i;
        positionJ = endNodes.get(0).j;

        List<String> sequences = endNodes.stream().map(e -> e.sequence).toList();

        return sequences;
    }
}
// probably add a new class with int int string (and then at the end I've found a List of endNodes<> with all the same cost and easy.
class Node {
    int i;
    int j;
    String sequence = "";

    public Node(int i, int j, String sequence) {
        this.i = i;
        this.j = j;
        this.sequence = sequence;
    }

    public int getCost(){
        return sequence.length();
    }
}