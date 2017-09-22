package org.ros.internal.message.msgfield;

import io.netty.buffer.ByteBuf;
import org.ros.internal.message.MessageClassAndFieldsProvider;
import org.ros.internal.message.field.FieldType;
import org.ros.message.MessageFactory;
import org.ros.message.MessageIdentifier;

import java.util.List;

/**
 * @author pavel.cernocky@artin.cz
 * @author pavel.erlebach@artin.cz
 */

public class MessageMsgField extends AbstractMsgField {

    private final String messageType;
    private final MessageFactory messageFactory;
    private final List<MsgField> msgFields;

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
        this.messageType = MessageIdentifier.of(fieldType.getName()).getType();
        this.messageFactory = messageFactory;
        this.msgFields = messageClassAndFieldsProvider.getMessageFields(messageType, messageFactory);
    }

    @Override
    protected void serialize(ByteBuf buffer, Object valueToBeSerialized) {
        for (MsgField msgField : msgFields) {
            msgField.writeObjectFieldToBuffer(valueToBeSerialized, buffer);
        }
    }

    @Override
    protected void serializeNull(ByteBuf buffer) {
        for (MsgField msgField : msgFields) {
            msgField.writeObjectFieldToBuffer(null, buffer);
        }
    }

    @Override
    protected Object deserialize(ByteBuf buffer) {
        Object msg = messageFactory.newFromType(messageType);
        for (MsgField msgField : msgFields) {
            msgField.setBufferValueToObject(msg, buffer);
        }

        return msg;
    }
}
