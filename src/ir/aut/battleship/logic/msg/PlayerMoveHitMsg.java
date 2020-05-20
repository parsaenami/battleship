package ir.aut.battleship.logic.msg;

import ir.aut.battleship.logic.BaseMessage;
import ir.aut.battleship.logic.MessageTypes;

import java.nio.ByteBuffer;

public class PlayerMoveHitMsg extends BaseMessage {
    private String mButtonNumber;

    public PlayerMoveHitMsg(int buttonNumber) {
        mButtonNumber = Integer.toString(buttonNumber);
        serialize();
    }

    public PlayerMoveHitMsg(byte[] serialized) {
        mSerialized = serialized;
        deserialize();
    }

    @Override
    protected void serialize() {

        int chatLength = mButtonNumber.getBytes().length;
        int messageLength = 4 + 1 + 1 + 4 + chatLength;
        ByteBuffer byteBuffer = ByteBuffer.allocate(messageLength);
        byteBuffer.putInt(messageLength);
        byteBuffer.put(MessageTypes.PROTOCOL_VERSION);
        byteBuffer.put(MessageTypes.PLAYER_MOVE_HIT);
        byteBuffer.putInt(chatLength);
        byteBuffer.put(mButtonNumber.getBytes());
        mSerialized = byteBuffer.array();
    }

    @Override
    protected void deserialize() {

        ByteBuffer byteBuffer = ByteBuffer.wrap(mSerialized);
        int messageLength = byteBuffer.getInt();
        byte protocolVersion = byteBuffer.get();
        byte messageType = byteBuffer.get();
        int buttonLength = byteBuffer.getInt();
        byte[] buttonBytes = new byte[buttonLength];
        byteBuffer.get(buttonBytes);
        mButtonNumber = new String(buttonBytes);
    }

    @Override
    public Byte getMassageType() {
        return MessageTypes.PLAYER_MOVE_HIT;
    }

    public int getButtonNumber() {
        return Integer.parseInt(mButtonNumber);
    }
}
