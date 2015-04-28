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
 * Thrown when a error occurs during template processing.
 */
public class TemplatingException extends MailException {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /**
     * Instantiates a new templating exception.
     */
    public TemplatingException() {
        super();
    }

    /**
     * Instantiates a new templating exception.
     * @param message
     *            the message
     * @param cause
     *            the cause
     */
    public TemplatingException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Instantiates a new templating exception.
     * @param message
     *            the message
     */
    public TemplatingException(final String message) {
        super(message);
    }

    /**
     * Instantiates a new templating exception.
     * @param cause
     *            the cause
     */
    public TemplatingException(final Throwable cause) {
        super(cause);
    }
}
