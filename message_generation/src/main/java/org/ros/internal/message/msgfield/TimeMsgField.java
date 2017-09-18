package org.ros.internal.message.msgfield;

import io.netty.buffer.ByteBuf;
import org.ros.message.Time;

/**
 * @author pavel.cernocky@artin.cz
 */

public class TimeMsgField extends ObjectMsgField {

    public TimeMsgField(Class<?> msgClass, String setterName) {
        super(msgClass, setterName, Time.class);
    }

    @Override
    protected Object deserialize(ByteBuf buffer) {
        return new Time(buffer.readInt(), buffer.readInt());
    }

}
