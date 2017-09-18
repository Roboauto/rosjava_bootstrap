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
 * @author damonkohler@google.com (Damon Kohler)
 * @author pavel.cernocky@artin.cz
 */
public class RoboMessageFactory implements MessageFactory {

  private final RoboMessageImplClassProvider messageImplClassProvider;

  public RoboMessageFactory(RoboMessageImplClassProvider messageImplClassProvider) {
    this.messageImplClassProvider = checkNotNull(messageImplClassProvider);
  }

  @Override
  public <T> T newFromType(String messageType) {
    try {
      return (T) messageImplClassProvider.getMessageImplClass(messageType).newInstance();
    }
    catch (InstantiationException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }
}
