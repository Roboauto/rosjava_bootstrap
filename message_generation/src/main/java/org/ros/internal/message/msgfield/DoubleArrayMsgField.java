package org.ros.internal.message.msgfield;

import com.google.common.base.Preconditions;
import io.netty.buffer.ByteBuf;

/**
 * @author pavel.cernocky@artin.cz
 */

public class DoubleArrayMsgField extends ObjectMsgField {

    private final int size;

    public DoubleArrayMsgField(Class<?> msgClass, String getterName, String setterName, int size) {
        super(msgClass, getterName, setterName, double[].class);
        this.size = size;
    }

    @Override
    protected void serialize(ByteBuf buffer, Object value) {
        Preconditions.checkArgument(value instanceof double[]);
        double[] typedValues = (double[]) value;
        if (size < 0) {
            buffer.writeInt(typedValues.length);
        }
        for (double doubleValue : typedValues) {
            buffer.writeDouble(doubleValue);
        }
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
