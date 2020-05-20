package ir.aut.battleship.logic.msg;

import ir.aut.battleship.logic.BaseMessage;
import ir.aut.battleship.logic.MessageTypes;

import java.nio.ByteBuffer;

public class PlayerAbortMsg extends BaseMessage {

    public String getmAborted() {
        return mAborted;
    }

    private String mAborted;

    public PlayerAbortMsg(String aborted) {
        mAborted = aborted;
        serialize();
    }

    public PlayerAbortMsg(byte[] serialized) {
        mSerialized = serialized;
        deserialize();
    }

    @Override
    protected void serialize() {
        int abortLength = mAborted.getBytes().length;
        int messageLength = 4 + 1 + 1 + 4 + abortLength;
        ByteBuffer byteBuffer = ByteBuffer.allocate(messageLength);
        byteBuffer.putInt(messageLength);
        byteBuffer.put(MessageTypes.PROTOCOL_VERSION);
        byteBuffer.put(MessageTypes.PLAYER_ABORT);
        byteBuffer.putInt(abortLength);
        byteBuffer.put(mAborted.getBytes());
        mSerialized = byteBuffer.array();
    }

    @Override
    protected void deserialize() {
        ByteBuffer byteBuffer = ByteBuffer.wrap(mSerialized);
        int messageLength = byteBuffer.getInt();
        byte protocolVersion = byteBuffer.get();
        byte messageType = byteBuffer.get();
        int abortLength;
        abortLength = byteBuffer.getInt();
        byte[] abortBytes;
        abortBytes = new byte[abortLength];
        byteBuffer.get(abortBytes);
        mAborted = new String(abortBytes);
    }

    @Override
    public Byte getMassageType() {
        return MessageTypes.PLAYER_ABORT;
    }
}
