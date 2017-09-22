package org.ros.internal.message.msgfield;

import com.google.common.base.Preconditions;
import io.netty.buffer.ByteBuf;

/**
 * @author pavel.cernocky@artin.cz
 * @author pavel.erlebach@artin.cz
 */

public class ShortArrayMsgField extends AbstractArrayMsgField {

    public ShortArrayMsgField(Class<?> msgClass, String getterName, String setterName, int size) {
        super(msgClass, getterName, setterName, short[].class, size);
    }

    @Override
    protected void serialize(ByteBuf buffer, Object valueToBeSerialized) {
        short[] typedValues = (short[]) valueToBeSerialized;
        if (size < 0) {
            buffer.writeInt(typedValues.length);
        } else {
            Preconditions.checkArgument(typedValues.length == size);
        }
        for (short shortValue : typedValues) {
            buffer.writeShort(shortValue);
        }
    }

    @Override
    protected Object deserialize(ByteBuf buffer) {
        int length = size < 0 ? buffer.readInt() : size;
        short[] value = new short[length];
        buffer.nioBuffer().asShortBuffer().get(value);
        buffer.skipBytes(length * Short.SIZE / 8);
        return value;
    }

    @Override
    void writeDefaultItemToBuffer(ByteBuf buffer) {
        buffer.writeShort(0);
    }
}
