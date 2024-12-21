package day21;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        try {
            List<String> input = readInput("src/main/java/day21/input.txt");
            System.out.println(solve1(input));
            System.out.println(solve2(input));
        } catch (IOException e) {
            System.err.println("Error reading input file: " + e.getMessage());
        }
    }

    public static List<String> readInput(String fileName) throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null && !line.isEmpty()) {
                lines.add(line);
            }
        }
        return lines;
    }

    public static long solve1(List<String> input) {
        List<String> codes = parseCodes(input);
        System.out.println(input);
        long sum = 0L;
        for (int i = 0; i < input.size(); i++) {
            String code = input.get(i);
            List<Character> shortestSequenceTotalForCode = getShortestSequenceTotalForCode(code);
            sum += (long) shortestSequenceTotalForCode.size() * extractNumericValue(code);
            System.out.println("Code: " + code + " - " + shortestSequenceTotalForCode.size() + " - " + extractNumericValue(code) + " - " + shortestSequenceTotalForCode.size() * extractNumericValue(code));
        }

        return sum;
    }

    public static List<Character> getShortestSequenceTotalForCode (String code) {
        List<Character> shortestSequenceTotal;
        char[][] keypadBig = {
                {'7', '8', '9'},
                {'4', '5', '6'},
                {'1', '2', '3'},
                {'-', '0', 'A'}
        };
        char[][] keypadSmall = {
                {'-', '^', 'A'},
                {'<', 'v', '>'},
        };
        // 456A
        // ^^<<A>A>AvvA
        // <AAv<AA>>^AvA^AvA^Av<AA>^A
        //
        Robot robotBigKeypad = new Robot(3, 2, keypadBig);
        Robot robotSmallKeypad1 = new Robot(0, 2, keypadSmall);
        Robot robotSmallKeypad2 = new Robot(0, 2, keypadSmall);

        List<Character> codeList = new ArrayList<>();
        for (int i = 0; i < code.length(); i++) {
            codeList.add(code.charAt(i));
        }

        //<<vA>>^A<A>AvA<^AA>A<vAAA>^A
        //<<vAA>A>^AvAA<^A>A<<vA>>^AvA^A<vA>^A<<vA>^A>AAvA^A<<vA>A>^AAAvA<^A>A

        //<vA<AA>>^AvAA<^A>A<v<A>>^AvA^A<vA>^A<v<A>^A>AAvA^A<v<A>A>^AAAvA<^A>A
        shortestSequenceTotal = getShortestSequenceForCodeAndRobot(codeList, robotBigKeypad);
        System.out.println(convertListCharToString(shortestSequenceTotal));
        shortestSequenceTotal = getShortestSequenceForCodeAndRobot(shortestSequenceTotal, robotSmallKeypad1);
        System.out.println(convertListCharToString(shortestSequenceTotal));
        shortestSequenceTotal = getShortestSequenceForCodeAndRobot(shortestSequenceTotal, robotSmallKeypad2);
        System.out.println(convertListCharToString(shortestSequenceTotal));

        return shortestSequenceTotal;
    }
    public static List<Character> getShortestSequenceForCodeAndRobot(List<Character> code, Robot robot) {
        List<Character> shortestSequenceTotal = new ArrayList<>();
        for (int j = 0; j < code.size(); j++) {
            char c = code.get(j);
            List<Character> ss = robot.getSequenceTo(c);
            shortestSequenceTotal.addAll(ss);
            shortestSequenceTotal.add('A');
        }
        return shortestSequenceTotal;
    }

    public static long solve2(List<String> input) {
        // Placeholder for Part 2 logic, if needed
        return 0;
    }

    private static List<String> parseCodes(List<String> input) {


        return input;
    }

    private static int extractNumericValue(String code) {
        String numericPart = code.replaceAll("[^0-9]", "");
        return Integer.parseInt(numericPart);
    }

    public static String convertListCharToString(List<Character> list) {
        StringBuilder sb = new StringBuilder();
        for (Character c : list) {
            sb.append(c);
        }
        return sb.toString();
    }
}