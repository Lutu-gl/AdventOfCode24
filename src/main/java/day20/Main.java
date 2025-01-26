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
        int endI = -1;
        int endJ = -1;

        // Find start (S) and end (E) positions
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[0].length; j++) {
                if (arr[i][j] == 'S') {
                    startI = i;
                    startJ = j;
                } else if (arr[i][j] == 'E') {
                    endI = i;
                    endJ = j;
                }
            }
        }
        return 0;
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

    static class State {
        int i, j, cost, cheatSteps;
        boolean appliedSkip;

        public State(int i, int j, int cost, boolean appliedSkip, int cheatSteps) {
            this.i = i;
            this.j = j;
            this.cost = cost;
            this.appliedSkip = appliedSkip;
            this.cheatSteps = cheatSteps;
        }
    }
}
