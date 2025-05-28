package day20;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        try {
            char[][] input = readInput("src/main/java/day20/input.txt");
            System.out.println(solve1(deepCopy(input)));
            System.out.println(solve2(deepCopy(input)));

        } catch (IOException e) {
            System.err.println("Error reading input file: " + e.getMessage());
        }
    }

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

        int[][] directions = {
                {-1, 0}, {1, 0}, {0, -1}, {0, 1}
        };

        int[][] multiplications = {
                {1, 1}, {1, -1}, {-1, 1}, {-1, -1}
        };

        // Find start (S) position
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[0].length; j++) {
                if (arr[i][j] == 'S') {
                    startI = i;
                    startJ = j;
                    break;
                }
            }
        }

        int[][] dists = new int[arr.length][arr[0].length];
        for (int i = 0; i < dists.length; i++) {
            Arrays.fill(dists[i], -1);
        }

        // bfs fill
        dists[startI][startJ] = 0;

        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[]{startI, startJ});

        while(!queue.isEmpty()) {
            int[] point = queue.poll();
            int ci = point[0];
            int cj = point[1];

            for (int[] dir : directions) {
                int ni = ci + dir[0];
                int nj = cj + dir[1];
                if (ni < 0 || nj < 0 || ni >= arr.length || nj >= arr[0].length) continue;
                if (arr[ni][nj] == '#') continue;
                if (dists[ni][nj] != -1) continue;

                dists[ni][nj] = dists[ci][cj] + 1;
                queue.add(new int[]{ni, nj});
            }
        }

        HashMap<Integer, Integer> integerIntegerHashMap = new HashMap<>();

        // consider saves.
        long savesOver100 = 0;
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[0].length; j++) {
                if(dists[i][j] == -1) continue;
                // consider every 2 size steps:

                Set<List<Integer>> considerPositions = new HashSet<>(); // List<Integer> and not int[] because with int[] duplicates are not recognized.
                for (int rs = 0; rs <= 2; rs++) { // row step
                    int cs = 2 - rs; // column step

                    for (int[] multiplication : multiplications) {
                        int ni = i + rs * multiplication[0];
                        int nj = j + cs * multiplication[1];
                        if (ni < 0 || nj < 0 || ni >= arr.length || nj >= arr[0].length) continue;
                        if (arr[ni][nj] == '#') continue;
                        if (dists[ni][nj] == -1) continue;
                        if (dists[ni][nj] - dists[i][j] - 2 <= 0) continue;

                        considerPositions.add(Arrays.asList(ni, nj));
                    }
                }
                for (List<Integer> considerPosition : considerPositions) {
                    int ni = considerPosition.get(0);
                    int nj = considerPosition.get(1);

                    integerIntegerHashMap.put(dists[ni][nj] - dists[i][j] - 2, integerIntegerHashMap.getOrDefault(dists[ni][nj] - dists[i][j] - 2, 0) + 1);
                    if (dists[ni][nj] - dists[i][j] - 2 >= 100) savesOver100++;
                }
            }
        }

        return savesOver100;
    }

    public static long solve2(char[][] arr) {
        int startI = -1;
        int startJ = -1;

        int[][] directions = {
                {-1, 0}, {1, 0}, {0, -1}, {0, 1}
        };

        int[][] multiplications = {
                {1, 1}, {1, -1}, {-1, 1}, {-1, -1}
        };

        // Find start (S) positions
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[0].length; j++) {
                if (arr[i][j] == 'S') {
                    startI = i;
                    startJ = j;
                    break;
                }
            }
        }

        int[][] dists = new int[arr.length][arr[0].length];
        for (int i = 0; i < dists.length; i++) {
            Arrays.fill(dists[i], -1);
        }

        // bfs fill
        dists[startI][startJ] = 0;

        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[]{startI, startJ});

        while(!queue.isEmpty()) {
            int[] point = queue.poll();
            int ci = point[0];
            int cj = point[1];

            for (int[] dir : directions) {
                int ni = ci + dir[0];
                int nj = cj + dir[1];
                if (ni < 0 || nj < 0 || ni >= arr.length || nj >= arr[0].length) continue;
                if (arr[ni][nj] == '#') continue;
                if (dists[ni][nj] != -1) continue;

                dists[ni][nj] = dists[ci][cj] + 1;
                queue.add(new int[]{ni, nj});
            }
        }

        HashMap<Integer, Integer> integerIntegerHashMap = new HashMap<>();

        // consider saves.
        long savesOver100 = 0;
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[0].length; j++) {
                if(dists[i][j] == -1) continue;

                for (int skipLength = 2; skipLength <= 20; skipLength++) { // skip length from 2 to 20

                    Set<List<Integer>> considerPositions = new HashSet<>(); // List<Integer> and not int[] because with int[] duplicates are not recognized.
                    for (int rs = 0; rs <= skipLength; rs++) { // row step
                        int cs = skipLength - rs; // column step

                        for (int[] multiplication : multiplications) {
                            int ni = i + rs * multiplication[0];
                            int nj = j + cs * multiplication[1];
                            if (ni < 0 || nj < 0 || ni >= arr.length || nj >= arr[0].length) continue;
                            if (arr[ni][nj] == '#') continue;
                            if (dists[ni][nj] == -1) continue;
                            if (dists[ni][nj] - dists[i][j] - skipLength <= 0) continue;

                            considerPositions.add(Arrays.asList(ni, nj));
                        }
                    }

                    for (List<Integer> considerPosition : considerPositions) {
                        int ni = considerPosition.get(0);
                        int nj = considerPosition.get(1);

                        integerIntegerHashMap.put(dists[ni][nj] - dists[i][j] - skipLength, integerIntegerHashMap.getOrDefault(dists[ni][nj] - dists[i][j] - skipLength, 0) + 1);
                        if (dists[ni][nj] - dists[i][j] - skipLength >= 100) savesOver100++;
                    }
                }
            }
        }

        return savesOver100;
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

    public static void printArr(int[][] arr) {
        for (int[] row : arr) {
            for (int val : row) {
                System.out.print(val + "\t");
            }
            System.out.println();
        }
    }

    public static void printArr(char[][] arr) {
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[0].length; j++) {
                System.out.print(arr[i][j]);
            }
            System.out.println();
        }
    }
}
