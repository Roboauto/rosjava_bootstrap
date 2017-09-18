package org.ros.internal.message.msgfield;

import io.netty.buffer.ByteBuf;

import java.lang.reflect.InvocationTargetException;

/**
 * @author pavel.cernocky@artin.cz
 */
public abstract class ObjectMsgField extends AbstractMsgField {

    public ObjectMsgField(Class<?> msgClass, String getterName, String setterName, Class<?> fieldClass) {
        super(msgClass, getterName, setterName, fieldClass);
    }

    @Override
    public void writeObjectValueToBuffer(Object object, ByteBuf buffer) {
        try {
            Object value = getter.invoke(object);
            serialize(buffer, value);
        }
        catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public final void setBufferValueToObject(Object object, ByteBuf buffer) {
        Object value = deserialize(buffer);
        try {
            setter.invoke(object, value);
        }
        catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    protected abstract void serialize(ByteBuf buffer, Object value);

    protected abstract Object deserialize(ByteBuf buffer);

}
