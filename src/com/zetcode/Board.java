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
    private Logger logger = Logger.getLogger(Board.class.getName());

    private static final int POINTS_HEIGHT = 20;

    private static final int B_WIDTH = 300;
    private static final int B_HEIGHT = 300;
    private static final int DOT_SIZE = 10;
    private static final int ALL_DOTS = 900;
    private static final int DELAY = 140;
    private static final String PATHTOFILE = "./src/com/zetcode/maxScore.txt";

    private final int[] x = new int[ALL_DOTS];
    private final int[] y = new int[ALL_DOTS];

    private int dots;
    private int apple_x;
    private int apple_y;
    private int maxScore;

    private boolean leftDirection = false;
    private boolean rightDirection = true;
    private boolean upDirection = false;
    private boolean downDirection = false;
    private boolean inGame = true;

    private Timer timer;
    private Image ball;
    private Image apple;
    private Image head;

    public Board() {
        this.initBoard();
    }

    private void displayPoints(Graphics g) {
        String points = "Points: " + this.dots;
        String max = "Max: " + this.maxScore;
        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metr = getFontMetrics(small);

        g.setColor(Color.red);
        g.fillRect(0, B_HEIGHT, B_WIDTH, POINTS_HEIGHT);
        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(max, (B_WIDTH - metr.stringWidth(points) - 230), B_HEIGHT + POINTS_HEIGHT);
        g.drawString(points, (B_WIDTH - metr.stringWidth(points)), B_HEIGHT + POINTS_HEIGHT);
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

        this.addKeyListener(new TAdapter());
        this.setBackground(Color.black);
        this.setFocusable(true);

        // read highest score
        this.readHighScore();

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

        this.doDrawing(g);
    }

    private void doDrawing(Graphics g) {

        if (this.inGame) {

            g.drawImage(apple, apple_x, apple_y, this);
            this.displayPoints(g);

            for (int z = 0; z < dots; z++) {
                if (z == 0)
                    g.drawImage(head, x[z], y[z], this);
                else
                    g.drawImage(ball, x[z], y[z], this);
            }

            Toolkit.getDefaultToolkit().sync();

        } else {
            this.gameOver(g);
        }
    }

    private void gameOver(Graphics g) {

        String msg = "Game Over";
        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metr = getFontMetrics(small);

        this.maxScore = this.dots;

        // Read the current high score
        this.readHighScore();

        // Write the current high score
        this.writeHighScore();

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(msg, (B_WIDTH - metr.stringWidth(msg)) / 2, B_HEIGHT / 2);
        // TODO: Add a way to play again
    }

    private void checkApple() {

        if ((x[0] == apple_x) && (y[0] == apple_y)) {
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
                this.inGame = false;
        }

        if (y[0] >= B_HEIGHT)
            this.inGame = false;

        if (y[0] < 0)
            this.inGame = false;

        if (x[0] >= B_WIDTH)
            this.inGame = false;

        if (x[0] < 0)
            this.inGame = false;

        if (!inGame)
            this.timer.stop();
    }

    private void locateApple() {
        Random rand = new Random();

        int r = rand.nextInt(30);
        apple_x = (r * DOT_SIZE);

        r = rand.nextInt(30);
        apple_y = (r * DOT_SIZE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (inGame) {
            this.checkApple();
            this.checkCollision();
            this.move();
        }

        this.repaint();
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
        }
    }
}
