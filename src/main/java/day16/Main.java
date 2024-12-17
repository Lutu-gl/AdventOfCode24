package day16;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


/**
 * My idea is to implement Dijsktra's algorithm to find the shortest path from the robot to the goal.
 */
public class Main {

    private static final int[] DI = {0, 1, 0, -1}; // Directions: E, S, W, N
    private static final int[] DJ = {1, 0, -1, 0};
    private static final String[] DIRECTIONS = {"E", "S", "W", "N"};

    public static void main(String[] args) {
        try {
            char[][] input = readInput("src/main/java/day16/input.txt");
            System.out.println(solve1(deepCopy(input)));
            //System.out.println(solve2(deepCopy(input)));
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
        //printArr(arr);

        return dijkstra(arr, startI, startJ);
    }

    // direction 0 = east
    public static long dijkstra(char[][] maze, int startI, int startJ) {
        // Priority Queue
        PriorityQueue<State> pq = new PriorityQueue<>(Comparator.comparing(s -> s.cost));
        pq.add(new State(startI, startJ, 0, 0));

        // visited with direction
        boolean visited[][][] = new boolean[maze.length][maze[0].length][4];

        while (!pq.isEmpty()) {
            State current = pq.poll();
            int i = current.i;
            int j = current.j;
            int direction = current.direction;
            int cost = current.cost;

            if (maze[i][j] == 'E') {
                return cost;
            }

            if (visited[i][j][direction]) {
                continue;
            }
            visited[i][j][direction] = true;

            //    private static final int[] DI = {0, 1, 0, -1};
            //    private static final int[] DJ = {1, 0, -1, 0};
            //    private static final String[] DIRECTIONS = {"E", "S", "W", "N"};
            int newI = i + DI[direction];
            int newJ = j + DJ[direction];

            // 1. Move Forward
            if(isValidMove(maze, newI, newJ)) {
                pq.add(new State(newI, newJ, direction, cost + 1));
            }

            // 2. Rotate Clockwise
            int newDirection = (direction + 1) % 4;
            pq.add(new State(i, j, newDirection, cost + 1000));

            // 3. Rotate Counter-Clockwise
            newDirection = (direction + 3) % 4;
            pq.add(new State(i, j, newDirection, cost + 1000));
        }

        return -1L;
    }


    private static boolean isValidMove(char[][] maze, int i, int j) {
        return i >= 0 && i < maze.length && j >= 0 && j < maze[0].length && maze[i][j] != '#';
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

    private static void printArr(int[][] arr) {
        final int CELL_WIDTH = 6; // Maximale Breite pro Zelle

        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[0].length; j++) {
                String value;
                if (arr[i][j] == Integer.MAX_VALUE) {
                    value = "X"; // Platzhalter für unerreichbare Felder
                } else {
                    value = String.valueOf(arr[i][j]);
                }
                // Die Ausgabe wird mit fester Breite ausgerichtet
                System.out.printf("%-" + CELL_WIDTH + "s", value);
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

class State {
    int i, j, direction, cost;

    public State(int i, int j, int direction, int cost) {
        this.i = i;
        this.j = j;
        this.direction = direction;
        this.cost = cost;
    }
}
