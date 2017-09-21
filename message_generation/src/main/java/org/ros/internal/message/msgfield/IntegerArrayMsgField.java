package org.ros.internal.message.msgfield;

import com.google.common.base.Preconditions;
import io.netty.buffer.ByteBuf;

/**
 * @author pavel.cernocky@artin.cz
 * @author pavel.erlebach@artin.cz
 */

public class IntegerArrayMsgField extends AbstractArrayMsgField {

    public IntegerArrayMsgField(Class<?> msgClass, String getterName, String setterName, int size) {
        super(msgClass, getterName, setterName, int[].class, size);
    }

    @Override
    protected void serialize(ByteBuf buffer, Object valueToBeSerialized) {
        Preconditions.checkArgument(valueToBeSerialized instanceof int[]);
        int[] typedValues = (int[]) valueToBeSerialized;
        if (size < 0) {
            buffer.writeInt(typedValues.length);
        }
        for (int intValue : typedValues) {
            buffer.writeInt(intValue);
        }
    }

    @Override
    protected Object deserialize(ByteBuf buffer) {
        int length = size < 0 ? buffer.readInt() : size;
        int[] value = new int[length];
        buffer.nioBuffer().asIntBuffer().get(value);
        buffer.skipBytes(length * Integer.SIZE / 8);
        return value;
    }

    @Override
    void writeDefaultItemToBuffer(ByteBuf buffer) {
        buffer.writeInt(0);
    }
}
