package org.ros.internal.message.msgfield;

import com.google.common.base.Preconditions;
import io.netty.buffer.ByteBuf;
import org.ros.message.Time;

/**
 * @author pavel.cernocky@artin.cz
 */

public class TimeMsgField extends ObjectMsgField {

    public TimeMsgField(Class<?> msgClass, String getterName, String setterName) {
        super(msgClass, getterName, setterName, Time.class);
    }

    @Override
    protected void serialize(ByteBuf buffer, Object value) {
        Preconditions.checkArgument(value instanceof Time);
        Time typedValue = (Time) value;
        buffer.writeInt(typedValue.secs);
        buffer.writeInt(typedValue.nsecs);
    }

    @Override
    protected Object deserialize(ByteBuf buffer) {
        return new Time(buffer.readInt(), buffer.readInt());
    }

}
