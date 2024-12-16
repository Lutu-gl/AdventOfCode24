package day15;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        try {
            char[][] input = readInput("src/main/java/day15/input.txt");
            ArrayList<Character> sequence = readMoves("src/main/java/day15/input.txt");
            System.out.println(solve1(deepCopy(input), sequence));
            System.out.println(solve2(deepCopy(input), sequence));
        } catch (IOException e) {
            System.err.println("Error reading input file: " + e.getMessage());
        }
    }

    private static ArrayList<Character> readMoves(String fileName) throws IOException {

        ArrayList<Character> moves = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            // skip the first n lines where the field is defined
            while(!reader.readLine().isEmpty()) {
                // skip
            }

            String line;
            while ((line = reader.readLine()) != null) {
                for (char c : line.toCharArray()) {
                    moves.add(c);
                }
            }
        }

        return moves;
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
            while (!reader.readLine().isEmpty()) {
                rowCount++;
            }
        }

        char[][] arr = new char[rowCount][columnCount];
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            int i = 0;
            while ((line = reader.readLine()) != null) {
                if(line.isEmpty()) {
                    break;
                }
                arr[i++] = line.toCharArray();
            }
        }

        return arr;
    }

    public static long solve1(char[][] arr, ArrayList<Character> sequence) {
        // find the robot:
        int robotI = -1;
        int robotJ = -1;

        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[0].length; j++) {
                if (arr[i][j] == '@') {
                    robotI = i;
                    robotJ = j;
                    break;
                }
            }
        }
        if (robotI == -1 || robotJ == -1) {
            return 0;
        }

        // move the robot
        for (int i = 0; i < sequence.size(); i++) {
            char move = sequence.get(i);
            //printArr(arr);
            //System.out.println("Move: " + move);
            if(move == '^') {
                if(arr[robotI - 1][robotJ] == '#') {
                    continue;
                } else if (arr[robotI - 1][robotJ] == '.') {
                    arr[robotI][robotJ] = '.';
                    arr[robotI - 1][robotJ] = '@';
                    robotI--;

                } else if (arr[robotI - 1][robotJ] == 'O') {
                    if (doMoveOfBox(arr, robotI, robotJ, move)) {   // if move is possbile then move the robot one up, move of box is done by function
                        arr[robotI][robotJ] = '.';
                        arr[robotI - 1][robotJ] = '@';
                        robotI--;
                    }
                }
            } else if (move == 'v') {
                if(arr[robotI + 1][robotJ] == '#') {
                    continue;
                } else if (arr[robotI + 1][robotJ] == '.') {
                    arr[robotI][robotJ] = '.';
                    arr[robotI + 1][robotJ] = '@';
                    robotI++;
                } else if (arr[robotI + 1][robotJ] == 'O') {
                    if (doMoveOfBox(arr, robotI, robotJ, move)) {
                        arr[robotI][robotJ] = '.';
                        arr[robotI + 1][robotJ] = '@';
                        robotI++;
                    }
                }
            } else if (move == '<') {
                if(arr[robotI][robotJ - 1] == '#') {
                    continue;
                } else if (arr[robotI][robotJ - 1] == '.') {
                    arr[robotI][robotJ] = '.';
                    arr[robotI][robotJ - 1] = '@';
                    robotJ--;
                } else if (arr[robotI][robotJ - 1] == 'O') {
                    if (doMoveOfBox(arr, robotI, robotJ, move)) {
                        arr[robotI][robotJ] = '.';
                        arr[robotI][robotJ - 1] = '@';
                        robotJ--;
                    }
                }
            } else if (move == '>') {
                if(arr[robotI][robotJ + 1] == '#') {
                    continue;
                } else if (arr[robotI][robotJ + 1] == '.') {
                    arr[robotI][robotJ] = '.';
                    arr[robotI][robotJ + 1] = '@';
                    robotJ++;
                } else if (arr[robotI][robotJ + 1] == 'O') {
                    if (doMoveOfBox(arr, robotI, robotJ, move)) {
                        arr[robotI][robotJ] = '.';
                        arr[robotI][robotJ + 1] = '@';
                        robotJ++;
                    }
                }
            }
        }


        //printArr(arr);
        //System.out.println("Sequence: " + sequence);



        return calculateGps(arr);
    }

    private static long calculateGps(char[][] arr) {
        long sum = 0;
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[0].length; j++) {
                if(arr[i][j] == 'O' || arr[i][j] == '[') {
                    sum += 100L * i + j;
                }
            }
        }
        return sum;
    }

    private static boolean doMoveOfBox(char[][] arr, int robotI, int robotJ, char move) {
        // is move possible? Is after x 'O' in the direction of move a '#' then its not possible
        int i = robotI;
        int j = robotJ;
        while (arr[i][j] != '#') {
            if (arr[i][j] == '.') {
                arr[i][j] = 'O';
                return true;
            }

            if (move == '^') {
                i--;
            } else if (move == 'v') {
                i++;
            } else if (move == '<') {
                j--;
            } else if (move == '>') {
                j++;
            }
        }
        return false;
    }


    private static void printArr(char[][] arr) {
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[0].length; j++) {
                System.out.print(arr[i][j]);
            }
            System.out.println();
        }
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

    public static long solve2(char[][] arrOld, ArrayList<Character> sequence) {
        //printArr(arrOld);

        // transform the field
        char[][] arr = new char[arrOld.length][arrOld[0].length * 2];
        for (int i = 0; i < arrOld.length; i++) {
            for (int j = 0; j < arrOld[0].length; j++) {
                if(arrOld[i][j] == '@') {
                    arr[i][2*j] = '@';
                    arr[i][2*j+1] = '.';
                } else if (arrOld[i][j] == 'O') {
                    arr[i][2*j] = '[';
                    arr[i][2*j+1] = ']';
                } else {
                    arr[i][2*j] = arrOld[i][j];
                    arr[i][2*j+1] = arrOld[i][j];
                }
            }
        }



        // find the robot:
        int robotI = -1;
        int robotJ = -1;

        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[0].length; j++) {
                if (arr[i][j] == '@') {
                    robotI = i;
                    robotJ = j;
                    break;
                }
            }
        }
        if (robotI == -1 || robotJ == -1) {
            return 0;
        }

        // move the robot
        for (int i = 0; i < sequence.size(); i++) {
            //printArr(arr);
            //System.out.println("Move: " + sequence.get(i));

            char move = sequence.get(i);
            if(move == '^') {
                if(arr[robotI - 1][robotJ] == '#') {
                    continue;
                } else if (arr[robotI - 1][robotJ] == '.') {
                    arr[robotI][robotJ] = '.';
                    arr[robotI - 1][robotJ] = '@';
                    robotI--;

                } else if (arr[robotI - 1][robotJ] == '[' || arr[robotI - 1][robotJ] == ']') {  // box
                    if (doMoveOfBoxVertically(arr, robotI, robotJ, move)) {   // if move is possbile then move the robot one up, move of box is done by function
                        arr[robotI][robotJ] = '.';
                        arr[robotI - 1][robotJ] = '@';
                        robotI--;
                    }
                }
            } else if (move == 'v') {
                if(arr[robotI + 1][robotJ] == '#') {
                    continue;
                } else if (arr[robotI + 1][robotJ] == '.') {
                    arr[robotI][robotJ] = '.';
                    arr[robotI + 1][robotJ] = '@';
                    robotI++;
                } else if (arr[robotI + 1][robotJ] == '[' || arr[robotI + 1][robotJ] == ']') {
                    if (doMoveOfBoxVertically(arr, robotI, robotJ, move)) {
                        arr[robotI][robotJ] = '.';
                        arr[robotI + 1][robotJ] = '@';
                        robotI++;
                    }
                }
            } else if (move == '<') {
                if(arr[robotI][robotJ - 1] == '#') {
                    continue;
                } else if (arr[robotI][robotJ - 1] == '.') {
                    arr[robotI][robotJ] = '.';
                    arr[robotI][robotJ - 1] = '@';
                    robotJ--;
                } else if (arr[robotI][robotJ - 1] == ']') {
                    if (doMoveOfBoxHorizontally(arr, robotI, robotJ, move)) {
                        arr[robotI][robotJ] = '.';
                        arr[robotI][robotJ - 1] = '@';
                        robotJ--;
                    }
                }
            } else if (move == '>') {
                if(arr[robotI][robotJ + 1] == '#') {
                    continue;
                } else if (arr[robotI][robotJ + 1] == '.') {
                    arr[robotI][robotJ] = '.';
                    arr[robotI][robotJ + 1] = '@';
                    robotJ++;
                } else if (arr[robotI][robotJ + 1] == '[') {
                    if (doMoveOfBoxHorizontally(arr, robotI, robotJ, move)) {
                        arr[robotI][robotJ] = '.';
                        arr[robotI][robotJ + 1] = '@';
                        robotJ++;
                    }
                }
            }
        }

        //printArr(arr);
        return calculateGps(arr);
    }

    // check if a move upwards is possible
    private static boolean checkFreeAbove (char[][] arr, int i, int j, char move) {
        int m = 0;
        if (move == '^') {
            m = -1;
        } else if (move == 'v') {
            m = 1;
        } else {
            return false;
        }

        if (arr[i+m][j] == '#') {
            return false;
        } else if (arr[i+m][j] == '.') {
            return true;
        } else if (arr[i+m][j] == '[') {
            return checkFreeAbove(arr, i+m, j, move) && checkFreeAbove(arr, i+m, j+1, move);
        } else if (arr[i+m][j] == ']') {
            return checkFreeAbove(arr, i+m, j, move) && checkFreeAbove(arr, i+m, j-1, move);
        }
        return true;

    }

    private static void moveBoxesAbove(char[][] arr, int i, int j, char move) {
        int m = 0;
        if (move == '^') {
            m = -1;
        } else if (move == 'v') {
            m = 1;
        } else {
            return;
        }

        if (arr[i+m][j] == '#') {
            return;
        } else if (arr[i+m][j] == '.') {
            arr[i+m][j] = arr[i][j];
            arr[i][j] = '.';
        } else if (arr[i+m][j] == '[') {
            moveBoxesAbove(arr, i+m, j, move);
            moveBoxesAbove(arr, i+m, j+1, move);
            arr[i+m][j] = arr[i][j];
            arr[i][j] = '.';
        } else if (arr[i+m][j] == ']') {
            moveBoxesAbove(arr, i+m, j, move);
            moveBoxesAbove(arr, i+m, j-1, move);
            arr[i+m][j] = arr[i][j];
            arr[i][j] = '.';
        }
    }

    private static boolean doMoveOfBoxVertically (char[][] arr, int robotI, int robotJ, char move) {

        if (checkFreeAbove(arr, robotI, robotJ, move)) {
            moveBoxesAbove(arr, robotI, robotJ, move);
            return true;
        }

        return false;
    }



    private static boolean doMoveOfBoxHorizontally(char[][] arr, int robotI, int robotJ, char move) {
        // is move possible? Is after x 'O' in the direction of move a '#' then its not possible
        int i = robotI;
        int j = robotJ;
        while (arr[i][j] != '#') {
            if (arr[i][j] == '.') {
                if (move == '<') {
                    arr[i][j] = ']'; // gets switched to ']'!
                } else {
                    arr[i][j] = '['; // gets switched to '['!
                }
                // go backwards and switch the '[' to ']' until it reaches the robot @

                while (arr[i][j] != '@') {
                    if (arr[i][j] == ']') {
                        arr[i][j] = '[';
                    } else {
                        arr[i][j] = ']';
                    }

                    if (move == '<') {
                        j++;
                    } else if (move == '>') {
                        j--;
                    }
                }

                return true;
            }

            if (move == '<') {
                j--;
            } else if (move == '>') {
                j++;
            }
        }
        return false;
    }
}
