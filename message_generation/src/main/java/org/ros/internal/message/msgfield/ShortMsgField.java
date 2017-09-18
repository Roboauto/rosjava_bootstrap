package org.ros.internal.message.msgfield;

import io.netty.buffer.ByteBuf;

import java.lang.reflect.InvocationTargetException;

/**
 * @author pavel.cernocky@artin.cz
 */
public class ShortMsgField extends AbstractMsgField {

    public ShortMsgField(Class<?> msgClass, String setterName) {
        super(msgClass, setterName, short.class);
    }

    public void setValue(Object object, ByteBuf buffer) {
        short fieldValue = buffer.readShort();
        try {
            setter.invoke(object, fieldValue);
        }
        catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

}
