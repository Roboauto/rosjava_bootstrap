package org.ros.internal.message.msgfield;

import com.google.common.base.Preconditions;
import io.netty.buffer.ByteBuf;

/**
 * @author pavel.cernocky@artin.cz
 * @author pavel.erlebach@artin.cz
 */

public class LongArrayMsgField extends AbstractArrayMsgField {

    public LongArrayMsgField(Class<?> msgClass, String getterName, String setterName, int size) {
        super(msgClass, getterName, setterName, long[].class, size);
    }

    @Override
    protected void serialize(ByteBuf buffer, Object valueToBeSerialized) {
        Preconditions.checkArgument(valueToBeSerialized instanceof long[]);
        long[] typedValues = (long[]) valueToBeSerialized;
        if (size < 0) {
            buffer.writeInt(typedValues.length);
        }
        for (long longValue : typedValues) {
            buffer.writeLong(longValue);
        }
    }

    @Override
    protected Object deserialize(ByteBuf buffer) {
        int length = size < 0 ? buffer.readInt() : size;
        long[] value = new long[length];
        buffer.nioBuffer().asLongBuffer().get(value);
        buffer.skipBytes(length * Long.SIZE / 8);
        return value;
    }

    @Override
    void writeDefaultItemToBuffer(ByteBuf buffer) {
        buffer.writeLong(0);
    }
}
