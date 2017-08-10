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

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import com.squareup.javapoet.ArrayTypeName;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import io.netty.buffer.ByteBuf;
import org.apache.commons.lang.StringEscapeUtils;
import org.ros.exception.RosMessageRuntimeException;
import org.ros.internal.message.context.MessageContext;
import org.ros.internal.message.context.MessageContextProvider;
import org.ros.internal.message.field.BooleanArrayField;
import org.ros.internal.message.field.ByteBufField;
import org.ros.internal.message.field.DoubleArrayField;
import org.ros.internal.message.field.Field;
import org.ros.internal.message.field.FieldType;
import org.ros.internal.message.field.FloatArrayField;
import org.ros.internal.message.field.IntegerArrayField;
import org.ros.internal.message.field.ListField;
import org.ros.internal.message.field.LongArrayField;
import org.ros.internal.message.field.MessageFields;
import org.ros.internal.message.field.PrimitiveFieldType;
import org.ros.internal.message.field.ShortArrayField;
import org.ros.message.MessageDeclaration;
import org.ros.message.MessageFactory;

import javax.lang.model.element.Modifier;
import java.util.List;
import java.util.Set;

/**
 * @author damonkohler@google.com (Damon Kohler)
 */
public class MessageInterfaceBuilder {

  private MessageDeclaration messageDeclaration;
  private String packageName;
  private String interfaceName;
  private boolean addConstantsAndMethods;
  private String nestedContent;

  // TODO(damonkohler): Upgrade Apache Commons Lang. See
  // https://issues.apache.org/jira/browse/LANG-437
  private static String escapeJava(String str) {
    return StringEscapeUtils.escapeJava(str).replace("\\/", "/").replace("'", "\\'");
  }

  public MessageDeclaration getMessageDeclaration() {
    return messageDeclaration;
  }

  public MessageInterfaceBuilder setMessageDeclaration(MessageDeclaration messageDeclaration) {
    Preconditions.checkNotNull(messageDeclaration);
    this.messageDeclaration = messageDeclaration;
    return this;
  }

  public String getPackageName() {
    return packageName;
  }

  /**
   * @param packageName
   *          the package name of the interface or {@code null} if no package
   *          name should be specified
   * @return this {@link MessageInterfaceBuilder}
   */
  public MessageInterfaceBuilder setPackageName(String packageName) {
    this.packageName = packageName;
    return this;
  }

  public String getInterfaceName() {
    return interfaceName;
  }

  public MessageInterfaceBuilder setInterfaceName(String interfaceName) {
    Preconditions.checkNotNull(interfaceName);
    this.interfaceName = interfaceName;
    return this;
  }

  public boolean getAddConstantsAndMethods() {
    return addConstantsAndMethods;
  }

  public void setAddConstantsAndMethods(boolean enabled) {
    addConstantsAndMethods = enabled;
  }

  public String getNestedContent() {
    return nestedContent;
  }

  public void setNestedContent(String nestedContent) {
    this.nestedContent = nestedContent;
  }

  public String build(MessageFactory messageFactory) {
    Preconditions.checkNotNull(messageDeclaration);
    Preconditions.checkNotNull(interfaceName);
    StringBuilder builder = new StringBuilder();
    if (packageName != null) {
      builder.append(String.format("package %s;\n\n", packageName));
    }
    builder.append(String.format(
        "public interface %s extends org.ros.internal.message.Message {\n", interfaceName));
    builder.append(String.format("  static final java.lang.String _TYPE = \"%s\";\n",
        messageDeclaration.getType()));
    builder.append(String.format("  static final java.lang.String _DEFINITION = \"%s\";\n",
        escapeJava(messageDeclaration.getDefinition())));
    if (addConstantsAndMethods) {
      MessageContextProvider messageContextProvider = new MessageContextProvider(messageFactory);
      MessageContext messageContext = messageContextProvider.get(messageDeclaration);
      appendConstants(messageContext, builder);
      appendSettersAndGetters(messageContext, builder);
    }
    if (nestedContent != null) {
      builder.append("\n");
      builder.append(nestedContent);
    }
    builder.append("}\n");
    return builder.toString();
  }

  @SuppressWarnings("deprecation")
  private String getJavaValue(PrimitiveFieldType primitiveFieldType, String value) {
    switch (primitiveFieldType) {
      case BOOL:
        return Boolean.valueOf(!value.equals("0") && !value.equals("false")).toString();
      case FLOAT32:
        return value + "f";
      case STRING:
        return "\"" + escapeJava(value) + "\"";
      case BYTE:
      case CHAR:
      case INT8:
      case UINT8:
      case INT16:
      case UINT16:
      case INT32:
      case UINT32:
      case INT64:
      case UINT64:
      case FLOAT64:
        return value;
      default:
        throw new RosMessageRuntimeException("Unsupported PrimitiveFieldType: " + primitiveFieldType);
    }
  }

  private void appendConstants(MessageContext messageContext, StringBuilder builder) {
    MessageFields messageFields = new MessageFields(messageContext);
    for (Field field : messageFields.getFields()) {
      if (field.isConstant()) {
        Preconditions.checkState(field.getType() instanceof PrimitiveFieldType);
        // We use FieldType and cast back to PrimitiveFieldType below to avoid a
        // bug in the Sun JDK: http://gs.sun.com/view_bug.do?bug_id=6522780
        FieldType fieldType = (FieldType) field.getType();
        String value = getJavaValue((PrimitiveFieldType) fieldType, field.getValue().toString());
        builder.append(String.format("  static final %s %s = %s;\n", fieldType.getJavaTypeName(),
            field.getName(), value));
      }
    }
  }

