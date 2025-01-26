package day03;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) {
        try {
            String input = readInput("src/main/java/day03/input.txt");
            System.out.println(solve1(input));
            solve2(input);
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

    public static int solve1(String input) {
        String regex = "mul\\((\\d{1,3}),(\\d{1,3})\\)";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);

        int sum = 0;
        while (matcher.find()) {
            int firstNum = Integer.parseInt(matcher.group(1));
            int secondNum = Integer.parseInt(matcher.group(2));
            sum += firstNum * secondNum;
        }

        return sum;
    }


    public static void solve2(String input) throws IOException {
        String regexOld = "do\\(\\)(?:(?!don't\\(\\)).)*mul\\((\\d{1,3}),(\\d{1,3})\\)";
        /*
        Regex der schaut ob do() vorkommt und dann die naechsten mult() capured.
        Das ?! ist ein negative lookahead, das bedeutet dass der naechste Teil nicht don't() sein darf.
        Das . ist ein wildcard, das bedeutet dass es jedes Zeichen matched.
        Das * bedeutet dass es 0 oder mehrere Zeichen matched.
        Das ?: bedeutet dass es nicht captured wird. (nicht in den Gruppen gespeichert wird)

        -> Leider geht das nicht da nur die letzte group von mul() captured wird.
        Neuer Ansatz: Zuerst alle strings finden die zwischen do() und don't liegen und dann darauf suchen
        Bringt leider Komplikationen da nested do oder don't vorkommen koennen.

        Neuer Ansatz Prozedural
         */
        String regexOld2 = "do\\(\\)(?:(?!don't\\(\\)).)*";
        boolean enabled = true;
        int sum = 0;

        input += "don't()"; // add a don't at the end to make sure the last mul is also counted

        Pattern mulPattern = Pattern.compile("mul\\((\\d{1,3}),(\\d{1,3})\\)");
        Pattern controlPattern = Pattern.compile("(do\\(\\)|don't\\(\\))");

        Matcher controlMatcher = controlPattern.matcher(input);
        Matcher mulMatcher = mulPattern.matcher(input);
        int lastIndex = 0;

        while (controlMatcher.find()) {
            String match = controlMatcher.group(0);

            mulMatcher.region(lastIndex, controlMatcher.start());
            while (mulMatcher.find()) {

                if(enabled) {
                    int firstNum = Integer.parseInt(mulMatcher.group(1));
                    int secondNum = Integer.parseInt(mulMatcher.group(2));
                    sum += firstNum * secondNum;
                }
            }
            if (match.equals("do()")) {
                enabled = true;
            } else {
                enabled = false;
            }
            lastIndex = controlMatcher.end();
        }

        System.out.println(sum);
    }

    // determine if the array should be reversed
    // check the first 4 elements and if the maximum of them are smaller then reverse
    private static boolean shouldReverse(Integer[] arr) {
        if (arr.length <= 3) {
            return false;
        }
        int rs = 0;
        int notRs = 0;
        if(arr[0] < arr[1]) {
            rs++;
        } else {
            notRs++;
        }
        if(arr[1] < arr[2]) {
            rs++;
        } else {
            notRs++;
        }
        if(arr[2] < arr[3]) {
            rs++;
        } else {
            notRs++;
        }
        return rs > notRs;
    }

    // -1 for safe and other for index of unsafe
    private static int isSafe(Integer[] arr) {
        if (arr.length <= 1) {
            return -1;
        }

        int prev = arr[0];
        boolean save = true;
        int i = 1;
        for (; i < arr.length; i++) {
            int diff = prev - arr[i];
            if(diff != 1 && diff != 2 && diff != 3) {
                save = false;
                break;
            }
            prev = arr[i];
        }
        if(save) {
            return -1;
        } else {
            return i;
        }
    }
}
