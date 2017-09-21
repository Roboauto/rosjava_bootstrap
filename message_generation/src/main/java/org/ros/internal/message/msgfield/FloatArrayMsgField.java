package org.ros.internal.message.msgfield;

import com.google.common.base.Preconditions;
import io.netty.buffer.ByteBuf;

/**
 * @author pavel.cernocky@artin.cz
 * @author pavel.erlebach@artin.cz
 */

public class FloatArrayMsgField extends AbstractArrayMsgField {

    public FloatArrayMsgField(Class<?> msgClass, String getterName, String setterName, int size) {
        super(msgClass, getterName, setterName, float[].class, size);
    }

    @Override
    protected void serialize(ByteBuf buffer, Object valueToBeSerialized) {
        Preconditions.checkArgument(valueToBeSerialized instanceof float[]);
        float[] typedValues = (float[]) valueToBeSerialized;
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

    @Override
    void writeDefaultItemToBuffer(ByteBuf buffer) {
        buffer.writeFloat(0);
    }
}
