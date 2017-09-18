package org.ros.internal.message.msgfield;

import io.netty.buffer.ByteBuf;

import java.lang.reflect.InvocationTargetException;

/**
 * @author pavel.cernocky@artin.cz
 */
public class IntegerMsgField extends AbstractMsgField {

    public IntegerMsgField(Class<?> msgClass, String setterName) {
        super(msgClass, setterName, int.class);
    }

    public void setValue(Object object, ByteBuf buffer) {
        int fieldValue = buffer.readInt();
        try {
            setter.invoke(object, fieldValue);
        }
        catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

}
