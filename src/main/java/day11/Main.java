package day11;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) {
        //String input = readInput("src/main/java/day01/input.txt");
        long[] arrInitial = {4022724, 951333, 0, 21633, 5857, 97, 702, 6};

        //int[] arrInitial = {125, 17};
        ArrayList<Long> arr = new ArrayList<>();
        for (long i : arrInitial) {
            arr.add(i);
        }
        //System.out.println(arr);
        solve1(arr);

        Map<Long, Long> stoneCounts = new HashMap<>();
        for (long stone : arrInitial) {
            stoneCounts.put(stone, stoneCounts.getOrDefault(stone, 0L) + 1);
        }
        solve2(stoneCounts);
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

    public static void solve1(ArrayList<Long> arr) {
        int sumSave=0;
        int blinks = 25;

        for (int b = 0; b < blinks; b++) {
            //System.out.println(arr);
            for (int i = 0; i < arr.size(); i++) {
                long num = arr.get(i);
                if(num == 0) {
                    arr.set(i, 1L);
                } else if((Math.floor(Math.log10(num)) + 1) %  2 == 0) { // even number of digits
                    String sNum = Long.toString(num);
                    Long leftHalf = Long.parseLong(sNum.substring(0, sNum.length()/2));
                    Long rightHalf = Long.parseLong(sNum.substring(sNum.length()/2));
                    arr.set(i, leftHalf);
                    arr.add(i+1, rightHalf);
                    i++; // skip the split number
                } else {
                    arr.set(i, num * 2024);
                }
            }
        }
        System.out.println(arr.size());
    }


    public static void solve2(Map<Long, Long> stoneCounts) {
        int blinks = 75;

        for (int b = 0; b < blinks; b++) {
            Map<Long, Long> nextStoneCounts = new HashMap<>();

            for (Map.Entry<Long, Long> entry : stoneCounts.entrySet()) {
                long stone = entry.getKey();
                long count = entry.getValue();

                if (stone == 0) {
                    nextStoneCounts.put(1L, nextStoneCounts.getOrDefault(1L, 0L) + count);
                } else if (countDigits(stone) % 2 == 0) {  // gerade Anzahl an Ziffern
                    long leftHalf = stone / (long) Math.pow(10, countDigits(stone) / 2);
                    long rightHalf = stone % (long) Math.pow(10, countDigits(stone) / 2);
                    nextStoneCounts.put(leftHalf, nextStoneCounts.getOrDefault(leftHalf, 0L) + count);
                    nextStoneCounts.put(rightHalf, nextStoneCounts.getOrDefault(rightHalf, 0L) + count);
                } else {
                    long newStone = stone * 2024;
                    nextStoneCounts.put(newStone, nextStoneCounts.getOrDefault(newStone, 0L) + count);
                }
            }

            stoneCounts = nextStoneCounts;
        }

        long totalStones = stoneCounts.values().stream().mapToLong(Long::longValue).sum();
        System.out.println(totalStones);
    }

    public static int countDigits(long number) {
        return (int) Math.log10(number) + 1;
    }
}
