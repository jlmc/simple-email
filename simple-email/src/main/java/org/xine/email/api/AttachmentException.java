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
public class AttachmentException extends MailException {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /**
     * Instantiates a new attachment exception.
     */
    public AttachmentException() {
        super();
    }

    /**
     * Instantiates a new attachment exception.
     * @param message
     *            the message
     * @param cause
     *            the cause
     */
    public AttachmentException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Instantiates a new attachment exception.
     * @param message
     *            the message
     */
    public AttachmentException(final String message) {
        super(message);
    }

    /**
     * Instantiates a new attachment exception.
     * @param cause
     *            the cause
     */
    public AttachmentException(final Throwable cause) {
        super(cause);
    }
}
