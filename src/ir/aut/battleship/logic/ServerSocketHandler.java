package ir.aut.battleship.logic;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerSocketHandler extends Thread {
    private ServerSocket mServerSocket;
    private IServerSocketHandlerCallback myServerSocketHandlerCallBack;
    private NetworkHandler.INetworkHandlerCallback mINetworkHandlerCallback;

    ServerSocketHandler(int port, NetworkHandler.INetworkHandlerCallback iNetworkHandlerCallback,
                        IServerSocketHandlerCallback iServerSocketHandlerCallback) {
        try {
            mServerSocket = new ServerSocket(port, 100);
            mServerSocket.setSoTimeout(100);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        myServerSocketHandlerCallBack = iServerSocketHandlerCallback;
        mINetworkHandlerCallback = iNetworkHandlerCallback;
    }

    @Override
    public void run() {
        while (!mServerSocket.isClosed() && !Thread.currentThread().isInterrupted()) {
            Socket mySocket;
            try {
                mySocket = mServerSocket.accept();
                NetworkHandler networkHandler = new NetworkHandler(mySocket, mINetworkHandlerCallback);
                myServerSocketHandlerCallBack.onNewConnectionReceived(networkHandler);
            } catch (IOException e) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    void stopSelf() {
        try {
            mServerSocket.close();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        interrupt();
    }

    public interface IServerSocketHandlerCallback {
        void onNewConnectionReceived(NetworkHandler networkHandler);
    }
}
