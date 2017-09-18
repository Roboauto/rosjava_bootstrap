package org.ros.internal.message.msgfield;

import com.google.common.base.Preconditions;
import io.netty.buffer.ByteBuf;

/**
 * @author pavel.cernocky@artin.cz
 */

public class ShortArrayMsgField extends ObjectMsgField {

    private final int size;

    public ShortArrayMsgField(Class<?> msgClass, String getterName, String setterName, int size) {
        super(msgClass, getterName, setterName, short[].class);
        this.size = size;
    }

    @Override
    protected void serialize(ByteBuf buffer, Object value) {
        Preconditions.checkArgument(value instanceof short[]);
        short[] typedValues = (short[]) value;
        if (size < 0) {
            buffer.writeInt(typedValues.length);
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

}
