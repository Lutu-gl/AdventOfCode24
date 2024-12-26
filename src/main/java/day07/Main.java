package day07;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        try {
            String input = readInput("src/main/java/day07/input.txt");
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
    public static String readInput(String fileName) throws IOException {
        StringBuilder content = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }

        return content.toString().trim();
    }

    public static long solve1(String input) {
        String[] lines = input.split("\n");
        long totalCalibrationResult = 0;

        for (String line : lines) {
            String[] parts = line.split(": ");
            long targetValue = Long.parseLong(parts[0]);
            String[] numbers = parts[1].split(" ");

            List<Integer> numList = new ArrayList<>();
            for (String number : numbers) {
                numList.add(Integer.parseInt(number));
            }

            if (canProduceTarget(numList, targetValue)) {
                totalCalibrationResult += targetValue;
            }
        }

        return totalCalibrationResult;
    }

    public static boolean canProduceTarget(List<Integer> numbers, long targetValue) {
        return evaluate(numbers, 0, numbers.get(0), targetValue);
    }

    private static boolean evaluate(List<Integer> numbers, int index, long current, long targetValue) {
        if (index == numbers.size() - 1) {
            return current == targetValue;
        }

        int nextValue = numbers.get(index + 1);

        if (evaluate(numbers, index + 1, current + nextValue, targetValue)) {
            return true;
        }

        if (evaluate(numbers, index + 1, current * nextValue, targetValue)) {
            return true;
        }

        return false;
    }

    public static long solve2(String input) {
        String[] lines = input.split("\n");
        long totalCalibrationResult = 0;

        for (String line : lines) {
            String[] parts = line.split(": ");
            long targetValue = Long.parseLong(parts[0]);
            String[] numbers = parts[1].split(" ");

            List<Integer> numList = new ArrayList<>();
            for (String number : numbers) {
                numList.add(Integer.parseInt(number));
            }

            if (canProduceTarget2(numList, targetValue)) {
                totalCalibrationResult += targetValue;
            }
        }

        return totalCalibrationResult;
    }

    public static boolean canProduceTarget2(List<Integer> numbers, long targetValue) {
        return evaluate2(numbers, 0, numbers.get(0), targetValue);
    }

    private static boolean evaluate2(List<Integer> numbers, int index, long current, long targetValue) {
        if (index == numbers.size() - 1) {
            return current == targetValue;
        }

        int nextValue = numbers.get(index + 1);

        if (evaluate2(numbers, index + 1, current + nextValue, targetValue)) {
            return true;
        }

        if (evaluate2(numbers, index + 1, current * nextValue, targetValue)) {
            return true;
        }

        long concatenated = Long.parseLong(current + "" + nextValue);
        if (evaluate2(numbers, index + 1, concatenated, targetValue)) {
            return true;
        }

        return false;
    }
}
