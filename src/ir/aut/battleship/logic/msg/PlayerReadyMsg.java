package ir.aut.battleship.logic.msg;

import ir.aut.battleship.logic.BaseMessage;
import ir.aut.battleship.logic.MessageTypes;

import java.nio.ByteBuffer;

public class PlayerReadyMsg extends BaseMessage {
    private String mReadiness;

    public String getmReadiness() {
        return mReadiness;
    }

    public PlayerReadyMsg(String readiness) {
        mReadiness = readiness;
        serialize();
    }

    public PlayerReadyMsg(byte[] serialized) {
        mSerialized = serialized;
        deserialize();
    }

    @Override
    protected void serialize() {
        int readyLength = mReadiness.getBytes().length;
        int messageLength = 4 + 1 + 1 + 4 + readyLength;
        ByteBuffer byteBuffer = ByteBuffer.allocate(messageLength);
        byteBuffer.putInt(messageLength);
        byteBuffer.put(MessageTypes.PROTOCOL_VERSION);
        byteBuffer.put(MessageTypes.PLAYER_READY);
        byteBuffer.putInt(readyLength);
        byteBuffer.put(mReadiness.getBytes());
        mSerialized = byteBuffer.array();
    }

    @Override
    protected void deserialize() {
        ByteBuffer byteBuffer = ByteBuffer.wrap(mSerialized);
        int messageLength = byteBuffer.getInt();
        byte protocolVersion = byteBuffer.get();
        byte messageType = byteBuffer.get();
        int readyLength = byteBuffer.getInt();
        byte[] readyBytes;
        readyBytes = new byte[readyLength];
        byteBuffer.get(readyBytes);
        mReadiness = new String(readyBytes);
    }

    @Override
    public Byte getMassageType() {
        return MessageTypes.PLAYER_READY;
    }
}
