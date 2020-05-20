package ir.aut.battleship.logic;

import java.io.*;
import java.net.*;

public class TcpChannel {
    private Socket mSocket;
    private OutputStream mOutputStream;
    private InputStream mInputStream;

    TcpChannel(SocketAddress socketAddress, int timeout) {
        try {
            mSocket = new Socket();
            mSocket.setSoTimeout(timeout);
            mSocket.connect(socketAddress);
            mOutputStream = mSocket.getOutputStream();
            mInputStream = mSocket.getInputStream();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    TcpChannel(Socket socket, int timeout) {
        mSocket = socket;
        try {
            mSocket.setSoTimeout(timeout);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        try {
            mOutputStream = socket.getOutputStream();
            mInputStream = socket.getInputStream();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    byte[] read(final int count) {
        byte[] bytesFin = new byte[count];
        try {
            mInputStream.read(bytesFin);
            return bytesFin;
        } catch (IOException ignored) {
        }
        return null;
    }

    void write(byte[] data) {
        try {
            mOutputStream.write(data);
            mOutputStream.flush();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    boolean isConnected() {
        return mSocket.isConnected();
    }

    public void closeChannel() {
        try {
            mSocket.close();
            mInputStream.close();
            mOutputStream.close();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
