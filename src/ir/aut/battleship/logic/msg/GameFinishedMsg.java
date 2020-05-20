package ir.aut.battleship.logic.msg;

import ir.aut.battleship.logic.BaseMessage;
import ir.aut.battleship.logic.MessageTypes;

import java.nio.ByteBuffer;

public class GameFinishedMsg extends BaseMessage {
    private String mFinish;

    public GameFinishedMsg(String fin) {
        mFinish = fin;
        serialize();
    }

    public GameFinishedMsg(byte[] serialized) {
        mSerialized = serialized;
        deserialize();
    }

    @Override
    protected void serialize() {
        int finishLength = mFinish.getBytes().length;
        int messageLength = 4 + 1 + 1 + 4 + finishLength;
        ByteBuffer byteBuffer = ByteBuffer.allocate(messageLength);
        byteBuffer.putInt(messageLength);
        byteBuffer.put(MessageTypes.PROTOCOL_VERSION);
        byteBuffer.put(MessageTypes.GAME_FINISHED);
        byteBuffer.putInt(finishLength);
        byteBuffer.put(mFinish.getBytes());
        mSerialized = byteBuffer.array();
    }

    @Override
    protected void deserialize() {
        ByteBuffer byteBuffer = ByteBuffer.wrap(mSerialized);
        int messageLength = byteBuffer.getInt();
        byte messageType = byteBuffer.get();
        byte protocolVersion = byteBuffer.get();
        int finishLength;
        finishLength = byteBuffer.getInt();
        byte[] finishBytes = new byte[finishLength];
        byteBuffer.get(finishBytes);
        mFinish = new String(finishBytes);
    }

    @Override
    public Byte getMassageType() {
        return MessageTypes.GAME_FINISHED;
    }
}
