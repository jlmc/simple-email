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

package org.xine.email.impl;

import com.sun.mail.smtp.SMTPMessage;

import java.io.InputStream;

import javax.mail.Header;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

/**
 * Extends {@link MimeMessage} to allow for the setting of the Message-ID.
 */
public class RootMimeMessage extends SMTPMessage {

    /** The message id. */
    private String messageId;

    /**
     * Instantiates a new root mime message.
     * @param session
     *            the session
     */
    public RootMimeMessage(final Session session) {
        super(session);
    }

    /**
     * Instantiates a new root mime message.
     * @param session
     *            the session
     * @param inputStream
     *            the input stream
     * @throws MessagingException
     *             the messaging exception
     */
    public RootMimeMessage(final Session session, final InputStream inputStream)
            throws MessagingException {
        super(session, inputStream);
    }

    /*
     * (non-Javadoc)
     * @see javax.mail.internet.MimeMessage#updateMessageID()
     */
    @Override
    protected void updateMessageID() throws MessagingException {
        final Header header = new Header("Message-ID", this.messageId);
        setHeader(header.getName(), header.getValue());
    }

    /**
     * Gets the message id.
     * @return the message id
     */
    public String getMessageId() {
        return this.messageId;
    }

    /**
     * Sets the message id.
     * @param messageId
     *            the new message id
     */
    public void setMessageId(final String messageId) {
        this.messageId = messageId;
    }
}
