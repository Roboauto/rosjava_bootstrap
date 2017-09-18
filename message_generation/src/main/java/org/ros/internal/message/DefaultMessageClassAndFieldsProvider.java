/*
 * Copyright (C) 2011 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package org.ros.internal.message;

import com.google.common.collect.ImmutableMap;
import org.ros.internal.message.context.MessageContext;
import org.ros.internal.message.context.MessageContextProvider;
import org.ros.internal.message.definition.MessageDefinitionReflectionProvider;
import org.ros.internal.message.field.*;
import org.ros.internal.message.msgfield.*;
import org.ros.message.MessageDeclaration;
import org.ros.message.MessageDefinitionProvider;
import org.ros.message.MessageFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author pavel.cernocky@artin.cz
 */
public class DefaultMessageClassAndFieldsProvider implements MessageClassAndFieldsProvider {

  private volatile ImmutableMap<String, ClassAndFields> cache = ImmutableMap.of();

  @Override
  public Class<?> getMessageClass(String messageType, MessageFactory messageFactory) {
    return getOrCreate(messageType, messageFactory).clazz;
  }

  @Override
  public List<MsgField> getMessageFields(String messageType, MessageFactory messageFactory) {
    return getOrCreate(messageType, messageFactory).fields;
  }

  private ClassAndFields getOrCreate(String messageType, MessageFactory messageFactory) {
    ClassAndFields result = cache.get(messageType);

    if (result == null) {
      synchronized (this) {
        result = cache.get(messageType);
        if (result == null) {
          Class<?> msgClass = getUncachedClass(messageType);
          List<MsgField> fields = getUncachedFields(messageType, messageFactory, msgClass);
          result = new ClassAndFields(msgClass, fields);
          cache = ImmutableMap.<String, ClassAndFields>builder()
                  .putAll(cache)
                  .put(messageType, result)
                  .build();
        }
      }
    }

    return result;
  }

  private Class<?> getUncachedClass(String messageType) {
    try {
      String className = messageType.replace("/", ".") + "Impl";
      return Class.forName(className);
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  private List<MsgField> getUncachedFields(String messageType, MessageFactory messageFactory, Class<?> msgClass) {
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

      String getterName = messageContext.getFieldGetterName(fieldName);
      String setterName = messageContext.getFieldSetterName(fieldName);

      msgFields.add(createField(msgClass, field, getterName, setterName, messageFactory));
    }

    return msgFields;
  }

  private MsgField createField(Class<?> msgClass, Field field, String getterName, String setterName, MessageFactory messageFactory) {
    FieldType fieldType = field.getType();

    if (field instanceof BooleanArrayField) {
      return new BooleanArrayMsgField(msgClass, getterName, setterName, getArrayFieldSize(field));
    }
    if (field instanceof ByteBufField) {
      return new ByteBufMsgField(msgClass, getterName, setterName, getArrayFieldSize(field));
    }
    if (field instanceof ShortArrayField) {
      return new ShortArrayMsgField(msgClass, getterName, setterName, getArrayFieldSize(field));
    }
    if (field instanceof IntegerArrayField) {
      return new IntegerArrayMsgField(msgClass, getterName, setterName, getArrayFieldSize(field));
    }
    if (field instanceof LongArrayField) {
      return new LongArrayMsgField(msgClass, getterName, setterName, getArrayFieldSize(field));
    }
    if (field instanceof FloatArrayField) {
      return new FloatArrayMsgField(msgClass, getterName, setterName, getArrayFieldSize(field));
    }
    if (field instanceof DoubleArrayField) {
      return new DoubleArrayMsgField(msgClass, getterName, setterName, getArrayFieldSize(field));
    }
    if (field instanceof ListField) {
      if (fieldType instanceof PrimitiveFieldType) {
        switch ((PrimitiveFieldType) fieldType) {
          case DURATION:
            return new DurationListMsgField(msgClass, getterName, setterName);
          case STRING:
            return new StringListMsgField(msgClass, getterName, setterName);
          case TIME:
            return new TimeListMsgField(msgClass, getterName, setterName);
          default:
            throw new IllegalStateException("Unknown list field type: " + fieldType);
        }
      } else {
        return new ListMsgField(msgClass, getterName, setterName, fieldType, messageFactory, this);
      }
    }

    // field  should be instanceof ValueField (which is package-private, so we can not user instanceof)
    if (fieldType instanceof PrimitiveFieldType) {
      switch ((PrimitiveFieldType) fieldType) {
        case BOOL:
          return new BooleanMsgField(msgClass, getterName, setterName);
        case INT8:
        case UINT8:
        case BYTE:
        case CHAR:
          return new ByteMsgField(msgClass, getterName, setterName);
        case INT16:
        case UINT16:
          return new ShortMsgField(msgClass, getterName, setterName);
        case INT32:
        case UINT32:
          return new IntegerMsgField(msgClass, getterName, setterName);
        case INT64:
        case UINT64:
          return new LongMsgField(msgClass, getterName, setterName);
        case FLOAT32:
          return new FloatMsgField(msgClass, getterName, setterName);
        case FLOAT64:
          return new DoubleMsgField(msgClass, getterName, setterName);
        case STRING:
          return new StringMsgField(msgClass, getterName, setterName);
        case TIME:
          return new TimeMsgField(msgClass, getterName, setterName);
        case DURATION:
          return new DurationMsgField(msgClass, getterName, setterName);
        default:
          throw new IllegalStateException("Unknown field type: " + fieldType);
      }
    }

    if (fieldType instanceof MessageFieldType) {
      return MessageMsgField.create(msgClass, getterName, setterName, fieldType, messageFactory, this);
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


  private class ClassAndFields {
    final Class<?> clazz;
    final List<MsgField> fields;

    ClassAndFields(Class<?> clazz, List<MsgField> fields) {
      this.clazz = clazz;
      this.fields = fields;
    }
  }
}
