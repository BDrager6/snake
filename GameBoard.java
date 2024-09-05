package snake;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JPanel;
import javax.swing.Timer;

public class GameBoard extends JPanel implements ActionListener {

    private final int B_WIDTH = 600;
    private final int B_HEIGHT = 600;
    private final int DOT_SIZE = 15;  // Size of the grid cell
    private final int ALL_DOTS = 900; // Maximum number of possible dots on the board
    private final int RAND_POS = 29;  // Random position generator constraint
    private final int DELAY = 60;    // Delay for the game loop (controls speed)

    private final int x[] = new int[ALL_DOTS]; // X coordinates of the snake's body
    private final int y[] = new int[ALL_DOTS]; // Y coordinates of the snake's body

    private int dots;                 // Current number of dots (snake's length)
    private int apple_x;              // X coordinate of the apple
    private int apple_y;              // Y coordinate of the apple

    private boolean leftDirection = false; // Direction flags
    private boolean rightDirection = true;
    private boolean upDirection = false;
    private boolean downDirection = false;
    private boolean space = true;
    private boolean inGame = true;    // Game state flag
    private Graphics g;

    private Timer timer;              // Timer to control game loop

    public GameBoard() {
        initBoard();
    }

    private void initBoard() {

        addKeyListener(new TAdapter());
        setBackground(Color.gray);
        setFocusable(true);

        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
        initGame();
    }

    private void initGame() {

        dots = 3;

        for (int z = 0; z < dots; z++) {
            x[z] = 15 - z * DOT_SIZE;
            y[z] = 15;
        }

        createApple();

        timer = new Timer(DELAY, this);
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        doDrawing(g);
    }

    private void doDrawing(Graphics g) {

        if (inGame) {

            // Draw the apple
            g.setColor(Color.red);
            g.fillRect(apple_x, apple_y, DOT_SIZE, DOT_SIZE);

            // Draw the snake
            for (int z = 0; z < dots; z++) {
                if (z == 0) {
                    g.setColor(Color.green); // Head of the snake
                } else {
                    g.setColor(Color.green); // Body of the snake
                }
                g.fillRect(x[z], y[z], DOT_SIZE, DOT_SIZE);
            }

        } else {
            gameOver(g);
        }
    }

    private void gameOver(Graphics g) {

        String msg = "Game Over";
        Font small = new Font("Helvetica", Font.BOLD, 28);
        FontMetrics metr = getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(msg, (B_WIDTH - metr.stringWidth(msg)) / 2, B_HEIGHT / 2);
    }

    private void gamePause(Graphics g) {

        String msg = "Game Paused";
        Font small = new Font("Helvetica", Font.BOLD, 28);
        FontMetrics metr = getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(msg, (B_WIDTH - metr.stringWidth(msg)) / 2, B_HEIGHT / 2);
    }

    private void checkApple() {

        if ((x[0] == apple_x) && (y[0] == apple_y)) {
            dots++;
            createApple();
        }
    }

    private void move() {

        for (int z = dots; z > 0; z--) {
            x[z] = x[(z - 1)];
            y[z] = y[(z - 1)];
        }

        if (leftDirection) {
            x[0] -= DOT_SIZE;
        }

        if (rightDirection) {
            x[0] += DOT_SIZE;
        }

        if (upDirection) {
            y[0] -= DOT_SIZE;
        }

        if (downDirection) {
            y[0] += DOT_SIZE;
        }
    }

    private void checkCollision() {

        for (int z = dots; z > 0; z--) {

            if ((z > 4) && (x[0] == x[z]) && (y[0] == y[z])) {
                inGame = false;
            }
        }

        if (y[0] >= B_HEIGHT) {
            inGame = false;
        }

        if (y[0] < 0) {
            inGame = false;
        }

        if (x[0] >= B_WIDTH) {
            inGame = false;
        }

        if (x[0] < 0) {
            inGame = false;
        }

        if (!inGame) {
            timer.stop();
        }
    }

    private void createApple() {

        int r = (int) (Math.random() * RAND_POS);
        apple_x = ((r * DOT_SIZE));

        r = (int) (Math.random() * RAND_POS);
        apple_y = ((r * DOT_SIZE));
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (inGame && space) {

            checkApple();
            checkCollision();
            move();
            repaint();
        }
    }

    private class TAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {

            int key = e.getKeyCode();

            if ((key == KeyEvent.VK_LEFT) && (!rightDirection)) {
                leftDirection = true;
                upDirection = false;
                downDirection = false;
            }

            if ((key == KeyEvent.VK_RIGHT) && (!leftDirection)) {
                rightDirection = true;
                upDirection = false;
                downDirection = false;
            }

            if ((key == KeyEvent.VK_UP) && (!downDirection)) {
                upDirection = true;
                rightDirection = false;
                leftDirection = false;
            }

            if ((key == KeyEvent.VK_DOWN) && (!upDirection)) {
                downDirection = true;
                rightDirection = false;
                leftDirection = false;
            }
            if (key == KeyEvent.VK_SPACE){
                if(space){
                    space = false;
                    g = getGraphics();
                    gamePause(g);
                } else {
                    space = true;
                }
            }
        }
    }
}