package ir.aut.battleship.logic.msg;

import ir.aut.battleship.logic.BaseMessage;
import ir.aut.battleship.logic.MessageTypes;

import java.nio.ByteBuffer;

public class PlayerMoveResultMsg extends BaseMessage {
    private String mResult;

    public String getmResult() {
        return mResult;
    }

    public PlayerMoveResultMsg(String result) {
        mResult = result;
        serialize();

    }

    public PlayerMoveResultMsg(byte[] serialized) {
        mSerialized = serialized;
        deserialize();
    }

    @Override
    protected void serialize() {
        int resultLength = mResult.getBytes().length;
        int messageLength = 4 + 1 + 1 + 4 + resultLength;
        ByteBuffer byteBuffer = ByteBuffer.allocate(messageLength);
        byteBuffer.putInt(messageLength);
        byteBuffer.put(MessageTypes.PROTOCOL_VERSION);
        byteBuffer.put(MessageTypes.PLAYER_MOVE_RESULT);
        byteBuffer.putInt(resultLength);
        byteBuffer.put(mResult.getBytes());
        mSerialized = byteBuffer.array();
    }

    @Override
    protected void deserialize() {
        ByteBuffer byteBuffer = ByteBuffer.wrap(mSerialized);
        int messageLength;
        messageLength = byteBuffer.getInt();
        byte protocolVersion = byteBuffer.get();
        byte messageType = byteBuffer.get();
        int resultLength = byteBuffer.getInt();
        byte[] resultBytes = new byte[resultLength];
        byteBuffer.get(resultBytes);
        mResult = new String(resultBytes);
    }

    @Override
    public Byte getMassageType() {
        return MessageTypes.PLAYER_MOVE_RESULT;
    }
}
