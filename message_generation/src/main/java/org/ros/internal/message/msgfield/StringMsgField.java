package org.ros.internal.message.msgfield;

import com.google.common.base.Preconditions;
import io.netty.buffer.ByteBuf;

import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;

/**
 * @author pavel.cernocky@artin.cz
 * @author pavel.erlebach@artin.cz
 */

public class StringMsgField extends AbstractMsgField {

    public static final Charset CHARSET = Charset.forName("UTF-8");

    public StringMsgField(Class<?> msgClass, String getterName, String setterName) {
        super(msgClass, getterName, setterName, String.class);
    }

    @Override
    protected void serialize(ByteBuf buffer, Object valueToBeSerialized) {
        Preconditions.checkArgument(valueToBeSerialized instanceof String);
        String typedValue = (String) valueToBeSerialized;
        byte[] bytes = typedValue.getBytes(CHARSET);
        buffer.writeInt(bytes.length);
        buffer.writeBytes(bytes);
    }

    @Override
    protected void serializeNull(ByteBuf buffer) {
        buffer.writeInt(0);
    }

    @Override
    protected Object deserialize(ByteBuf buffer) {
        int length = buffer.readInt();
        return buffer.readSlice(length).toString(CHARSET);
    }

}
