package org.ros.internal.message.msgfield;

import io.netty.buffer.ByteBuf;

import java.lang.reflect.InvocationTargetException;

/**
 * @author pavel.cernocky@artin.cz
 */
public class LongMsgField extends AbstractMsgField {

    public LongMsgField(Class<?> msgClass, String setterName) {
        super(msgClass, setterName, long.class);
    }

    public void setValue(Object object, ByteBuf buffer) {
        long fieldValue = buffer.readLong();
        try {
            setter.invoke(object, fieldValue);
        }
        catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

}
