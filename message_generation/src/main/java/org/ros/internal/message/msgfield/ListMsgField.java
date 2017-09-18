package org.ros.internal.message.msgfield;

import io.netty.buffer.ByteBuf;
import org.ros.internal.message.DefaultMessageDeserializer;
import org.ros.internal.message.RoboMessageDeserializer;
import org.ros.internal.message.RoboMessageImplClassProvider;
import org.ros.internal.message.field.FieldType;
import org.ros.message.MessageFactory;
import org.ros.message.MessageIdentifier;

import java.util.ArrayList;
import java.util.List;

/**
 * @author pavel.cernocky@artin.cz
 */

public class ListMsgField extends ObjectMsgField {

    private final RoboMessageDeserializer messageDeserializer;

    // factory method, because constructor can not handle try-catch around super()
    public static ListMsgField create(Class<?> msgClass, String setterName, FieldType componentFieldType, MessageFactory messageFactory, RoboMessageImplClassProvider messageImplClassProvider) {
        Class<?> componentClass;
        try {
            componentClass = Class.forName(componentFieldType.getJavaTypeName());
        }
        catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return new ListMsgField(msgClass, setterName, componentFieldType, componentClass, messageFactory, messageImplClassProvider);
    }

    private ListMsgField(Class<?> msgClass, String setterName, FieldType componentFieldType, Class<?> componentClass, MessageFactory messageFactory, RoboMessageImplClassProvider messageImplClassProvider) {
        super(msgClass, setterName, List.class);
        MessageIdentifier messageIdentifier = MessageIdentifier.of(componentFieldType.getName());
        messageDeserializer = new RoboMessageDeserializer(messageIdentifier, messageFactory, messageImplClassProvider);
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
