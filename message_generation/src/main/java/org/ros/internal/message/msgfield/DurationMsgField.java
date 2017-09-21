package org.ros.internal.message.msgfield;

import com.google.common.base.Preconditions;
import io.netty.buffer.ByteBuf;
import org.ros.message.Duration;

import java.lang.reflect.InvocationTargetException;

/**
 * @author pavel.cernocky@artin.cz
 * @author pavel.erlebach@artin.cz
 */

public class DurationMsgField extends AbstractMsgField {

    public DurationMsgField(Class<?> msgClass, String getterName, String setterName) {
        super(msgClass, getterName, setterName, Duration.class);
    }

    @Override
    protected void serialize(ByteBuf buffer, Object valueToBeSerialized) {
        Preconditions.checkArgument(valueToBeSerialized instanceof Duration);
        Duration typedValue = (Duration) valueToBeSerialized;
        buffer.writeInt(typedValue.secs);
        buffer.writeInt(typedValue.nsecs);
    }

    @Override
    protected void serializeNull(ByteBuf buffer) {
        buffer.writeInt(0).writeInt(0);
    }

    @Override
    protected Object deserialize(ByteBuf buffer) {
        return new Duration(buffer.readInt(), buffer.readInt());
    }

}
