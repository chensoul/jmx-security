/*
 * Copyright (c) 2008-2017 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.jmix.security.user.event;

import org.springframework.context.ApplicationEvent;

/**
 * This event is used to notify listeners about user invalidation: when the user has been deleted or disabled.
 */
public abstract class AbstractUserInvalidationEvent extends ApplicationEvent {
    private static final long serialVersionUID = 1024128494761069121L;

    public AbstractUserInvalidationEvent(String username) {
        super(username);
    }

    /**
     * @return username to invalidate
     */
    public String getUsername() {
        return (String) getSource();
    }
}
