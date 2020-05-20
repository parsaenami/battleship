package ir.aut.battleship.logic;

import ir.aut.battleship.logic.msg.*;

import javax.swing.*;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.List;

import static java.lang.System.exit;

public class MessageManager implements NetworkHandler.INetworkHandlerCallback, ServerSocketHandler.IServerSocketHandlerCallback {
    private ServerSocketHandler mServerSocketHandler;
    private List<NetworkHandler> mNetworkHandlerList;
    private NetworkHandler mNetworkHandler;
    private String mName;
    private JTextArea mJTextArea;
    private JButton[] jButtons;
    private boolean checkingTurn = false;
    private int[][] checkingMatrix;
    private int[][] hitMatrix = new int[10][10];
    private boolean checkTurn;
    private JFrame waitForOpponentReadyFrame = new JFrame();
    private JLabel waitForOpponentReadyLabel = new JLabel("Wait For Other Opponent To Get Ready");

    public MessageManager(int port, JTextArea jTextArea, JButton[] jButtonsTable, int[][] checkMatrix, boolean booleanTurn) {
        checkingTurn = booleanTurn;
        mJTextArea = jTextArea;
        checkingMatrix = checkMatrix;
        this.jButtons = jButtonsTable;
        mServerSocketHandler = new ServerSocketHandler(port, this, this);
        mServerSocketHandler.start();
    }

    public MessageManager(String ip, int port, JTextArea jTextArea, JButton[] jButtonsTable, int[][] checkMatrix, boolean booleanTurn) {
        checkingTurn = booleanTurn;
        mJTextArea = jTextArea;
        checkingMatrix = checkMatrix;
        jButtons = jButtonsTable;
        SocketAddress socketAddress = new InetSocketAddress(ip, port);
        mNetworkHandler = new NetworkHandler(socketAddress, this);
        mNetworkHandler.start();
    }

    @Override
    public void onNewConnectionReceived(NetworkHandler networkHandler) {
        mNetworkHandler = networkHandler;
        mNetworkHandler.start();
    }

    @Override
    public void onMessageReceived(BaseMessage baseMessage) {
        switch (baseMessage.getMassageType()) {
            case MessageTypes.REQUEST_LOGIN_CLIENT:
                consumeRequestLoginClient((RequestLoginClientMsg) baseMessage);
                break;
            case MessageTypes.REQUEST_LOGIN_HOST:
                consumeRequestLoginHost((RequestLoginHostMsg) baseMessage);
                break;
            case MessageTypes.PLAYER_CHAT:
                consumePlayerChatMsg((PlayerChatMsg) baseMessage);
                break;
            case MessageTypes.PLAYER_READY:
                consumePlayerReadyMsg((PlayerReadyMsg) baseMessage);
                break;
            case MessageTypes.PLAYER_MOVE_HIT:
                consumePlayerMoveHitMsg((PlayerMoveHitMsg) baseMessage);
                break;
            case MessageTypes.PLAYER_MOVE_RESULT:
                consumePlayerHitResultMsg((PlayerMoveResultMsg) baseMessage);
                break;
            case MessageTypes.GAME_FINISHED:
                consumeGameFinishedMsg((GameFinishedMsg) baseMessage);
                break;
            case MessageTypes.PLAYER_ABORT:
                consumePlayerAbortMsg((PlayerAbortMsg) baseMessage);
                break;
        }
    }

    @Override
    public void onSocketClosed() {
        mServerSocketHandler.stopSelf();
    }

    private void consumeRequestLoginClient(RequestLoginClientMsg requestLoginClientMsg) {
        mName = requestLoginClientMsg.getPassword();
    }

    private void consumeRequestLoginHost(RequestLoginHostMsg requestLoginClientMsg) {
        mName = requestLoginClientMsg.getPassword();
    }

    private void consumePlayerChatMsg(PlayerChatMsg playerChatMsg) {
        mJTextArea.append("\n" + mName + ">>>" + playerChatMsg.getmChat() + "    " + System.currentTimeMillis());
    }

    private void consumeGameFinishedMsg(GameFinishedMsg gameFinishedMsg) {
        JOptionPane.showMessageDialog(null, "YOU LOSE!", "Game Result", JOptionPane.PLAIN_MESSAGE);
    }

    private void consumePlayerHitResultMsg(PlayerMoveResultMsg playerMoveResultMsg) {
        String bool = playerMoveResultMsg.getmResult().substring(playerMoveResultMsg.getmResult().indexOf("-") + 1);
        String num = playerMoveResultMsg.getmResult().substring(0, playerMoveResultMsg.getmResult().indexOf("-"));

        switch (bool) {
            case "true":
                jButtons[Integer.parseInt(num)].setText("-");
                hitMatrix[(Integer.parseInt(num) - (Integer.parseInt(num) % 10)) / 10][Integer.parseInt(num) % 10] = 4;
                break;
            case "false":
                disable(false);
                jButtons[Integer.parseInt(num)].setText(".");
                createShownMatrix();
                hitMatrix[(Integer.parseInt(num) - (Integer.parseInt(num) % 10)) / 10][Integer.parseInt(num) % 10] = 3;
                break;
            case "lost":
                JOptionPane.showMessageDialog(null, "YOU LOST!", "Game Result", JOptionPane.PLAIN_MESSAGE);
                break;
        }
    }

