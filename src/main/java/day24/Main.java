package day24;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.function.BiFunction;

public class Main {
    public static void main(String[] args) {
        try {
            List<String> input = readInput("src/main/java/day24/input.txt");
            System.out.println(solve1(input));
            //System.out.println(solve2(input));
        } catch (IOException e) {
            System.err.println("Error reading input file: " + e.getMessage());
        }
    }

    public static List<String> readInput(String fileName) throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }
        return lines;
    }

    public static long solve1(List<String> input) {
        Map<String, BiFunction<Integer, Integer, Integer>> operators = new HashMap<>();
        operators.put("OR", (int1, int2) -> int1 | int2);
        operators.put("AND", (int1, int2) -> int1 & int2);
        operators.put("XOR", (int1, int2) -> int1 ^ int2);

        Map<String, Integer> assignments = new HashMap<>();
        Map<String, Formula> formulas = new HashMap<>();
        String s = input.getFirst();
        int i = 0;

        // handle first part of input: definitive assignment
        do {
            String[] split = s.split(": ");
            assignments.put(split[0], Integer.parseInt(split[1]));
            i++;
            s = input.get(i);
        } while (!s.isBlank());

        // handle second part of input: formulas
        while(++i < input.size()) {
            s = input.get(i);
            String[] split = s.replace("-> ", "").split(" ");
            Formula formula = new Formula(split[1], split[0], split[2]);
            formulas.put(split[3], formula);
        }


        String z = "";
        // solve all zs
        for (int j = 0; ; j++) {
            String var = String.format("z%02d", j);
            if (!formulas.containsKey(var)) {
                break;
            }

            z = (solve(var, assignments, formulas, operators)) + z;
        }


        return Long.parseLong(z, 2);
    }

    public static Integer solve(String var, Map<String, Integer> assignments, Map<String, Formula> formulas, Map<String, BiFunction<Integer, Integer, Integer>> operators) {
        if (assignments.containsKey(var)) return assignments.get(var);
        Formula formula = formulas.get(var);

        Integer val1 = solve(formula.var1, assignments, formulas, operators);
        Integer val2 = solve(formula.var2, assignments, formulas, operators);
        Integer res = operators.get(formula.operator).apply(val1, val2);

        return res;
    }


}

class Formula {
    String operator;
    String var1;
    String var2;

    public Formula(String operator, String var1, String var2) {
        this.operator = operator;
        this.var1 = var1;
        this.var2 = var2;
    }
}