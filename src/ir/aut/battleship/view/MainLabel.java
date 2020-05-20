package ir.aut.battleship.view;

import ir.aut.battleship.logic.MessageManager;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

class MainLabel {
    void setShipType(ShipType shipType) {
        MainLabel.shipType = shipType;
    }

    private static ShipType shipType;
    private int fourBlocksCounter = 1, threeBlocksCounter = 2, twoBlocksCounter = 3, oneBlockCounter = 4;
    private JButton fourBlocksShip, threeBlocksShip, twoBlocksShip, oneBlockShip, rotateButton;
    private int[][] matrix = new int[10][10];
    private int[][] savedMatrix;
    private JButton[] buttons;
    private HashMap<Integer, ShipType> buttonType = new HashMap<>();
    private JPanel jPanel = new JPanel(new GridBagLayout());
    private MessageManager mMessageManager;
    private HashMap<Integer, String> border = new HashMap<>();
    private HashMap<Integer, String> borderIn = new HashMap<>();

    MainLabel(JButton[] mJButtons, MessageManager messageManager, int[][] sMatrix) {
        buttons = mJButtons;
        savedMatrix = sMatrix;
        mMessageManager = messageManager;
        fourBlocksShip = new JButton("1");
        threeBlocksShip = new JButton("2");
        twoBlocksShip = new JButton("3");
        oneBlockShip = new JButton("4");
        rotateButton = new JButton("Rotate");
    }

    private void fillMap() {
        for (int i = 0; i < 100; i += 10) {
            border.put(i, "border");
        }
        for (int i = 9; i < 100; i += 10) {
            borderIn.put(i, "borderIn");
        }
    }

    void resetCounters() {
        fourBlocksCounter = 1;
        threeBlocksCounter = 2;
        twoBlocksCounter = 3;
        oneBlockCounter = 4;
    }

    int checkCounters() {
        return oneBlockCounter + twoBlocksCounter + threeBlocksCounter + fourBlocksCounter;
    }