    private void consumePlayerMoveHitMsg(PlayerMoveHitMsg playerMoveHitMSG) {
        int hittedButton = playerMoveHitMSG.getButtonNumber();
        if (checkingMatrix[(hittedButton - (hittedButton % 10)) / 10][hittedButton % 10] == 2) {
            sendPlayerHitResultMsg(hittedButton + "-true");
            checkingMatrix[(hittedButton - (hittedButton % 10)) / 10][hittedButton % 10] = 4;

            if (finish()) {
                sendPlayerHitResultMsg("0-lost");
                JOptionPane.showMessageDialog(null, "YOU WON!", "Game Result", JOptionPane.PLAIN_MESSAGE);
                exit(1);
            }

        } else {
            disable(true);
            fillHitMat();
            sendPlayerHitResultMsg(hittedButton + "-false");
            checkingMatrix[(hittedButton - (hittedButton % 10)) / 10][hittedButton % 10] = 3;
        }
    }

    private void consumePlayerAbortMsg(PlayerAbortMsg playerAbortMsg) {
        if (playerAbortMsg.getmAborted().equals("notReady")) {
            checkingTurn = false;
        } else if (playerAbortMsg.getmAborted().equals("left")) {
            JOptionPane.showMessageDialog(null, "opponent left the game", "note", JOptionPane.PLAIN_MESSAGE);
        }
    }

    private void consumePlayerReadyMsg(PlayerReadyMsg playerReadyMsg) {
        if (!checkingTurn) {
            checkingTurn = true;
            int turn = Integer.parseInt(playerReadyMsg.getmReadiness());
            checkTurn = turn % 2 == 0;
        } else {
            waitForOpponentReadyFrame.setVisible(false);

            if (checkTurn) {
                disable(false);
                createShownMatrix();
            }
        }
        if (playerReadyMsg.getmReadiness().equals("2")) {
            disable(false);
        } else {
            disable(true);
        }
    }

    public void sendRequestLoginClientMsg(String string1, String string2, String string3) {
        RequestLoginClientMsg requestLoginClientMsg = new RequestLoginClientMsg(string1, string2, string3);
        mNetworkHandler.sendMessage(requestLoginClientMsg);
    }

    public void sendRequestLoginHostMSG(String string1, String string2) {
        RequestLoginHostMsg requestLoginHostMsg = new RequestLoginHostMsg(string1, string2);
        mNetworkHandler.sendMessage(requestLoginHostMsg);
    }

    public void sendPlayerChatMsg(String string) {
        PlayerChatMsg playerChatMsg = new PlayerChatMsg(string);
        mNetworkHandler.sendMessage(playerChatMsg);
    }

    private void sendPlayerHitResultMsg(String string) {
        PlayerMoveResultMsg playerMoveResultMsg = new PlayerMoveResultMsg(string);
        mNetworkHandler.sendMessage(playerMoveResultMsg);
    }

    public void sendPlayerWonMSG(String string) {
        GameFinishedMsg gameFinishedMsg = new GameFinishedMsg(string);
        mNetworkHandler.sendMessage(gameFinishedMsg);
    }

    public void sendPlayerAbortMsg(String string) {
        PlayerAbortMsg playerAbortMsg = new PlayerAbortMsg(string);
        mNetworkHandler.sendMessage(playerAbortMsg);

    }

    public void sendPlayerReadyMSG(String string) {

        if (!checkingTurn) {

            PlayerReadyMsg playerReadyMsg = new PlayerReadyMsg(string);
            mNetworkHandler.sendMessage(playerReadyMsg);

            waitForOpponentReadyFrame.setTitle("note");
            waitForOpponentReadyFrame.add(waitForOpponentReadyLabel);
            waitForOpponentReadyLabel.setVisible(true);
            waitForOpponentReadyFrame.setVisible(true);
            waitForOpponentReadyFrame.setLocation(300, 300);
            waitForOpponentReadyFrame.setSize(250, 100);

            checkingTurn = true;
        } else {
            waitForOpponentReadyFrame.setVisible(false);
            PlayerReadyMsg playerReadyMsg;
            if (checkTurn) {
                playerReadyMsg = new PlayerReadyMsg("2");
            } else {
                playerReadyMsg = new PlayerReadyMsg("1");
            }
            mNetworkHandler.sendMessage(playerReadyMsg);
            if (checkTurn) {
                disable(true);
            } else {
                disable(false);
                createShownMatrix();
            }
        }
    }

    public void sendPlayerMoveHit(int index) {
        PlayerMoveHitMsg playerMoveHitMsg = new PlayerMoveHitMsg(index);
        mNetworkHandler.sendMessage(playerMoveHitMsg);
    }

    public String getmName() {
        return mName;
    }

    private void disable(boolean isEnable) {
        if (isEnable) {
            for (int i = 0; i < 100; i++) {
                jButtons[i].setEnabled(true);
            }
        } else {
            for (int i = 0; i < 100; i++) {
                jButtons[i].setEnabled(false);
            }
        }
    }

    private boolean finish() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (checkingMatrix[i][j] == 2) {
                    return false;
                }
            }
        }
        return true;
    }

    private void createShownMatrix() {
        for (int i = 0; i < 100; i++) {
            jButtons[i].setText("");
        }
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                switch (checkingMatrix[i][j]) {
                    case 2:
                        jButtons[i * 10 + j].setText("x");
                        break;
                    case 3:
                        jButtons[i * 10 + j].setText(".");
                        break;
                    case 4:
                        jButtons[i * 10 + j].setText("-");
                        break;
                }
            }
        }
    }

    private void fillHitMat() {
        for (int i = 0; i < 100; i++) {
            jButtons[i].setText("");
        }
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                switch (hitMatrix[i][j]) {
                    case 4:
                        jButtons[i * 10 + j].setText("-");
                        break;
                    case 3:
                        jButtons[i * 10 + j].setText(".");
                        break;
                }
            }
        }
    }
}
