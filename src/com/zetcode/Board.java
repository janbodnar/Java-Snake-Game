package com.zetcode;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Logger;
import java.util.Random;
import java.util.logging.Level;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Board extends JPanel implements ActionListener {
    private static enum inGame {
        IN_GAME, MENU, GAME_OVER
    }

    // Font vars
    private final Font small = new Font("Helvetica", Font.BOLD, 14);
    private final FontMetrics metr = getFontMetrics(small);

    // Panel and snake vars
    private static final int B_WIDTH = 300;
    private static final int B_HEIGHT = 300;
    private static final int DOT_SIZE = 10;
    private static final int ALL_DOTS = 900;

    // Useful vars + logger
    private static final Logger logger = Logger.getLogger(Board.class.getName());
    private static final String PATHTOFILE = "./src/com/zetcode/maxScore.txt";
<<<<<<< master
    private static final String[] MENUOPTIONS = {
            "Press SPACE to start\n",
            "Press C to chooose the snake color\n",
            "Press S to choose the snake speed\n",
            "Press L to choose the snake length\n",
            "Press A to choose the apple color\n",
            "Press M to choose the apple multiplier\n",
            "Press ESC to exit"
    };

    private final TAdapter myKeyListener = new TAdapter();
    private final MenuKeyListener menuListener = new MenuKeyListener();
=======
    private static final int POINTS_HEIGHT = 20;
    private final TAdapter myKeyListener = new TAdapter();
    private final MenuListener ml = new MenuListener();
>>>>>>> local

    // Cells vars
    private final int[] x = new int[ALL_DOTS];
    private final int[] y = new int[ALL_DOTS];

    // Logic vars
    private int dots;
    private int appleX;
    private int appleY;
    private int maxScore;

    // Direction vars
    private boolean leftDirection = false;
    private boolean rightDirection = true;
    private boolean upDirection = false;
    private boolean downDirection = false;
<<<<<<< master

    private enum inGame {
        IN_GAME, NOT_IN_GAME, IN_MENU
    }

    private inGame status = inGame.IN_MENU;
=======
    private inGame status = inGame.MENU;
>>>>>>> local

    // Timer vars
    private Timer timer;
    private static final int DELAY = 140;

    // Image vars
    private Image ball;
    private Image apple;
    private Image head;

    // Menu vars
    private static final String[] MENUOPTIONS = {
            "S to Start", "H for Highscore", "Q to Exit"
    };

    public Board() {
        // TODO: make a main menu with some customizable options:
        // - change snake color
        // - change snake speed
        // - change snake starting length
        // - change apple color
        // - add score multiplier

        this.initBoard();
    }

    private void drawMenu(Graphics g) {
        this.setBackground(Color.GRAY);
        g.setColor(Color.CYAN);

        for (int i = 0; i < MENUOPTIONS.length; i++) {
            g.drawString(MENUOPTIONS[i], (B_WIDTH - this.metr.stringWidth(MENUOPTIONS[i])) / 2, B_HEIGHT / 2 + i * 15);
        }

        this.addKeyListener(this.ml);
    }

    private void displayPoints(Graphics g) {
        String points = "Points: " + this.dots;
        String max = "Max: " + this.maxScore;

        g.setColor(Color.red);
        g.fillRect(0, B_HEIGHT, B_WIDTH, POINTS_HEIGHT);
        g.setColor(Color.white);
        g.setFont(this.small);
        g.drawString(max, (B_WIDTH - this.metr.stringWidth(points) - 230), B_HEIGHT + POINTS_HEIGHT);
        g.drawString(points, (B_WIDTH - this.metr.stringWidth(points)), B_HEIGHT + POINTS_HEIGHT);
    }

    private void writeHighScore() {
        File file = new File(PATHTOFILE);

        try (FileWriter fw = new FileWriter(file)) {

            // decide what to write to file
            if (this.dots > this.maxScore)
                fw.write(String.valueOf(this.dots));
            else
                fw.write(String.valueOf(this.maxScore));

        } catch (IOException ie) {
            logger.log(Level.WARNING, "Could not find file to write to", ie);
        } // fw gets autoclosed
    }

    private void readHighScore() {
        try (FileReader fr = new FileReader(PATHTOFILE);
                BufferedReader br = new BufferedReader(fr)) {

            String line = br.readLine();

            if (line.isEmpty())
                throw new IOException();

            this.maxScore = Integer.parseInt(line);

        } catch (IOException ioe) {
            logger.log(Level.SEVERE, "Could not find file to read from, setting score to 1", ioe);
            this.maxScore = 1;
        }
    }

    private void initBoard() {
        this.addKeyListener(this.myKeyListener);

        this.setBackground(Color.black);
        this.setFocusable(true);

        this.setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT + POINTS_HEIGHT));
        this.loadImages();
        this.initGame();
    }

    private void loadImages() {

        ImageIcon iid = new ImageIcon("src/resources/dot.png");
        ball = iid.getImage();

        ImageIcon iia = new ImageIcon("src/resources/apple.png");
        apple = iia.getImage();

        ImageIcon iih = new ImageIcon("src/resources/head.png");
        head = iih.getImage();
    }

    private void initGame() {

        // read highest score
        this.readHighScore();

        this.dots = 1;

        for (int z = 0; z < dots; z++) {
            x[z] = 50 - z * 10;
            y[z] = 50;
        }

        locateApple();

        this.timer = new Timer(DELAY, this);
        this.timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        this.drawMenu(g);
        this.doDrawing(g);
    }

    private void drawMenu(Graphics g) {

        int optionHeightPos = B_HEIGHT / 2 - 50;

        this.setBackground(Color.GRAY);

        g.setColor(Color.CYAN);
        FontMetrics metr = getFontMetrics(g.getFont());
        g.setFont(new Font("Helvetica", Font.BOLD, 13));

        for (int i = 0; i < MENUOPTIONS.length; i++) {
            g.drawString(MENUOPTIONS[i], (B_WIDTH - metr.stringWidth(MENUOPTIONS[i])) / 2, optionHeightPos);
            optionHeightPos += 20;
        }

        this.addKeyListener(this.menuListener);
    }

    private void doDrawing(Graphics g) {

        if (this.status == inGame.IN_GAME) {
<<<<<<< master

            g.drawImage(apple, apple_x, apple_y, this);
=======
            this.setBackground(Color.BLACK);
            g.drawImage(apple, appleX, appleY, this);
>>>>>>> local
            this.displayPoints(g);

            for (int z = 0; z < dots; z++) {
                if (z == 0)
                    g.drawImage(head, x[z], y[z], this);
                else
                    g.drawImage(ball, x[z], y[z], this);
            }

            Toolkit.getDefaultToolkit().sync();

<<<<<<< master
        } else if (this.status == inGame.NOT_IN_GAME) {
=======
        } else if (this.status == inGame.MENU) {
            this.drawMenu(g);
        } else {
>>>>>>> local
            this.gameOver(g);
        } else {
            this.drawMenu(g);
        }
    }

    private void gameOver(Graphics g) {

        String msg = "Game Over";
        String gameOverMsg = "Press ENTER to go back to the menu";

        this.maxScore = this.dots;

        // Read the current high score
        this.readHighScore();

        // Write the current high score
        this.writeHighScore();

        // Display game over string
        g.setColor(Color.white);
        g.setFont(this.small);
        g.drawString(msg, (B_WIDTH - this.metr.stringWidth(msg)) / 2, B_HEIGHT / 2);

        // Draw the high score at the end of the game
        this.displayPoints(g);

        // Play again
        g.drawString(gameOverMsg, (B_WIDTH - metr.stringWidth(gameOverMsg)) / 2,
                B_HEIGHT / 2 + 20);
        this.addKeyListener(this.myKeyListener);
    }

    private void checkApple() {

        if ((x[0] == appleX) && (y[0] == appleY)) {
            this.dots++;
            this.locateApple();
        }
    }

    private void move() {

        for (int z = dots; z > 0; z--) {
            x[z] = x[(z - 1)];
            y[z] = y[(z - 1)];
        }

        if (leftDirection)
            x[0] -= DOT_SIZE;

        if (rightDirection)
            x[0] += DOT_SIZE;

        if (upDirection)
            y[0] -= DOT_SIZE;

        if (downDirection)
            y[0] += DOT_SIZE;
    }

    private void checkCollision() {

        for (int z = dots; z > 0; z--) {
            if ((z > 4) && (x[0] == x[z]) && (y[0] == y[z]))
<<<<<<< master
                this.status = inGame.NOT_IN_GAME;
        }

        if (y[0] >= B_HEIGHT)
            this.status = inGame.NOT_IN_GAME;

        if (y[0] < 0)
            this.status = inGame.NOT_IN_GAME;

        if (x[0] >= B_WIDTH)
            this.status = inGame.NOT_IN_GAME;

        if (x[0] < 0)
            this.status = inGame.NOT_IN_GAME;

        if (this.status == inGame.NOT_IN_GAME)
=======
                this.status = inGame.GAME_OVER;
        }

        if (y[0] >= B_HEIGHT)
            this.status = inGame.GAME_OVER;

        if (y[0] < 0)
            this.status = inGame.GAME_OVER;

        if (x[0] >= B_WIDTH)
            this.status = inGame.GAME_OVER;

        if (x[0] < 0)
            this.status = inGame.GAME_OVER;

        if (this.status == inGame.GAME_OVER)
