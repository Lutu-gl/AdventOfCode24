package day10;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) {
        try {
            int[][] input = readInputAsInt("src/main/java/day10/input.txt");
            System.out.println(solve1(input));
            System.out.println(solve2(input));
            //System.out.println("-----------------");
            //solve2("xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))mul(2,2)");
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
    public static int[][] readInputAsInt(String fileName) throws IOException {
        int rowCount = 1;
        int columnCount = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            columnCount = reader.readLine().length();
            while (reader.readLine() != null) {
                rowCount++;
            }
        }

        int[][] arr = new int[rowCount][columnCount];
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            int i = 0;
            while ((line = reader.readLine()) != null) {
                for (int j = 0; j < columnCount; j++) {
                    arr[i][j] = line.charAt(j) - '0';
                }
                i++;
            }
        }

        return arr;
    }


    public static int solve1(int[][] arr) {
        int sum = 0;
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr.length; j++) {
                if (arr[i][j] != 0) continue;
                int[][] arrcopy = deepCopy(arr);
                sum += checkTrailhead(arrcopy, i, j, 0);
            }
        }

        return sum;
    }

    private static int checkTrailhead(int[][] arr, int i, int j, int c) {
        // out of bounds check
        if (i < 0 || i >= arr.length || j < 0 || j >= arr.length) return 0;

        // check if right order
        if (arr[i][j] != c) return 0;

        // check if trailhead completed
        if (arr[i][j] == 9) {
            arr[i][j] = -1; // mark as visited
            return 1;
        }

        // recursive check
        return checkTrailhead(arr, i + 1, j, c + 1) +
                checkTrailhead(arr, i - 1, j, c + 1) +
                checkTrailhead(arr, i, j + 1, c + 1) +
                checkTrailhead(arr, i, j - 1, c + 1);
    }

    public static int[][] deepCopy(int[][] original) {
        int[][] copy = new int[original.length][];
        for (int i = 0; i < original.length; i++) {
            copy[i] = original[i].clone(); // Kopiere jedes innere Array
        }
        return copy;
    }


    public static int solve2(int[][] arr) {
        int sum = 0;
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr.length; j++) {
                if (arr[i][j] != 0) continue;
                sum += checkTrailheadDistinct(arr, i, j, 0);
            }
        }

        return sum;
    }


    private static int checkTrailheadDistinct(int[][] arr, int i, int j, int c) {
        // out of bounds check
        if (i < 0 || i >= arr.length || j < 0 || j >= arr.length) return 0;

        // check if right order
        if (arr[i][j] != c) return 0;

        // check if trailhead completed
        if (arr[i][j] == 9) {
            return 1;
        }

        // recursive check
        return checkTrailheadDistinct(arr, i + 1, j, c + 1) +
                checkTrailheadDistinct(arr, i - 1, j, c + 1) +
                checkTrailheadDistinct(arr, i, j + 1, c + 1) +
                checkTrailheadDistinct(arr, i, j - 1, c + 1);
    }

}
