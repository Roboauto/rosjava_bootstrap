package org.ros.internal.message.msgfield;

import io.netty.buffer.ByteBuf;

/**
 * @author pavel.cernocky@artin.cz
 */

public class ByteBufMsgField extends ObjectMsgField {

    private final int size;

    public ByteBufMsgField(Class<?> msgClass, String setterName, int size) {
        super(msgClass, setterName, ByteBuf.class);
        this.size = size;
    }

    @Override
    protected Object deserialize(ByteBuf buffer) {
        int length = size < 0 ? buffer.readInt() : size;
        return buffer.readSlice(length);
    }

}
