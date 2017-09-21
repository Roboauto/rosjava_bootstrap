package org.ros.internal.message.msgfield;

import com.google.common.base.Preconditions;
import io.netty.buffer.ByteBuf;

import java.lang.reflect.InvocationTargetException;

/**
 * @author pavel.cernocky@artin.cz
 * @author pavel.erlebach@artin.cz
 */
public class ByteMsgField extends AbstractMsgField {

    public ByteMsgField(Class<?> msgClass, String getterName, String setterName) {
        super(msgClass, getterName, setterName, byte.class);
    }

    @Override
    protected void serialize(ByteBuf buffer, Object valueToBeSerialized) {
        Preconditions.checkArgument(valueToBeSerialized instanceof Byte);
        buffer.writeByte((Byte) valueToBeSerialized);
    }

    @Override
    protected void serializeNull(ByteBuf buffer) {
        buffer.writeByte(0);
    }

    @Override
    protected Object deserialize(ByteBuf buffer) {
        return buffer.readByte();
    }
}
