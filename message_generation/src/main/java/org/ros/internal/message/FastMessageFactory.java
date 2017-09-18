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

import org.ros.message.MessageFactory;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author pavel.cernocky@artin.cz
 */
public class FastMessageFactory implements MessageFactory {

  private final MessageClassAndFieldsProvider messageClassAndFieldsProvider;

  public FastMessageFactory(MessageClassAndFieldsProvider messageClassAndFieldsProvider) {
    this.messageClassAndFieldsProvider = checkNotNull(messageClassAndFieldsProvider);
  }

  @Override
  public <T> T newFromType(String messageType) {
    try {
      return (T) messageClassAndFieldsProvider.getMessageClass(messageType, this).newInstance();
    }
    catch (InstantiationException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }
}