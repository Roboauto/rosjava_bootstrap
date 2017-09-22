package org.ros.internal.message.msgfield;

import com.google.common.base.Preconditions;
import io.netty.buffer.ByteBuf;

import java.lang.reflect.InvocationTargetException;

/**
 * @author pavel.cernocky@artin.cz
 * @author pavel.erlebach@artin.cz
 */

public class FloatMsgField extends AbstractMsgField {

    public FloatMsgField(Class<?> msgClass, String getterName, String setterName) {
        super(msgClass, getterName, setterName, float.class);
    }

    @Override
    protected void serialize(ByteBuf buffer, Object valueToBeSerialized) {
        buffer.writeFloat((Float) valueToBeSerialized);
    }

    @Override
    protected void serializeNull(ByteBuf buffer) {
        buffer.writeFloat(0);
    }

    @Override
    protected Object deserialize(ByteBuf buffer) {
        return buffer.readFloat();
    }
}
