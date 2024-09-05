package snake;

import javax.swing.JFrame;

public class SnakeGame extends JFrame {

    public SnakeGame() {
        add(new GameBoard());
        setTitle("Snake Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        new SnakeGame();
    }
}