package day17;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) {
        try {
            String input = readInput("src/main/java/day17/input.txt");
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
    public static void solve1(String input) {
        String[] split = input.split("\n\n");
        String[] registers = split[0].split("\n");

        String regex = "(\\d+)";
        Pattern pattern = Pattern.compile(regex);

        Matcher matcherA = pattern.matcher(registers[0]);
        long regA = matcherA.find() ? Long.parseLong(matcherA.group(1)) : -1;

        Matcher matcherB = pattern.matcher(registers[1]);
        long regB = matcherB.find() ? Long.parseLong(matcherB.group(1)) : -1;

        Matcher matcherC = pattern.matcher(registers[2]);
        long regC = matcherC.find() ? Long.parseLong(matcherC.group(1)) : -1;

        Matcher instructionMatcher = pattern.matcher(split[1]);
        List<Long> instructions = new ArrayList<>();
        while (instructionMatcher.find()) {
            instructions.add(Long.parseLong(instructionMatcher.group(1)));
        }


        ChronospatialComputer computer = new ChronospatialComputer(regA, regB, regC, instructions);
        computer.run();
        System.out.println("solve1: " + computer.getOutputSeperatedByComma());
    }

    public static void solve2(String input) {
        String[] split = input.split("\n\n");
        String[] registers = split[0].split("\n");

        String regex = "(\\d+)";
        Pattern pattern = Pattern.compile(regex);

        Matcher matcherA = pattern.matcher(registers[0]);
        long regA = matcherA.find() ? Long.parseLong(matcherA.group(1)) : -1;

        Matcher matcherB = pattern.matcher(registers[1]);
        long regB = matcherB.find() ? Long.parseLong(matcherB.group(1)) : -1;

        Matcher matcherC = pattern.matcher(registers[2]);
        long regC = matcherC.find() ? Long.parseLong(matcherC.group(1)) : -1;

        Matcher instructionMatcher = pattern.matcher(split[1]);
        List<Long> instructions = new ArrayList<>();
        while (instructionMatcher.find()) {
            instructions.add(Long.parseLong(instructionMatcher.group(1)));
        }

        // brute force (takes too long) - idea would be to analyze the instructions and find a pattern. However, I did not do that yet.
        for (long possibleA = 0; ; possibleA++) {
            ChronospatialComputer computer = new ChronospatialComputer(possibleA, regB, regC, instructions, new ArrayList<>(instructions));
            computer.run();

            if (computer.getOutput().equals(instructions)) {
                System.out.println("Value found: " + possibleA);
                break;
            }


            if (possibleA % 10000000 == 0) {
                System.out.println("possibleA: " + possibleA);
            }
        }
    }
}
