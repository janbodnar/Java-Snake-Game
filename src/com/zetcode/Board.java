package com.zetcode;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Board extends JPanel implements ActionListener {

    private final int BOARD_WIDTH = 300;
    private final int BOARD_HEIGHT = 300;
    private final int DOT_SIZE = 10; //A 'dot' is a single segmentation of the snake's body.
    private final int MAX_LENGTH = 900;
    private final int RAND_POS = 29; //The value assigned here is the multiplier for the random value generated later.
    private final int DELAY = 140; //The delay between 'ticks' of the game (refreshes of the game board)

    private final int[] dotXPos = new int[MAX_LENGTH]; //Holds the x position of each dot on the snake.
    private final int[] dotYPos = new int[MAX_LENGTH]; //Holds the y position of each dot on the snake.

    //Property variables
    private int currentSnakeLength;
    private int appleXPos, appleYPos;

    //Movement variables
    private boolean leftDirection = false;
    private boolean rightDirection = true;
    private boolean upDirection = false;
    private boolean downDirection = false;
    //Game-management variables
    private boolean gameRunning = true;
    private Timer timer;
    private Image body, head, apple;

    public Board() {
        initBoard();
    }

    private void initBoard() {

        addKeyListener(new InputHandler());
        setBackground(Color.black);
        setFocusable(true);

        setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));
        loadImages();
        initGame();
    }

    private void loadImages() {
        body = new ImageIcon("src/resources/dot.png").getImage();

        apple = new ImageIcon("src/resources/apple.png").getImage();

        head = new ImageIcon("src/resources/head.png").getImage();
    }

    private void initGame() {

        currentSnakeLength = 3;

        for (int z = 0; z < currentSnakeLength; z++) {
            dotXPos[z] = 50 - z * 10;
            dotYPos[z] = 50;
        }

        locateApple();

        timer = new Timer(DELAY, this);
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        doDrawing(g);
    }

    private void doDrawing(Graphics g) {

        if (gameRunning) {

            g.drawImage(apple, appleXPos, appleYPos, this);

            for (int z = 0; z < currentSnakeLength; z++) {
                if (z == 0) {
                    g.drawImage(head, dotXPos[z], dotYPos[z], this);
                } else {
                    g.drawImage(body, dotXPos[z], dotYPos[z], this);
                }
            }

            Toolkit.getDefaultToolkit().sync();

        } else {

            gameOver(g);
        }
    }

    private void gameOver(Graphics g) {

        String gameOverMessage = "Game Over";
        String restartMessage = "Press 'R' to Restart";

        Font endGameFont = new Font("Helvetica", Font.BOLD, 28);

        FontMetrics fontMetric = getFontMetrics(endGameFont);

        int gameOverTextXPos = (BOARD_WIDTH - fontMetric.stringWidth(gameOverMessage)) / 2;
        int gameOverTextYPos = (BOARD_HEIGHT / 2) - 30;

        g.setColor(Color.white);
        g.setFont(endGameFont);
        g.drawString(gameOverMessage, gameOverTextXPos, gameOverTextYPos);
        g.drawString(restartMessage, gameOverTextXPos - 50, gameOverTextYPos + 60);
    }

    private void checkApple() {

        //If the snake's head is at the same x and y pos of the apple, add to the length, and move the apple.
        if ((dotXPos[0] == appleXPos) && (dotYPos[0] == appleYPos)) {

            currentSnakeLength++;
            locateApple();
        }
    }

    private void move() {

        //Moves every piece of the snake's body to the x and y pos of the body-part in-front of it, creating the effect
        //of the snake moving forward. This must be done *before* the snake's head is moved.
        for (int z = currentSnakeLength; z > 0; z--) {
            dotXPos[z] = dotXPos[(z - 1)];
            dotYPos[z] = dotYPos[(z - 1)];
        }

        //Moves the head according to the enabled direction.
        if (leftDirection) {
            dotXPos[0] -= DOT_SIZE;
        }

        if (rightDirection) {
            dotXPos[0] += DOT_SIZE;
        }

        if (upDirection) {
            dotYPos[0] -= DOT_SIZE;
        }

        if (downDirection) {
            dotYPos[0] += DOT_SIZE;
        }
    }

    private void checkCollision() {

        /*
        Loops through every part of the snake's body and for each piece checks to see if that piece is touching the
        head. If it is, ends the game. Removed a check that was initially here which ignored collisions with early parts
        of the body. With that check in place, it was super easy to pass straight through the body anytime you got trapped.
        */
        for (int z = currentSnakeLength; z > 0; z--) {
            if ((dotXPos[0] == dotXPos[z]) && (dotYPos[0] == dotYPos[z])) {
                gameRunning = false;
            }
        }

        //All of these checks are to ensure the snake is within the bounds of the game board.
        if (dotYPos[0] >= BOARD_HEIGHT) {
            gameRunning = false;
        }

        if (dotYPos[0] < 0) {
            gameRunning = false;
        }

        if (dotXPos[0] >= BOARD_WIDTH) {
            gameRunning = false;
        }

        if (dotXPos[0] < 0) {
            gameRunning = false;
        }

        if (!gameRunning) {
            timer.stop();
        }
    }

    //Randomizes the x and y position of the apple.
    private void locateApple() {

        int r = (int) (Math.random() * RAND_POS);
        appleXPos = ((r * DOT_SIZE));

        r = (int) (Math.random() * RAND_POS);
        appleYPos = ((r * DOT_SIZE));
    }

    //This is run every 'tick' of the game-loop. First everything is updated and refreshed, put in it's proper place,
    //and then the screen is updated to reflect these changes.
    @Override
    public void actionPerformed(ActionEvent e) {

        if (gameRunning) {
            checkApple();
            checkCollision();
            move();
        }

        repaint();
    }

    //Class responsible for the handling of user-input and movement of the snake
    private class InputHandler extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {

            int key = e.getKeyCode();

            //Added support for the 'w' 'a' 's' 'd' keys here
            if ( ( (key == KeyEvent.VK_LEFT) || (key == KeyEvent.VK_A) ) && (!rightDirection)) {
                leftDirection = true;
                upDirection = false;
                downDirection = false;
            }

            if ( ( (key == KeyEvent.VK_RIGHT) || (key == KeyEvent.VK_D) ) && (!leftDirection)) {
                rightDirection = true;
                upDirection = false;
                downDirection = false;
            }

            if ( ( (key == KeyEvent.VK_UP) || (key == KeyEvent.VK_W) ) && (!downDirection)) {
                upDirection = true;
                rightDirection = false;
                leftDirection = false;
            }

            if ( ( (key == KeyEvent.VK_DOWN) || (key == KeyEvent.VK_S) ) && (!upDirection)) {
                downDirection = true;
                rightDirection = false;
                leftDirection = false;
            }

            if(key == KeyEvent.VK_R && !gameRunning){
                Snake.restartGame();
            }

        }

    }
}
