package org.ros.internal.message.msgfield;

import com.google.common.base.Preconditions;
import io.netty.buffer.ByteBuf;

import java.lang.reflect.InvocationTargetException;

/**
 * @author pavel.cernocky@artin.cz
 * @author pavel.erlebach@artin.cz
 */
public class DoubleMsgField extends AbstractMsgField {

    public DoubleMsgField(Class<?> msgClass, String getterName, String setterName) {
        super(msgClass, getterName, setterName, double.class);
    }

    @Override
    protected void serialize(ByteBuf buffer, Object valueToBeSerialized) {
        buffer.writeDouble((Double) valueToBeSerialized);
    }

    @Override
    protected void serializeNull(ByteBuf buffer) {
        buffer.writeDouble(0);
    }

    @Override
    protected Object deserialize(ByteBuf buffer) {
        return buffer.readDouble();
    }
}
