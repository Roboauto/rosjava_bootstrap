package org.ros.internal.message.msgfield;

import io.netty.buffer.ByteBuf;

/**
 * @author pavel.cernocky@artin.cz
 */

public class LongArrayMsgField extends ObjectMsgField {

    private final int size;

    public LongArrayMsgField(Class<?> msgClass, String setterName, int size) {
        super(msgClass, setterName, long[].class);
        this.size = size;
    }

    @Override
    protected Object deserialize(ByteBuf buffer) {
        int length = size < 0 ? buffer.readInt() : size;
        long[] value = new long[length];
        buffer.nioBuffer().asLongBuffer().get(value);
        buffer.skipBytes(length * Long.SIZE / 8);
        return value;
    }

}
