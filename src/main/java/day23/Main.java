package day23;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        try {
            List<String> input = readInput("src/main/java/day23/input.txt");
            System.out.println(solve1(input));
            System.out.println(solve2(input));
        } catch (IOException e) {
            System.err.println("Error reading input file: " + e.getMessage());
        }
    }

    public static List<String> readInput(String fileName) throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null && !line.isEmpty()) {
                lines.add(line);
            }
        }
        return lines;
    }

    public static long solve1(List<String> input) {
        List<String[]> edges = new ArrayList<>();
        for (String s : input) {
            String[] split = s.split("-");
            edges.add(split);
        }
        HashMap<String, Set<String>> edgeSet = new HashMap<>();
        for (String[] edge : edges) {
            if(!edgeSet.containsKey(edge[0])) edgeSet.put(edge[0], new HashSet<>());
            if(!edgeSet.containsKey(edge[1])) edgeSet.put(edge[1], new HashSet<>());
            edgeSet.get(edge[0]).add(edge[1]);
            edgeSet.get(edge[1]).add(edge[0]);
        }

        Set<CliqueOfThree> cliqueOfThrees = new HashSet<>();
        for (String node1 : edgeSet.keySet()) {
            Set<String> node2s = edgeSet.get(node1);
            for (String node2 : node2s) {
                if (node2.equals(node1)) continue;
                Set<String> node3s = edgeSet.get(node2);
                for (String node3 : node3s) {
                    if (node3.equals(node1) || node3.equals(node2)) continue;
                    if (node2s.contains(node3)) {
                        CliqueOfThree cliqueOfThree = new CliqueOfThree(node1, node2, node3);
                        cliqueOfThrees.add(cliqueOfThree);
                    }
                }
            }
        }

        return cliqueOfThrees.stream()
                .filter(CliqueOfThree::containsT)
                .count();
    }


    public static String solve2(List<String> input) {
        List<String[]> edges = new ArrayList<>();
        for (String s : input) {
            String[] split = s.split("-");
            edges.add(split);
        }
        HashMap<String, Set<String>> edgeSet = new HashMap<>();
        Set<String> allNodes = new HashSet<>();
        for (String[] edge : edges) {
            allNodes.add(edge[0]);
            allNodes.add(edge[1]);

            if(!edgeSet.containsKey(edge[0])) edgeSet.put(edge[0], new HashSet<>());
            if(!edgeSet.containsKey(edge[1])) edgeSet.put(edge[1], new HashSet<>());
            edgeSet.get(edge[0]).add(edge[1]);
            edgeSet.get(edge[1]).add(edge[0]);
        }

        // find maximum set - aka. bron kerbosch algorithm
        Set<String> bestClique = new HashSet<>();
        bronKerbosch(new HashSet<>(), new HashSet<>(allNodes), new HashSet<>(), edgeSet, bestClique);

        String password = bestClique.stream()
                .sorted()
                .collect(Collectors.joining(","));


        return password;
    }

    private static void bronKerbosch(Set<String> R, Set<String> P, Set<String> X, HashMap<String, Set<String>> edgeSet, Set<String> bestClique) {
        if (P.isEmpty() && X.isEmpty()) {
            if (R.size() > bestClique.size()) {
                bestClique.clear();
                bestClique.addAll(R);
            }
            return;
        }
        Set<String> unionPX = new HashSet<>(P);
        unionPX.addAll(X);

        String u = (String) unionPX.toArray()[0];

        Set<String> neighbours = edgeSet.get(u);

        Set<String> differencePN = new HashSet<>(P);
        differencePN.removeAll(neighbours);

        for (String v : differencePN) {
            Set<String> newR = new HashSet<>(R);
            newR.add(v);

            Set<String> newP = new HashSet<>(P);
            newP.retainAll(edgeSet.getOrDefault(v, Collections.emptySet()));

            Set<String> newX = new HashSet<>(X);
            newX.retainAll(edgeSet.getOrDefault(v, Collections.emptySet()));

            bronKerbosch(newR, newP, newX, edgeSet, bestClique);

            P.remove(v);
            X.add(v);
        }
    }

}

class CliqueOfThree {
    final List<String> sortedList;

    public CliqueOfThree(String node1, String node2, String node3) {
        List<String> temp = new ArrayList<>(List.of(node1, node2, node3));
        temp.sort(String::compareTo);
        sortedList = List.copyOf(temp);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CliqueOfThree that = (CliqueOfThree) o;
        return Objects.equals(sortedList, that.sortedList);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(sortedList);
    }

    public boolean containsT() {
        return sortedList.stream().anyMatch((string) -> string.startsWith("t"));
    }
}
