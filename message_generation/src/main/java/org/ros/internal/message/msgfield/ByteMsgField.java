package org.ros.internal.message.msgfield;

import io.netty.buffer.ByteBuf;

import java.lang.reflect.InvocationTargetException;

/**
 * @author pavel.cernocky@artin.cz
 */
public class ByteMsgField extends AbstractMsgField {

    public ByteMsgField(Class<?> msgClass, String setterName) {
        super(msgClass, setterName, byte.class);
    }

    public void setValue(Object object, ByteBuf buffer) {
        byte fieldValue = buffer.readByte();
        try {
            setter.invoke(object, fieldValue);
        }
        catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

}
