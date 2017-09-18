package org.ros.internal.message.msgfield;

import com.google.common.base.Preconditions;
import io.netty.buffer.ByteBuf;

/**
 * @author pavel.cernocky@artin.cz
 */

public class FloatArrayMsgField extends ObjectMsgField {

    private final int size;

    public FloatArrayMsgField(Class<?> msgClass, String getterName, String setterName, int size) {
        super(msgClass, getterName, setterName, float[].class);
        this.size = size;
    }

    @Override
    protected void serialize(ByteBuf buffer, Object value) {
        Preconditions.checkArgument(value instanceof float[]);
        float[] typedValues = (float[]) value;
        if (size < 0) {
            buffer.writeInt(typedValues.length);
        }
        for (float floatValue : typedValues) {
            buffer.writeFloat(floatValue);
        }
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
