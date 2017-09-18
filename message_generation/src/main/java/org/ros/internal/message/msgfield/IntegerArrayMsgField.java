package org.ros.internal.message.msgfield;

import io.netty.buffer.ByteBuf;

/**
 * @author pavel.cernocky@artin.cz
 */

public class IntegerArrayMsgField extends ObjectMsgField {

    private final int size;

    public IntegerArrayMsgField(Class<?> msgClass, String setterName, int size) {
        super(msgClass, setterName, int[].class);
        this.size = size;
    }

    @Override
    protected Object deserialize(ByteBuf buffer) {
        int length = size < 0 ? buffer.readInt() : size;
        int[] value = new int[length];
        buffer.nioBuffer().asIntBuffer().get(value);
        buffer.skipBytes(length * Integer.SIZE / 8);
        return value;
    }

}
