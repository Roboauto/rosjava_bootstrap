package org.ros.internal.message.msgfield;

import com.google.common.base.Preconditions;
import io.netty.buffer.ByteBuf;

/**
 * @author pavel.cernocky@artin.cz
 */
public class BooleanArrayMsgField extends ObjectMsgField {

    private final int size;

    public BooleanArrayMsgField(Class<?> msgClass, String getterName, String setterName, int size) {
        super(msgClass, getterName, setterName, boolean[].class);
        this.size = size;
    }

    @Override
    protected void serialize(ByteBuf buffer, Object value) {
        Preconditions.checkArgument(value instanceof boolean[]);
        boolean[] typedValues = (boolean[]) value;
        if (size < 0) {
            buffer.writeInt(typedValues.length);
        }
        for (boolean booleanValue : typedValues) {
            buffer.writeInt(booleanValue ? 1 : 0);
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

}
