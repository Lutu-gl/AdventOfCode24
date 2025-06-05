package day25;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        try {
            List<List<String>> locksAndKeys = readInput("src/main/java/day25/input.txt");
            System.out.println(solve1(locksAndKeys));
        } catch (IOException e) {
            System.err.println("Error reading input file: " + e.getMessage());
        }
    }

    public static List<List<String>> readInput(String fileName) throws IOException {
        List<List<String>> locksAndKeys = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            List<String> lockAndKey = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                if(line.isEmpty()) {
                    locksAndKeys.add(lockAndKey);
                    lockAndKey = new ArrayList<>();
                    continue;
                }
                lockAndKey.add(line);
            }
            locksAndKeys.add(lockAndKey);
        }
        return locksAndKeys;
    }

    public static long solve1(List<List<String>> locksAndKeys) {
        List<LockKey> locks = new ArrayList<>();
        List<LockKey> keys = new ArrayList<>();
        long matches = 0L;

        for (List<String> lockAndKey : locksAndKeys) {
            int line = 0;
            Map<Integer, Integer> heights = new HashMap<>();
            boolean isLock = lockAndKey.get(0).charAt(0) == '#';

            for (String s : lockAndKey) {
                for (int i = 0; i < s.length(); i++) {
                    if(s.charAt(i) == (isLock ? '.' : '#')) {
                        int finalLine = line;
                        heights.computeIfAbsent(i, key -> isLock ? finalLine-1 : 5 - (finalLine-1));
                    }
                }
                line++;
            }
            if (isLock) {
                locks.add(new Lock(heights));
            } else {
                keys.add(new Key(heights));
            }
        }


        for (LockKey lock : locks) {
            for (LockKey key : keys) {
                boolean match = true;
                for (int i = 0; i < 5; i++) {
                    int lockNumber = lock.heights.get(i);
                    int keyNumber = key.heights.get(i);
                    if(lockNumber + keyNumber > 5) match = false;
                }
                if(match){
                    matches++;
                }
            }
        }

        return matches;
    }

    private static void printHeight(List<LockKey> list) {
        for (LockKey element : list) {
            for (int i = 0; i < 5; i++) {
                System.out.print(element.heights.get(i) + " ");
            }
            System.out.println();
        }
    }


}

class LockKey {
    Map<Integer, Integer> heights;

    public LockKey(Map<Integer, Integer> heights) {
        this.heights = heights;
    }
}

class Lock extends LockKey {
    public Lock(Map<Integer, Integer> heights) {
        super(heights);
    }
}

class Key extends LockKey {
    public Key(Map<Integer, Integer> heights) {
        super(heights);
    }
}