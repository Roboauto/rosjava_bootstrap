package org.ros.internal.message.msgfield;

import io.netty.buffer.ByteBuf;

import java.util.concurrent.atomic.AtomicInteger;

public class HeaderSeqMsgField extends IntegerMsgField {
    private final AtomicInteger SEQUENCE_NUMBER = new AtomicInteger(0);

    public HeaderSeqMsgField(Class<?> msgClass, String getterName, String setterName) {
        super(msgClass, getterName, setterName);
    }

    @Override
    protected void serialize(ByteBuf buffer, Object valueToBeSerialized) {
        // we ignore supplied value and overwrite it with the sequence number
        serializeNull(buffer);
    }

    @Override
    protected void serializeNull(ByteBuf buffer) {
        buffer.writeInt(SEQUENCE_NUMBER.getAndIncrement());
    }

}
