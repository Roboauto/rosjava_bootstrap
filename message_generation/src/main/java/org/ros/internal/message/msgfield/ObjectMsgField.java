package org.ros.internal.message.msgfield;

import io.netty.buffer.ByteBuf;

import java.lang.reflect.InvocationTargetException;

/**
 * @author pavel.cernocky@artin.cz
 */
public abstract class ObjectMsgField extends AbstractMsgField {

    public ObjectMsgField(Class<?> msgClass, String setterName, Class<?> fieldClass) {
        super(msgClass, setterName, fieldClass);
    }

    @Override
    public final void setValue(Object object, ByteBuf buffer) {
        Object value = deserialize(buffer);
        try {
            setter.invoke(object, value);
        }
        catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    protected abstract Object deserialize(ByteBuf buffer);

}
