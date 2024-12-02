package day02;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.IntStream;

public class Main {

    public static void main(String[] args) {
        try {
            //String input = readInput("src/main/java/day01/input.txt");
            solve1("src/main/java/day02/input.txt");
            solve2("src/main/java/day02/input.txt");
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

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] split = line.split("\\s+");
                Integer[] numbers = Arrays.stream(split)
                        .map(Integer::parseInt)
                        .toArray(Integer[]::new);

                if (numbers.length <= 1) {
                    sumSave++;
                    continue;
                }
                if(numbers[0] < numbers[1]) {   // if decreasing order then reverse
                    Collections.reverse(Arrays.asList(numbers));
                }

                int prev = numbers[0];
                boolean save = true;
                for (int i = 1; i < numbers.length; i++) {
                    int diff = prev - numbers[i];
                    if(diff != 1 && diff != 2 && diff != 3) {
                        save = false;
                        break;
                    }
                    prev = numbers[i];
                }
                if(save) {
                    sumSave++;
                }
            }
        }
        System.out.println(sumSave);
    }


    public static void solve2(String fileName) throws IOException {
        StringBuilder content = new StringBuilder();
        int sumSave=0;

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] split = line.split("\\s+");
                Integer[] numbers = Arrays.stream(split)
                        .map(Integer::parseInt)
                        .toArray(Integer[]::new);

                if (numbers.length <= 1) {
                    sumSave++;
                    continue;
                }
                if(shouldReverse(numbers)) {   // if decreasing order then reverse
                    Collections.reverse(Arrays.asList(numbers));
                }


                int prev = numbers[0];
                boolean save = true;
                int index = isSafe(numbers);
                if (index == -1) {
                    sumSave++;
                    continue;
                }
                // unsafe: now remove the unsafe element and check again
                ArrayList<Integer> list1 = new ArrayList<>(Arrays.asList(numbers));
                ArrayList<Integer> list2 = new ArrayList<>(Arrays.asList(numbers));

                list1.remove(index);
                list2.remove(index-1);
                Integer[] arr1 = list1.toArray(new Integer[0]);
                Integer[] arr2 = list2.toArray(new Integer[0]);
                if (isSafe(arr1) == -1 || isSafe(arr2) == -1) {
                    sumSave++;
                }
            }
        }
        System.out.println(sumSave);
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
