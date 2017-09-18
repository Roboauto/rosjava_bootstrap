package org.ros.internal.message.msgfield;

import io.netty.buffer.ByteBuf;
import org.ros.internal.message.FastMessageDeserializer;
import org.ros.internal.message.FastMessageSerializer;
import org.ros.internal.message.Message;
import org.ros.internal.message.MessageClassAndFieldsProvider;
import org.ros.internal.message.field.FieldType;
import org.ros.message.MessageFactory;
import org.ros.message.MessageIdentifier;

/**
 * @author pavel.cernocky@artin.cz
 */

public class MessageMsgField extends ObjectMsgField {

    private final FastMessageSerializer messageSerializer;
    private final FastMessageDeserializer messageDeserializer;

    // factory method, because constructor can not handle try-catch around super()
    public static MessageMsgField create(Class<?> msgClass, String getterName, String setterName, FieldType fieldType, MessageFactory messageFactory, MessageClassAndFieldsProvider messageClassAndFieldsProvider) {
        Class<?> fieldClass;
        try {
            fieldClass = Class.forName(fieldType.getJavaTypeName());
        }
        catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return new MessageMsgField(msgClass, getterName, setterName, fieldType, fieldClass, messageFactory, messageClassAndFieldsProvider);
    }

    private MessageMsgField(Class<?> msgClass, String getterName, String setterName, FieldType fieldType, Class<?> fieldClass, MessageFactory messageFactory, MessageClassAndFieldsProvider messageClassAndFieldsProvider) {
        super(msgClass, getterName, setterName, fieldClass);
        MessageIdentifier messageIdentifier = MessageIdentifier.of(fieldType.getName());
        messageSerializer = new FastMessageSerializer(messageIdentifier, messageFactory, messageClassAndFieldsProvider);
        messageDeserializer = new FastMessageDeserializer(messageIdentifier, messageFactory, messageClassAndFieldsProvider);
    }

    @Override
    protected void serialize(ByteBuf buffer, Object value) {
        messageSerializer.serialize((Message) value, buffer);
    }

    @Override
    protected Object deserialize(ByteBuf buffer) {
        return messageDeserializer.deserialize(buffer);
    }

}
