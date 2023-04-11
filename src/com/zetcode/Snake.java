package com.zetcode;

import java.awt.EventQueue;
import javax.swing.*;

public class Snake extends JFrame {

    private static JFrame frame;

    public Snake() {
        initUI();
    }

    private void initUI() {

        add(new Board());

        setResizable(false);
        pack();

        setTitle("Snake");
        setIconImage(new ImageIcon("src/resources/icon.png").getImage());
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    //Basic method for restarting the game via disposing the current frame and initializing a new one.
    public static void restartGame(){
        frame.dispose();

        startGame();
    }

    public static void main(String[] args) {
        startGame();
    }

    private static void startGame(){
        EventQueue.invokeLater(() -> {
            frame = new Snake();
            frame.setVisible(true);
        });
    }

}
