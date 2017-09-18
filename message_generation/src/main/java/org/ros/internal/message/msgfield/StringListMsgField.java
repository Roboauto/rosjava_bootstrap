package org.ros.internal.message.msgfield;

import com.google.common.base.Preconditions;
import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.List;

/**
 * @author pavel.erlebach@artin.cz
 */
public class StringListMsgField extends ObjectMsgField {

    public StringListMsgField(Class<?> msgClass, String getterName, String setterName) {
        super(msgClass, getterName, setterName, List.class);
    }

    @Override
    protected void serialize(ByteBuf buffer, Object value) {
        Preconditions.checkArgument(value instanceof List);
        List<String> typedValues = (List) value;
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
        List<Object> value = new ArrayList<>(length);
        for (int i = 0; i < length; i++) {
            int stringLength = buffer.readInt();
            value.add(buffer.readSlice(stringLength).toString(StringMsgField.CHARSET));
        }
        return value;
    }

}
