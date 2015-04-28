/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.xine.email.api;

/**
 * Defines the available message receipt headers.
 */
public enum MailHeader {

    /** The delivery reciept. */
    DELIVERY_RECIEPT("Return-Receipt-To"),
    /** The read reciept. */
    READ_RECIEPT("Disposition-Notification-To");

    /** The header value. */
    private String headerValue;

    /**
     * Instantiates a new mail header.
     * @param headerValue
     *            the header value
     */
    private MailHeader(final String headerValue) {
        this.headerValue = headerValue;
    }

    /**
     * Header value.
     * @return the string
     */
    public String headerValue() {
        return this.headerValue;
    }
}
