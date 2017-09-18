package org.ros.internal.message.msgfield;

import com.google.common.base.Preconditions;
import io.netty.buffer.ByteBuf;

import java.lang.reflect.InvocationTargetException;

/**
 * @author pavel.cernocky@artin.cz
 */
public class DoubleMsgField extends AbstractMsgField {

    public DoubleMsgField(Class<?> msgClass, String getterName, String setterName) {
        super(msgClass, getterName, setterName, double.class);
    }

    @Override
    public void writeObjectValueToBuffer(Object object, ByteBuf buffer) {
        try {
            Object value = getter.invoke(object);
            Preconditions.checkArgument(value instanceof Double);
            buffer.writeDouble((Double) value);
        }
        catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setBufferValueToObject(Object object, ByteBuf buffer) {
        double fieldValue = buffer.readDouble();
        try {
            setter.invoke(object, fieldValue);
        }
        catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

}
