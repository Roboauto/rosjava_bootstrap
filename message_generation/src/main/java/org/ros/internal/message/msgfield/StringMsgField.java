package org.ros.internal.message.msgfield;

import io.netty.buffer.ByteBuf;

import java.nio.charset.Charset;

/**
 * @author pavel.cernocky@artin.cz
 */

public class StringMsgField extends ObjectMsgField {

    private static final Charset CHARSET = Charset.forName("UTF-8");

    public StringMsgField(Class<?> msgClass, String setterName) {
        super(msgClass, setterName, String.class);
    }

    @Override
    protected Object deserialize(ByteBuf buffer) {
        int length = buffer.readInt();
        return buffer.readSlice(length).toString(CHARSET);
    }

}
