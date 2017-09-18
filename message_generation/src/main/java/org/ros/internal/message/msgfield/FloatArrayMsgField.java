package org.ros.internal.message.msgfield;

import io.netty.buffer.ByteBuf;

/**
 * @author pavel.cernocky@artin.cz
 */

public class FloatArrayMsgField extends ObjectMsgField {

    private final int size;

    public FloatArrayMsgField(Class<?> msgClass, String setterName, int size) {
        super(msgClass, setterName, float[].class);
        this.size = size;
    }

    @Override
    protected Object deserialize(ByteBuf buffer) {
        int length = size < 0 ? buffer.readInt() : size;
        float[] value = new float[length];
        buffer.nioBuffer().asFloatBuffer().get(value);
        buffer.skipBytes(length * Float.SIZE / 8);
        return value;
    }

}
