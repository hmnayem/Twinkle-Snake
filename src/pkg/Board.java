package pkg;


import javax.swing.*;
import java.awt.*;

public class Board extends JPanel{

    public static final int COLCOUNT = 25;
    public static final int ROWCOUNT = 25;
    public static final int TILESIZE = 20;

    private static final Font FONT = new Font("Tahoma", Font.BOLD, 25);

    private SnakeGame game;
    private Tile [] tiles;

    public Board(SnakeGame game){
        this.game = game;
        this.tiles = new Tile[ROWCOUNT * COLCOUNT];

        setPreferredSize(new Dimension(COLCOUNT*TILESIZE, ROWCOUNT*TILESIZE));
        setBackground(Color.white);
    }

    public void clearBoard(){
        for (int i = 0; i < tiles.length; i++) {
            tiles[i] = null;
        }
    }

    //set Tile method start
    public void setTile(Point point, Tile tile){
        setTile(point.x, point.y, tile);
    }

    public void setTile(int x, int y, Tile tile){
        tiles[y * ROWCOUNT + x] = tile;
    }
    // set Tile method ends

    public Tile getTile(int x, int y){
        return tiles[y * ROWCOUNT + x];
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (int x = 0; x < COLCOUNT; x++) {
            for (int y = 0; y < ROWCOUNT; y++) {

                Tile type = getTile(x, y);

                if (type != null) {
                    drawTile(x * TILESIZE, y * TILESIZE, type, g);
                }
            }
        }


        g.setColor(Color.BLUE.darker().darker());
        g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);


        for (int x = 0; x < COLCOUNT; x++) {
            for (int y = 0; y < ROWCOUNT; y++) {

                g.drawLine(x * TILESIZE, 0, x * TILESIZE, getHeight());
                g.drawLine(0, y * TILESIZE, getWidth(), y * TILESIZE);
            }
        }
        

        if (game.isGameOver() || game.isNewGame() || game.isPaused()) {

            g.setColor(Color.GREEN.darker().darker());

            int centerX = getWidth() / 2;
            int centerY = getHeight() / 2;

            String largeMessage = null;
            String smallMessage = null;

            if (game.isNewGame()) {
                largeMessage = "Twinkle SNAKE";
                smallMessage = "Press Enter To Start";
            } else if (game.isGameOver()) {
                largeMessage = "Game Over";
                smallMessage = "Press Enter to Restart";
            } else if (game.isPaused()) {
                largeMessage = "Paused";
                smallMessage = "Press P to Resume";
            }
            g.setFont(FONT);
            g.drawString(largeMessage, centerX - g.getFontMetrics().stringWidth(largeMessage) / 2, centerY - 50);
            g.drawString(smallMessage, centerX - g.getFontMetrics().stringWidth(smallMessage) / 2, centerY + 50);
        }

    }

    private void drawTile(int x, int y, Tile tile, Graphics g){

        switch (tile){

            case Fruit:
                g.setColor(Color.RED.darker());
                g.fillOval(x+2, y+2, TILESIZE-4, TILESIZE-4);
                break;

            case SnakeBody:
                g.setColor(Color.BLACK);
                g.fillRect(x, y, TILESIZE, TILESIZE);
                g.setColor(Color.BLUE.darker());
                g.fillRect(x+1, y+1, TILESIZE-2, TILESIZE-2);
                break;

            case SnakeHead:

                g.setColor(Color.RED.darker());

                g.fillRect(x, y, TILESIZE, TILESIZE);

                break;
        }
    }
}
























