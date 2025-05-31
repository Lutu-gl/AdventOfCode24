package day21;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {
    static char[][] keypadBig = {
            {'7', '8', '9'},
            {'4', '5', '6'},
            {'1', '2', '3'},
            {'-', '0', 'A'}
    };
    static char[][] keypadSmall = {
            {'-', '^', 'A'},
            {'<', 'v', '>'},
    };

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
        long sum = 0L;
        for (int i = 0; i < input.size(); i++) {
            String code = input.get(i);
            String shortestSequenceTotalForCode = getShortestSequenceTotalForCode1(code);
            sum += (long) shortestSequenceTotalForCode.length() * extractNumericValue(code);
        }

        return sum;
    }

    public static String getShortestSequenceTotalForCode1(String code) {
        List<String> shortestSequencesTotal;


        Robot robotBigKeypad = new Robot(3, 2, keypadBig);
        Robot robotSmallKeypad1 = new Robot(0, 2, keypadSmall);
        Robot robotSmallKeypad2 = new Robot(0, 2, keypadSmall);

        // big Keypad
        shortestSequencesTotal = getShortestSequencesForCodeAndRobot(code, robotBigKeypad);

        // small Keypad1
        List<String> newTotalList = new ArrayList<>();
        for (String s : shortestSequencesTotal) {
            shortestSequencesTotal = getShortestSequencesForCodeAndRobot(s, robotSmallKeypad1);
            newTotalList.addAll(shortestSequencesTotal);
        }
        shortestSequencesTotal = newTotalList;

        // remove length greater
        filterMinLength(shortestSequencesTotal);

        // smallKeypad2
        newTotalList = new ArrayList<>();
        for (String s : shortestSequencesTotal) {
            shortestSequencesTotal = getShortestSequencesForCodeAndRobot(s, robotSmallKeypad2);
            newTotalList.addAll(shortestSequencesTotal);
        }
        shortestSequencesTotal = newTotalList;

        filterMinLength(shortestSequencesTotal);

        String s = shortestSequencesTotal.stream().min((e1, e2) -> e1.length() - e2.length()).get();
        return s;
    }

    private static void filterMinLength(List<String> shortestSequencesTotal) {
        int minLen = shortestSequencesTotal.stream()
                .mapToInt(String::length)
                .min()
                .orElse(0);
        shortestSequencesTotal.removeIf(str -> str.length() > minLen);
    }

    public static List<String> getShortestSequencesForCodeAndRobot(String code, Robot robot) {
        List<String> shortestSequencesTotal = new ArrayList<>();
        for (int j = 0; j < code.length(); j++) {
            char c = code.charAt(j);
            List<String> sequencesForChar = robot.getSequenceTo(c);
            if(shortestSequencesTotal.isEmpty()) {
                for (String s : sequencesForChar) {
                    shortestSequencesTotal.add(s + 'A');
                }
            } else {
                List<String> newShortestSequencesTotal = new ArrayList<>();
                for (String already : shortestSequencesTotal) {
                    for (String charSequence : sequencesForChar) {
                        newShortestSequencesTotal.add(already + charSequence + 'A');
                    }
                }
                shortestSequencesTotal = newShortestSequencesTotal;
            }
        }
        return shortestSequencesTotal;
    }

    public static long solve2(List<String> input) {
        long sum = 0L;
        for (int i = 0; i < input.size(); i++) {
            String code = input.get(i);
            String shortestSequenceTotalForCode = getShortestSequenceTotalForCode2(code);
            sum += (long) shortestSequenceTotalForCode.length() * extractNumericValue(code);
            //System.out.println("Code: " + code + " - " + shortestSequenceTotalForCode.length() + " - " + extractNumericValue(code) + " - " + shortestSequenceTotalForCode.length() * extractNumericValue(code));
        }

        return sum;
    }

    public static String getShortestSequenceTotalForCode2(String code) {
        List<String> shortestSequencesTotal = new ArrayList<>();

        Robot robotBigKeypad = new Robot(3, 2, keypadBig);
        List<Robot> robots = new ArrayList<>();
        robots.add(robotBigKeypad);
        for (int i = 0; i < 2; i++) { // 3 is already too big.. have to find another way to not work with strings.
            robots.add( new Robot(0, 2, keypadSmall));
        }

        shortestSequencesTotal.add(code);
        for (Robot robot : robots) {
            List<String> newTotalList = new ArrayList<>();
            for (String s : shortestSequencesTotal) {
                shortestSequencesTotal = getShortestSequencesForCodeAndRobot(s, robot);
                newTotalList.addAll(shortestSequencesTotal);
            }
            shortestSequencesTotal = newTotalList;
            filterMinLength(shortestSequencesTotal);
        }

        String s = shortestSequencesTotal.stream().min((e1, e2) -> e1.length() - e2.length()).get();
        return s;
    }


    private static int extractNumericValue(String code) {
        String numericPart = code.replaceAll("[^0-9]", "");
        return Integer.parseInt(numericPart);
    }
}