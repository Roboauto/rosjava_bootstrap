package org.ros.internal.message.msgfield;

import com.google.common.base.Preconditions;
import io.netty.buffer.ByteBuf;

import java.lang.reflect.InvocationTargetException;

/**
 * @author pavel.cernocky@artin.cz
 * @author pavel.erlebach@artin.cz
 */
public class LongMsgField extends AbstractMsgField {

    public LongMsgField(Class<?> msgClass, String getterName, String setterName) {
        super(msgClass, getterName, setterName, long.class);
    }

    @Override
    protected void serialize(ByteBuf buffer, Object valueToBeSerialized) {
        Preconditions.checkArgument(valueToBeSerialized instanceof Long);
        buffer.writeLong((Long) valueToBeSerialized);
    }

    @Override
    protected void serializeNull(ByteBuf buffer) {
        buffer.writeLong(0);
    }

    @Override
    protected Object deserialize(ByteBuf buffer) {
        return buffer.readLong();
    }
}
