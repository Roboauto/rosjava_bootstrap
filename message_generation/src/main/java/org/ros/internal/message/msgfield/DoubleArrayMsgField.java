package org.ros.internal.message.msgfield;

import io.netty.buffer.ByteBuf;

/**
 * @author pavel.cernocky@artin.cz
 */

public class DoubleArrayMsgField extends ObjectMsgField {

    private final int size;

    public DoubleArrayMsgField(Class<?> msgClass, String setterName, int size) {
        super(msgClass, setterName, double[].class);
        this.size = size;
    }

    @Override
    protected Object deserialize(ByteBuf buffer) {
        int length = size < 0 ? buffer.readInt() : size;
        double[] value = new double[length];
        buffer.nioBuffer().asDoubleBuffer().get(value);
        buffer.skipBytes(length * Double.SIZE / 8);
        return value;
    }

}
