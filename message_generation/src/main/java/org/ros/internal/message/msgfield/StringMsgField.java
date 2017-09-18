package org.ros.internal.message.msgfield;

import com.google.common.base.Preconditions;
import io.netty.buffer.ByteBuf;

import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;

/**
 * @author pavel.cernocky@artin.cz
 */

public class StringMsgField extends ObjectMsgField {

    public static final Charset CHARSET = Charset.forName("UTF-8");

    public StringMsgField(Class<?> msgClass, String getterName, String setterName) {
        super(msgClass, getterName, setterName, String.class);
    }

    @Override
    protected void serialize(ByteBuf buffer, Object value) {
        Preconditions.checkArgument(value instanceof String);
        String typedValue = (String) value;
        byte[] bytes = typedValue.getBytes(CHARSET);
        buffer.writeInt(bytes.length);
        buffer.writeBytes(bytes);
    }

    @Override
    protected Object deserialize(ByteBuf buffer) {
        int length = buffer.readInt();
        return buffer.readSlice(length).toString(CHARSET);
    }

}
