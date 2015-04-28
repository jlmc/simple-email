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
 * Any exception that is raised by the mail module extends from this runtime exception class,
 * making it easy for other modules
 * and extensions to catch all mail-related exceptions in a single catch block, if need be.
 */
public class MailException extends RuntimeException {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /**
     * Instantiates a new mail exception.
     */
    public MailException() {
        super();
    }

    /**
     * Instantiates a new mail exception.
     * @param message
     *            the message
     * @param cause
     *            the cause
     */
    public MailException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Instantiates a new mail exception.
     * @param message
     *            the message
     */
    public MailException(final String message) {
        super(message);
    }

    /**
     * Instantiates a new mail exception.
     * @param cause
     *            the cause
     */
    public MailException(final Throwable cause) {
        super(cause);
    }
}
