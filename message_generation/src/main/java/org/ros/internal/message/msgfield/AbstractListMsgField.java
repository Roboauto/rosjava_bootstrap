package org.ros.internal.message.msgfield;

import io.netty.buffer.ByteBuf;

/**
 * @author pavel.erlebach@artin.cz
 */
public abstract class AbstractListMsgField extends AbstractMsgField {

    public AbstractListMsgField(Class<?> msgClass, String getterName, String setterName, Class<?> fieldClass) {
        super(msgClass, getterName, setterName, fieldClass);
    }

    @Override
    protected void serializeNull(ByteBuf buffer) {
        buffer.writeInt(0);
    }
}
