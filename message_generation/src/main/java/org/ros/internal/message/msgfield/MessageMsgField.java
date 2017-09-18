package org.ros.internal.message.msgfield;

import io.netty.buffer.ByteBuf;
import org.ros.internal.message.DefaultMessageDeserializer;
import org.ros.internal.message.RoboMessageDeserializer;
import org.ros.internal.message.RoboMessageImplClassProvider;
import org.ros.internal.message.field.FieldType;
import org.ros.message.MessageDeserializer;
import org.ros.message.MessageFactory;
import org.ros.message.MessageIdentifier;

/**
 * @author pavel.cernocky@artin.cz
 */

public class MessageMsgField extends ObjectMsgField {

    private final RoboMessageDeserializer messageDeserializer;

    // factory method, because constructor can not handle try-catch around super()
    public static MessageMsgField create(Class<?> msgClass, String setterName, FieldType fieldType, MessageFactory messageFactory, RoboMessageImplClassProvider messageImplClassProvider) {
        Class<?> fieldClass;
        try {
            fieldClass = Class.forName(fieldType.getJavaTypeName());
        }
        catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return new MessageMsgField(msgClass, setterName, fieldType, fieldClass, messageFactory, messageImplClassProvider);
    }

    private MessageMsgField(Class<?> msgClass, String setterName, FieldType fieldType, Class<?> fieldClass, MessageFactory messageFactory, RoboMessageImplClassProvider messageImplClassProvider) {
        super(msgClass, setterName, fieldClass);
        MessageIdentifier messageIdentifier = MessageIdentifier.of(fieldType.getName());
        messageDeserializer = new RoboMessageDeserializer(messageIdentifier, messageFactory, messageImplClassProvider);
    }

    @Override
    protected Object deserialize(ByteBuf buffer) {
        return messageDeserializer.deserialize(buffer);
    }

}
