package ir.aut.battleship.logic.msg;

import ir.aut.battleship.logic.BaseMessage;
import ir.aut.battleship.logic.MessageTypes;

import java.nio.ByteBuffer;

public class RequestLoginHostMsg extends BaseMessage {
    private String mName;
    private String mPort;

    public RequestLoginHostMsg(String name, String port) {
        mName = name;
        mPort = port;
        serialize();
    }

    public RequestLoginHostMsg(byte[] serialized) {
        mSerialized = serialized;
        deserialize();
    }

    @Override
    protected void serialize() {
        int nameLength = mName.getBytes().length;
        int portLength = mPort.getBytes().length;
        int messageLength = 4 + 1 + 1 + 4 + nameLength + 4 + portLength;
        ByteBuffer byteBuffer = ByteBuffer.allocate(messageLength);
        byteBuffer.putInt(messageLength);
        byteBuffer.put(MessageTypes.PROTOCOL_VERSION);
        byteBuffer.put(MessageTypes.REQUEST_LOGIN_HOST);
        byteBuffer.putInt(nameLength);
        byteBuffer.put(mName.getBytes());
        byteBuffer.putInt(portLength);
        byteBuffer.put(mPort.getBytes());
        mSerialized = byteBuffer.array();
    }

    @Override
    protected void deserialize() {

        ByteBuffer byteBuffer = ByteBuffer.wrap(mSerialized);
        int messageLength = byteBuffer.getInt();
        byte protocolVersion = byteBuffer.get();
        byte messageType = byteBuffer.get();
        int nameLength = byteBuffer.getInt();
        byte[] nameBytes = new byte[nameLength];
        byteBuffer.get(nameBytes);
        mName = new String(nameBytes);
        int portLength = byteBuffer.getInt();
        byte[] portBytes = new byte[portLength];
        byteBuffer.get(portBytes);
        mPort = new String(portBytes);

    }

    @Override
    public Byte getMassageType() {
        return MessageTypes.REQUEST_LOGIN_HOST;
    }

    public String getPassword() {
        return mName;
    }

    public String getUsername() {
        return mPort;
    }
}
