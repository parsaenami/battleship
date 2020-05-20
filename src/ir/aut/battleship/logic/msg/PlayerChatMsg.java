package ir.aut.battleship.logic.msg;

import ir.aut.battleship.logic.BaseMessage;
import ir.aut.battleship.logic.MessageTypes;

import java.nio.ByteBuffer;

public class PlayerChatMsg extends BaseMessage {
    private String mChat;

    public PlayerChatMsg(String chat) {
        mChat = chat;
        serialize();

    }

    public PlayerChatMsg(byte[] serialized) {
        mSerialized = serialized;
        deserialize();
    }

    @Override
    protected void serialize() {

        int chatLength = mChat.getBytes().length;
        int messageLength = 4 + 1 + 1 + 4 + chatLength;
        ByteBuffer byteBuffer = ByteBuffer.allocate(messageLength);
        byteBuffer.putInt(messageLength);
        byteBuffer.put(MessageTypes.PROTOCOL_VERSION);
        byteBuffer.put(MessageTypes.PLAYER_CHAT);
        byteBuffer.putInt(chatLength);
        byteBuffer.put(mChat.getBytes());
        mSerialized = byteBuffer.array();

    }

    @Override
    protected void deserialize() {

        ByteBuffer byteBuffer = ByteBuffer.wrap(mSerialized);
        int messageLength = byteBuffer.getInt();
        byte protocolVersion;
        protocolVersion = byteBuffer.get();
        byte messageType = byteBuffer.get();
        int chatLength;
        chatLength = byteBuffer.getInt();
        byte[] chatBytes = new byte[chatLength];
        byteBuffer.get(chatBytes);
        mChat = new String(chatBytes);
    }

    @Override
    public Byte getMassageType() {
        return MessageTypes.PLAYER_CHAT;
    }

    public String getmChat() {
        return mChat;
    }
}
