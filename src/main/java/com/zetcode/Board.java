package com.zetcode;

import lombok.extern.slf4j.Slf4j;
import walaniam.snake.Direction;
import walaniam.snake.GameParams;
import walaniam.snake.ImageResource;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import static java.util.Optional.ofNullable;
import static walaniam.snake.GameParams.DEFAULT_SNAKE_SIZE;
import static walaniam.snake.GameParams.DEFAULT_SPEED;
import static walaniam.snake.ImageUtils.loadImage;

@Slf4j
public class Board extends JPanel implements ActionListener {

    private static final int B_WIDTH = 300;
    private static final int B_HEIGHT = B_WIDTH;
    private static final int DOT_SIZE = 10;
    private static final int ALL_DOTS = 900;
    private static final int RAND_POS = 29;

    private final Image ball = loadImage(ImageResource.DOT);
    private final Image apple = loadImage(ImageResource.APPLE);
    private final Image head = loadImage(ImageResource.HEAD);
    private final GameParams params;

    private final int x[] = new int[ALL_DOTS];
    private final int y[] = new int[ALL_DOTS];

    private int dots;
    private int apple_x;
    private int apple_y;

    private Direction direction = Direction.RIGHT;
    private boolean inGame = true;

    private Timer timer;

    public Board(GameParams params) {
        this.params = params;
        initBoard();
    }

    private void initBoard() {

        addKeyListener(new MovementsAdapter());
        setBackground(Color.black);
        setFocusable(true);

        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
        initGame();
    }

    private void initGame() {

        dots = ofNullable(params.getSnakeSize()).orElse(DEFAULT_SNAKE_SIZE);

        for (int i = 0; i < dots; i++) {
            x[i] = 50 - i * 10;
            y[i] = 50;
        }

        locateApple();

        timer = new Timer(ofNullable(params.getSpeed()).orElse(DEFAULT_SPEED), this);
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawing(g);
    }

    private void doDrawing(Graphics g) {

        if (inGame) {

            g.drawImage(apple, apple_x, apple_y, this);

            for (int i = 0; i < dots; i++) {
                if (i == 0) {
                    g.drawImage(head, x[i], y[i], this);
                } else {
                    g.drawImage(ball, x[i], y[i], this);
                }
            }

            Toolkit.getDefaultToolkit().sync();

        } else {
            gameOver(g);
        }
    }

    private void gameOver(Graphics g) {
        String msg = "Game Over";
        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metr = getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(msg, (B_WIDTH - metr.stringWidth(msg)) / 2, B_HEIGHT / 2);
    }

    private void checkApple() {
        if ((x[0] == apple_x) && (y[0] == apple_y)) {
            dots++;
            locateApple();
        }
    }

    private void move() {

        for (int i = dots; i > 0; i--) {
            x[i] = x[(i - 1)];
            y[i] = y[(i - 1)];
        }

        switch (direction) {
            case UP -> y[0] -= DOT_SIZE;
            case DOWN -> y[0] += DOT_SIZE;
            case LEFT -> x[0] -= DOT_SIZE;
            case RIGHT -> x[0] += DOT_SIZE;
        }
    }

    private void checkCollision() {
        boolean selfCollision = false;
        for (int i = dots; i > 0; i--) {
            if ((i > 4) && (x[0] == x[i]) && (y[0] == y[i])) {
                selfCollision = true;
                break;
            }
        }
        boolean borderCollision = y[0] >= B_HEIGHT || y[0] < 0 || x[0] >= B_WIDTH || x[0] < 0;

        inGame = !selfCollision && !borderCollision;

        if (!inGame) {
            log.info("Collision self={}, wall={}", selfCollision, borderCollision);
            timer.stop();
        }
    }

    private void locateApple() {
        int r = (int) (Math.random() * RAND_POS);
        apple_x = r * DOT_SIZE;
        r = (int) (Math.random() * RAND_POS);
        apple_y = r * DOT_SIZE;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (inGame) {
            checkApple();
            checkCollision();
            move();
        }

        repaint();
    }

    private class MovementsAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            Board.this.direction = Board.this.direction.forKeyPressed(key);
        }
    }
}
