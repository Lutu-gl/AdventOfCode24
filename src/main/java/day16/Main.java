package day16;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


/**
 * My idea is to implement Dijsktra's algorithm to find the shortest path from the robot to the goal.
 */
public class Main {

    public static void main(String[] args) {
        try {
            char[][] input = readInput("src/main/java/day16/input.txt");
            System.out.println(solve1(deepCopy(input)));
            System.out.println(solve2(deepCopy(input)));
        } catch (IOException e) {
            System.err.println("Error reading input file: " + e.getMessage());
        }
    }

    /**
     * Reads the content of the input file and returns it as a String.
     *
     * @param fileName The name of the file to read.
     * @return The content of the file as a String.
     * @throws IOException If an error occurs while reading the file.
     */
    public static char[][] readInput(String fileName) throws IOException {
        List<char[]> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null && !line.isEmpty()) {
                lines.add(line.toCharArray());
            }
        }

        char[][] maze = new char[lines.size()][];
        for (int i = 0; i < lines.size(); i++) {
            maze[i] = lines.get(i);
        }
        return maze;
    }

    public static long solve1(char[][] arr) {
        int startI = -1;
        int startJ = -1;

        // find starting position
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[0].length; j++) {
                if (arr[i][j] == 'S') {
                    startI = i;
                    startJ = j;
                    break;
                }
            }
        }

        // run dijkstra with priority queue
        PriorityQueue<Node> pq = new PriorityQueue<>();
        Set<List<Integer>> seen = new HashSet<>();

        pq.add(new Node(0, startI, startJ, 0, 1)); // facing east

        while (!pq.isEmpty()) {
            Node node = pq.poll();
            seen.add(Arrays.asList(node.i, node.j, node.di, node.dj));
            if (arr[node.i][node.j] == 'E') {
               return node.cost;
            }

            List<Node> newNodes = new ArrayList<>();
            newNodes.add(new Node(node.cost + 1,node.i + node.di, node.j + node.dj, node.di, node.dj)); // move in direction
            newNodes.add(new Node(node.cost + 1000, node.i, node.j, node.dj, -node.di)); // rotate clockwise
            newNodes.add(new Node(node.cost + 1000, node.i, node.j, - node.dj, node.di)); // rotate counterclockwise

            for (Node newNode : newNodes) {
                if(newNode.i < 0 || newNode.i >= arr.length || newNode.j < 0 || newNode.j >= arr[0].length) continue;
                if(arr[newNode.i][newNode.j] == '#') continue;
                if(seen.contains(Arrays.asList(newNode.i, newNode.j, newNode.di, newNode.dj))) continue;

                pq.add(newNode);
            }

        }
        return -1;
    }

    public static long solve2(char[][] arr) {
        long cost = -1;
        int startI = -1;
        int startJ = -1;

        // find starting position
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[0].length; j++) {
                if (arr[i][j] == 'S') {
                    startI = i;
                    startJ = j;
                    break;
                }
            }
        }

        // run dijkstra with priority queue
        PriorityQueue<Node> pq = new PriorityQueue<>();
        Map<Node, Integer> costs = new HashMap<>(); // node -> cost instead of seen set
        Map<Node, Set<Node>> backtrack = new HashMap<>(); // backtrack not only one but could be a set of nodes.
        Set<Node> endNodes = new HashSet<>();
        pq.add(new Node(0, startI, startJ, 0, 1)); // facing east

        while (!pq.isEmpty()) {
            Node node = pq.poll();
            if(costs.containsKey(node)) {
                if (costs.get(node) < node.cost) continue;      // if greater do not consider.
            } else {
                costs.put(node, node.cost);
            }
            if (arr[node.i][node.j] == 'E') {
                if (cost == -1 || node.cost < cost) {
                    cost = node.cost;
                    endNodes = new HashSet<>();
                    endNodes.add(node);
                } else if (node.cost == cost) {
                    endNodes.add(node);
                }
            }


            List<Node> newNodes = new ArrayList<>();
            newNodes.add(new Node(node.cost + 1,node.i + node.di, node.j + node.dj, node.di, node.dj)); // move in direction
            newNodes.add(new Node(node.cost + 1000, node.i, node.j, node.dj, -node.di)); // rotate clockwise
            newNodes.add(new Node(node.cost + 1000, node.i, node.j, - node.dj, node.di)); // rotate counterclockwise

            for (Node newNode : newNodes) {
                if(newNode.i < 0 || newNode.i >= arr.length || newNode.j < 0 || newNode.j >= arr[0].length) continue;
                if(arr[newNode.i][newNode.j] == '#') continue;
                if(costs.containsKey(newNode)) {
                    if (costs.get(newNode) < newNode.cost){
                        continue;
                    } else if (costs.get(newNode) > newNode.cost) {
                        costs.put(newNode, newNode.cost);
                        backtrack.put(newNode, new HashSet<>());
                    }

                    Set<Node> newSet = backtrack.get(newNode);
                    newSet.add(node);
                    backtrack.put(newNode, newSet);
                } else {
                    Set<Node> newSet = new HashSet<>();
                    if (backtrack.containsKey(newNode)) {
                        newSet = backtrack.get(newNode);
                    }
                    newSet.add(node);
                    backtrack.put(newNode, newSet);
                    costs.put(newNode, newNode.cost);
                }
                pq.add(newNode);
            }
        }

        Queue<Node> queue = new LinkedList<>(endNodes);

        while (!queue.isEmpty()) {
            Node node = queue.poll();
            if (arr[node.i][node.j] == 'S') continue;  // found last element
            arr[node.i][node.j] = 'O';
            for (Node newNode : backtrack.get(node)) {
                queue.add(newNode);
            }
        }
        //printArr(arr);

        int tilesSolution=1; // because of start
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[0].length; j++) {
                if(arr[i][j] == 'O') tilesSolution++;
            }
        }

        return tilesSolution;
    }

    private static void printArr(char[][] arr) {
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[0].length; j++) {
                System.out.print(arr[i][j]);
            }
            System.out.println();
        }
    }

    private static void printArr(boolean[][] arr) {
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[0].length; j++) {
                if(arr[i][j]) {
                    System.out.print("O");
                } else {
                    System.out.print("#");
                }
            }
            System.out.println();
        }
    }


    public static char[][] deepCopy(char[][] original) {
        if (original == null) {
            return null;
        }

        final char[][] result = new char[original.length][];
        for (int i = 0; i < original.length; i++) {
            result[i] = Arrays.copyOf(original[i], original[i].length);
        }
        return result;
    }
}

class Node implements Comparable<Node> {
    int cost;
    int i;
    int j;
    int di; // direction i
    int dj; // direction j

    public Node(int cost,int i,int j,int di,int dj) {
        this.cost = cost;
        this.i = i;
        this.j = j;
        this.di = di;
        this.dj = dj;
    }

    @Override
    public int compareTo(Node o) {
        return Integer.compare(this.cost, o.cost);
    }

    @Override
    public String toString() {
        return "Node{" +
                "cost=" + cost +
                ", i=" + i +
                ", j=" + j +
                ", di=" + di +
                ", dj=" + dj +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Node node = (Node) obj;
        return i == node.i && j == node.j && di == node.di && dj == node.dj;
    }

    @Override
    public int hashCode() {
        return Objects.hash(i, j, di, dj); // to make hashmap work as intended don't consider cost
    }
}
