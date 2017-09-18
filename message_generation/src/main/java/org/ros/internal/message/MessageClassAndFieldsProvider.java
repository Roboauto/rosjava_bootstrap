package org.ros.internal.message;

import org.ros.internal.message.msgfield.MsgField;
import org.ros.message.MessageFactory;

import java.util.List;

/**
 * @author pavel.erlebach@artin.cz
 */
public interface MessageClassAndFieldsProvider {
    Class<?> getMessageClass(String messageType, MessageFactory messageFactory);

    List<MsgField> getMessageFields(String messageType, MessageFactory messageFactory);
}
