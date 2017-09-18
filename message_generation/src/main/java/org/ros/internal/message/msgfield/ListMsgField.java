package org.ros.internal.message.msgfield;

import com.google.common.base.Preconditions;
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
 */

public class ListMsgField extends ObjectMsgField {

    private final FastMessageSerializer messageSerializer;
    private final FastMessageDeserializer messageDeserializer;

    public ListMsgField(Class<?> msgClass, String getterName, String setterName, FieldType componentFieldType, MessageFactory messageFactory, MessageClassAndFieldsProvider messageClassAndFieldsProvider) {
        super(msgClass, getterName, setterName, List.class);

        MessageIdentifier messageIdentifier = MessageIdentifier.of(componentFieldType.getName());
        messageSerializer = new FastMessageSerializer(messageIdentifier, messageFactory, messageClassAndFieldsProvider);
        messageDeserializer = new FastMessageDeserializer(messageIdentifier, messageFactory, messageClassAndFieldsProvider);
    }

    @Override
    protected void serialize(ByteBuf buffer, Object value) {
        Preconditions.checkArgument(value instanceof List);
        List<Object> typedValues = (List) value;
        buffer.writeInt(typedValues.size());

        for (Object objectValue : typedValues) {
            messageSerializer.serialize((Message) objectValue, buffer);
        }
    }

    @Override
    protected Object deserialize(ByteBuf buffer) {
        int length = buffer.readInt();
        List<Object> value = new ArrayList<>(length);
        for (int i = 0; i < length; i++) {
            value.add(messageDeserializer.deserialize(buffer));
        }
        return value;
    }

}
