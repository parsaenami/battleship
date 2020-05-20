package ir.aut.battleship.view;

import javax.swing.*;
import java.awt.event.KeyEvent;

import static java.lang.System.exit;

class MainMenu extends JMenuBar {

    MainMenu() {
        JMenu mainMenuFile = new JMenu("File");
        JMenu mainMenuHelp = new JMenu("Help");
        mainMenuFile.setMnemonic(KeyEvent.VK_F);
        mainMenuHelp.setMnemonic(KeyEvent.VK_H);
        JMenuItem mainItemFile1 = new JMenuItem("Conversation History", KeyEvent.VK_V);
        JMenuItem mainItemFile2 = new JMenuItem("Close", KeyEvent.VK_C);
        mainItemFile2.addActionListener(e -> exit(1));
        mainMenuFile.add(mainItemFile1);
        mainMenuFile.add(mainItemFile2);
        add(mainMenuFile);
        add(mainMenuHelp);
    }
}
