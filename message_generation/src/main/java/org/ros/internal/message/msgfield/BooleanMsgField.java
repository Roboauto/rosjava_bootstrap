package org.ros.internal.message.msgfield;

import io.netty.buffer.ByteBuf;

import java.lang.reflect.InvocationTargetException;

/**
 * @author pavel.cernocky@artin.cz
 */
public class BooleanMsgField extends AbstractMsgField {

    public BooleanMsgField(Class<?> msgClass, String setterName) {
        super(msgClass, setterName, boolean.class);
    }

    public void setValue(Object object, ByteBuf buffer) {
        boolean fieldValue = buffer.readByte() == 1;
        try {
            setter.invoke(object, fieldValue);
        }
        catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

}
