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
public class RoboMessageDeserializer<T> implements MessageDeserializer<T> {

    private final MessageIdentifier messageIdentifier;
    private final MessageFactory messageFactory;

    private final List<MsgField> msgFields;

    public RoboMessageDeserializer(MessageIdentifier messageIdentifier, MessageFactory messageFactory, RoboMessageImplClassProvider messageImplClassProvider) {
        this.messageIdentifier = messageIdentifier;
        this.messageFactory = messageFactory;
        this.msgFields = new RoboMsgFieldsFactory(messageFactory, messageImplClassProvider).createMsgFields(messageIdentifier);
    }

    @SuppressWarnings("unchecked")
    @Override
    public T deserialize(ByteBuf buffer) {
        buffer = buffer.order(ByteOrder.LITTLE_ENDIAN);

        Object msg = messageFactory.newFromType(messageIdentifier.getType());
        for (MsgField msgField : msgFields) {
            msgField.setValue(msg, buffer);
        }

        return (T) msg;
    }
}
