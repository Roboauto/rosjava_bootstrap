package org.ros.internal.message.msgfield;

import io.netty.buffer.ByteBuf;
import org.ros.message.Duration;

/**
 * @author pavel.cernocky@artin.cz
 */

public class DurationMsgField extends ObjectMsgField {

    public DurationMsgField(Class<?> msgClass, String setterName) {
        super(msgClass, setterName, Duration.class);
    }

    @Override
    protected Object deserialize(ByteBuf buffer) {
        return new Duration(buffer.readInt(), buffer.readInt());
    }

}