    void matrix() {
        int x, y;
        for (int i = 0; i < 100; i++) {
            y = i % 10;
            x = (i - i % 10) / 10;
            if (buttons[i].getText().equals("")) {
                matrix[x][y] = 0;
            } else if (buttons[i].getText().equals("x")) {
                matrix[x][y] = 2;
            } else if (buttons[i].getText().equals(".")) {
                matrix[x][y] = 3;
            } else if (buttons[i].getText().equals("-")) {
                matrix[x][y] = 4;
            }
        }

        if (oneBlockCounter != 0 || twoBlocksCounter != 0 || threeBlocksCounter != 0 || fourBlocksCounter != 0) {
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    if (matrix[i][j] == 2) {
                        for (int k = i - 1; k < i + 2; k++) {
                            for (int l = j - 1; l < j + 2; l++) {
                                if (k == i && l == j) {
                                    continue;
                                }
                                if (k >= 0 && k < 10 && l >= 0 && l < 10) {
                                    if (matrix[k][l] == 0) {
                                        matrix[k][l] = 1;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    void saveMatrix() {
        for (int i = 0; i < 10; i++) {
            System.arraycopy(matrix[i], 0, savedMatrix[i], 0, 10);
        }
    }

    private void rotateCheck(ShipStatus shipStatus, int shipLength, int index, JButton[] buttonArray, int[][] matrix) {
        int supVertical = shipLength * 10 + 1;
        int infHorizontal = shipLength + 10;
        int checkerVertical = 1, checkerHorizontal = 1;
        boolean isRotatable;
        fillMap();

        switch (shipStatus) {
            case VERTICAL:
                for (int k = index - 11; k <= index + supVertical; k++) {
                    if (k >= 0 && k < 100) {
                        if (buttonArray[k].getText().equals("x")) {
                            for (int i = 0; i < shipLength; i++) {
                                if (k == index + i * 10) {
                                    buttonArray[k].setText("");
                                }
                            }
                        } else if (buttonArray[k].getText().equals("x")) {
                            buttonArray[k].setText("");
                        }
                    }
                    if (checkerVertical % 3 == 0 && checkerVertical != 0) {
                        k += 7;
                    }
                    checkerVertical++;
                }
                matrix();
                break;
            case HORIZONTAL:
                for (int m = index - infHorizontal; m <= index + 11; m++) {
                    if (m >= 0 && m < 100) {
                        if (buttonArray[m].getText().equals("x")) {
                            for (int i = 0; i < shipLength; i++) {
                                if (m == index - i) {
                                    buttonArray[m].setText("");
                                }
                            }
                        } else if (buttonArray[m].getText().equals("x")) {
                            buttonArray[m].setText("");
                        }
                    }
                    if (checkerHorizontal % (shipLength + 2) == 0 && checkerHorizontal != 0) {
                        switch (shipLength) {
                            case 4:
                                m += 4;
                                break;
                            case 3:
                                m += 5;
                                break;
                            case 2:
                                m += 6;
                                break;
                        }
                    }
                    checkerHorizontal++;
                }
                matrix();
                break;
            case VERTICAL_CHECK:
                isRotatable = true;
                int bound = 0;
                for (int i = index - 9; i <= index + 11; i += 10) {
                    if (i >= 0) {
                        bound++;
                    }
                }
                int[] bounds = new int[bound];
                for (int i = 0; i < bound; i++) {
                    if (index < 10 && index >= 0) {
                        bounds[i] = i;
                    } else {
                        bounds[i] = ((index + (i - 1) * 10 + 1) - ((index + (i - 1) * 10 + 1) % 10)) / 10;
                    }
                }
                int k = 0;
                for (int n = index - infHorizontal; n <= index + 11; n++) {
                    if (n >= 0 && n < 100 && bounds[k] == (n - (n % 10)) / 10) {
                        if (matrix[(n - n % 10) / 10][n % 10] == 2/* && n != index - t && t < shipLength*/) {
                            if ((border.containsKey(n) && borderIn.containsKey(index)) || (borderIn.containsKey(n) && border.containsKey(index - shipLength + 1))) {
                                break;
                            }
                            isRotatable = false;
                            break;
                        }
                    }
                    if (checkerHorizontal % (shipLength + 2) == 0 && checkerHorizontal != 0) {
                        switch (shipLength) {
                            case 2:
                                n += 6;
                                if (index - 10 > 0)
                                    k++;
                                break;
                            case 3:
                                n += 5;
                                if (index - 10 > 0)
                                    k++;
                                break;
                            case 4:
                                n += 4;
                                if (index - 10 > 0)
                                    k++;
                                break;
                        }
                    }
                    checkerHorizontal++;
                }
                if (isRotatable) {
                    int[] boundCheck = new int[shipLength];
                    for (int i = 0; i < shipLength; i++) {
                        boundCheck[i] = ((index - i) - (index - i) % 10) / 10;
                    }
                    if (boundCheck[0] == boundCheck[shipLength - 1]) {
                        for (int i = 0; i < shipLength; i++) {
                            buttonArray[index - i].setText("x");
                        }
                        matrix();
                    } else {
                        for (int i = 0; i < shipLength * 10; i += 10) {
                            buttonArray[index + i].setText("x");
                        }
                    }
                    matrix();
                } else {
                    for (int i = 0; i < shipLength * 10; i += 10) {
                        buttonArray[index + i].setText("x");
                    }
                    matrix();
                }
                break;
            case HORIZONTAL_CHECK:
                isRotatable = true;
                for (int p = index - 11; p <= index + supVertical; p++) {
                    if (p >= 0 && p < 100) {
                        if (matrix[(p - p % 10) / 10][p % 10] == 2) {
                            if (border.containsKey(p) && borderIn.containsKey(index)) {
                                break;
                            }
                            isRotatable = false;
                            break;
                        }
                    }
                    if (checkerVertical % 3 == 0 && checkerVertical != 0) {
                        p += 7;
                    }
                    checkerVertical++;
                }
                if (isRotatable) {
                    int[] boundCheck = new int[shipLength];
                    for (int i = 0; i < shipLength; i++) {
                        boundCheck[i] = (index + i * 10) % 10;
                    }
                    if (boundCheck[0] == boundCheck[shipLength - 1]) {
                        for (int i = 0; i < shipLength; i++) {
                            buttonArray[index + i * 10].setText("x");
                        }
                    } else {
                        for (int i = 0; i < shipLength; i++) {
                            buttonArray[index - i].setText("x");
                        }
                    }
                    matrix();
                } else {
                    for (int i = 0; i < shipLength; i++) {
                        buttonArray[index - i].setText("x");
                    }
                    matrix();
                }
                break;
        }
    }

    JPanel bottomSideMaker2() {
        jPanel.setBackground(Color.black);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 70;
        gbc.gridwidth = 4;
        jPanel.add(fourBlocksShip, gbc);
        fourBlocksShip.addActionListener(e -> shipType = ShipType.FOUR_BLOCKS);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        gbc.ipadx = 50;
        jPanel.add(threeBlocksShip, gbc);
        threeBlocksShip.addActionListener(e -> shipType = ShipType.THREE_BLOCKS);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.ipadx = 30;
        jPanel.add(twoBlocksShip, gbc);
        twoBlocksShip.addActionListener(e -> shipType = ShipType.TWO_BLOCKS);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.ipadx = 10;
        jPanel.add(oneBlockShip, gbc);
        oneBlockShip.addActionListener(e -> shipType = ShipType.ONE_BLOCK);
        gbc.gridx = 10;
        gbc.gridy = 0;
        gbc.gridwidth = 0;
        jPanel.add(rotateButton, gbc);
        rotateButton.addActionListener(e -> shipType = ShipType.ROTATE_BLOCKS);
        return jPanel;
    }

    private void updateShipsCounters() {
        twoBlocksShip.setText(String.valueOf(twoBlocksCounter));
        twoBlocksShip.setVisible(false);
        twoBlocksShip.setVisible(true);
        twoBlocksShip.revalidate();
        twoBlocksShip.repaint();
        jPanel.repaint();
        jPanel.revalidate();
    }

    JLabel leftSideMaker() {
        JPanel jPanel1 = new JPanel();
        JLabel jLabel = new JLabel();
        jLabel.setSize(1500, 1500);

        for (int i = 0; i < 100; i++) {
            buttons[i] = new JButton("");
            buttons[i].setSize(15, 15);
            buttons[i].setVisible(true);
            final int temp = i;
            buttons[i].addActionListener(e -> {
                switch (shipType) {
                    case ONE_BLOCK:
                        while (oneBlockCounter != 0) {
                            if (matrix[(temp - temp % 10) / 10][temp % 10] != 1) {
                                buttons[temp].setText("x");
                                buttonType.put(temp, shipType);
                                oneBlockCounter--;
                                updateShipsCounters();
                                matrix();
                                break;
                            }
                            break;
                        }
                        for (int i1 = 0; i1 < 10; i1++) {
                            for (int j = 0; j < 10; j++) {
                                System.out.print(matrix[i1][j] + "  ");
                            }
                            System.out.println();
                        }
                        break;
                    case TWO_BLOCKS:
                        while (twoBlocksCounter != 0) {
                            if (89 < temp && temp <= 99
                                    && matrix[(temp - temp % 10) / 10][temp % 10] != 1
                                    && matrix[((temp - 10) - (temp - 10) % 10) / 10][(temp - 10) % 10] != 1) {
                                buttons[temp - 10].setText("x");
                                buttons[temp].setText("x");
                                buttonType.put(temp, shipType);
                                buttonType.put(temp - 10, shipType);
                                twoBlocksCounter--;
                                updateShipsCounters();
                                matrix();
                                break;
                            } else if (matrix[(temp - temp % 10) / 10][temp % 10] != 1
                                    && temp < 90
                                    && matrix[((temp + 10) - (temp + 10) % 10) / 10][(temp + 10) % 10] != 1) {
                                buttons[temp].setText("x");
                                buttons[temp + 10].setText("x");
                                buttonType.put(temp, shipType);
                                buttonType.put(temp + 10, shipType);
                                twoBlocksCounter--;
                                updateShipsCounters();
                                matrix();
                                break;
                            }
                            break;
                        }
                        for (int i1 = 0; i1 < 10; i1++) {
                            for (int j = 0; j < 10; j++) {
                                System.out.print(matrix[i1][j] + "  ");
                            }
                            System.out.println();
                        }
                        break;
                    case THREE_BLOCKS:
                        while (threeBlocksCounter != 0) {
                            if (79 < temp && temp <= 89
                                    && matrix[(temp - temp % 10) / 10][temp % 10] != 1
                                    && matrix[((temp - 10) - (temp - 10) % 10) / 10][(temp - 10) % 10] != 1
                                    && matrix[((temp + 10) - (temp + 10) % 10) / 10][(temp + 10) % 10] != 1) {
                                buttons[temp - 10].setText("x");
                                buttons[temp].setText("x");
                                buttons[temp + 10].setText("x");
                                buttonType.put(temp - 10, shipType);
                                buttonType.put(temp + 10, shipType);
                                buttonType.put(temp, shipType);
                                threeBlocksCounter--;
                                updateShipsCounters();
                                matrix();
                                break;
                            } else if (89 < temp && temp <= 99
                                    && matrix[(temp - temp % 10) / 10][temp % 10] != 1
                                    && matrix[((temp - 10) - (temp - 10) % 10) / 10][(temp - 10) % 10] != 1
                                    && matrix[((temp - 20) - (temp - 20) % 10) / 10][(temp - 20) % 10] != 1) {
                                buttons[temp - 20].setText("x");
                                buttons[temp - 10].setText("x");
                                buttons[temp].setText("x");
                                buttonType.put(temp - 20, shipType);
                                buttonType.put(temp - 10, shipType);
                                buttonType.put(temp, shipType);
                                threeBlocksCounter--;
                                updateShipsCounters();
                                matrix();
                                break;
                            } else if (matrix[(temp - temp % 10) / 10][temp % 10] != 1
                                    && temp < 80
                                    && matrix[((temp + 20) - (temp + 20) % 10) / 10][(temp + 20) % 10] != 1
                                    && matrix[((temp + 10) - (temp + 10) % 10) / 10][(temp + 10) % 10] != 1) {
                                buttons[temp].setText("x");
                                buttons[temp + 10].setText("x");
                                buttons[temp + 20].setText("x");
                                buttonType.put(temp + 10, shipType);
                                buttonType.put(temp + 20, shipType);
                                buttonType.put(temp, shipType);
                                threeBlocksCounter--;
                                updateShipsCounters();
                                matrix();
                                break;
                            }
                            break;
                        }
                        for (int i1 = 0; i1 < 10; i1++) {
                            for (int j = 0; j < 10; j++) {
                                System.out.print(matrix[i1][j] + "  ");
                            }
                            System.out.println();
                        }
                        break;
                    case FOUR_BLOCKS:
                        while (fourBlocksCounter != 0) {
                            if (69 < temp && temp <= 79
                                    && matrix[(temp - temp % 10) / 10][temp % 10] != 1
                                    && matrix[((temp - 10) - (temp - 10) % 10) / 10][(temp - 10) % 10] != 1
                                    && matrix[((temp + 10) - (temp + 10) % 10) / 10][(temp + 10) % 10] != 1
                                    && matrix[((temp + 20) - (temp + 20) % 10) / 10][(temp + 20) % 10] != 1) {
                                buttons[temp - 10].setText("x");
                                buttons[temp].setText("x");
                                buttons[temp + 10].setText("x");
                                buttons[temp + 20].setText("x");
                                buttonType.put(temp - 10, shipType);
                                buttonType.put(temp, shipType);
                                buttonType.put(temp + 10, shipType);
                                buttonType.put(temp + 20, shipType);
                                fourBlocksCounter--;
                                updateShipsCounters();
                                matrix();
                                break;
                            } else if (79 < temp && temp <= 89
                                    && matrix[(temp - temp % 10) / 10][temp % 10] != 1
                                    && matrix[((temp - 10) - (temp - 10) % 10) / 10][(temp - 10) % 10] != 1
                                    && matrix[((temp + 10) - (temp + 10) % 10) / 10][(temp + 10) % 10] != 1
                                    && matrix[((temp - 20) - (temp - 20) % 10) / 10][(temp - 20) % 10] != 1) {
                                buttons[temp - 20].setText("x");
                                buttons[temp - 10].setText("x");
                                buttons[temp].setText("x");
                                buttons[temp + 10].setText("x");
                                buttonType.put(temp - 10, shipType);
                                buttonType.put(temp, shipType);
                                buttonType.put(temp + 10, shipType);
                                buttonType.put(temp - 20, shipType);
                                fourBlocksCounter--;
                                updateShipsCounters();
                                matrix();
                                break;
                            } else if (89 < temp && temp <= 99
                                    && matrix[(temp - temp % 10) / 10][temp % 10] != 1
                                    && matrix[((temp - 10) - (temp - 10) % 10) / 10][(temp - 10) % 10] != 1
                                    && matrix[((temp - 30) - (temp - 30) % 10) / 10][(temp - 30) % 10] != 1
                                    && matrix[((temp - 20) - (temp - 20) % 10) / 10][(temp - 20) % 10] != 1) {
                                buttons[temp - 30].setText("x");
                                buttons[temp - 20].setText("x");
                                buttons[temp - 10].setText("x");
                                buttons[temp].setText("x");
                                buttonType.put(temp - 30, shipType);
                                buttonType.put(temp, shipType);
                                buttonType.put(temp - 10, shipType);
                                buttonType.put(temp - 20, shipType);
                                fourBlocksCounter--;
                                updateShipsCounters();
                                matrix();
                                break;
                            } else if (matrix[(temp - temp % 10) / 10][temp % 10] != 1
                                    && temp < 70
                                    && matrix[((temp + 30) - (temp + 30) % 10) / 10][(temp + 30) % 10] != 1
                                    && matrix[((temp + 10) - (temp + 10) % 10) / 10][(temp + 10) % 10] != 1
                                    && matrix[((temp + 20) - (temp + 20) % 10) / 10][(temp + 20) % 10] != 1) {
                                buttons[temp].setText("x");
                                buttons[temp + 10].setText("x");
                                buttons[temp + 20].setText("x");
                                buttons[temp + 30].setText("x");
                                buttonType.put(temp + 30, shipType);
                                buttonType.put(temp, shipType);
                                buttonType.put(temp + 10, shipType);
                                buttonType.put(temp + 20, shipType);
                                fourBlocksCounter--;
                                updateShipsCounters();
                                matrix();
                                break;
                            }
                            break;
                        }
                        for (int i1 = 0; i1 < 10; i1++) {
                            for (int j = 0; j < 10; j++) {
                                System.out.print(matrix[i1][j] + "  ");
                            }
                            System.out.println();
                        }
                        break;
                    case ROTATE_BLOCKS:
                        ShipType shipTypeForRotate = buttonType.get(temp);
                        fillMap();
                        switch (shipTypeForRotate) {
                            case TWO_BLOCKS:
                                if ((temp - 10 >= 0
                                        && buttons[temp + 10].getText().equals("x")
                                        && buttons[temp - 10].getText().equals(""))
                                        || (temp - 10 < 0
                                        && buttons[temp + 10].getText().equals("x"))) {
                                    rotateCheck(ShipStatus.VERTICAL, 2, temp, buttons, matrix);
                                    rotateCheck(ShipStatus.VERTICAL_CHECK, 2, temp, buttons, matrix);
                                } else if ((buttons[temp - 1].getText().equals("x")
                                        && buttons[temp + 1].getText().equals("")
                                        && ((temp - 1) - (temp - 1) % 10) / 10 == ((temp + 1) - (temp + 1) % 10) / 10)
                                        || (((temp - 1) - (temp - 1) % 10) / 10 != ((temp + 1) - (temp + 1) % 10) / 10
                                        && buttons[temp - 1].getText().equals("x"))) {
                                    rotateCheck(ShipStatus.HORIZONTAL, 2, temp, buttons, matrix);
                                    rotateCheck(ShipStatus.HORIZONTAL_CHECK, 2, temp, buttons, matrix);
                                }
                                break;
                            case THREE_BLOCKS:
                                if ((temp - 10 >= 0
                                        && buttons[temp + 10].getText().equals("x")
                                        && buttons[temp - 10].getText().equals(""))
                                        || (temp - 10 < 0
                                        && buttons[temp + 10].getText().equals("x"))) {
                                    rotateCheck(ShipStatus.VERTICAL, 3, temp, buttons, matrix);
                                    rotateCheck(ShipStatus.VERTICAL_CHECK, 3, temp, buttons, matrix);
                                } else if ((buttons[temp - 1].getText().equals("x")
                                        && buttons[temp + 1].getText().equals("")
                                        && ((temp - 1) - (temp - 1) % 10) / 10 == ((temp + 1) - (temp + 1) % 10) / 10)
                                        || (((temp - 1) - (temp - 1) % 10) / 10 != ((temp + 1) - (temp + 1) % 10) / 10
                                        && buttons[temp - 1].getText().equals("x"))) {
                                    rotateCheck(ShipStatus.HORIZONTAL, 3, temp, buttons, matrix);
                                    rotateCheck(ShipStatus.HORIZONTAL_CHECK, 3, temp, buttons, matrix);
                                }
                                break;
                            case FOUR_BLOCKS:
                                if ((temp - 10 >= 0
                                        && buttons[temp + 10].getText().equals("x")
                                        && buttons[temp - 10].getText().equals(""))
                                        || (temp - 10 < 0
                                        && buttons[temp + 10].getText().equals("x"))) {
                                    rotateCheck(ShipStatus.VERTICAL, 4, temp, buttons, matrix);
                                    rotateCheck(ShipStatus.VERTICAL_CHECK, 4, temp, buttons, matrix);
                                } else if ((buttons[temp - 1].getText().equals("x")
                                        && buttons[temp + 1].getText().equals(""))
                                        || (buttons[temp - 1].getText().equals("x")
                                        && border.containsKey(temp + 1))) {
                                    rotateCheck(ShipStatus.HORIZONTAL, 4, temp, buttons, matrix);
                                    rotateCheck(ShipStatus.HORIZONTAL_CHECK, 4, temp, buttons, matrix);
                                }
                                break;
                        }
                        break;
                    case NEW_BLOCKS:
                        mMessageManager.sendPlayerMoveHit(temp);
                }
                if (checkCounters() == 0) {
                    matrix();
                }
            });
            jPanel1.add(buttons[i]);
        }
        jLabel.setSize(400, 400);
        jLabel.setVisible(true);
        jPanel1.setLayout(new GridLayout(10, 10));
        jPanel1.setSize(300, 300);
        jPanel1.setVisible(true);
        jLabel.add(jPanel1);
        if (oneBlockCounter == 0 && twoBlocksCounter == 0 && threeBlocksCounter == 0 && fourBlocksCounter == 0) {
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    System.out.print(matrix[i][j] + "  ");
                }
                System.out.println();
            }
        }
        return jLabel;
    }

    void disable(boolean isEnable) {
        if (!isEnable) {
            for (int i = 0; i < 100; i++) {
                buttons[i].setEnabled(false);
            }
        } else {
            for (int i = 0; i < 100; i++) {
                buttons[i].setEnabled(true);
            }
        }
    }
}