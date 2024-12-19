package day18;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        try {
            char[][] input = readInput("src/main/java/day18/input.txt");
            System.out.println(solve1(deepCopy(input)));
            List<List<Integer>> restOfInput = readInputRest("src/main/java/day18/input.txt");
            System.out.println(solve2(deepCopy(input), restOfInput));
        } catch (IOException e) {
            System.err.println("Error reading input file: " + e.getMessage());
        }
    }

    private static List<List<Integer>> readInputRest(String fileName) throws IOException {
        int maxCount = 1024;

        List<List<Integer>> restOfInput = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            int i = 0;
            while ((line = reader.readLine()) != null) {
                if(i < maxCount) {
                    i++;
                    continue;
                }
                String[] split = line.split(",");
                int x = Integer.parseInt(split[0]);
                int y = Integer.parseInt(split[1]);
                restOfInput.add(Arrays.asList(x, y));
            }
        }

        return restOfInput;

    }

    /**
     * Reads the content of the input file and returns it as a String.
     *
     * @param fileName The name of the file to read.
     * @return The content of the file as a String.
     * @throws IOException If an error occurs while reading the file.
     */
    public static char[][] readInput(String fileName) throws IOException {
        int maxCount = 1024;

        char[][] arr = new char[71][71];
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[0].length; j++) {
                arr[i][j] = '.';
            }
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            int i = 0;
            while ((line = reader.readLine()) != null && i < maxCount) {
                i++;
                String[] split = line.split(",");
                int x = Integer.parseInt(split[0]);
                int y = Integer.parseInt(split[1]);
                arr[y][x] = '#';
            }
        }

        return arr;
    }

    public static long solve1(char[][] arr) {
        //printArr(arr);
        return findShortestPath(arr, 0, 0, 70, 70);
    }

    public static List<Integer> solve2(char[][] arr, List<List<Integer>> restOfInput) {
        //printArr(arr);

        // binary sort over the rest of the input to find the first byte that prevents the maze from being solved
        if (findShortestPath(arr, 0, 0, 70, 70) == -1) {
            return null;
        }

        // simple iteration
        for (int i = 0; i < restOfInput.size(); i++) {
            char[][] newArr = deepCopy(arr);
            writeToArray(newArr, restOfInput, i);
            //System.out.println("now trying: " + restOfInput.get(i));
            if (findShortestPath(newArr, 0, 0, 70, 70) == -1) {
                return restOfInput.get(i);
            }
        }

        return null;

    }

    public static void writeToArray(char[][] arr, List<List<Integer>> restOfInput, int maxCount) {
        for (int i = 0; i < restOfInput.size(); i++) {
            if(i > maxCount) {
                break;
            }
            //System.out.println("writing: " + restOfInput.get(i));
            List<Integer> integers = restOfInput.get(i);
            int x = integers.get(0);
            int y = integers.get(1);
            arr[y][x] = '#';
        }
    }

    public static int findShortestPath(char[][] grid, int startX, int startY, int endX, int endY) {
        int rows = grid.length;
        int cols = grid[0].length;

        int[] dRow = {-1, 1, 0, 0};
        int[] dCol = {0, 0, -1, 1};

        boolean[][] visited = new boolean[rows][cols];

        Queue<Position> queue = new LinkedList<>();
        queue.add(new Position(startX, startY, 0));

        while (!queue.isEmpty()) {
            Position current = queue.poll();
            int row = current.row;
            int col = current.col;
            int steps = current.steps;

            if (row == endX && col == endY) {
                return steps;
            }

            for (int i = 0; i < 4; i++) {
                int newRow = row + dRow[i];
                int newCol = col + dCol[i];

                if (isValidMove(grid, newRow, newCol) && !visited[newRow][newCol]) {
                    visited[newRow][newCol] = true;
                    queue.add(new Position(newRow, newCol, steps + 1));
                }
            }
        }
        return -1;
    }

    private static boolean isValidMove(char[][] arr, int i, int j) {
        return i >= 0 && i < arr.length && j >= 0 && j < arr[0].length && arr[i][j] != '#';
    }

    private static void printArr(char[][] arr) {
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[0].length; j++) {
                System.out.print(arr[i][j]);
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

// used for the BFS
class Position {
    int row;
    int col;
    int steps;

    public Position(int row, int col, int steps) {
        this.row = row;
        this.col = col;
        this.steps = steps;
    }
}