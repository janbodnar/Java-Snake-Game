package src;

import org.junit.Assert;

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
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Board extends JPanel implements ActionListener {

    private final int B_WIDTH = 300;
    private final int B_HEIGHT = 300;
    private final int DOT_SIZE = 10;
    private final int ALL_DOTS = 900;
    private final int RAND_POS = 29;
    private final int DELAY = 250;

    private final int x[] = new int[ALL_DOTS];
    private final int y[] = new int[ALL_DOTS];

    private int dots;

    private boolean leftDirection = false;
    private boolean rightDirection = true;
    private boolean upDirection = false;
    private boolean downDirection = false;
    private boolean inGame = true;

    private Timer timer;
    private Image ball;
    private Image apple;
    private Image pear;

    private List<Element> elements = new ArrayList<>();
    private int score = 0;

    public Board() {
        initBoard();
    }

    private void initBoard() {
        addKeyListener(new TAdapter());
        setBackground(Color.black);
        setFocusable(true);

        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
        loadImages();
        initGame();
    }

    private void loadImages() {
        ImageIcon iid = new ImageIcon("src/resources/dot.png");
        ball = iid.getImage();

        ImageIcon iia = new ImageIcon("src/resources/apple.png");
        apple = iia.getImage();

        ImageIcon iip = new ImageIcon("src/resources/pear.png");
        pear = iip.getImage();
    }

    private void initGame() {
        dots = 3;
        for (int z = 0; z < dots; z++) {
            x[z] = 50 - z * 10;
            y[z] = 50;
        }
        locateElement();

        timer = new Timer(DELAY, this);
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawing(g);
    }

    private void doDrawing(Graphics g) {
        if (inGame) {
            for (Element e : elements) {
                g.drawImage(e.getImage(), e.getX(), e.getY(), this);
            }
            for (int z = 0; z < dots; z++) {
                g.drawImage(ball, x[z], y[z], this);
            }
            Toolkit.getDefaultToolkit().sync();
        } else {
            gameOver(g);
        }
    }
    
    private void locateElement() {
        Random random = new Random();
        int r = (int) (random.nextDouble() * RAND_POS);
        int x = ((r * DOT_SIZE));

        r = (int) (random.nextDouble() * RAND_POS);
        int y = ((r * DOT_SIZE));
		Assert.assertTrue(apple != null && pear != null);
        elements.add(new Element(random.nextBoolean() ? apple : pear, random.nextInt(10), x, y));
    }
    
    private void move() {
        for (int z = dots; z > 0; z--) {
            x[z] = x[(z - 1)];
            y[z] = y[(z - 1)];
        }
        if (leftDirection) {
            x[0] -= DOT_SIZE;
        }
        if (rightDirection) {
            x[0] += DOT_SIZE;
        }
        if (upDirection) {
            y[0] -= DOT_SIZE;
        }
        if (downDirection) {
            y[0] += DOT_SIZE;
        }
    }
    
    private void checkCollision() {
        for (int z = dots; z > 0; z--) {
            if (isSuicide(z)) {
            	endGame();
            	break;
            }
        }
        if (commitedSuicide())
            endGame();
    }
    
    private Boolean commitedSuicide() {
        return (y[0] >= B_HEIGHT || y[0] < 0 || x[0] >= B_WIDTH || x[0] < 0);
    }

    private Boolean isSuicide(int z) {
        return (z > 4) && (x[0] == x[z]) && (y[0] == y[z]);
    }
    
    private void endGame() {
        inGame = false;
        timer.stop();
    }
    
    private void checkElement() {
        for (int i = 0; i < elements.size(); ++i) {
            Element e = elements.get(i);
            if (checkSnakeAteFruit(e)) {
                snakeAteFruit(e);
                break;
            }
        }
    }

    private Boolean checkSnakeAteFruit(Element e) {
        return (x[0] == e.getX()) && (y[0] == e.getY());
    }

    private void snakeAteFruit(Element e) {
        dots++;
        locateElement();
        score += getScore(e);
        elements.remove(e);
    }

    private int getScore(Element e) {
        if (e.getScore() <= 5) return 5;
        if (e.getScore() <= 10) return 10;
        return 0;
    }

    private void gameOver(Graphics g) {
        String msg = "Game Over";
        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metr = getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(msg, (B_WIDTH - metr.stringWidth(msg)) / 2, B_HEIGHT / 2 - 10);
        msg = "Your score is: " + score;
        g.drawString(msg, (B_WIDTH - metr.stringWidth(msg)) / 2, B_HEIGHT / 2 + 10);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (inGame) {
            checkElement();
            checkCollision();
            move();
            if (new Random().nextDouble() < 0.05) {
                locateElement();
            }
        }
        repaint();
    }

    private class TAdapter extends KeyAdapter {
    	int key;

        @Override
        public void keyPressed(KeyEvent e) {
            key = e.getKeyCode();
            if (canMoveLeft()) {
                leftDirection = true;
                upDirection = false;
                downDirection = false;
            }
            if (canMoveRight()) {
                rightDirection = true;
                upDirection = false;
                downDirection = false;
            }
            if (canMoveUp()) {
                upDirection = true;
                rightDirection = false;
                leftDirection = false;
            }
            if (canMoveDown()) {
                downDirection = true;
                rightDirection = false;
                leftDirection = false;
            }
        }
        
        private Boolean canMoveLeft() {
        	return (key == KeyEvent.VK_LEFT) && (!rightDirection);
        }
        
        private Boolean canMoveRight() {
        	return (key == KeyEvent.VK_RIGHT) && (!leftDirection);
        }
        
        private Boolean canMoveUp() {
        	return (key == KeyEvent.VK_UP) && (!downDirection);
        }
        
        private Boolean canMoveDown() {
        	return (key == KeyEvent.VK_DOWN) && (!upDirection);
        }
    }
}
