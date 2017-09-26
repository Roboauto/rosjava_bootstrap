package org.ros.internal.message.msgfield;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.List;

/**
 * @author pavel.erlebach@artin.cz
 */
public class StringListMsgField extends AbstractListMsgField {

    public StringListMsgField(Class<?> msgClass, String getterName, String setterName) {
        super(msgClass, getterName, setterName, List.class);
    }

    @Override
    protected void serialize(ByteBuf buffer, Object valueToBeSerialized) {
        List<String> typedValues = (List) valueToBeSerialized;
        buffer.writeInt(typedValues.size());

        for (String typedValue : typedValues) {
            byte[] bytes = typedValue.getBytes(StringMsgField.CHARSET);
            buffer.writeInt(bytes.length);
            buffer.writeBytes(bytes);
        }
    }

    @Override
    protected Object deserialize(ByteBuf buffer) {
        int length = buffer.readInt();
        List<String> value = new ArrayList<>(length);
        for (int i = 0; i < length; i++) {
            int stringLength = buffer.readInt();
            value.add(buffer.readSlice(stringLength).toString(StringMsgField.CHARSET));
        }
        return value;
    }

}
