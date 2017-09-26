package org.ros.internal.message;

import io.netty.buffer.ByteBuf;
import org.ros.internal.message.msgfield.MsgField;
import org.ros.message.MessageDeserializer;
import org.ros.message.MessageFactory;
import org.ros.message.MessageIdentifier;

import java.nio.ByteOrder;
import java.util.List;

/**
 * @author pavel.cernocky@artin.cz
 */
public class FastMessageDeserializer<T> implements MessageDeserializer<T> {

    private final MessageIdentifier messageIdentifier;
    private final MessageFactory messageFactory;

    private final List<MsgField> msgFields;

    public FastMessageDeserializer(MessageIdentifier messageIdentifier, MessageFactory messageFactory, MessageClassAndFieldsProvider messageClassAndFieldsProvider) {
        this.messageIdentifier = messageIdentifier;
        this.messageFactory = messageFactory;
        this.msgFields = messageClassAndFieldsProvider.getMessageFields(messageIdentifier.getType(), messageFactory);
    }

    @SuppressWarnings("unchecked")
    @Override
    public T deserialize(ByteBuf buffer) {
        buffer = buffer.order(ByteOrder.LITTLE_ENDIAN);

        Object msg = messageFactory.newFromType(messageIdentifier.getType());
        for (MsgField msgField : msgFields) {
            msgField.setBufferValueToObject(msg, buffer);
        }

        return (T) msg;
    }
}
