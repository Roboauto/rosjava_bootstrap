package org.ros.internal.message;

import org.ros.internal.message.context.MessageContext;
import org.ros.internal.message.context.MessageContextProvider;
import org.ros.internal.message.definition.MessageDefinitionReflectionProvider;
import org.ros.internal.message.field.*;
import org.ros.internal.message.msgfield.*;
import org.ros.message.MessageDeclaration;
import org.ros.message.MessageDefinitionProvider;
import org.ros.message.MessageFactory;
import org.ros.message.MessageIdentifier;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author pavel.cernocky@artin.cz
 */
public class RoboMsgFieldsFactory {
    private final MessageFactory messageFactory;
    private final RoboMessageImplClassProvider messageImplClassProvider;

    public RoboMsgFieldsFactory(MessageFactory messageFactory, RoboMessageImplClassProvider messageImplClassProvider) {
        this.messageFactory = checkNotNull(messageFactory);
        this.messageImplClassProvider = checkNotNull(messageImplClassProvider);
    }

    public List<MsgField> createMsgFields(MessageIdentifier messageIdentifier) {
        String messageType = messageIdentifier.getType();
        Class<?> msgClass = messageImplClassProvider.getMessageImplClass(messageType);

        MessageContextProvider messageContextProvider = new MessageContextProvider(messageFactory);

        MessageDefinitionProvider messageDefinitionProvider = new MessageDefinitionReflectionProvider();
        String messageDefinition = messageDefinitionProvider.get(messageType);
        MessageDeclaration messageDeclaration = MessageDeclaration.of(messageType, messageDefinition);

        List<MsgField> msgFields = new ArrayList<>();

        MessageContext messageContext = messageContextProvider.get(messageDeclaration);
        for (String fieldName : messageContext.getFieldNames()) {
            FieldFactory fieldFactory = messageContext.getFieldFactory(fieldName);
            Field field = fieldFactory.create();

            if (field.isConstant()) {
                continue;
            }

            String setterName = messageContext.getFieldSetterName(fieldName);

            msgFields.add(createField(msgClass, field, setterName));
        }

        return msgFields;
    }

    private MsgField createField(Class<?> msgClass, Field field, String setterName) {
        FieldType fieldType = field.getType();

        if (field instanceof BooleanArrayField) {
            return new BooleanArrayMsgField(msgClass, setterName, getArrayFieldSize(field));
        }
        if (field instanceof ByteBufField) {
            return new ByteBufMsgField(msgClass, setterName, getArrayFieldSize(field));
        }
        if (field instanceof ShortArrayField) {
            return new ShortArrayMsgField(msgClass, setterName, getArrayFieldSize(field));
        }
        if (field instanceof IntegerArrayField) {
            return new IntegerArrayMsgField(msgClass, setterName, getArrayFieldSize(field));
        }
        if (field instanceof LongArrayField) {
            return new LongArrayMsgField(msgClass, setterName, getArrayFieldSize(field));
        }
        if (field instanceof FloatArrayField) {
            return new FloatArrayMsgField(msgClass, setterName, getArrayFieldSize(field));
        }
        if (field instanceof DoubleArrayField) {
            return new DoubleArrayMsgField(msgClass, setterName, getArrayFieldSize(field));
        }
        if (field instanceof ListField) {
            return ListMsgField.create(msgClass, setterName, fieldType, messageFactory, messageImplClassProvider);
        }

        // field  should be instanceof ValueField (which is package-private, so we can not user instanceof)
        if (fieldType instanceof PrimitiveFieldType) {
            switch ((PrimitiveFieldType) fieldType) {
                case BOOL:
                    return new BooleanMsgField(msgClass, setterName);
                case INT8:
                case UINT8:
                case BYTE:
                case CHAR:
                    return new ByteMsgField(msgClass, setterName);
                case INT16:
                case UINT16:
                    return new ShortMsgField(msgClass, setterName);
                case INT32:
                case UINT32:
                    return new IntegerMsgField(msgClass, setterName);
                case INT64:
                case UINT64:
                    return new LongMsgField(msgClass, setterName);
                case FLOAT32:
                    return new FloatMsgField(msgClass, setterName);
                case FLOAT64:
                    return new DoubleMsgField(msgClass, setterName);
                case STRING:
                    return new StringMsgField(msgClass, setterName);
                case TIME:
                    return new TimeMsgField(msgClass, setterName);
                case DURATION:
                    return new DurationMsgField(msgClass, setterName);
                default:
                    throw new IllegalStateException("Unknown field type: " + fieldType);
            }
        }

        if (fieldType instanceof MessageFieldType) {
            return MessageMsgField.create(msgClass, setterName, fieldType, messageFactory, messageImplClassProvider);
        }

        throw new IllegalStateException("Can not handle field " + field);
    }

    private int getArrayFieldSize(Field field) {
        try {
            java.lang.reflect.Field sizeField = field.getClass().getDeclaredField("size");
            sizeField.setAccessible(true);
            return sizeField.getInt(field);
        }
        catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
