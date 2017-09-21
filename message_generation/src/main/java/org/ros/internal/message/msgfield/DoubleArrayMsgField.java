package org.ros.internal.message.msgfield;

import com.google.common.base.Preconditions;
import io.netty.buffer.ByteBuf;

/**
 * @author pavel.cernocky@artin.cz
 * @author pavel.erlebach@artin.cz
 */

public class DoubleArrayMsgField extends AbstractArrayMsgField {

    public DoubleArrayMsgField(Class<?> msgClass, String getterName, String setterName, int size) {
        super(msgClass, getterName, setterName, double[].class, size);
    }

    @Override
    protected void serialize(ByteBuf buffer, Object valueToBeSerialized) {
        Preconditions.checkArgument(valueToBeSerialized instanceof double[]);
        double[] typedValues = (double[]) valueToBeSerialized;
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

    @Override
    void writeDefaultItemToBuffer(ByteBuf buffer) {
        buffer.writeDouble(0);
    }
}
