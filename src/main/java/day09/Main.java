package day09;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

public class Main {

    public static void main(String[] args) {
        try {
            String input = readInput("src/main/java/day09/input.txt");
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

        return content.toString().trim(); // Remove trailing newline
    }


    public static long solve1(String input) {
        long sum = 0;
        // transform
        ArrayList<Integer> transformedArray = getTransformedArray(input);
        // moving
        performMoving(transformedArray);
        // calc checksum
        sum = generateChecksum(transformedArray);


        return sum;
    }

    private static long generateChecksum(ArrayList<Integer> transformedArray) {
        long sum = 0;
        /*
        for (int i = 0; i < transformedArray.size(); i++) {
            if (transformedArray.get(i) != -1){
                sum += transformedArray.get(i) * i;
            }
        }
        System.out.println(sum);*/
        sum = LongStream.range(0, transformedArray.size())
                .filter(i -> transformedArray.get((int) i) != -1)
                .map(i -> transformedArray.get((int) i) * i)
                .sum();


        return sum;
    }

    private static void performMoving(ArrayList<Integer> transformedArray) {
        int leftmostFree = 0;
        int rightmostElement = Integer.MAX_VALUE-100;

        while (leftmostFree < rightmostElement+1 && leftmostFree != -1) {
            leftmostFree = transformedArray.indexOf(-1);
            rightmostElement = 0;

            for (int i = transformedArray.size()-1; i >= 0; i--) {
                if(transformedArray.get(i) != -1){
                    rightmostElement = i;
                    break;
                }
            }

            if (leftmostFree < rightmostElement + 1 && leftmostFree != -1){
                transformedArray.set(leftmostFree, transformedArray.get(rightmostElement));
                transformedArray.set(rightmostElement, -1);
            }
        }
    }

    private static ArrayList<Integer> getTransformedArray(String input) {
        int id=0;
        ArrayList<Integer> transformedArray = new ArrayList<>();

        boolean isFile = true;
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (isFile){
                for (int j = 0; j < Integer.parseInt(c+""); j++) {
                    transformedArray.add(id);
                }
                id++;
            } else {
                for (int j = 0; j < Integer.parseInt(c+""); j++) {
                    transformedArray.add(-1);
                }
            }
            isFile = !isFile;
        }
        return transformedArray;
    }

    public static long solve2(String input) {
        long sum = 0;
        // transform
        ArrayList<Integer> transformedArray = getTransformedArray(input);
        // moving

        performMoving2(transformedArray);

        // calc checksum
        sum = generateChecksum(transformedArray);


        return sum;
    }

    private static void performMoving2(ArrayList<Integer> transformedArray) {
        int maxId = transformedArray.stream().max(Integer::compareTo).get();

        for (int i = maxId; i >= 0; i--) {
            int sizeOfGroup = getSizeOfGroup(transformedArray, i);

            int firstEncounter = -1;
            int sizeOfFree = 0;
            for (int j = 0; j < transformedArray.size() && transformedArray.get(j) != i; j++) {
                if(transformedArray.get(j) == -1){
                    if(sizeOfFree == 0) firstEncounter = j;

                    sizeOfFree++;
                } else {
                    sizeOfFree = 0;
                }

                if(sizeOfFree >= sizeOfGroup){
                    // move group to free space
                    int finalI = i;
                    transformedArray.replaceAll(integer -> (integer == finalI ? -1 : integer));

                    for (int k = 0; k < sizeOfGroup; k++) {
                        transformedArray.set(firstEncounter + k, i);
                    }
                    break;
                }
            }
        }


    }

    private static int getSizeOfGroup(ArrayList<Integer> transformedArray, int id){
        return transformedArray.stream().filter(integer -> integer == id).toArray().length;
    }

}
