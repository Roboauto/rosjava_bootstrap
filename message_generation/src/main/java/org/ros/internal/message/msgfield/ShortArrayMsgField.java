package org.ros.internal.message.msgfield;

import io.netty.buffer.ByteBuf;

/**
 * @author pavel.cernocky@artin.cz
 */

public class ShortArrayMsgField extends ObjectMsgField {

    private final int size;

    public ShortArrayMsgField(Class<?> msgClass, String setterName, int size) {
        super(msgClass, setterName, short[].class);
        this.size = size;
    }

    @Override
    protected Object deserialize(ByteBuf buffer) {
        int length = size < 0 ? buffer.readInt() : size;
        short[] value = new short[length];
        buffer.nioBuffer().asShortBuffer().get(value);
        buffer.skipBytes(length * Short.SIZE / 8);
        return value;
    }

}
