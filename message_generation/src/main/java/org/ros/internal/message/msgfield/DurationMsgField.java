package org.ros.internal.message.msgfield;

import com.google.common.base.Preconditions;
import io.netty.buffer.ByteBuf;
import org.ros.message.Duration;

import java.lang.reflect.InvocationTargetException;

/**
 * @author pavel.cernocky@artin.cz
 */

public class DurationMsgField extends ObjectMsgField {

    public DurationMsgField(Class<?> msgClass, String getterName, String setterName) {
        super(msgClass, getterName, setterName, Duration.class);
    }

    @Override
    protected void serialize(ByteBuf buffer, Object value) {
        Preconditions.checkArgument(value instanceof Duration);
        Duration typedValue = (Duration) value;
        buffer.writeInt(typedValue.secs);
        buffer.writeInt(typedValue.nsecs);
    }

    @Override
    protected Object deserialize(ByteBuf buffer) {
        return new Duration(buffer.readInt(), buffer.readInt());
    }

}
