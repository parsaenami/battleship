package ir.aut.battleship.view;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | UnsupportedLookAndFeelException | IllegalAccessException e) {
            e.printStackTrace();
        }

        JFrame jFrame = new JFrame();
        BeginningGui beginningGui = new BeginningGui();
        jFrame.add(beginningGui);
        jFrame.setSize(350, 300);
        jFrame.setLocation(500, 200);
        jFrame.setVisible(true);
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}
