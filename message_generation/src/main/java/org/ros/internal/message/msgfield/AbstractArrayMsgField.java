package org.ros.internal.message.msgfield;

import io.netty.buffer.ByteBuf;

/**
 * @author pavel.erlebach@artin.cz
 */
public abstract class AbstractArrayMsgField extends AbstractMsgField {

    protected final int size;

    public AbstractArrayMsgField(Class<?> msgClass, String getterName, String setterName, Class<?> fieldClass, int size) {
        super(msgClass, getterName, setterName, fieldClass);
        this.size = size;
    }

    @Override
    protected void serializeNull(ByteBuf buffer) {
        if (size < 0) {
            buffer.writeInt(0);
        } else {
            for (int i = 0; i < size; i++) {
                writeDefaultItemToBuffer(buffer);
            }
        }
    }

    abstract void writeDefaultItemToBuffer(ByteBuf buffer);
}
