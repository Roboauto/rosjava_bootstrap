package org.ros.internal.message.msgfield;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import io.netty.buffer.ByteBuf;
import org.ros.message.Time;

import java.util.ArrayList;
import java.util.List;

/**
 * @author pavel.erlebach@artin.cz
 * @author pavel.erlebach@artin.cz
 */
public class TimeListMsgField extends AbstractListMsgField {

    public TimeListMsgField(Class<?> msgClass, String getterName, String setterName) {
        super(msgClass, getterName, setterName, List.class);
    }

    @Override
    protected void serialize(ByteBuf buffer, Object valueToBeSerialized) {
        List<Time> typedValues = (List) valueToBeSerialized;
        buffer.writeInt(typedValues.size());

        for (Time typedValue : typedValues) {
            buffer.writeInt(typedValue.secs);
            buffer.writeInt(typedValue.nsecs);
        }
    }

    @Override
    protected Object deserialize(ByteBuf buffer) {
        int length = buffer.readInt();
        List<Time> value = new ArrayList<>(length);
        for (int i = 0; i < length; i++) {
            value.add(new Time(buffer.readInt(), buffer.readInt()));
        }
        return value;
    }

}
