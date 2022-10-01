package walaniam.snake;

import java.awt.event.KeyEvent;

public enum Direction {

    UP, RIGHT, DOWN, LEFT;

    public Direction forKeyPressed(int keyEventCode) {
        switch (keyEventCode) {
            case KeyEvent.VK_LEFT:
                return this == RIGHT ? this : LEFT;
            case KeyEvent.VK_RIGHT:
                return this == LEFT ? this : RIGHT;
            case KeyEvent.VK_UP:
                return this == DOWN ? this : UP;
            case KeyEvent.VK_DOWN:
                return this == UP ? this : DOWN;
            default:
                return this;
        }
    }
}
