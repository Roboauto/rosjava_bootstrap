package org.ros.internal.message.msgfield;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import io.netty.buffer.ByteBuf;
import org.ros.message.Duration;

import java.util.ArrayList;
import java.util.List;

/**
 * @author pavel.erlebach@artin.cz
 */
public class DurationListMsgField extends AbstractListMsgField {

    public DurationListMsgField(Class<?> msgClass, String getterName, String setterName) {
        super(msgClass, getterName, setterName, List.class);
    }

    @Override
    protected void serialize(ByteBuf buffer, Object valueToBeSerialized) {
        List<Duration> typedValues = (List<Duration>) valueToBeSerialized;
        buffer.writeInt(typedValues.size());

        for (Duration typedValue : typedValues) {
            buffer.writeInt(typedValue.secs);
            buffer.writeInt(typedValue.nsecs);
        }
    }

    @Override
    protected void serializeNull(ByteBuf buffer) {
        buffer.writeInt(0);
    }

    @Override
    protected Object deserialize(ByteBuf buffer) {
        int length = buffer.readInt();
        List<Duration> value = new ArrayList<>(length);
        for (int i = 0; i < length; i++) {
            value.add(new Duration(buffer.readInt(), buffer.readInt()));
        }
        return value;
    }

}
