package org.ros.internal.message.msgfield;

import com.google.common.base.Preconditions;
import io.netty.buffer.ByteBuf;
import org.ros.message.Duration;

import java.util.ArrayList;
import java.util.List;

/**
 * @author pavel.erlebach@artin.cz
 */
public class DurationListMsgField extends ObjectMsgField {

    public DurationListMsgField(Class<?> msgClass, String getterName, String setterName) {
        super(msgClass, getterName, setterName, List.class);
    }

    @Override
    protected void serialize(ByteBuf buffer, Object value) {
        Preconditions.checkArgument(value instanceof List);
        List<Duration> typedValues = (List) value;
        buffer.writeInt(typedValues.size());

        for (Duration typedValue : typedValues) {
            buffer.writeInt(typedValue.secs);
            buffer.writeInt(typedValue.nsecs);
        }
    }

    @Override
    protected Object deserialize(ByteBuf buffer) {
        int length = buffer.readInt();
        List<Object> value = new ArrayList<>(length);
        for (int i = 0; i < length; i++) {
            value.add(new Duration(buffer.readInt(), buffer.readInt()));
        }
        return value;
    }

}
