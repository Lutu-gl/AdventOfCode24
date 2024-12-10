package day06;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        try {
            char[][] input = readInput("src/main/java/day06/input.txt");
            System.out.println(solve1(deepCopy(input)));
            System.out.println(solve2(deepCopy(input)));
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

    public static char[][] deepCopy(char[][] original) {
        if (original == null) {
            return null;
        }

        final char[][] result = new char[original.length][];
        for (int i = 0; i < original.length; i++) {
            result[i] = Arrays.copyOf(original[i], original[i].length);
        }
        return result;
    }

    public static int solve1(char[][] arr) {
        int sum = 0;
        int startX = 0;
        int startY = 0;

        // find out starting point
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[0].length; j++) {
                if(arr[i][j] == '^' || arr[i][j] == 'v' || arr[i][j] == '<' || arr[i][j] == '>') {
                    startX = j;
                    startY = i;
                    break;
                }
            }
        }


        int traverse = traverse(arr, startX, startY, arr[startY][startX]);

        //printArr(arr);
        return traverse;
    }

    public static void printArr(char[][] arr) {
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[0].length; j++) {
                System.out.print(arr[i][j]);
            }
            System.out.println();
        }
    }

    public static int traverse (char[][] arr, int x, int y, char direction) {

        if (x < 0 || x >= arr[0].length || y < 0 || y >= arr.length ) {
            return 0;
        }
        int sum = 0;
        boolean stop = false;
        while (!stop) {
            if (y < 0 || y >= arr.length || x < 0 || x >= arr[0].length) {
                stop = true;
                break;
            }

            if(arr[y][x] != 'X') {
                sum++;
                arr[y][x] = 'X';
            }

            switch (direction) {
                case '^':
                    if (isWand(arr, x, y-1)) {
                        sum += traverse(arr, x, y, '>');
                        stop = true;
                    }
                    y--;
                    break;
                case 'v':
                    if (isWand(arr, x, y+1)) {
                        sum += traverse(arr, x, y, '<');
                        stop = true;
                    }
                    y++;
                    break;
                case '<':
                    if (isWand(arr, x-1, y)) {
                        sum += traverse(arr, x, y, '^');
                        stop = true;
                    }
                    x--;
                    break;
                case '>':
                    if (isWand(arr, x+1, y)) {
                        sum += traverse(arr, x, y, 'v');
                        stop = true;
                    }
                    x++;
                    break;
            }
        }

        return sum;
    }

    public static boolean isWand(char[][] arr, int x, int y) {
        if (x < 0 || x >= arr[0].length || y < 0 || y >= arr.length) {
            return false;
        }
        return arr[y][x] == '#';
    }


    public static int solve2(char[][] arr) {
        int sum = 0;
        int startX = 0;
        int startY = 0;

        // find out starting point
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[0].length; j++) {
                if(arr[i][j] == '^' || arr[i][j] == 'v' || arr[i][j] == '<' || arr[i][j] == '>') {
                    startX = j;
                    startY = i;
                    break;
                }
            }
        }
        char[][] path_arr = deepCopy(arr);
        traverse(path_arr, startX, startY, arr[startY][startX]);        // get the path of the first traverse, obstacles make only sense there

        for (int i = 0; i < path_arr.length; i++) {
            for (int j = 0; j < path_arr[0].length; j++) {
                if (path_arr[i][j] == 'X') {     // go through all positions
                    int[][] control_arr = initControlArr(arr);
                    char[][] deepCopyArr = deepCopy(arr);
                    deepCopyArr[i][j] = '#';        // set the obstacle
                    if (isInLoopWhileTraversing(deepCopyArr, startX, startY, arr[startY][startX], control_arr)) {
                        sum++;
                    }

                }
            }
        }

        return sum;
    }

    private static int[][] initControlArr(char[][] arr) {
        int[][] carr = new int[arr.length][arr[0].length];
        for (int i = 0; i < carr.length; i++) {
            for (int j = 0; j < carr[0].length; j++) {
                carr[i][j] = 0;
            }
        }
        return carr;
    }

    // returns true if traverse gets stuck in a loop
    public static boolean isInLoopWhileTraversing (char[][] arr, int x, int y, char direction, int[][] control_arr) {

        if (x < 0 || x >= arr[0].length || y < 0 || y >= arr.length ) {
            return false;
        }
        int sum = 0;
        boolean stop = false;
        while (!stop) {
            if (y < 0 || y >= arr.length || x < 0 || x >= arr[0].length) {
                stop = true;
                break;
            }

            if(arr[y][x] != 'X') {
                sum++;
                arr[y][x] = 'X';
            }

            switch (direction) {
                case '^':
                    if (isWand(arr, x, y-1)) {
                        if(control_arr[y-1][x] > 4) {
                            return true;
                        } else {
                            control_arr[y-1][x]++;
                        }
                        return isInLoopWhileTraversing(arr, x, y, '>', control_arr);
                    }
                    y--;
                    break;
                case 'v':
                    if (isWand(arr, x, y+1)) {
                        if(control_arr[y+1][x] > 4) {
                            return true;
                        } else {
                            control_arr[y+1][x]++;
                        }
                        return isInLoopWhileTraversing(arr, x, y, '<', control_arr);
                    }
                    y++;
                    break;
                case '<':
                    if (isWand(arr, x-1, y)) {
                        if(control_arr[y][x-1] > 4) {
                            return true;
                        } else {
                            control_arr[y][x-1]++;
                        }
                        return isInLoopWhileTraversing(arr, x, y, '^', control_arr);
                    }
                    x--;
                    break;
                case '>':
                    if (isWand(arr, x+1, y)) {
                        if(control_arr[y][x+1] > 4) {
                            return true;
                        } else {
                            control_arr[y][x+1]++;
                        }
                        return isInLoopWhileTraversing(arr, x, y, 'v', control_arr);
                    }
                    x++;
                    break;
            }
        }

        return false;
    }
}
