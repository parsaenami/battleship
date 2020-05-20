package ir.aut.battleship.logic;

public class MessageTypes {

    public static final byte PROTOCOL_VERSION = 1;

    public static final byte REQUEST_LOGIN_HOST = 1;

    public static final byte REQUEST_LOGIN_CLIENT = 2;

    public static final byte HOST_RESPONSE = 3;

    public static final byte PLAYER_READY = 4;

    public static final byte PLAYER_ABORT = 5;

    public static final byte PLAYER_CHAT = 6;

    public static final byte PLAYER_MOVE_HIT = 7;

    public static final byte PLAYER_MOVE_RESULT = 8;

    public static final byte GAME_FINISHED = 9;


}
