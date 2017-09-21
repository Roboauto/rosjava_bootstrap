package org.ros.internal.message.msgfield;

import com.google.common.base.Preconditions;
import io.netty.buffer.ByteBuf;

import java.lang.reflect.InvocationTargetException;

/**
 * @author pavel.cernocky@artin.cz
 * @author pavel.erlebach@artin.cz
 */
public class ShortMsgField extends AbstractMsgField {

    public ShortMsgField(Class<?> msgClass, String getterName, String setterName) {
        super(msgClass, getterName, setterName, short.class);
    }

    @Override
    protected void serialize(ByteBuf buffer, Object valueToBeSerialized) {
        Preconditions.checkArgument(valueToBeSerialized instanceof Short);
        buffer.writeShort((Short) valueToBeSerialized);
    }

    @Override
    protected void serializeNull(ByteBuf buffer) {
        buffer.writeShort(0);
    }

    @Override
    protected Object deserialize(ByteBuf buffer) {
        return buffer.readShort();
    }
}
