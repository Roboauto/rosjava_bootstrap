package org.ros.internal.message.msgfield;

import com.google.common.base.Preconditions;
import io.netty.buffer.ByteBuf;

/**
 * @author pavel.cernocky@artin.cz
 */

public class LongArrayMsgField extends ObjectMsgField {

    private final int size;

    public LongArrayMsgField(Class<?> msgClass, String getterName, String setterName, int size) {
        super(msgClass, getterName, setterName, long[].class);
        this.size = size;
    }

    @Override
    protected void serialize(ByteBuf buffer, Object value) {
        Preconditions.checkArgument(value instanceof long[]);
        long[] typedValues = (long[]) value;
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

}
