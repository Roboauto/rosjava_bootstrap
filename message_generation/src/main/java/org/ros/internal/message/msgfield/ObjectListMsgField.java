package org.ros.internal.message.msgfield;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import io.netty.buffer.ByteBuf;
import org.ros.internal.message.FastMessageDeserializer;
import org.ros.internal.message.FastMessageSerializer;
import org.ros.internal.message.Message;
import org.ros.internal.message.MessageClassAndFieldsProvider;
import org.ros.internal.message.field.FieldType;
import org.ros.message.MessageFactory;
import org.ros.message.MessageIdentifier;

import java.util.ArrayList;
import java.util.List;

/**
 * @author pavel.cernocky@artin.cz
 * @author pavel.erlebach@artin.cz
 */

public class ObjectListMsgField extends AbstractListMsgField {

    private final String messageType;
    private final MessageFactory messageFactory;
    private final List<MsgField> msgFields;

    public ObjectListMsgField(Class<?> msgClass, String getterName, String setterName, FieldType componentFieldType, MessageFactory messageFactory, MessageClassAndFieldsProvider messageClassAndFieldsProvider) {
        super(msgClass, getterName, setterName, List.class);
        this.messageType = MessageIdentifier.of(componentFieldType.getName()).getType();
        this.messageFactory = messageFactory;
        this.msgFields = messageClassAndFieldsProvider.getMessageFields(messageType, messageFactory);
    }

    @Override
    protected void serialize(ByteBuf buffer, Object valueToBeSerialized) {
        Preconditions.checkArgument(valueToBeSerialized instanceof List);
        List<Object> typedValues = (List) valueToBeSerialized;
        buffer.writeInt(typedValues.size());

        for (Object objectValue : typedValues) {
            for (MsgField msgField : msgFields) {
                msgField.writeObjectValueToBuffer(objectValue, buffer);
            }
        }
    }

    @Override
    protected Object deserialize(ByteBuf buffer) {
        int length = buffer.readInt();
        List<Object> value = new ArrayList<>(length);
        for (int i = 0; i < length; i++) {
            Object msg = messageFactory.newFromType(messageType);
            for (MsgField msgField : msgFields) {
                msgField.setBufferValueToObject(msg, buffer);
            }
            value.add(msg);
        }
        return value;
    }

}