  private void appendSettersAndGetters(MessageContext messageContext, StringBuilder builder) {
    MessageFields messageFields = new MessageFields(messageContext);
    Set<String> getters = Sets.newHashSet();
    for (Field field : messageFields.getFields()) {
      if (field.isConstant()) {
        continue;
      }
      String type = field.getJavaTypeName();
      String getter = messageContext.getFieldGetterName(field.getName());
      String setter = messageContext.getFieldSetterName(field.getName());
      if (getters.contains(getter)) {
        // In the case that two or more message fields have the same name except
        // for capitalization, we only generate a getter and setter pair for the
        // first one. The following fields will only be accessible via the
        // RawMessage interface.
        continue;
      }
      getters.add(getter);
      builder.append(String.format("  %s %s();\n", type, getter));
      builder.append(String.format("  void %s(%s value);\n", setter, type));
    }
  }

  public String buildClass(MessageFactory messageFactory) {
    Preconditions.checkNotNull(messageDeclaration);
    Preconditions.checkNotNull(interfaceName);

    TypeSpec.Builder classBuilder = TypeSpec.classBuilder(interfaceName + "Impl")
      .addModifiers(Modifier.PUBLIC)
      .addSuperinterface(ClassName.get(packageName, interfaceName));

    MessageContextProvider messageContextProvider = new MessageContextProvider(messageFactory);
    MessageContext messageContext = messageContextProvider.get(messageDeclaration);

    MessageFields messageFields = new MessageFields(messageContext);
    Set<String> getters = Sets.newHashSet();
    for (Field field : messageFields.getFields()) {
      if (field.isConstant()) {
        continue;
      }
      String getter = messageContext.getFieldGetterName(field.getName());
      String setter = messageContext.getFieldSetterName(field.getName());
      if (getters.contains(getter)) {
        // In the case that two or more message fields have the same name except
        // for capitalization, we only generate a getter and setter pair for the
        // first one. The following fields will only be accessible via the
        // RawMessage interface.
        continue;
      }
      getters.add(getter);

      TypeName fieldType = getFieldTypeName(field);
      FieldSpec fieldSpec = FieldSpec.builder(fieldType, field.getName(), Modifier.PRIVATE).build();
      classBuilder.addField(fieldSpec);
      classBuilder.addMethod(
        MethodSpec.methodBuilder(getter)
          .addModifiers(Modifier.PUBLIC)
          .addAnnotation(Override.class)
          .returns(fieldType)
          .addStatement("return $N", fieldSpec)
          .build()
      );

      ParameterSpec paramSpec;
      classBuilder.addMethod(
        MethodSpec.methodBuilder(setter)
          .addModifiers(Modifier.PUBLIC)
          .addAnnotation(Override.class)
          .addParameter(paramSpec = ParameterSpec.builder(fieldType, field.getName()).build())
          .addStatement("this.$N = $N", fieldSpec, paramSpec)
          .build()
      );
    }

    classBuilder.addMethod(
      MethodSpec.methodBuilder("toRawMessage")
        .addModifiers(Modifier.PUBLIC)
        .addAnnotation(Override.class)
        .returns(ClassName.get(RawMessage.class))
        .addStatement("throw new $T()", UnsupportedOperationException.class)
        .build()
    );

    JavaFile javaFile = JavaFile.builder(packageName, classBuilder.build()).build();
    return javaFile.toString();
  }

  private TypeName getFieldTypeName(Field field) {
    if (field instanceof BooleanArrayField) {
      return ArrayTypeName.of(boolean.class);
    }
    if (field instanceof ByteBufField) {
      return ClassName.get(ByteBuf.class);
    }
    if (field instanceof ShortArrayField) {
      return ArrayTypeName.of(short.class);
    }
    if (field instanceof IntegerArrayField) {
      return ArrayTypeName.of(int.class);
    }
    if (field instanceof LongArrayField) {
      return ArrayTypeName.of(long.class);
    }
    if (field instanceof FloatArrayField) {
      return ArrayTypeName.of(float.class);
    }
    if (field instanceof DoubleArrayField) {
      return ArrayTypeName.of(double.class);
    }

    String javaTypeName = field.getType().getJavaTypeName();

    if (field instanceof ListField) {
      return ParameterizedTypeName.get(ClassName.get(List.class), ClassName.bestGuess(javaTypeName));
    }

    if (javaTypeName.equals("float")) {
      return TypeName.FLOAT;
    }
    if (javaTypeName.equals("long")) {
      return TypeName.LONG;
    }
    if (javaTypeName.equals("boolean")) {
      return TypeName.BOOLEAN;
    }
    if (javaTypeName.equals("double")) {
      return TypeName.DOUBLE;
    }
    if (javaTypeName.equals("short")) {
      return TypeName.SHORT;
    }
    if (javaTypeName.equals("byte")) {
      return TypeName.BYTE;
    }
    if (javaTypeName.equals("int")) {
      return TypeName.INT;
    }
    return ClassName.bestGuess(javaTypeName);
  }

}
