package org.ros.internal.message.msgfield;

import com.google.common.base.Preconditions;
import io.netty.buffer.ByteBuf;

import java.lang.reflect.InvocationTargetException;

/**
 * @author pavel.cernocky@artin.cz
 */
public class BooleanMsgField extends AbstractMsgField {

    public BooleanMsgField(Class<?> msgClass, String getterName, String setterName) {
        super(msgClass, getterName, setterName, boolean.class);
    }

    @Override
    public void writeObjectValueToBuffer(Object object, ByteBuf buffer) {
        try {
            Object value = getter.invoke(object);
            Preconditions.checkArgument(value instanceof Boolean);
            buffer.writeInt((Boolean) value ? 1 : 0);
        }
        catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setBufferValueToObject(Object object, ByteBuf buffer) {
        boolean fieldValue = buffer.readByte() == 1;
        try {
            setter.invoke(object, fieldValue);
        }
        catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

}
