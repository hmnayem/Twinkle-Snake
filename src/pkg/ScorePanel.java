package pkg;


import javax.swing.*;
import java.awt.*;

public class ScorePanel extends JPanel {

    private static final Font MEDIUMFONT = new Font("Tahoma", Font.BOLD, 19);
    private static final Font SMALLFONT = new Font("Tahoma", Font.BOLD, 15);

    private SnakeGame game;

    public ScorePanel(SnakeGame game) {
        this.game = game;
        setPreferredSize(new Dimension(Board.COLCOUNT * Board.TILESIZE, 100));
        setBackground(Color.WHITE.darker());

    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.BLUE.darker().darker());
        g.fillRect(0, 0, this.getWidth(), this.getHeight());


        g.setColor(Color.WHITE);
        g.setFont(SMALLFONT);
        g.drawString("SCORE: " + game.getScore(), 40, 40);
        g.drawString("EATEN: " + game.getFruitsEaten(), 170, 40);
        g.drawString("Fruit SCORE: " + game.getNextFruitScore(), 300, 40);

        g.setColor(Color.RED.darker());
        g.drawString("HIGH SCORE: " + game.getHighScore(), 40, 70);
        g.drawString("MAX EATEN: " + game.getHighestFruit(), 220, 70);

    }
}
















