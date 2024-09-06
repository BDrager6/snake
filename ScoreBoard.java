package snake;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import javax.swing.JPanel;

public class ScoreBoard extends JPanel {
    private static final int width = 600;
    private static final int height = 100;
    private static int score = 0;
    
    public ScoreBoard(){
        initScoreBoard();
    }

    private void initScoreBoard(){
        setBackground(Color.darkGray);
        setFocusable(false);
        setPreferredSize(new Dimension(width, height));
        score = GameBoard.dots;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        display(g);
    }

    private void display(Graphics g){
        String Strmsg = "Your Score: ";
        String msg = Strmsg + score;
        Font small = new Font("Helvetica", Font.BOLD, 28);
        FontMetrics metr = getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(msg, (width - metr.stringWidth(msg)) / 2, height / 2);
    }
}