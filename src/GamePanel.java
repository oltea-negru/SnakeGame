import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

/**
 *  Class for the game logic and UI
 */
public class GamePanel extends JPanel implements ActionListener {

    /**
     * Screen dimensions
     */
    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    /**
     * A fixed size for both the "apples" and each body part of the snake
     */
    static final int UNIT_SIZE = 30;
    /**
     * Total number of units that would fit in the screen
     */
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
    /**
     * The speed of the snake
     */
    static final int DELAY = 70;

    /**
     * The coordinates for the body parts of the snake with x[0] and y[0] being the head's coordinates
     */
    final int[] x = new int[GAME_UNITS];
    final int[] y = new int[GAME_UNITS];
    /**
     * Number of body parts
     */
    int bodyParts = 3;
    /**
     * Score
     */
    int score;
    /**
     * Coordinates of an apple
     */
    int appleX;
    int appleY;
    /**
     * Direction of moving
     */
    char direction = 'R';
    /**
     * Checks whether the game is finished or not
     */
    boolean running = false;


    /**
     * Timer for snake's speed
     */
    Timer timer;
    /**
     * Random object used for assigning random coordinates to a new apple
     */
    Random random;
    /**
     * Audio support for music and sound effects
     */
    AudioPlayer player;

    /**
     * Constructor where we instantiate the Random object, set panel sizes, background characteristics and listeners
     */
    GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        player = new AudioPlayer();
        startGame();
    }

    /**
     * Listener for actively moving the snake, increasing score and checking for collisions
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }

    /**
     * Spawning a new apple at the very beginning and starting the timer
     */
    public void startGame() {
        newApple();
        player.playMusic("/Resource/snakemusic.wav");
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    /**
     * Method for updating with graphics every unit
     * @param g
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    /**
     * Method responsible for all the color movement
     * @param g
     */
    public void draw(Graphics g) {
        //if game is not finished
        if (running) {
            //paint apple red
            g.setColor(Color.RED);
            //make apple an oval shape
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            //coloring the body parts
            for (int i = 0; i < bodyParts; i++) {
                //if it is the head
                if (i == 0) {
                    g.setColor(new Color(0,100,0));
                }
                //if it is a body part
                else {
                    g.setColor(new Color(0,128,0));
                }
                g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
            }
            g.setColor(Color.RED);
            //set a text for score
            g.setFont(new Font("Ink Free", Font.BOLD, 40));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: " + score, (SCREEN_WIDTH - metrics.stringWidth("Score: " + score)) / 2, g.getFont().getSize());
        } else {
            gameOver(g);
        }
    }

    /**
     * Spawns random numbers for new coordinates of a random apple
     */
    public void newApple() {
        appleX = random.nextInt(SCREEN_WIDTH / UNIT_SIZE) * UNIT_SIZE;
        appleY = random.nextInt(SCREEN_HEIGHT / UNIT_SIZE) * UNIT_SIZE;
    }

    /**
     * Responsible for repainting units so that it gives the illusion of moving
     */
    public void move() {
        //every body part takes the coordinates of the one closer to the head
        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }
        //decides where the head will be in order to change direction
        switch (direction) {
            case 'U' -> y[0] = y[0] - UNIT_SIZE;
            case 'D' -> y[0] = y[0] + UNIT_SIZE;
            case 'L' -> x[0] = x[0] - UNIT_SIZE;
            case 'R' -> x[0] = x[0] + UNIT_SIZE;
        }
    }

    /**
     * Checks for increased score
     */
    public void checkApple() {
        //if the coordinates of the head are the same with the apple's
        if (x[0] == appleX && y[0] == appleY) {
            player.playSound("/Resource/point.wav");
            //increase score
            score++;
            //create new apple
            newApple();
            //lengthen the snake
            bodyParts++;
        }
    }

    /**
     * Checks for head of the snake touching either its own body or the edges of the screen
     */
    public void checkCollisions() {
        //checks if head collides with body
        for (int i = bodyParts; i > 0; i--) {
            if ((x[0] == x[i]) && (y[0] == y[i])) {
                running = false;
                break;
            }
        }

        //checks if head collides with the borders
        if (x[0] < 0 || x[0] > SCREEN_WIDTH || y[0] < 0 || y[0] > SCREEN_HEIGHT) {
            running = false;
        }

        //checks if game should be over
        if (!running) {
            player.playSound("/Resource/gameover.wav");
            timer.stop();
        }
    }

    /**
     * Responsible for game over screen
     * @param g
     */
    public void gameOver(Graphics g) {
        //Game Over text
        g.setColor(Color.RED);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("GAME OVER!!!",
                (SCREEN_WIDTH - metrics.stringWidth("GAME OVER!!!")) / 2, SCREEN_HEIGHT / 2);
        g.drawString("Score: " + score, (SCREEN_WIDTH - metrics.stringWidth("Score: " + score)) / 2, g.getFont().getSize());

    }

    /**
     *
     * Listens for what keyboard arrow was pressed in order to change snake's direction of moving
     */
    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (direction != 'R')
                        direction = 'L';
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direction != 'L')
                        direction = 'R';
                    break;
                case KeyEvent.VK_UP:
                    if (direction != 'D')
                        direction = 'U';
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction != 'U')
                        direction = 'D';
                    break;
            }
        }
    }}
