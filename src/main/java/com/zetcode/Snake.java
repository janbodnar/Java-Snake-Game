package com.zetcode;

import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
public class Snake extends JFrame {

    public Snake() {
        initUI();
    }

    private void initUI() {

        add(new Board());

        setResizable(false);
        pack();

        setTitle("Snake");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        log.info("Starting Snake with params: {}", Arrays.stream(args).collect(Collectors.joining(", ")));
        EventQueue.invokeLater(() -> {
            JFrame frame = new Snake();
            frame.setVisible(true);
        });
    }
}
