package org.ros.internal.message.msgfield;

import com.google.common.base.Preconditions;
import io.netty.buffer.ByteBuf;

import java.lang.reflect.InvocationTargetException;

/**
 * @author pavel.cernocky@artin.cz
 */
public class ShortMsgField extends AbstractMsgField {

    public ShortMsgField(Class<?> msgClass, String getterName, String setterName) {
        super(msgClass, getterName, setterName, short.class);
    }

    @Override
    public void writeObjectValueToBuffer(Object object, ByteBuf buffer) {
        try {
            Object value = getter.invoke(object);
            Preconditions.checkArgument(value instanceof Short);
            buffer.writeShort((Short) value);
        }
        catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public void setBufferValueToObject(Object object, ByteBuf buffer) {
        short fieldValue = buffer.readShort();
        try {
            setter.invoke(object, fieldValue);
        }
        catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

}
