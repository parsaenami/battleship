package ir.aut.battleship.logic.msg;

import ir.aut.battleship.logic.BaseMessage;
import ir.aut.battleship.logic.MessageTypes;

import java.nio.ByteBuffer;

public class HostResponseMsg extends BaseMessage {
    private String mResponse;

    public HostResponseMsg(String response) {
        mResponse = response;
        serialize();
    }

    public HostResponseMsg(byte[] serialized) {
        mSerialized = serialized;
        deserialize();
    }

    @Override
    protected void serialize() {
        int responseLength = mResponse.getBytes().length;
        int messageLength = 4 + 1 + 1 + 4 + responseLength;
        ByteBuffer byteBuffer = ByteBuffer.allocate(messageLength);
        byteBuffer.putInt(messageLength);
        byteBuffer.put(MessageTypes.PROTOCOL_VERSION);
        byteBuffer.put(MessageTypes.HOST_RESPONSE);
        byteBuffer.putInt(responseLength);
        byteBuffer.put(mResponse.getBytes());
        mSerialized = byteBuffer.array();

    }

    @Override
    protected void deserialize() {

        ByteBuffer byteBuffer = ByteBuffer.wrap(mSerialized);
        int messageLength;
        messageLength = byteBuffer.getInt();
        byte protocolVersion = byteBuffer.get();
        byte messageType = byteBuffer.get();
        int responseLength;
        responseLength = byteBuffer.getInt();
        byte[] responseBytes;
        responseBytes = new byte[responseLength];
        byteBuffer.get(responseBytes);
        mResponse = new String(responseBytes);
    }

    @Override
    public Byte getMassageType() {
        return MessageTypes.HOST_RESPONSE;
    }
}
