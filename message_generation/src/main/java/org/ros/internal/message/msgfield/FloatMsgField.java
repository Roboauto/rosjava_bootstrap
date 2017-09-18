package org.ros.internal.message.msgfield;

import io.netty.buffer.ByteBuf;

import java.lang.reflect.InvocationTargetException;

/**
 * @author pavel.cernocky@artin.cz
 */

public class FloatMsgField extends AbstractMsgField {

    public FloatMsgField(Class<?> msgClass, String setterName) {
        super(msgClass, setterName, float.class);
    }

    public void setValue(Object object, ByteBuf buffer) {
        float fieldValue = buffer.readFloat();
        try {
            setter.invoke(object, fieldValue);
        }
        catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

}
