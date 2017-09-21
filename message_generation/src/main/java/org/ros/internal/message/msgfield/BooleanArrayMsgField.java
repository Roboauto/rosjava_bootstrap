package org.ros.internal.message.msgfield;

import com.google.common.base.Preconditions;
import io.netty.buffer.ByteBuf;
import org.apache.commons.lang.ArrayUtils;

/**
 * @author pavel.cernocky@artin.cz
 * @author pavel.erlebach@artin.cz
 */
public class BooleanArrayMsgField extends AbstractArrayMsgField {

    public BooleanArrayMsgField(Class<?> msgClass, String getterName, String setterName, int size) {
        super(msgClass, getterName, setterName, boolean[].class, size);
    }

    @Override
    protected void serialize(ByteBuf buffer, Object valueToBeSerialized) {
        Preconditions.checkArgument(valueToBeSerialized instanceof boolean[]);
        boolean[] typedValues = (boolean[]) valueToBeSerialized;
        if (size < 0) {
            buffer.writeInt(typedValues.length);
        }
        for (boolean booleanValue : typedValues) {
            buffer.writeByte(booleanValue ? 1 : 0);
        }
    }

    @Override
    protected Object deserialize(ByteBuf buffer) {
        int length = size < 0 ? buffer.readInt() : size;
        boolean[] value = new boolean[length];
        for (int i = 0; i < length; i++) {
            value[i] = buffer.readByte() == 1;
        }
        return value;
    }

    @Override
    void writeDefaultItemToBuffer(ByteBuf buffer) {
        buffer.writeByte(0);
    }
}
