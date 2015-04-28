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

package org.xine.email.impl.util;

import org.xine.email.api.ContentDisposition;
import org.xine.email.api.EmailMessage;
import org.xine.email.api.MailException;
import org.xine.email.impl.attachments.InputStreamAttachment;

import java.io.IOException;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMultipart;

/**
 * The Class MessageConverter.
 */
public class MessageConverter {

    /** The email message. */
    private EmailMessage emailMessage;

    /**
     * Convert.
     * @param m
     *            the m
     * @return the email message
     * @throws MailException
     *             the mail exception
     */
    public static EmailMessage convert(final Message m) throws MailException {
        final MessageConverter mc = new MessageConverter();
        return mc.convertMessage(m);
    }

    /**
     * Convert message.
     * @param m
     *            the m
     * @return the email message
     * @throws MailException
     *             the mail exception
     */
    public EmailMessage convertMessage(final Message m) throws MailException {
        this.emailMessage = new EmailMessage();

        try {
            this.emailMessage.setFromAddresses(MailUtility.getInternetAddressses(m.getFrom()));
            this.emailMessage.getToAddresses().addAll(
                    MailUtility.getInternetAddressses(m.getRecipients(RecipientType.TO)));
            this.emailMessage.setCcAddresses(MailUtility.getInternetAddressses(m
                    .getRecipients(RecipientType.CC)));
            this.emailMessage.setBccAddresses(MailUtility.getInternetAddressses(m
                    .getRecipients(RecipientType.BCC)));
            this.emailMessage.setSubject(m.getSubject());
            this.emailMessage.setMessageId(m.getHeader("Message-ID")[0]);
            this.emailMessage.getHeaders().addAll(MailUtility.getHeaders(m.getAllHeaders()));

            if (m.getContentType().toLowerCase().contains("multipart/")) {
                addMultiPart((MimeMultipart) m.getContent());
            } else if (m.isMimeType("text/plain")) {
                this.emailMessage.setTextBody((String) m.getContent());
            }
        } catch (final IOException e) {
            throw new MailException(e);
        } catch (final MessagingException e) {
            throw new MailException(e);
        }

        return this.emailMessage;
    }

    /**
     * Adds the multi part.
     * @param mp
     *            the mp
     * @throws MessagingException
     *             the messaging exception
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    private void addMultiPart(final MimeMultipart mp) throws MessagingException, IOException {
        for (int i = 0; i < mp.getCount(); i++) {
            final BodyPart bp = mp.getBodyPart(i);
            if (bp.getContentType().toLowerCase().contains("multipart/")) {
                addMultiPart((MimeMultipart) bp.getContent());
            } else {
                addPart(mp.getBodyPart(i));
            }
        }
    }

    /**
     * Adds the part.
     * @param bp
     *            the bp
     * @throws MessagingException
     *             the messaging exception
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    private void addPart(final BodyPart bp) throws MessagingException, IOException {

        if (bp.getContentType().toLowerCase().contains("multipart/")) {
            addMultiPart((MimeMultipart) bp.getContent());
        } else if (bp.getContentType().toLowerCase().contains("text/plain")) {
            this.emailMessage.setTextBody((String) bp.getContent());
        } else if (bp.getContentType().toLowerCase().contains("text/html")) {
            this.emailMessage.setHtmlBody((String) bp.getContent());
        } else if (bp.getContentType().toLowerCase().contains("application/octet-stream")) {
            this.emailMessage.addAttachment(new InputStreamAttachment(bp.getFileName(), bp
                    .getContentType(), ContentDisposition.mapValue(bp.getDisposition()), bp
                    .getInputStream()));
        }
    }
}
