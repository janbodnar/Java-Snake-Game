package src;

import java.awt.Image;

public class Element {
    private Image image;
    private int score;
    private int x;
    private int y;

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Element(Image image, int score, int x, int y) {

        this.image = image;
        this.score = score;
        this.x = x;
        this.y = y;
    }

    public int getScore() {
        return score;
    }

    public Image getImage() {

        return image;
    }
}
