package day14;

import javax.swing.*;
import java.awt.*;


public class RobotVisualizer extends JPanel {
    private int[][] robots;
    private int width, height, time;

    public RobotVisualizer(int[][] robots, int width, int height) {
        this.robots = robots;
        this.width = width;
        this.height = height;
        this.time = 0;
    }

    public void updateTime(int time) {
        this.time = time;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());

        int cellWidth = getWidth() / width;
        int cellHeight = getHeight() / height;

        g.setColor(Color.RED);
        for (int[] robot : robots) {
            int x = myMod(robot[0] + time * robot[2], width);
            int y = myMod(robot[1] + time * robot[3], height);
            g.fillRect(x * cellWidth, y * cellHeight, cellWidth, cellHeight);
        }
    }

    public static int myMod(int a, int b) {
        return (a % b + b) % b;
    }

    // example usage
    public static void main(String[] args) {
        int width = 101, height = 103, seconds = 100;
        int[][] robots = {
                {50, 50, 1, 1}, // Beispielroboter
                {30, 20, 1, -1},
                {80, 40, -1, 1}
        };

        JFrame frame = new JFrame("Robot Visualizer");
        RobotVisualizer visualizer = new RobotVisualizer(robots, width, height);
        frame.add(visualizer);
        frame.setSize(800, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Update the visualization over time
        for (int t = 0; t <= seconds; t++) {
            visualizer.updateTime(t);
            try {
                Thread.sleep(100); // 100ms delay for animation
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
