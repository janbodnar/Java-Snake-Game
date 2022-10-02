package com.zetcode;

import lombok.extern.slf4j.Slf4j;
import walaniam.snake.GameParams;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
public class Snake extends JFrame {

    public Snake(GameParams params) {
        initUI(params);
    }

    private void initUI(GameParams params) {

        add(new Board(params));

        setResizable(false);
        pack();

        setTitle("Snake");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        log.info("Starting Snake with params: {}", Arrays.stream(args).collect(Collectors.joining(", ")));
        var paramsBuilder = GameParams.builder();
        if (args.length > 0) {
            paramsBuilder.speed(Integer.parseInt(args[0]));
        }
        if (args.length > 1) {
            paramsBuilder.snakeSize(Integer.parseInt(args[1]));
        }
        EventQueue.invokeLater(() -> {
            JFrame frame = new Snake(paramsBuilder.build());
            frame.setVisible(true);
        });
    }
}