>>>>>>> local
            this.timer.stop();
    }

    private void locateApple() {
        Random rand = new Random();

        int r = rand.nextInt(30);
        appleX = (r * DOT_SIZE);

        r = rand.nextInt(30);
        appleY = (r * DOT_SIZE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
<<<<<<< master

=======
>>>>>>> local
        if (this.status == inGame.IN_GAME) {
            this.checkApple();
            this.checkCollision();
            this.move();
        }

        this.repaint();
    }

    private class MenuListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {

            int key = e.getKeyCode();

            if (status == inGame.MENU) {
                switch (key) {
                    case KeyEvent.VK_Q:
                        System.exit(0);
                        break;
                    case KeyEvent.VK_S:
                        status = inGame.IN_GAME;
                        break;
                    case KeyEvent.VK_H:
                        // show high score
                        break;
                    default:
                        break;

                }
            }
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

<<<<<<< master
            if (key == KeyEvent.VK_ENTER && (status == inGame.NOT_IN_GAME)) {
                status = inGame.IN_GAME;
=======
            if (key == KeyEvent.VK_ENTER && (status == inGame.GAME_OVER)) {
                status = inGame.MENU;
>>>>>>> local
                downDirection = false;
                rightDirection = true;
                upDirection = false;
                leftDirection = false;
                initBoard();
            }
        }
    }

    private class MenuKeyListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();

            switch (key) {
                case KeyEvent.VK_SPACE:
                    status = inGame.IN_GAME;
                    break;
                case KeyEvent.VK_ESCAPE:
                    System.exit(0);
                    break;
                case KeyEvent.VK_C:
                    // Change snake color
                    break;
                case KeyEvent.VK_S:
                    // Change snake speed
                    break;
                case KeyEvent.VK_L:
                    // Change snake starting length
                    break;
                case KeyEvent.VK_A:
                    // Change apple color
                    break;
                case KeyEvent.VK_M:
                    // Change apple multiplier
                    break;

                default:
                    break;
            }
        }

    }
}
