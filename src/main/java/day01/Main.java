package day01;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        try {
            String input = readInput("src/main/java/day01/input.txt");
            solve1(input);
            solve2(input);
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
    public static String readInput(String fileName) throws IOException {
        StringBuilder content = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }

        return content.toString().trim(); // Remove trailing newline
    }

    /**
     * Processes the input and solves the puzzle for the first part
     *
     * @param input The input data as a String.
     */
    public static void solve1(String input) {
        String[] lines = input.split("\n");

        ArrayList<Integer> list1 = new ArrayList<>();
        ArrayList<Integer> list2 = new ArrayList<>();

        for (String line : lines) {
            String[] parts = line.trim().split("\\s+");
            list1.add(Integer.parseInt(parts[0]));
            list2.add(Integer.parseInt(parts[1]));
        }

        int[] arr1 = list1.stream().mapToInt(i -> i).toArray();
        int[] arr2 = list2.stream().mapToInt(i -> i).toArray();

        Arrays.sort(arr1);
        Arrays.sort(arr2);

        int sum = 0;
        for (int i = 0; i < arr1.length; i++) {
            sum += calcDistance(arr1[i], arr2[i]);
        }

        System.out.println("Sum of distances: " + sum);
    }

    /**
     * Processes the input and solves the puzzle for the second part
     *
     * @param input The input data as a String.
     */
    public static void solve2(String input) {
        String[] lines = input.split("\n");

        ArrayList<Integer> list1 = new ArrayList<>();
        ArrayList<Integer> list2 = new ArrayList<>();

        for (String line : lines) {
            String[] parts = line.trim().split("\\s+");
            list1.add(Integer.parseInt(parts[0]));
            list2.add(Integer.parseInt(parts[1]));
        }

        int[] arr1 = list1.stream().mapToInt(i -> i).toArray();
        int[] arr2 = list2.stream().mapToInt(i -> i).toArray();

        Arrays.sort(arr1);
        Arrays.sort(arr2);

        int sum = 0;
        for (int i = 0; i < arr1.length; i++) {
            sum += countOccurrences(arr2, arr1[i]) * arr1[i];
        }

        System.out.println("Similarity: " + sum);
    }

    public static int calcDistance(int a, int b) {
        return Math.abs(a-b);
    }

    public static int countOccurrences(int[] arr, int target) {
        int count = 0;
        for (int num : arr) {
            if (num == target) {
                count++;
            }
        }
        return count;
    }
}
