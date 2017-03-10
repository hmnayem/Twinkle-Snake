package pkg;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.Random;

public class SnakeGame extends JFrame {

    private static final long FRAMETIME = 1000L / 50L;
    private static final int SNAKELENGTH = 3;
    private static final int MAXDIRECTION = 3;

    private Board board;
    private ScorePanel scorePanel;
    private Random random;
    private GameTimer gameTimer;


    private boolean isNewGame;
    private boolean isGameOver;
    private boolean isPaused;

    private LinkedList<Point> snakePoints;
    private LinkedList<Direction> directions;

    private int score;
    private int fruitsEaten;
    private int nextFruitScore;
    private int highScore = 0;
    private int highestFruit = 0;

    public SnakeGame(){

        super("Twinkle SNAKE");
        setLayout(new BorderLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        this.board = new Board(this);
        this.scorePanel = new ScorePanel(this);

        add(board, BorderLayout.CENTER);
        add(scorePanel, BorderLayout.SOUTH);

        addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e){

                switch (e.getKeyCode()){

                    case KeyEvent.VK_W:
                    case KeyEvent.VK_UP:
                        if (!isPaused && !isGameOver){
                            if (directions.size() < MAXDIRECTION){
                                Direction last = directions.peekLast();
                                if (last != Direction.South && last != Direction.North){
                                    directions.addLast(Direction.North);
                                }
                            }
                        }
                        break;

                    case KeyEvent.VK_S:
                    case KeyEvent.VK_DOWN:
                        if (!isPaused && !isGameOver){
                            if (directions.size() < MAXDIRECTION){
                                Direction last = directions.peekLast();
                                if (last != Direction.North && last != Direction.South){
                                    directions.addLast(Direction.South);
                                }
                            }
                        }
                        break;

                    case KeyEvent.VK_A:
                    case KeyEvent.VK_LEFT:
                        if (!isPaused && !isGameOver){
                            if (directions.size() < MAXDIRECTION){
                                Direction last = directions.peekLast();
                                if (last != Direction.East && last != Direction.West){
                                    directions.addLast(Direction.West);
                                }
                            }
                        }
                        break;

                    case KeyEvent.VK_D:
                    case KeyEvent.VK_RIGHT:
                        if (!isPaused && !isGameOver){
                            if (directions.size() < MAXDIRECTION){
                                Direction last = directions.peekLast();
                                if (last != Direction.West && last != Direction.East){
                                    directions.addLast(Direction.East);
                                }
                            }
                        }
                        break;

                    case KeyEvent.VK_P:
                        if (!isGameOver) {
                            isPaused = !isPaused;
                            gameTimer.setPaused(isPaused);
                        }
                        break;

                    case KeyEvent.VK_ENTER:
                        if (isNewGame || isGameOver){
                            resetGame();
                        }
                        break;
                }
            }
        });

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void startGame(){

        this.random = new Random();
        this.snakePoints = new LinkedList<>();
        this.directions = new LinkedList<>();
        this.gameTimer = new GameTimer(7.0f);
        this.isNewGame = true;

        gameTimer.setPaused(true);

        while (true) {
            long start = System.nanoTime();

            gameTimer.update();

            if (gameTimer.hasElapsedCycle()) {
                updateGame();
            }

            board.repaint();
            scorePanel.repaint();

            long delta = (System.nanoTime() - start) / 1000000L;
            if (delta < FRAMETIME) {
                try {
                    Thread.sleep(FRAMETIME - delta);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void updateGame(){

        Tile collision = updateSnake();

        if (collision == Tile.Fruit) {
            fruitsEaten++;
            score += nextFruitScore;
            spawnFruit();
        } else if (collision == Tile.SnakeBody) {
            isGameOver = true;
            gameTimer.setPaused(true);
        } else if (nextFruitScore > 10) {
            nextFruitScore--;
        }

    }

    private Tile updateSnake(){

        Direction direction = directions.peekFirst();

        Point head = new Point(snakePoints.peekFirst());

        switch (direction) {

            case North:
                head.y--;
                break;

            case South:
                head.y++;
                break;

            case West:
                head.x--;
                break;

            case East:
                head.x++;
                break;
        }

        if (head.x < 0 || head.x >= Board.COLCOUNT || head.y < 0 || head.y >= Board.ROWCOUNT) {
            return Tile.SnakeBody;
        }

        Tile old = board.getTile(head.x, head.y);

        if (old != Tile.Fruit && snakePoints.size() > SNAKELENGTH) {
            Point tail = snakePoints.removeLast();
            board.setTile(tail, null);
            old = board.getTile(head.x, head.y);
        }

        if (old != Tile.SnakeBody) {
            board.setTile(snakePoints.peekFirst(), Tile.SnakeBody);
            snakePoints.push(head);
            board.setTile(head, Tile.SnakeHead);
            if (directions.size() > 1) {
                directions.poll();
            }
        }

        return old;
    }

    private void resetGame(){

        if (this.score > highScore) {
            highScore = this.score;
        }

        if (this.fruitsEaten > highestFruit) {
            highestFruit = this.fruitsEaten;
        }

        this.score = 0;
        this.fruitsEaten = 0;

        this.isNewGame = false;
        this.isGameOver = false;

        Point head = new Point(Board.COLCOUNT/2, Board.ROWCOUNT/2);

        snakePoints.clear();
        snakePoints.add(head);

        board.clearBoard();
        board.setTile(head, Tile.SnakeHead);

        directions.clear();
        directions.add(Direction.North);

        gameTimer.reset();

        spawnFruit();
    }

    public boolean isNewGame() {
        return isNewGame;
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    public boolean isPaused() {
        return isPaused;
    }

    private void spawnFruit(){

        this.nextFruitScore = 100;

        int index = random.nextInt(Board.COLCOUNT*Board.ROWCOUNT - snakePoints.size());

        int freeFound = -1;

        for (int x = 0; x < Board.COLCOUNT; x++) {
            for (int y = 0; y < Board.ROWCOUNT; y++) {
                Tile type = board.getTile(x, y);

                if (type == null || type == Tile.Fruit) {
                    if (++freeFound == index) {
                        board.setTile(x, y, Tile.Fruit);
                        break;
                    }
                }
            }
        }
    }

    public int getScore(){
        return score;
    }

    public int getHighScore(){ return highScore;}

    public int getFruitsEaten(){
        return fruitsEaten;
    }

    public int getHighestFruit(){
        return highestFruit;}

    public int getNextFruitScore(){
        return nextFruitScore;
    }

    public Direction getDirection(){
        return directions.peek();
    }

    public static void main(String[] args){
        SnakeGame game = new SnakeGame();
        game.startGame();

    }


}


















