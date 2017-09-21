package org.ros.internal.message.msgfield;

import com.google.common.base.Preconditions;
import io.netty.buffer.ByteBuf;

import java.lang.reflect.InvocationTargetException;

/**
 * @author pavel.cernocky@artin.cz
 * @author pavel.erlebach@artin.cz
 */
public class BooleanMsgField extends AbstractMsgField {

    public BooleanMsgField(Class<?> msgClass, String getterName, String setterName) {
        super(msgClass, getterName, setterName, boolean.class);
    }

    @Override
    protected void serialize(ByteBuf buffer, Object valueToBeSerialized) {
        Preconditions.checkArgument(valueToBeSerialized instanceof Boolean);
        buffer.writeByte((Boolean) valueToBeSerialized ? 1 : 0);
    }

    @Override
    protected void serializeNull(ByteBuf buffer) {
        buffer.writeByte(0);
    }

    @Override
    protected Object deserialize(ByteBuf buffer) {
        return buffer.readByte() == 1;
    }

}
