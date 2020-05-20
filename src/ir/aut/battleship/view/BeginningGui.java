package ir.aut.battleship.view;

import ir.aut.battleship.logic.MessageManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;

import static java.lang.System.exit;

public class BeginningGui extends JPanel {
    private JTextField jTextField = new JTextField(15);
    private JRadioButton jRadioButton1 = new JRadioButton("Host");
    private JTextField jTextField1 = new JTextField(15);
    private JRadioButton jRadioButton2 = new JRadioButton("Guest");
    private JTextField jTextField2 = new JTextField(15);
    private JTextField jTextField3 = new JTextField(15);
    private JButton startButton = new JButton("Start");
    private MessageManager mMessageManager;
    private Boolean makeSure;
    private String ourName = "me";
    private JTextArea jTextArea;
    private JButton[] jButtons;
    private int[][] savedMatrix;
    private boolean cancel;

    public BeginningGui() {
        savedMatrix = new int[10][10];
        jTextArea = new JTextArea(">>>   The Chat Box   <<<" + "\n" + "\n" + "\n");
        handler h = new handler();
        jRadioButton1.addItemListener(h);
        jRadioButton2.addItemListener(h);
        handler1 hh = new handler1();
        jButtons = new JButton[100];
        startButton.addActionListener(hh);
        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(e -> exit(1));
        setSize(300, 300);
        setLocation(300, 200);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel jLabel = new JLabel("Name :");
        add(jLabel, gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        add(jTextField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.insets = new Insets(20, 0, 0, 0);
        add(jRadioButton1, gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 0, 0);
        JLabel jLabel1 = new JLabel("port :");
        add(jLabel1, gbc);
        gbc.gridx = 1;
        gbc.gridy = 2;
        add(jTextField1, gbc);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.insets = new Insets(20, 0, 0, 0);
        add(jRadioButton2, gbc);
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.insets = new Insets(0, 0, 0, 0);
        JLabel jLabel2 = new JLabel("IP :");
        add(jLabel2, gbc);
        gbc.gridx = 1;
        gbc.gridy = 4;
        add(jTextField2, gbc);
        gbc.gridx = 0;
        gbc.gridy = 5;
        JLabel jLabel3 = new JLabel("port :");
        add(jLabel3, gbc);
        gbc.gridx = 1;
        gbc.gridy = 5;
        add(jTextField3, gbc);
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.ipadx = 50;
        gbc.ipady = 15;
        gbc.insets = new Insets(40, 60, 0, 0);
        add(exitButton, gbc);
        gbc.gridx = 1;
        gbc.gridy = 8;
        gbc.insets = new Insets(40, 0, 0, 0);
        add(startButton, gbc);

        setVisible(true);
        repaint();
        revalidate();
    }

    private class handler implements ItemListener {
        Boolean b = false;

        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getSource() == jRadioButton1) {
                makeSure = true;
                jRadioButton2.setEnabled(b);
                jTextField2.setEnabled(b);
                jTextField3.setEnabled(b);
                b = !b;
            }
            if (e.getSource() == jRadioButton2) {
                makeSure = false;
                jRadioButton1.setEnabled(b);
                jTextField1.setEnabled(b);
                b = !b;
            }
        }
    }

    private class handler1 implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == startButton) {
                String name = jTextField.getText();
                if (makeSure) {
                    String portH = jTextField1.getText();
                    new Thread(() -> mMessageManager = new MessageManager(Integer.parseInt(portH), jTextArea, jButtons, savedMatrix, cancel)).start();
                    try {
                        Thread.sleep(4000);
                    } catch (InterruptedException ignored) {
                    }
                    ourName = name.substring(name.length());
                    mMessageManager.sendRequestLoginHostMSG(name, portH);
                } else {
                    String portC = jTextField2.getText();
                    String ipC = jTextField3.getText();
                    new Thread(() -> mMessageManager = new MessageManager(portC, Integer.parseInt(ipC), jTextArea, jButtons, savedMatrix, cancel)).start();
                    try {
                        Thread.sleep(4000);
                    } catch (InterruptedException ignored) {
                    }
                    ourName = name.substring(name.length());
                    mMessageManager.sendRequestLoginClientMsg(name, portC, ipC);
                }

                try {
                    new MainFrame(ourName, mMessageManager, jTextArea, jButtons, savedMatrix, cancel);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    public MessageManager getmMessageManager() {
        return mMessageManager;
    }

    public String getOurName() {
        return ourName;
    }


}
