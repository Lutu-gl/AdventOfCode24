package day19;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        try {
            List<String> patterns = readInput("src/main/java/day19/input.txt");
            List<String> towels = readTowels("src/main/java/day19/input.txt");
            System.out.println(solve1(patterns, towels));
            System.out.println(solve2(patterns, towels));
        } catch (IOException e) {
            System.err.println("Error reading input file: " + e.getMessage());
        }
    }

    private static List<String> readTowels(String fileName) throws IOException {
        List<String> towels = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            // skip the first n lines where the field is defined
            while(!reader.readLine().isEmpty()) {
                // skip
            }

            String line;
            while ((line = reader.readLine()) != null) {
                if(line.isEmpty()) {
                    break;
                }
                towels.add(line);
            }
        }

        return towels;
    }

    /**
     * Reads the content of the input file and returns it as a String.
     *
     * @param fileName The name of the file to read.
     * @throws IOException If an error occurs while reading the file.
     */
    public static List<String> readInput(String fileName) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            int i = 0;
            while ((line = reader.readLine()) != null) {
                if(line.isEmpty()) {
                    break;
                }
                String[] split = line.split(",");
                List<String> patterns = new ArrayList<>();
                for (int j = 0; j < split.length; j++) {
                    patterns.add(split[j].strip());
                }
                return patterns;
            }
        }
        return null;
    }

    public static int solve1(List<String> patterns, List<String> towels) {
        int count = 0;
        for (int i = 0; i < towels.size(); i++) {
            String towel = towels.get(i);
            if (isConstructable(patterns, towel, "")) {
                count++;
            }
        }

        return count;
    }

    private static boolean isConstructable(List<String> patterns, String towel, String myTowel) {
        if (myTowel.length() > towel.length()) {
            return false;
        }

        if (myTowel.length() == towel.length()) {
            return myTowel.equals(towel);
        }

        for (String pattern : patterns) {
            if (towel.startsWith(myTowel + pattern)) {
                if (isConstructable(patterns, towel, myTowel + pattern)) {
                    return true;
                }
            }
        }

        return false;
    }

    public static long solve2(List<String> patterns, List<String> towels) {
        long count = 0;
        for (String towel : towels) {
            Map<String, Long> memo = new HashMap<>();
            count += countConstructable(patterns, towel, "", memo);
        }
        return count;
    }

    // Dynamic programming solution
    private static long countConstructable(List<String> patterns, String towel, String myTowel, Map<String, Long> memo) {
        if (myTowel.length() > towel.length()) {
            return 0;
        }

        if (myTowel.length() == towel.length()) {
            return myTowel.equals(towel) ? 1 : 0;
        }

        if (memo.containsKey(myTowel)) {
            return memo.get(myTowel);
        }

        long sum = 0;
        for (String pattern : patterns) {
            if (towel.startsWith(myTowel + pattern)) {
                sum += countConstructable(patterns, towel, myTowel + pattern, memo);
            }
        }

        memo.put(myTowel, sum);
        return sum;
    }

}
