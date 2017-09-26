package org.ros.internal.message.msgfield;

import io.netty.buffer.ByteBuf;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author pavel.cernocky@artin.cz
 * @author pavel.erlebach@artin.cz
 */
public abstract class AbstractMsgField implements MsgField {

    private final Method getter;
    private final Method setter;

    public AbstractMsgField(Class<?> msgClass, String getterName, String setterName, Class<?> fieldClass) {
        try {
            getter = msgClass.getMethod(getterName);
            setter = msgClass.getMethod(setterName, fieldClass);
        }
        catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public final void writeObjectFieldToBuffer(Object objectToBeSerialized, ByteBuf buffer) {
        try {
            Object valueToBeSerialized = null;
            if (objectToBeSerialized != null) {
                valueToBeSerialized = getter.invoke(objectToBeSerialized);
            }
            // let's split it here to have the distinction explicit and to avoid all serialize methods having this branching
            if (valueToBeSerialized == null) {
                serializeNull(buffer);
            } else {
                serialize(buffer, valueToBeSerialized);
            }
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

    protected abstract void serialize(ByteBuf buffer, Object valueToBeSerialized);

    protected abstract void serializeNull(ByteBuf buffer);

    protected abstract Object deserialize(ByteBuf buffer);
}
