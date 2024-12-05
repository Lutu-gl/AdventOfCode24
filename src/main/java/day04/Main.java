package day04;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) {
        try {
            char[][] input = readInput("src/main/java/day04/input.txt");
            System.out.println(solve1(input));
            System.out.println(solve2(input));
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
    public static char[][] readInput(String fileName) throws IOException {
        int rowCount = 1;
        int columnCount = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            columnCount = reader.readLine().length();
            while (reader.readLine() != null) {
                rowCount++;
            }
        }

        char[][] arr = new char[rowCount][columnCount];
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            int i = 0;
            while ((line = reader.readLine()) != null) {
                arr[i++] = line.toCharArray();
            }
        }

        return arr;
    }

    public static int solve1(char[][] arr) {
        int sum = 0;
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr.length; j++) {
                if (arr[i][j] != 'X') continue;
                sum += checkXMAS(arr, i, j);
            }
        }

        return sum;
    }

    private static int checkXMAS(char[][] arr, int i, int j) {
        int count = 0;
        if(checkXmasN(arr, i, j)) count++;
        if(checkXmasNE(arr, i, j)) count++;
        if(checkXmasE(arr, i, j)) count++;
        if(checkXmasSE(arr, i, j)) count++;
        if(checkXmasS(arr, i, j)) count++;
        if(checkXmasSW(arr, i, j)) count++;
        if(checkXmasW(arr, i, j)) count++;
        if(checkXmasNW(arr, i, j)) count++;

        return count;
    }

    private static boolean checkXmasNW(char[][] arr, int i, int j) {
        if (i - 3 < 0 || j - 3 < 0) return false;
        return arr[i - 1][j - 1] == 'M' && arr[i - 2][j - 2] == 'A' && arr[i - 3][j - 3] == 'S';
    }


    private static boolean checkXmasW(char[][] arr, int i, int j) {
        if (j - 3 < 0) return false;
        return arr[i][j - 1] == 'M' && arr[i][j - 2] == 'A' && arr[i][j - 3] == 'S';    }

    private static boolean checkXmasSW(char[][] arr, int i, int j) {
        if (i + 3 >= arr.length || j - 3 < 0) return false;
        return arr[i + 1][j - 1] == 'M' && arr[i + 2][j - 2] == 'A' && arr[i + 3][j - 3] == 'S';
    }

    private static boolean checkXmasN(char[][] arr, int i, int j) {
        if (i - 3 < 0) return false;
        return arr[i-1][j] == 'M' && arr[i-2][j] == 'A' && arr[i-3][j] == 'S';
    }

    private static boolean checkXmasNE(char[][] arr, int i, int j) {
        if (i - 3 < 0 || j + 3 >= arr.length) return false;
        return arr[i - 1][j + 1] == 'M' && arr[i - 2][j + 2] == 'A' && arr[i - 3][j + 3] == 'S';
    }

    private static boolean checkXmasE(char[][] arr, int i, int j) {
        if (j + 3 >= arr.length) return false;
        return arr[i][j + 1] == 'M' && arr[i][j + 2] == 'A' && arr[i][j + 3] == 'S';
    }

    private static boolean checkXmasSE(char[][] arr, int i, int j) {
        if (i + 3 >= arr.length || j + 3 >= arr.length) return false;
        return arr[i + 1][j + 1] == 'M' && arr[i + 2][j + 2] == 'A' && arr[i + 3][j + 3] == 'S';
    }

    private static boolean checkXmasS(char[][] arr, int i, int j) {
        if (i + 3 >= arr.length) return false;
        return arr[i+1][j] == 'M' && arr[i+2][j] == 'A' && arr[i+3][j] == 'S';
    }


    public static int solve2(char[][] arr) {
        int sum = 0;
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr.length; j++) {
                if (arr[i][j] != 'A') continue;
                sum += checkX_MAS(arr, i, j);
            }
        }

        return sum;
    }

    private static int checkX_MAS(char[][] arr, int i, int j) {
        if(checkChars(arr, i-1, j-1, i+1, j+1) && checkChars(arr, i-1, j+1, i+1, j-1)) return 1;
        return 0;
    }

    private static boolean checkChars(char[][] arr, int i1, int j1, int i2, int j2){
        if (i1 < 0 || i1 >= arr.length || j1 < 0 || j1 >= arr.length) return false;
        if (i2 < 0 || i2 >= arr.length || j2 < 0 || j2 >= arr.length) return false;

        return (arr[i1][j1] == 'M' && arr[i2][j2] == 'S') || (arr[i1][j1] == 'S' && arr[i2][j2] == 'M');
    }



}
