package day22;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class MainOptimized {
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
        // Precompute prices and changes for all buyers
        List<List<Integer>> allPrices = new ArrayList<>();
        List<List<Integer>> allChanges = new ArrayList<>();

        for (int secret : input) {
            List<Integer> prices = generatePrices(secret, 2000);
            allPrices.add(prices);
            allChanges.add(calculatePriceChanges(prices));
        }

        // Search for the best sequence in parallel
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        int maxBananas = 0;
        int[] bestSequence = null;

        try {
            List<Future<int[]>> futures = new ArrayList<>();

            for (int a = -9; a <= 9; a++) {
                final int finalA = a;
                futures.add(executor.submit(() -> {
                    int localMax = 0;
                    int[] localBestSequence = null;

                    for (int b = -9; b <= 9; b++) {
                        for (int c = -9; c <= 9; c++) {
                            for (int d = -9; d <= 9; d++) {
                                int[] sequence = {finalA, b, c, d};
                                int bananas = calculateBananas(allPrices, allChanges, sequence);
                                if (bananas > localMax) {
                                    localMax = bananas;
                                    localBestSequence = sequence;
                                }
                            }
                        }
                        System.out.println("Finished sequence: " + finalA + ", " + b);
                    }
                    return new int[] {localMax, localBestSequence[0], localBestSequence[1], localBestSequence[2], localBestSequence[3]};
                }));
            }

            for (Future<int[]> future : futures) {
                int[] result = future.get();
                if (result[0] > maxBananas) {
                    maxBananas = result[0];
                    bestSequence = new int[] {result[1], result[2], result[3], result[4]};
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }

        System.out.println("Best sequence: " + bestSequence[0] + ", " + bestSequence[1] + ", " + bestSequence[2] + ", " + bestSequence[3]);
        return maxBananas;
    }

    private static int calculateBananas(List<List<Integer>> allPrices, List<List<Integer>> allChanges, int[] sequence) {
        int totalBananas = 0;

        for (int i = 0; i < allPrices.size(); i++) {
            List<Integer> prices = allPrices.get(i);
            List<Integer> changes = allChanges.get(i);

            for (int j = 0; j <= changes.size() - sequence.length; j++) {
                boolean matches = true;
                for (int k = 0; k < sequence.length; k++) {
                    if (changes.get(j + k) != sequence[k]) {
                        matches = false;
                        break;
                    }
                }
                if (matches) {
                    totalBananas += prices.get(j + sequence.length);
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
        secret = secret.xor(value); // Mix with XOR
        secret = secret.mod(BigInteger.valueOf(16777216)); // Prune with modulo
        return secret;
    }
}
