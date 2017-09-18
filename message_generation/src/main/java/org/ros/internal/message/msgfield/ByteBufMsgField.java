package org.ros.internal.message.msgfield;

import com.google.common.base.Preconditions;
import io.netty.buffer.ByteBuf;

/**
 * @author pavel.cernocky@artin.cz
 */

public class ByteBufMsgField extends ObjectMsgField {

    private final int size;

    public ByteBufMsgField(Class<?> msgClass, String getterName, String setterName, int size) {
        super(msgClass, getterName, setterName, ByteBuf.class);
        this.size = size;
    }

    @Override
    protected void serialize(ByteBuf buffer, Object value) {
        Preconditions.checkArgument(value instanceof ByteBuf);
        ByteBuf typedValue = (ByteBuf) value;
        if (size < 0) {
            buffer.writeInt(typedValue.readableBytes());
        }
        // By specifying the start index and length we avoid modifying value's
        // indices and marks.
        buffer.writeBytes(typedValue, 0, typedValue.readableBytes());
    }

    @Override
    protected Object deserialize(ByteBuf buffer) {
        int length = size < 0 ? buffer.readInt() : size;
        return buffer.readSlice(length);
    }

}
