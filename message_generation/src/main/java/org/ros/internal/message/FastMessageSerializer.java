package org.ros.internal.message;

import io.netty.buffer.ByteBuf;
import org.ros.internal.message.msgfield.MsgField;
import org.ros.message.MessageFactory;
import org.ros.message.MessageIdentifier;
import org.ros.message.MessageSerializer;

import java.nio.ByteOrder;
import java.util.List;

/**
 * @author pavel.erlebach@artin.cz
 */
public class FastMessageSerializer implements MessageSerializer<Message> {

    private final List<MsgField> msgFields;

    public FastMessageSerializer(MessageIdentifier messageIdentifier, MessageFactory messageFactory, MessageClassAndFieldsProvider messageClassAndFieldsProvider) {
        this.msgFields = messageClassAndFieldsProvider.getMessageFields(messageIdentifier.getType(), messageFactory);
    }

    @Override
    public void serialize(Message message, ByteBuf buffer) {
        buffer = buffer.order(ByteOrder.LITTLE_ENDIAN);

        for (MsgField msgField : msgFields) {
            msgField.writeObjectFieldToBuffer(message, buffer);
        }
    }
}
