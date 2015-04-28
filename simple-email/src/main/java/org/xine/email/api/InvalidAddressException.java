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
 * Thrown when an email address fails to validate as RFC822.
 */
public class InvalidAddressException extends MailException {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /**
     * Instantiates a new invalid address exception.
     */
    public InvalidAddressException() {
        super();
    }

    /**
     * Instantiates a new invalid address exception.
     * @param message
     *            the message
     * @param cause
     *            the cause
     */
    public InvalidAddressException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Instantiates a new invalid address exception.
     * @param message
     *            the message
     */
    public InvalidAddressException(final String message) {
        super(message);
    }

    /**
     * Instantiates a new invalid address exception.
     * @param cause
     *            the cause
     */
    public InvalidAddressException(final Throwable cause) {
        super(cause);
    }
}
