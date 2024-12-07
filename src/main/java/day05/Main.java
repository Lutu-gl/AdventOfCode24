package day05;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public class Main {

    public static void main(String[] args) {
        try {
            //String input = readInput("src/main/java/day01/input.txt");
            solve1("src/main/java/day05/input.txt");
            solve2("src/main/java/day05/input.txt");
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

    public static void solve1(String fileName) throws IOException {
        StringBuilder content = new StringBuilder();
        int sumSave=0;

        int rowCount = 1;
        int columnCount = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            columnCount = reader.readLine().length();
            while (reader.readLine() != null) {
                rowCount++;
            }
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            String regex = "(\\d\\d)\\|(\\d\\d)";
            Pattern pattern = Pattern.compile(regex);
            int[][] arr = new int[rowCount][2];
            int i = 0;
            while ((line = reader.readLine()) != null) {
                if(line.isEmpty()) {
                    continue;
                }
                Matcher matcher = pattern.matcher(line);
                if(matcher.find()) { // still in rules part
                    arr[i][0] = Integer.parseInt(matcher.group(1));
                    arr[i][1] = Integer.parseInt(matcher.group(2));
                    i++;
                    continue;
                }
                // now in the check part
                String[] splitString = line.split(",");
                int[] split = Arrays.stream(splitString)
                        .mapToInt(Integer::parseInt)
                        .toArray();
                boolean safe = true;
                for (int i1 = 0; i1 < split.length; i1++) {
                    for (int i2 = 0; i2 < arr.length; i2++) {
                        if (arr[i2][0] == split[i1]){
                            for (int i3 = 0; i3 < i1; i3++) {
                                if(split[i3] == arr[i2][1]) {
                                    safe = false;
                                    break;
                                }
                            }
                        }
                    }
                    if(!safe) {
                        break;
                    }
                }
                if(safe) {
                    sumSave += split[split.length/2];
                }
            }
        }
        System.out.println(sumSave);
    }


    public static void solve2(String fileName) throws IOException {
        StringBuilder content = new StringBuilder();
        int sumSave=0;

        int rowCount = 1;
        int columnCount = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            columnCount = reader.readLine().length();
            while (reader.readLine() != null) {
                rowCount++;
            }
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            String regex = "(\\d\\d)\\|(\\d\\d)";
            Pattern pattern = Pattern.compile(regex);
            int[][] arr = new int[rowCount][2];
            int i = 0;
            while ((line = reader.readLine()) != null) {
                if(line.isEmpty()) {
                    continue;
                }
                Matcher matcher = pattern.matcher(line);
                if(matcher.find()) { // still in rules part
                    arr[i][0] = Integer.parseInt(matcher.group(1));
                    arr[i][1] = Integer.parseInt(matcher.group(2));
                    i++;
                    continue;
                }
                // now in the check part
                String[] splitString = line.split(",");
                int[] split = Arrays.stream(splitString)
                        .mapToInt(Integer::parseInt)
                        .toArray();
                boolean safe;
                boolean wasSafe = true;
                do {
                    safe = true;
                    for (int i1 = 0; i1 < split.length; i1++) {
                        for (int i2 = 0; i2 < arr.length; i2++) {
                            if (arr[i2][0] == split[i1]){
                                for (int i3 = 0; i3 < i1; i3++) {
                                    if(split[i3] == arr[i2][1]) { // move one position up to get correct ordering
                                        int temp = split[i3];
                                        split[i3] = split[i3+1];
                                        split[i3+1] = temp;
                                        wasSafe = false;
                                        safe = false;
                                    }
                                }
                            }
                        }
                    }
                } while (!safe);

                if(!wasSafe) {
                    //System.out.println(Arrays.toString(split));
                    sumSave += split[split.length/2];
                }
            }
        }
        System.out.println(sumSave);
    }
}
