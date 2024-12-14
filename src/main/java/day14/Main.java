package day14;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) {
        try {
            String input = readInput("src/main/java/day14/input.txt");
            System.out.println(solve1(input));
            solve2(input);
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
        int width = 101;
        int height = 103;
        int seconds = 100;

        // split the input by line that is totally empty and only contains newline
        String[] robotsString = input.split("\n");
        String regex = "p=(-?\\d+),(-?\\d+)\\sv=(-?\\d+),(-?\\d+)";

        Pattern pattern = Pattern.compile(regex);

        int[][] robots = new int[robotsString.length][4];

        // get information
        for (int i = 0; i < robotsString.length; i++) {
            Matcher matcher = pattern.matcher(robotsString[i]);
            if (matcher.find()) {
                int x = Integer.parseInt(matcher.group(1));
                int y = Integer.parseInt(matcher.group(2));
                int vx = Integer.parseInt(matcher.group(3));
                int vy = Integer.parseInt(matcher.group(4));
                robots[i][0] = x;
                robots[i][1] = y;
                robots[i][2] = vx;
                robots[i][3] = vy;
            }
        }

        // quadrants
        int luq = 0;
        int ruq = 0;
        int ldq = 0;
        int rdq = 0;

        int[][] grid = new int[height][width];

        // calculate robots position after 100 seconds
        for (int i = 0; i < robots.length; i++) {
            int[] robot = robots[i];

            robot[0] = myMod(seconds * robot[2] + robot[0], width);
            robot[1] = myMod(seconds * robot[3] + robot[1], height);

            //System.out.println("Robot " + i + " is at: " + robot[0] + ", " + robot[1]);
            // check in which quadrant the robot is
            if (robot[0] < width / 2 && robot[1] < height / 2) {
                luq++;
            } else if (robot[0] > width / 2 && robot[1] < height / 2) {
                ruq++;
            } else if (robot[0] < width / 2 && robot[1] > height / 2) {
                ldq++;
            } else if (robot[0] > width / 2 && robot[1] > height / 2) {
                rdq++;
            }
            grid[robot[1]][robot[0]] += 1;
        }
        //printGrid(grid);
        //System.out.println("luq: " + luq + " ruq: " + ruq + " ldq: " + ldq + " rdq: " + rdq);

        return (long) luq * ruq * ldq * rdq;
    }

    public static void printGrid(int[][] grid) {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                System.out.print(grid[i][j]);
            }
            System.out.println();
        }
    }
    // makes sure that the result is always positive
    // does the mod that python does
    public static int myMod (int a, int b) {
        return (a % b + b) % b;
    }


    public static void solve2(String input) {
        int width = 101;
        int height = 103;
        int seconds = 100000;

        // split the input by line that is totally empty and only contains newline
        String[] robotsString = input.split("\n");
        String regex = "p=(-?\\d+),(-?\\d+)\\sv=(-?\\d+),(-?\\d+)";

        Pattern pattern = Pattern.compile(regex);

        int[][] robots = new int[robotsString.length][4];

        JFrame frame = new JFrame("Robot Visualizer");
        RobotVisualizer visualizer = new RobotVisualizer(robots, width, height);
        frame.add(visualizer);
        frame.setSize(800, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // get information
        for (int i = 0; i < robotsString.length; i++) {
            Matcher matcher = pattern.matcher(robotsString[i]);
            if (matcher.find()) {
                int x = Integer.parseInt(matcher.group(1));
                int y = Integer.parseInt(matcher.group(2));
                int vx = Integer.parseInt(matcher.group(3));
                int vy = Integer.parseInt(matcher.group(4));
                robots[i][0] = x;
                robots[i][1] = y;
                robots[i][2] = vx;
                robots[i][3] = vy;
            }
        }

        for (int i = 0; i < seconds; i++) {
            int[][] positions = new int[robots.length][2];
            for (int j = 0; j < robots.length; j++) {
                int[] robot = robots[j];
                int x = myMod(robot[0] + i * robot[2], width);
                int y = myMod(robot[1] + i * robot[3], height);
                positions[j][0] = x;
                positions[j][1] = y;
            }
            if(connectedScore(positions) > 1100){
                System.out.println("Connected score: " + connectedScore(positions) + " at time: " + i);
                visualizer.updateTime(i);
                try {
                    Thread.sleep(1000); // 100ms delay for animation
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    private static int connectedScore(int[][] grid) {
        int score = 0;
        for (int i = 0; i < grid.length; i++) {
            score += calcNeighbours(grid, grid[i][0], grid[i][1]);
        }
        return score;
    }

    private static int calcNeighbours(int[][] grid, int i, int i1) {
        int score = 0;
        for (int j = 0; j < grid.length; j++) {
            if (Math.abs(i - grid[j][0]) <= 1 && Math.abs(i1 - grid[j][1]) <= 1) {
                score++;
            }
        }
        return score;
    }


}
