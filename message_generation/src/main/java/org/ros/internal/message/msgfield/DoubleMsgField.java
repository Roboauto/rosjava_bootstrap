package org.ros.internal.message.msgfield;

import io.netty.buffer.ByteBuf;

import java.lang.reflect.InvocationTargetException;

/**
 * @author pavel.cernocky@artin.cz
 */
public class DoubleMsgField extends AbstractMsgField {

    public DoubleMsgField(Class<?> msgClass, String setterName) {
        super(msgClass, setterName, double.class);
    }

    public void setValue(Object object, ByteBuf buffer) {
        double fieldValue = buffer.readDouble();
        try {
            setter.invoke(object, fieldValue);
        }
        catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

}
