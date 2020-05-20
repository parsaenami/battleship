package ir.aut.battleship.view;

import ir.aut.battleship.logic.MessageManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.Random;

import static java.lang.System.exit;

class MainFrame extends JFrame {
    private MainButton buttonReset;
    private MainButton buttonReady;
    private MainPanel panelGame;
    private MainLabel labelGameTable;
    private JTextArea mJTextArea;
    private JTextField chatArea;
    private MessageManager mMessageManger;
    private String ourName;
    private boolean readyChecker;
    private boolean chechText = true;

    MainFrame(String string, MessageManager messageManager, JTextArea jTextArea, JButton[] jButtonsArray, int[][] sMatrix, boolean c) throws HeadlessException, IOException {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        ourName = string;
        readyChecker = c;
        mMessageManger = messageManager;
        setTitle("BattleShip Game");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(900, 600);
        double width = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        double height = Toolkit.getDefaultToolkit().getScreenSize().getHeight();
        setLocation((int) width / 2 - this.getWidth() / 2, (int) height / 2 - this.getHeight() / 2 - 10);
        setVisible(true);
        setResizable(true);
        MainMenu menu = new MainMenu();
        setLayout(new BorderLayout());
        add(menu, BorderLayout.NORTH);
        labelGameTable = new MainLabel(jButtonsArray, messageManager, sMatrix);
        panelGame = new MainPanel();
        panelGame.setBackground(Color.lightGray);
        final JLabel[] tempLabel = {labelGameTable.leftSideMaker()};
        panelGame.setLayout(null);
        tempLabel[0].setBounds(190, 100, 300, 300);
        panelGame.add(tempLabel[0]);
        panelGame.setVisible(true);
        panelGame.repaint();
        add(panelGame, BorderLayout.CENTER);
        MainLabel panelShipSelector = new MainLabel(jButtonsArray, messageManager, sMatrix);
        MainPanel panelTools = new MainPanel();
        panelTools.setLayout(new GridBagLayout());
        panelTools.setBackground(Color.BLACK);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.ipadx = 10;
        gbc.gridwidth = 4;
        gbc.insets = new Insets(5, 5, 5, 200);
        panelTools.add(panelShipSelector.bottomSideMaker2(), gbc);
        panelTools.setBounds(0, 480, 700, 100);
        panelGame.add(panelTools);
        repaint();
        MainPanel panelChat = new MainPanel();
        panelChat.setBackground(Color.blue);
        panelChat.setLayout(new BorderLayout());
        chatArea = new JTextField("Type Here...");
        chatArea.setPreferredSize(new Dimension(200, 20));
        handler h = new handler();
        chatArea.addActionListener(h);
        chatArea.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                chatArea.setText("");
            }
        });
        mJTextArea = jTextArea;
        JScrollPane jScrollPane = new JScrollPane(jTextArea);
        jTextArea.setEditable(false);
        jTextArea.setPreferredSize(new Dimension(200, 600));
        panelChat.add(chatArea, BorderLayout.SOUTH);
        panelChat.add(jScrollPane, BorderLayout.CENTER);
        repaint();
        chatArea.setVisible(true);
        panelChat.setVisible(true);
        add(panelChat, BorderLayout.EAST);
        chatArea.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                chatArea.setText("");
            }
        });
        chatArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (chechText) {
                    chatArea.setText("");
                    chechText = false;
                }
            }
        });
        buttonReset = new MainButton("Reset");
        buttonReset.addActionListener(e -> {
            if (buttonReset.getText().equals("Reset")) {
                panelGame.remove(tempLabel[0]);
                panelGame.repaint();
                tempLabel[0] = labelGameTable.leftSideMaker();
                labelGameTable.matrix();
                tempLabel[0].setBounds(190, 100, 300, 300);
                panelGame.add(tempLabel[0]);
                tempLabel[0].setVisible(true);
                panelGame.repaint();
                tempLabel[0].repaint();
                panelGame.revalidate();
                tempLabel[0].revalidate();
                labelGameTable.resetCounters();
            } else if (buttonReset.getText().equals("Cancel")) {
                labelGameTable.disable(true);
                panelGame.remove(tempLabel[0]);
                panelGame.repaint();
                tempLabel[0] = labelGameTable.leftSideMaker();
                labelGameTable.matrix();
                panelGame.add(tempLabel[0]);
                tempLabel[0].setVisible(true);
                panelGame.repaint();
                tempLabel[0].repaint();
                panelGame.revalidate();
                tempLabel[0].revalidate();
                labelGameTable.resetCounters();
                buttonReset.setText("Reset");
                buttonReady.setText("Ready");

                gbc.insets = new Insets(70, 650, 5, 10);
                panelTools.add(buttonReset, gbc);
                gbc.insets = new Insets(70, 500, 5, 0);
                panelTools.add(buttonReady, gbc);

                readyChecker = false;
                messageManager.sendPlayerAbortMsg("notReady");
            }
        });
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.insets = new Insets(70, 650, 5, 10);
//        gbc.insets = new Insets(70, 900, 5, 260);
        panelTools.add(buttonReset, gbc);
        buttonReady = new MainButton("Ready");
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.insets = new Insets(70, 500, 5, 0);
//        gbc1.insets = new Insets(70, 750, 5, 250);
        buttonReady.addActionListener(e -> {
            if (buttonReady.getText().equals("Ready")) {
                if (labelGameTable.checkCounters() == 0) {
                    buttonReady.setText("Leave");
                    buttonReset.setText("Cancel");

                    gbc.insets = new Insets(70, 900, 5, 260);
                    panelTools.add(buttonReset, gbc);
                    gbc.insets = new Insets(70, 750, 5, 250);
                    panelTools.add(buttonReady, gbc);

                    labelGameTable.setShipType(ShipType.NEW_BLOCKS);
                    labelGameTable.saveMatrix();
                    panelGame.remove(tempLabel[0]);
                    panelGame.repaint();
                    tempLabel[0] = labelGameTable.leftSideMaker();
                    labelGameTable.matrix();
                    tempLabel[0].setBounds(190, 100, 300, 300);
                    panelGame.add(tempLabel[0]);
                    tempLabel[0].setVisible(true);
                    panelGame.repaint();
                    tempLabel[0].repaint();
                    panelGame.revalidate();
                    tempLabel[0].revalidate();
                    labelGameTable.resetCounters();
                    labelGameTable.disable(false);
                    Random random = new Random();
                    messageManager.sendPlayerReadyMSG(Integer.toString(random.nextInt()));
                }
            } else if (buttonReady.getText().equals("Leave")) {
                messageManager.sendPlayerAbortMsg("left");
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                exit(1);
            }
        });
        panelTools.add(buttonReady, gbc);
        repaint();
        revalidate();
    }

    private class handler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == chatArea) {
                mMessageManger.sendPlayerChatMsg(e.getActionCommand());
                mJTextArea.append("\n" + ourName + ">>>" + e.getActionCommand() + "     " + System.currentTimeMillis());
                chatArea.setText("Type Here...");
                chechText = true;

            }
        }
    }
}
