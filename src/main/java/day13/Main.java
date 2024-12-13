package day13;

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
            String input = readInput("src/main/java/day13/input.txt");
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
    public static int solve1(String input) {

        // split the input by line that is totally empty and only contains newline
        String[] claw_machines = input.split("\n\n");
        int sum = 0;
        for (int i = 0; i < claw_machines.length; i++) {
            sum += solve_claw_machine(claw_machines[i]);
        }
        return sum;
    }


    // it costs 3 tokens for A and 1 token for B
    private static int solve_claw_machine(String clawMachine) {
        // read the setting of the claw machine. Settings look like:
        //Button A: X+27, Y+18
        //Button B: X+17, Y+44
        //Prize: X=8105, Y=9698
        String[] settings = clawMachine.split("\n");
        int buttonA_X = Integer.parseInt(settings[0].split(" ")[2].split("\\+")[1].replace(",", ""));
        int buttonA_Y = Integer.parseInt(settings[0].split(" ")[3].split("\\+")[1]);
        int buttonB_X = Integer.parseInt(settings[1].split(" ")[2].split("\\+")[1].replace(",", ""));
        int buttonB_Y = Integer.parseInt(settings[1].split(" ")[3].split("\\+")[1]);
        int prize_X = Integer.parseInt(settings[2].split(" ")[1].split("=")[1].replace(",", ""));
        int prize_Y = Integer.parseInt(settings[2].split(" ")[2].split("=")[1]);

        // buttonA_X * N + buttonB_X * M = prize_X
        // buttonA_Y * N + buttonB_Y * M = prize_Y

        // solve the equation with Cramer's rule

        /*
        double detA = buttonA_X * buttonB_Y - buttonB_X * buttonA_Y;
        double detN = prize_X * buttonB_Y - buttonB_X * prize_Y;
        double detM = buttonA_X * prize_Y - prize_X * buttonA_Y;

        if (detA != 0) {
            double N = detN / detA;
            double M = detM / detA;

            // Ergebnis ausgeben
            System.out.println("Lösung:");
            System.out.println("N = " + N);
            System.out.println("M = " + M);
        } else {
            System.out.println("Das Gleichungssystem hat keine eindeutige Lösung (detA = 0).");
        }*/

        // Iterative simple
        int maxSearchLimit = 100; // stop criteria

        for (int N = 0; N <= maxSearchLimit; N++) {
            for (int M = 0; M <= maxSearchLimit; M++) {
                if ((buttonA_X * N + buttonB_X * M == prize_X) && (buttonA_Y * N + buttonB_Y * M == prize_Y)) {
                    //System.out.println("N = " + N);
                    //System.out.println("M = " + M);
                    return 3*N + M;
                }
            }
        }

        return 0;
    }


    // it costs 3 tokens for A and 1 token for B
    private static long solve_claw_machine2(String clawMachine) {
        // read the setting of the claw machine. Settings look like:
        //Button A: X+27, Y+18
        //Button B: X+17, Y+44
        //Prize: X=8105, Y=9698
        String[] settings = clawMachine.split("\n");
        long buttonA_X = Integer.parseInt(settings[0].split(" ")[2].split("\\+")[1].replace(",", ""));
        long buttonA_Y = Integer.parseInt(settings[0].split(" ")[3].split("\\+")[1]);
        long buttonB_X = Integer.parseInt(settings[1].split(" ")[2].split("\\+")[1].replace(",", ""));
        long buttonB_Y = Integer.parseInt(settings[1].split(" ")[3].split("\\+")[1]);
        long prize_X = Integer.parseInt(settings[2].split(" ")[1].split("=")[1].replace(",", ""));
        long prize_Y = Integer.parseInt(settings[2].split(" ")[2].split("=")[1]);

        prize_Y += 10000000000000L;
        prize_X += 10000000000000L;
        // buttonA_X * N + buttonB_X * M = prize_X
        // buttonA_Y * N + buttonB_Y * M = prize_Y

        // solve the equation with Cramer's rule
        // Berechne Determinanten (Cramer's Rule)
        long detA = buttonA_X * buttonB_Y - buttonB_X * buttonA_Y;
        long detN = prize_X * buttonB_Y - buttonB_X * prize_Y;
        long detM = buttonA_X * prize_Y - prize_X * buttonA_Y;

        if (detA == 0) {    // no solution
            return 0;
        }

        // Check if solutions are int values.
        if (detN % detA == 0 && detM % detA == 0) {
            long N = detN / detA;
            long M = detM / detA;
            return 3 * (long) N + (long) M;
        } else {    // no int solution
            return 0;
        }
    }

    public static long solve2(String input) {

        // split the input by line that is totally empty and only contains newline
        String[] claw_machines = input.split("\n\n");
        long sum = 0L;
        for (int i = 0; i < claw_machines.length; i++) {
            sum += solve_claw_machine2(claw_machines[i]);
        }
        return sum;
    }
}
