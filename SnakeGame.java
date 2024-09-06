package snake;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class SnakeGame extends JFrame {

    public static JPanel sbPanel = new JPanel();
    private final JFrame window = new JFrame("Snake");

    public SnakeGame() {
        sbPanel.add(new ScoreBoard());
        window.add(new GameBoard());
        window.setTitle("Snake Game");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.pack();
        window.setLocationRelativeTo(null);
        window.getContentPane().add(BorderLayout.SOUTH, sbPanel);
        window.setVisible(true);
        window.setSize(600, 745);
    }

    public static void main(String[] args) {
        new SnakeGame();
    }
}