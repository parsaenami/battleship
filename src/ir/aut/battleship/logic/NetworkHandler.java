package ir.aut.battleship.logic;

import ir.aut.battleship.logic.msg.*;

import java.net.Socket;
import java.net.SocketAddress;
import java.util.LinkedList;
import java.util.Queue;

public class NetworkHandler extends Thread {
    private TcpChannel mTcpChannel;
    private Queue<byte[]> mSendQueue;
    private Queue<byte[]> mReceivedQueue;
    private ReceivedMessageConsumer mConsumerThread;
    private INetworkHandlerCallback mINetworkHandlerCallback;

    NetworkHandler(SocketAddress socketAddress, INetworkHandlerCallback iNetworkHandlerCallback) {
        mTcpChannel = new TcpChannel(socketAddress, 300);
        mINetworkHandlerCallback = iNetworkHandlerCallback;
        mSendQueue = new LinkedList<>();
        mReceivedQueue = new LinkedList<>();
        mSendQueue.clear();
        mReceivedQueue.clear();
        mConsumerThread = new ReceivedMessageConsumer();
        mConsumerThread.start();
    }

    NetworkHandler(Socket socket, INetworkHandlerCallback iNetworkHandlerCallback) {
        mTcpChannel = new TcpChannel(socket, 300);
        mINetworkHandlerCallback = iNetworkHandlerCallback;
        mSendQueue = new LinkedList<>();
        mReceivedQueue = new LinkedList<>();
        mSendQueue.clear();
        mReceivedQueue.clear();
        mConsumerThread = new ReceivedMessageConsumer();
        mConsumerThread.start();
    }

    void sendMessage(BaseMessage baseMessage) {
        mSendQueue.add(baseMessage.getSerialized());
    }

    @Override
    public void run() {
        while (mTcpChannel.isConnected() && !Thread.currentThread().isInterrupted()) {
            if (!mSendQueue.isEmpty()) {
                mTcpChannel.write(mSendQueue.poll());
            } else {
                byte[] a = readChannel();
                if (a != null)
                    mReceivedQueue.add(a);
                try {
                    sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void stopSelf() {
        interrupt();
        mConsumerThread.interrupt();
    }

    private byte[] readChannel() {
        return mTcpChannel.read(100);
    }

    private int byteArrayToInt(byte[] b) {
        return b[3] & 0xFF | (b[2] & 0xFF) << 8 | (b[1] & 0xFF) << 16 | (b[0] & 0xFF) << 24;
    }

    private class ReceivedMessageConsumer extends Thread {
        @Override
        public void run() {
            while (mTcpChannel.isConnected() && !Thread.currentThread().isInterrupted()) {
                if (!mReceivedQueue.isEmpty()) {
                    byte[] msg = mReceivedQueue.poll();
                    switch (msg[5]) {
                        case MessageTypes.REQUEST_LOGIN_HOST:
                            RequestLoginHostMsg rlh = new RequestLoginHostMsg(msg);
                            mINetworkHandlerCallback.onMessageReceived(rlh);
                            break;
                        case MessageTypes.REQUEST_LOGIN_CLIENT:
                            RequestLoginClientMsg rlc = new RequestLoginClientMsg(msg);
                            mINetworkHandlerCallback.onMessageReceived(rlc);
                            break;
                        case MessageTypes.HOST_RESPONSE:
                            HostResponseMsg hr = new HostResponseMsg(msg);
                            mINetworkHandlerCallback.onMessageReceived(hr);
                            break;
                        case MessageTypes.PLAYER_READY:
                            PlayerReadyMsg pr = new PlayerReadyMsg(msg);
                            mINetworkHandlerCallback.onMessageReceived(pr);
                            break;
                        case MessageTypes.PLAYER_ABORT:
                            PlayerAbortMsg pa = new PlayerAbortMsg(msg);
                            mINetworkHandlerCallback.onMessageReceived(pa);
                            break;
                        case MessageTypes.PLAYER_CHAT:
                            PlayerChatMsg pc = new PlayerChatMsg(msg);
                            mINetworkHandlerCallback.onMessageReceived(pc);
                            break;
                        case MessageTypes.PLAYER_MOVE_HIT:
                            PlayerMoveHitMsg pmh = new PlayerMoveHitMsg(msg);
                            mINetworkHandlerCallback.onMessageReceived(pmh);
                            break;
                        case MessageTypes.PLAYER_MOVE_RESULT:
                            PlayerMoveResultMsg pmr = new PlayerMoveResultMsg(msg);
                            mINetworkHandlerCallback.onMessageReceived(pmr);
                            break;
                        case MessageTypes.GAME_FINISHED:
                            GameFinishedMsg gf = new GameFinishedMsg(msg);
                            mINetworkHandlerCallback.onMessageReceived(gf);
                            break;
                    }
                } else {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ignored) {
                    }
                }
            }
        }
    }

    public interface INetworkHandlerCallback {
        void onMessageReceived(BaseMessage baseMessage);

        void onSocketClosed();
    }
}
