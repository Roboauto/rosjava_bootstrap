package org.ros.internal.message.msgfield;

import com.google.common.base.Preconditions;
import io.netty.buffer.ByteBuf;
import org.ros.message.Time;

/**
 * @author pavel.cernocky@artin.cz
 * @author pavel.erlebach@artin.cz
 */

public class TimeMsgField extends AbstractMsgField {

    public TimeMsgField(Class<?> msgClass, String getterName, String setterName) {
        super(msgClass, getterName, setterName, Time.class);
    }

    @Override
    protected void serialize(ByteBuf buffer, Object valueToBeSerialized) {
        Preconditions.checkArgument(valueToBeSerialized instanceof Time);
        Time typedValue = (Time) valueToBeSerialized;
        buffer.writeInt(typedValue.secs);
        buffer.writeInt(typedValue.nsecs);
    }

    @Override
    protected void serializeNull(ByteBuf buffer) {
        buffer.writeInt(0).writeInt(0);
    }

    @Override
    protected Object deserialize(ByteBuf buffer) {
        return new Time(buffer.readInt(), buffer.readInt());
    }

}
