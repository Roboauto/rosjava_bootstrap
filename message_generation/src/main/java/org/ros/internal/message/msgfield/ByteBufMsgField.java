package org.ros.internal.message.msgfield;

import com.google.common.base.Preconditions;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * @author pavel.cernocky@artin.cz
 * @author pavel.erlebach@artin.cz
 */

public class ByteBufMsgField extends AbstractArrayMsgField {

    public ByteBufMsgField(Class<?> msgClass, String getterName, String setterName, int size) {
        super(msgClass, getterName, setterName, ByteBuf.class, size);
    }

    @Override
    protected void serialize(ByteBuf buffer, Object valueToBeSerialized) {
        Preconditions.checkArgument(valueToBeSerialized instanceof ByteBuf);
        ByteBuf typedValue = (ByteBuf) valueToBeSerialized;
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

    @Override
    void writeDefaultItemToBuffer(ByteBuf buffer) {
        buffer.writeByte(0);
    }
}
