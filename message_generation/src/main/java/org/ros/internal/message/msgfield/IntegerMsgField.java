package org.ros.internal.message.msgfield;

import com.google.common.base.Preconditions;
import io.netty.buffer.ByteBuf;

import java.lang.reflect.InvocationTargetException;

/**
 * @author pavel.cernocky@artin.cz
 * @author pavel.erlebach@artin.cz
 */
public class IntegerMsgField extends AbstractMsgField {

    public IntegerMsgField(Class<?> msgClass, String getterName, String setterName) {
        super(msgClass, getterName, setterName, int.class);
    }

    @Override
    protected void serialize(ByteBuf buffer, Object valueToBeSerialized) {
        buffer.writeInt((Integer) valueToBeSerialized);
    }

    @Override
    protected void serializeNull(ByteBuf buffer) {
        buffer.writeInt(0);
    }

    @Override
    protected Object deserialize(ByteBuf buffer) {
        return buffer.readInt();
    }
}
