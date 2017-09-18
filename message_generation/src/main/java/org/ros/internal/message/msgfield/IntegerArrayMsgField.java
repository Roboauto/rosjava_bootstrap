package org.ros.internal.message.msgfield;

import com.google.common.base.Preconditions;
import io.netty.buffer.ByteBuf;

/**
 * @author pavel.cernocky@artin.cz
 */

public class IntegerArrayMsgField extends ObjectMsgField {

    private final int size;

    public IntegerArrayMsgField(Class<?> msgClass, String getterName, String setterName, int size) {
        super(msgClass, getterName, setterName, int[].class);
        this.size = size;
    }

    @Override
    protected void serialize(ByteBuf buffer, Object value) {
        Preconditions.checkArgument(value instanceof int[]);
        int[] typedValues = (int[]) value;
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

}