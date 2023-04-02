package main;

import java.awt.*;
import javax.swing.*;

import panels.CardPanel;

public class Main {

    JFrame window;

    public static void main(String[] args) {

        EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Main();
            }
        });
        
    }

    public Main() {

        // SETTING THE INITIAL WINDOW PARAMETERS
        window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setLocationRelativeTo(null);
        window.setVisible(true);

        // ADD AND SET A CUSTOM FONT FOR THE ENTIRE PROGRAM
        FontHandler fontHandler = new FontHandler();
        fontHandler.addNewFont("static/fonts/PixelMplus12-Regular.ttf");
        fontHandler.setFont("PixelMplus12", Font.BOLD, 20);

        // ADD THE CARDLAYOUT PANEL, WHICH INCLUDES THE ACTUAL GAME AND ALL OTHER PANELS
        window.setLayout(new CardLayout());
        CardPanel cardPanel = new CardPanel();
        window.add(cardPanel);
        window.pack();
        
    }

};
