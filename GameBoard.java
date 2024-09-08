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
import java.io.IOException;
import javax.swing.JPanel;
import javax.swing.Timer;

public class GameBoard extends JPanel implements ActionListener {
    private final int B_WIDTH = 600;
    private final int B_HEIGHT = 600;
    private final int DOT_SIZE = 15;  // Size of the grid cell
    private final int ALL_DOTS = 900; // Maximum number of possible dots on the board
    private final int RAND_POS = 29;  // Random position generator constraint
    public static final int DELAY = 60;    // Delay for the game loop (controls speed)

    private final int x[] = new int[ALL_DOTS]; // X coordinates of the snake's body
    private final int y[] = new int[ALL_DOTS]; // Y coordinates of the snake's body

    public static int dots = 3;                 // Current number of dots (snake's length)
    private int apple_x;              // X coordinate of the apple
    private int apple_y;              // Y coordinate of the apple

    private boolean leftDirection = false; // Direction flags
    private boolean rightDirection = true;
    private boolean upDirection = false;
    private boolean downDirection = false;
    private boolean space = true;
    private boolean change = false;
    private boolean inGame = true;    // Game state flag
    private Color scol = Color.green;
    private Color acol = Color.red;

    public static Timer timer;              // Timer to control game loop

    public GameBoard() {
        initBoard();
    }

    private void initBoard() {
        addKeyListener(new TAdapter());
        setBackground(Color.darkGray);
        setFocusable(true);
        setBorder(getBorder());
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
        if (inGame && space) {
            // Draw the apple
            g.setColor(acol);
            g.fillRect(apple_x, apple_y, DOT_SIZE, DOT_SIZE);
            // Draw the snake
            for (int z = 0; z < dots; z++) {
                if (z == 0) {
                    g.setColor(scol); // Head of the snake
                } else {
                    g.setColor(scol); // Body of the snake
                }
                g.fillRect(x[z], y[z], DOT_SIZE, DOT_SIZE);
            }
        } else if (inGame && !space){
            gamePause(g);
        } else {
            gameOver(g);
        }
    }

    private void gameOver(Graphics g) {
        String firstmsg = "Game Over";
        String secmsg = "You Scored: ";
        String thirdmsg = "Press Enter To Play Again";
        String secondmsg = secmsg + dots;
        Font small = new Font("Helvetica", Font.BOLD, 28);
        FontMetrics metr = getFontMetrics(small);

        darkScreen();
        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(firstmsg, (B_WIDTH - metr.stringWidth(firstmsg)) / 2, B_HEIGHT / 4);
        g.drawString(secondmsg, (B_WIDTH - metr.stringWidth(secondmsg)) / 2, B_HEIGHT / 3);
        g.drawString(thirdmsg, (B_WIDTH - metr.stringWidth(thirdmsg)) / 2, B_HEIGHT / 2);
        SnakeGame.window.remove(SnakeGame.sbPanel);
        SnakeGame.window.setSize(600, 600);
    }

    private void gamePause(Graphics g) {
        String msg = "Paused";
        Font small = new Font("Helvetica", Font.BOLD, 28);
        FontMetrics metr = getFontMetrics(small);

        darkScreen();
        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(msg, (B_WIDTH - metr.stringWidth(msg))/2, B_HEIGHT/2);
    }

    private void darkScreen(){
        setBackground(Color.darkGray.darker());
        scol = Color.green.darker();
        acol = Color.red.darker(); 
    }

    private void checkApple() {
        if ((x[0] == apple_x) && (y[0] == apple_y)) {
            dots++;
            JPanel newsb = new JPanel();
            newsb.add(new ScoreBoard());
            SnakeGame.sbPanel.add(newsb);
            SnakeGame.sbPanel.repaint();
            createApple();
        }
    }

    private void move() {
        scol = Color.green;
        acol = Color.red;
        setBackground(Color.darkGray);

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
            if ((x[0] == x[z]) && (y[0] == y[z])) {
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

        for(int i=0;i<dots;i++){
            if(x[i]==apple_x && y[i]==apple_y){
                createApple();
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (inGame && space) {
            checkApple();
            checkCollision();
            move();
        }
        repaint();
        change = false;
    }

    private class TAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();

            if ((key == KeyEvent.VK_LEFT) && (!rightDirection) && (!change)) {
                leftDirection = true;
                upDirection = false;
                downDirection = false;
                change = true;
            }
            if ((key == KeyEvent.VK_RIGHT) && (!leftDirection) && (!change)) {
                rightDirection = true;
                upDirection = false;
                downDirection = false;
                change = true;
            }
            if ((key == KeyEvent.VK_UP) && (!downDirection) && (!change)) {
                upDirection = true;
                rightDirection = false;
                leftDirection = false;
                change = true;
            }
            if ((key == KeyEvent.VK_DOWN) && (!upDirection) && (!change)) {
                downDirection = true;
                rightDirection = false;
                leftDirection = false;
                change = true;
            }
            if (key == KeyEvent.VK_SPACE){
                if(space && inGame){
                    space = false;
                } else {
                    space = true;
                }
            }
            if(key == KeyEvent.VK_ENTER && !inGame){
                SnakeGame.window.dispose();
                try{
                    Runtime.getRuntime().exec("javac C:\\Users\\BWDra\\OneDrive\\Documents\\Code\\Java\\snake\\SnakeGame.java").waitFor();
                    Runtime.getRuntime().exec("java C:\\Users\\BWDra\\OneDrive\\Documents\\Code\\Java\\snake\\SnakeGame.java");
                } catch (IOException | InterruptedException err) {
                    err.printStackTrace();
                }
            }
        }
    }
}