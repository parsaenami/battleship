package ir.aut.battleship.logic;

public abstract class BaseMessage {
    protected byte[] mSerialized;

    protected abstract void serialize();

    protected abstract void deserialize();

    public abstract Byte getMassageType();

    byte[] getSerialized() {
        return mSerialized;
    }
}
