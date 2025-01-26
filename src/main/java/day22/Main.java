package day22;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        try {
            List<Integer> input = readInput("src/main/java/day22/input.txt");
            System.out.println(solve1(input));
            System.out.println(solve2(input));
        } catch (IOException e) {
            System.err.println("Error reading input file: " + e.getMessage());
        }
    }

    /**
     * Reads the content of the input file and returns it as a List of Integers.
     *
     * @param fileName The name of the file to read.
     * @return A List of Integers representing the input data.
     * @throws IOException If an error occurs while reading the file.
     */
    public static List<Integer> readInput(String fileName) throws IOException {
        List<Integer> input = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                input.add(Integer.parseInt(line.trim()));
            }
        }

        return input;
    }


    public static BigInteger solve1(List<Integer> input) {
        BigInteger sum = BigInteger.ZERO;
        for (int secret : input) {
            sum = sum.add(simulateSecrets(secret, 2000));
        }
        return sum;
    }


    public static int solve2(List<Integer> input) {
        int maxBananas = 0;
        int[] bestSequence = null;

        // Generate all possible sequences of four price changes (-9 to 9)
        for (int a = -9; a <= 9; a++) {
            for (int b = -9; b <= 9; b++) {
                for (int c = -9; c <= 9; c++) {
                    for (int d = -9; d <= 9; d++) {
                        int[] sequence = {a, b, c, d};
                        int bananas = calculateBananas(input, sequence);
                        if (bananas > maxBananas) {
                            maxBananas = bananas;
                            bestSequence = sequence;
                        }
                        System.out.println("Finished sequence: " + a + ", " + b + ", " + c + ", " + d);
                    }
                }
            }
        }

        System.out.println("Best sequence: " + bestSequence[0] + ", " + bestSequence[1] + ", " + bestSequence[2] + ", " + bestSequence[3]);
        return maxBananas;
    }

    private static int calculateBananas(List<Integer> input, int[] sequence) {
        int totalBananas = 0;

        for (int secret : input) {
            List<Integer> prices = generatePrices(secret, 2000);
            List<Integer> changes = calculatePriceChanges(prices);

            for (int i = 0; i <= changes.size() - sequence.length; i++) {
                boolean matches = true;
                for (int j = 0; j < sequence.length; j++) {
                    if (changes.get(i + j) != sequence[j]) {
                        matches = false;
                        break;
                    }
                }
                if (matches) {
                    totalBananas += prices.get(i + sequence.length);
                    break;
                }
            }
        }

        return totalBananas;
    }

    private static List<Integer> generatePrices(int secret, int count) {
        List<Integer> prices = new ArrayList<>();
        BigInteger current = BigInteger.valueOf(secret);

        for (int i = 0; i < count; i++) {
            current = evolveSecret(current);
            prices.add(current.mod(BigInteger.TEN).intValue());
        }

        return prices;
    }

    private static List<Integer> calculatePriceChanges(List<Integer> prices) {
        List<Integer> changes = new ArrayList<>();

        for (int i = 1; i < prices.size(); i++) {
            changes.add(prices.get(i) - prices.get(i - 1));
        }

        return changes;
    }

    private static BigInteger simulateSecrets(int secret, int n) {
        BigInteger current = BigInteger.valueOf(secret);
        for (int i = 0; i < n; i++) {
            current = evolveSecret(current);
        }
        return current;
    }


    private static BigInteger evolveSecret(BigInteger secret) {
        // Step 1: Multiply by 64, XOR, and prune
        secret = mixAndPrune(secret, secret.multiply(BigInteger.valueOf(64)));

        // Step 2: Divide by 32, round down, XOR, and prune
        secret = mixAndPrune(secret, secret.divide(BigInteger.valueOf(32)));

        // Step 3: Multiply by 2048, XOR, and prune
        secret = mixAndPrune(secret, secret.multiply(BigInteger.valueOf(2048)));

        return secret;
    }

    private static BigInteger mixAndPrune(BigInteger secret, BigInteger value) {
        secret = secret.xor(value);
        secret = secret.mod(BigInteger.valueOf(16777216));
        return secret;
    }
}