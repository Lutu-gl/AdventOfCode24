package day12;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class Main {

    public static void main(String[] args) {
        try {
            char[][] input = readInput("src/main/java/day12/input.txt");
            System.out.println(solve1(input));
            System.out.println(solve2(input));
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
        int rowCount = 1;
        int columnCount = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            columnCount = reader.readLine().length();
            while (reader.readLine() != null) {
                rowCount++;
            }
        }

        char[][] arr = new char[rowCount][columnCount];
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            int i = 0;
            while ((line = reader.readLine()) != null) {
                arr[i++] = line.toCharArray();
            }
        }

        return arr;
    }

    public static long solve1(char[][] arr) {
        boolean[][] visited = new boolean[arr.length][arr[0].length];



        long[] metrics = new long[2];   // 0: area, 1: perimeter
        long sum = 0;
        char current;
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr.length; j++) {
                if(!visited[i][j]) {
                    current = arr[i][j];
                    calculateMetrics(arr, visited, i, j, current, metrics);
                    sum += metrics[0] * metrics[1];
                    metrics[0] = 0;
                    metrics[1] = 0;
                }
            }
        }

        return sum;
    }

    private static long[]  calculateMetrics(char[][] arr, boolean[][] visited, int i, int j, char current, long[] metrics) {
        if (i < 0 || i >= arr.length || j < 0 || j >= arr[0].length || visited[i][j] || arr[i][j] != current) {
            return metrics;
        }

        visited[i][j] = true;
        metrics[0]++;

        // 4 neighborhood
        int[] dx = {-1, 1, 0, 0};
        int[] dy = {0, 0, -1, 1};

        for (int d = 0; d < 4; d++) {
            int newRow = i + dx[d];
            int newCol = j + dy[d];

            if (isInArea(arr, newRow, newCol, current)) {
                calculateMetrics(arr, visited, newRow, newCol, current, metrics);
            } else {
                metrics[1]++;
            }
        }

        return metrics;
    }

    private static boolean isInArea(char[][] arr, int i, int j, char current) {
        return i >= 0 && i < arr.length && j >= 0 && j < arr[0].length && arr[i][j] == current;
    }


    public static long solve2(char[][] arr) {
        boolean[][] visited = new boolean[arr.length][arr[0].length];
        Set<Character>[][] sidesCheck = new Set[arr.length][arr[0].length];

        // fill it so no null values are present: This stores if a line has already been made with the chars U, D, L, R
        for (int i = 0; i < sidesCheck.length; i++) {
            for (int j = 0; j < sidesCheck[0].length; j++) {
                sidesCheck[i][j] = new HashSet<>();
            }
        }

        long[] metrics = new long[2];   // 0: area, 1: sides
        long sum = 0;
        char current;
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[0].length; j++) {
                if(!visited[i][j]) {
                    current = arr[i][j];
                    calculateMetrics2(arr, visited, sidesCheck, i, j, current, metrics);
                    System.out.println("Area of " + current + ": " + metrics[0] + " Sides: " + metrics[1] + " Total: " + metrics[0] * metrics[1]);
                    sum += metrics[0] * metrics[1];
                    metrics[0] = 0;
                    metrics[1] = 0;
                }
            }
        }

        return sum;
    }

    private static long[]  calculateMetrics2(char[][] arr, boolean[][] visited, Set<Character>[][] sidesCheck, int i, int j, char current, long[] metrics) {
        if (i < 0 || i >= arr.length || j < 0 || j >= arr[0].length || visited[i][j] || arr[i][j] != current) {
            return metrics;
        }
        updateSidesCheckedAll(arr, sidesCheck,  current); // always update all sides checked
        visited[i][j] = true;
        metrics[0]++;

        // 4 neighborhood and copy all sides checked
        int[] dx = {-1, 1, 0, 0};
        int[] dy = {0, 0, -1, 1};
        for (int d = 0; d < 4; d++) {
            int newRow = i + dx[d];
            int newCol = j + dy[d];

            if (isInArea(arr, newRow, newCol, current)) {
                calculateMetrics2(arr, visited, sidesCheck, newRow, newCol, current, metrics);
            } else {
                // update sides checked
                updateSidesChecked(arr, sidesCheck, i, j, current);

                // check for sides
                if (d == 0 && !sidesCheck[i][j].contains('U')) {
                    metrics[1]++;
                    sidesCheck[i][j].add('U');
                }
                if (d == 1 && !sidesCheck[i][j].contains('D')) {
                    metrics[1]++;
                    sidesCheck[i][j].add('D');
                }
                if (d == 2 && !sidesCheck[i][j].contains('L')) {
                    metrics[1]++;
                    sidesCheck[i][j].add('L');
                }
                if (d == 3 && !sidesCheck[i][j].contains('R')) {
                    metrics[1]++;
                    sidesCheck[i][j].add('R');
                }
            }
        }

        return metrics;
    }

    public static void updateSidesCheckedAll(char[][] arr, Set<Character>[][] sidesCheck, char current) {
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[0].length; j++) {
                updateSidesChecked(arr, sidesCheck, i, j, arr[i][j]);
            }
        }
    }

    public static void updateSidesChecked(char[][] arr, Set<Character>[][] sidesCheck, int i, int j, char current) {
        // get sideschecked from neighbors
        int[] dx = {-1, 1, 0, 0};
        int[] dy = {0, 0, -1, 1};
        for (int d = 0; d < 4; d++) {
            int newRow = i + dx[d];
            int newCol = j + dy[d];

            if (isInArea(arr, newRow, newCol, current)) {
                sidesCheck[i][j].addAll(sidesCheck[newRow][newCol]);
            }
        }
        // now remove non non possible sides
        if (isInArea(arr, i - 1, j, current)) {
            sidesCheck[i][j].remove('U');
        }
        if (isInArea(arr, i + 1, j, current)) {
            sidesCheck[i][j].remove('D');
        }
        if (isInArea(arr, i, j - 1, current)) {
            sidesCheck[i][j].remove('L');
        }
        if (isInArea(arr, i, j + 1, current)) {
            sidesCheck[i][j].remove('R');
        }
    }
}
