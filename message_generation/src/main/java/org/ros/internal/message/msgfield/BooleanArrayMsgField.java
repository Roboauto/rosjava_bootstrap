package org.ros.internal.message.msgfield;

import io.netty.buffer.ByteBuf;

/**
 * @author pavel.cernocky@artin.cz
 */
public class BooleanArrayMsgField extends ObjectMsgField {

    private final int size;

    public BooleanArrayMsgField(Class<?> msgClass, String setterName, int size) {
        super(msgClass, setterName, boolean[].class);
        this.size = size;
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
