import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Random;

public class SnakeGame extends JPanel implements ActionListener, KeyListener {
    private class Tile {
        int x, y;

        Tile(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    private int boardWidth = 600;
    private int boardHeight = 600;
    private int cellSize = 25;

    private Tile snakeHead;
    private ArrayList<Tile> snakeBody;

    private Tile food;
    private Random random;

    // Game logic
    private Timer gameLoop;
    private int velocityX;
    private int velocityY;
    private boolean gameOver = false;

    public SnakeGame(int boardWidth, int boardHeight) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        setPreferredSize(new Dimension(this.boardWidth, this.boardHeight));
        setBackground(Color.BLACK);
        addKeyListener(this);
        setFocusable(true);

        snakeHead = new Tile(5, 5);
        snakeBody = new ArrayList<>();

        food = new Tile(10, 10);
        random = new Random();
        placeFood();

        velocityX = 1;
        velocityY = 0;

        gameLoop = new Timer(100, this);
        gameLoop.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        // Food
        g.setColor(Color.RED);
        g.fill3DRect(food.x * cellSize, food.y * cellSize, cellSize, cellSize, true);

        // Snake Head
        g.setColor(Color.GREEN);
        g.fill3DRect(snakeHead.x * cellSize, snakeHead.y * cellSize, cellSize, cellSize, true);

        // Snake Body
        for (Tile snakePart : snakeBody) {
            g.fill3DRect(snakePart.x * cellSize, snakePart.y * cellSize, cellSize, cellSize, true);
        }

        // Score or Game Over
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        g.setColor(gameOver ? Color.RED : Color.WHITE);
        g.drawString(gameOver ? "Game Over: " + snakeBody.size() : "Score: " + snakeBody.size(), cellSize - 16,
                cellSize);
    }

    public void placeFood() {
        food.x = random.nextInt(boardWidth / cellSize);
        food.y = random.nextInt(boardHeight / cellSize);
    }

    public boolean collision(Tile tile1, Tile tile2) {
        return tile1.x == tile2.x && tile1.y == tile2.y;
    }

    public void move() {
        if (collision(snakeHead, food)) {
            snakeBody.add(new Tile(food.x, food.y));
            placeFood();
        }

        for (int i = snakeBody.size() - 1; i > 0; i--) {
            Tile snakePart = snakeBody.get(i);
            Tile prevSnakePart = snakeBody.get(i - 1);
            snakePart.x = prevSnakePart.x;
            snakePart.y = prevSnakePart.y;
        }
        if (!snakeBody.isEmpty()) {
            Tile firstSnakePart = snakeBody.get(0);
            firstSnakePart.x = snakeHead.x;
            firstSnakePart.y = snakeHead.y;
        }

        snakeHead.x += velocityX;
        snakeHead.y += velocityY;

        for (Tile snakePart : snakeBody) {
            if (collision(snakeHead, snakePart)) {
                gameOver = true;
            }
        }

        if (snakeHead.x < 0 || snakeHead.x >= boardWidth / cellSize || snakeHead.y < 0
                || snakeHead.y >= boardHeight / cellSize) {
            gameOver = true;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();

        if (gameOver) {
            gameLoop.stop();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP && velocityY != 1) {
            velocityX = 0;
            velocityY = -1;
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN && velocityY != -1) {
            velocityX = 0;
            velocityY = 1;
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT && velocityX != 1) {
            velocityX = -1;
            velocityY = 0;
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT && velocityX != -1) {
            velocityX = 1;
            velocityY = 0;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Snake Game");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            SnakeGame snakeGame = new SnakeGame(600, 600);
            frame.add(snakeGame);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
