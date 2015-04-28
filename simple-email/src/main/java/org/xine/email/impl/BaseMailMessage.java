/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in
 * writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific
 * language governing permissions and limitations under the License.
 */

package org.xine.email.impl;

import org.xine.email.api.ContentDisposition;
import org.xine.email.api.ContentType;
import org.xine.email.api.Header;
import org.xine.email.api.InvalidAddressException;
import org.xine.email.api.MailHeader;
import org.xine.email.api.MessagePriority;
import org.xine.email.api.RecipientType;
import org.xine.email.impl.attachments.AttachmentPart;
import org.xine.email.impl.attachments.BaseAttachment;
import org.xine.email.impl.util.MailUtility;

import com.sun.mail.smtp.SMTPMessage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;

/**
 * The Class BaseMailMessage.
 */
public class BaseMailMessage {

    /** The root mime message. */
    private RootMimeMessage rootMimeMessage;

    /** The charset. */
    private final String charset;

    /** The root content type. */
    private final ContentType rootContentType;

    /** The attachments. */
    private final Map<String, AttachmentPart> attachments = new HashMap<String, AttachmentPart>();

    /** The root multipart. */
    private MimeMultipart rootMultipart;

    /** The related multipart. */
    private final MimeMultipart relatedMultipart = new MimeMultipart(ContentType.RELATED.getValue());

    /** The session. */
    private final Session session;

    /**
     * Instantiates a new base mail message.
     * @param session
     *            the session
     * @param charset
     *            the charset
     * @param rootContentType
     *            the root content type
     */
    public BaseMailMessage(final Session session, final String charset,
            final ContentType rootContentType) {
        this.session = session;
        this.rootContentType = rootContentType;
        this.charset = charset;
        initialize();
    }

    /**
     * Initialize.
     */
    private void initialize() {
        this.rootMimeMessage = new RootMimeMessage(this.session);
        this.rootMultipart = new MimeMultipart(this.rootContentType.getValue());
        setSentDate(new Date());

        try {

            this.rootMimeMessage.setContent(this.rootMultipart);
        } catch (final MessagingException e) {
            throw new RuntimeException("Unable to set RootMultiPart", e);
        }

        initializeMessageId();

    }

    /**
     * Adds the recipient.
     * @param recipientType
     *            the recipient type
     * @param emailAddress
     *            the email address
     */
    public void addRecipient(final RecipientType recipientType, final InternetAddress emailAddress) {
        try {
            this.rootMimeMessage.addRecipient(recipientType.getRecipientType(), emailAddress);
        } catch (final MessagingException e) {
            throw new RuntimeException("Unable to add recipient " + recipientType + ": "
                    + emailAddress.toString() + " to MIME message", e);
        }
    }

    /**
     * Adds the recipients.
     * @param recipientType
     *            the recipient type
     * @param emailAddresses
     *            the email addresses
     */
    public void addRecipients(final RecipientType recipientType,
            final InternetAddress[] emailAddresses) {
        try {
            this.rootMimeMessage.addRecipients(recipientType.getRecipientType(), emailAddresses);
        } catch (final MessagingException e) {
            throw new RuntimeException("Unable to add " + recipientType
                    + ":  Collection<Recipients>to MIME message", e);
        }
    }

    /**
     * Adds the recipients.
     * @param recipientType
     *            the recipient type
     * @param emailAddresses
     *            the email addresses
     */
    public void addRecipients(final RecipientType recipientType,
            final Collection<InternetAddress> emailAddresses) {
        try {
            this.rootMimeMessage.addRecipients(recipientType.getRecipientType(),
                    MailUtility.getInternetAddressses(emailAddresses));
        } catch (final MessagingException e) {}
    }

    /**
     * Sets the from.
     * @param emailAddress
     *            the new from
     */
    public void setFrom(final InternetAddress emailAddress) {
        try {
            this.rootMimeMessage.setFrom(emailAddress);
        } catch (final MessagingException e) {
            throw new RuntimeException("Unable to From Addresses", e);
        }
    }

    /**
     * Sets the from.
     * @param emailAddresses
     *            the email addresses
     * @return the base mail message
     */
    public BaseMailMessage setFrom(final Collection<InternetAddress> emailAddresses) {
        try {
            if (emailAddresses.size() > 0) {
                this.rootMimeMessage.addFrom(MailUtility.getInternetAddressses(emailAddresses));
            }
        } catch (final MessagingException e) {
            throw new RuntimeException("Unable to From Addresses", e);
        }
        return this;
    }

    /**
     * Sets the reply to.
     * @param address
     *            the new reply to
     * @throws InvalidAddressException
     *             the invalid address exception
     */
    public void setReplyTo(final String address) throws InvalidAddressException {
        setReplyTo(MailUtility.internetAddress(address));
    }

    /**
     * Sets the reply to.
     * @param name
     *            the name
     * @param address
     *            the address
     */
    public void setReplyTo(final String name, final String address) {
        setReplyTo(MailUtility.internetAddress(address, name));
    }

    /**
     * Sets the reply to.
     * @param emailAddress
     *            the new reply to
     */
    public void setReplyTo(final InternetAddress emailAddress) {
        final List<InternetAddress> emailAddresses = new ArrayList<InternetAddress>();
        emailAddresses.add(emailAddress);
        setReplyTo(emailAddresses);
    }

    /**
     * Sets the reply to.
     * @param emailAddresses
     *            the new reply to
     */
    public void setReplyTo(final Collection<InternetAddress> emailAddresses) {
        try {
            this.rootMimeMessage.setReplyTo(MailUtility.getInternetAddressses(emailAddresses));
        } catch (final MessagingException e) {
            throw new RuntimeException("Unable to set Reply-To", e);
        }
    }

    /**
     * Sets the subject.
     * @param value
     *            the new subject
     */
    public void setSubject(final String value) {
        setSubject(value, this.charset);
    }

    /**
     * Sets the subject.
     * @param value
     *            the value
     * @param charset
     *            the charset
     */
    private void setSubject(final String value, final String charset) {
        try {
            this.rootMimeMessage.setSubject(value, charset);
        } catch (final MessagingException e) {
            throw new RuntimeException("Unable to add subject:" + value
                    + " to MIME message with charset: " + charset, e);
        }
    }

    /**
     * Sets the sent date.
     * @param date
     *            the new sent date
     */
    public void setSentDate(final Date date) {
        try {
            this.rootMimeMessage.setSentDate(date);
        } catch (final MessagingException e) {
            throw new RuntimeException("Unable to set Sent Date on MimeMessage", e);
        }
    }

    /**
     * Sets the message id.
     * @param messageId
     *            the new message id
     */
    public void setMessageID(final String messageId) {
        this.rootMimeMessage.setMessageId("<" + messageId + ">");
    }

    /**
     * Initialize message id.
     */
    private void initializeMessageId() {
        final String mailerDomainName = this.session.getProperty(MailUtility.DOMAIN_PROPERTY_KEY);

        if (mailerDomainName != null && mailerDomainName.length() > 0) {
            setMessageID(UUID.randomUUID().toString() + "@" + mailerDomainName);
        } else {
            setMessageID(UUID.randomUUID().toString() + "@" + MailUtility.getHostName());
        }
    }

    /**
     * Sets the envelope from.
     * @param value
     *            the new envelope from
     */
    public void setEnvelopeFrom(final String value) {
        this.rootMimeMessage.setEnvelopeFrom(value);
    }

    /**
     * Adds the delivery reciept addresses.
     * @param addresses
     *            the addresses
     */
    public void addDeliveryRecieptAddresses(final Collection<InternetAddress> addresses) {
        for (final InternetAddress address : addresses) {
            addDeliveryReciept(address.getAddress());
        }
    }

    /**
     * Adds the read reciept addresses.
     * @param addresses
     *            the addresses
     */
    public void addReadRecieptAddresses(final Collection<InternetAddress> addresses) {
        for (final InternetAddress address : addresses) {
            addReadReciept(address.getAddress());
        }
    }

    /**
     * Adds the delivery reciept.
     * @param address
     *            the address
     */
    public void addDeliveryReciept(final String address) {
        addHeader(new Header(MailHeader.DELIVERY_RECIEPT.headerValue(), "<" + address + ">"));
    }

    /**
     * Adds the read reciept.
     * @param address
     *            the address
     */
    public void addReadReciept(final String address) {
        addHeader(new Header(MailHeader.READ_RECIEPT.headerValue(), "<" + address + ">"));
    }

    /**
     * Sets the importance.
     * @param messagePriority
     *            the new importance
     */
    public void setImportance(final MessagePriority messagePriority) {
        if (messagePriority != null && messagePriority != MessagePriority.NORMAL) {
            setHeader(new Header("X-Priority", messagePriority.getX_priority()));
            setHeader(new Header("Priority", messagePriority.getPriority()));
            setHeader(new Header("Importance", messagePriority.getImportance()));
        }
    }

    /**
     * Sets the header.
     * @param header
     *            the new header
     */
    public void setHeader(final Header header) {
        try {
            this.rootMimeMessage.setHeader(header.getName(), header.getValue());
        } catch (final MessagingException e) {
            throw new RuntimeException("Unable to SET Header: + " + header.getName()
                    + " to Value: " + header.getValue(), e);
        }
    }

    /**
     * Adds the headers.
     * @param headers
     *            the headers
     */
    public void addHeaders(final Collection<Header> headers) {
        for (final Header header : headers) {
            addHeader(header);
        }
    }

    /**
     * Adds the header.
     * @param header
     *            the header
     */
    public void addHeader(final Header header) {
        try {
            this.rootMimeMessage.addHeader(header.getName(), header.getValue());
        } catch (final MessagingException e) {
            throw new RuntimeException("Unable to ADD Header: + " + header.getName()
                    + " to Value: " + header.getValue(), e);
        }
    }

    /**
     * Sets the text.
     * @param text
     *            the new text
     */
    public void setText(final String text) {
        try {
            this.rootMultipart.addBodyPart(buildTextBodyPart(text));
        } catch (final MessagingException e) {
            throw new RuntimeException("Unable to add TextBody to MimeMessage", e);
        }
    }

    /**
     * Sets the html.
     * @param html
     *            the new html
     */
    public void setHTML(final String html) {
        final MimeBodyPart relatedBodyPart = new MimeBodyPart();
        try {
            this.relatedMultipart.addBodyPart(buildHTMLBodyPart(html));
            relatedBodyPart.setContent(this.relatedMultipart);
            this.rootMultipart.addBodyPart(relatedBodyPart);
        } catch (final MessagingException e) {
            throw new RuntimeException("Unable to add TextBody to MimeMessage", e);
        }
    }

    /**
     * Sets the HTML not related.
     * @param html
     *            the new HTML not related
     */
    public void setHTMLNotRelated(final String html) {
        try {
            this.rootMultipart.addBodyPart(buildHTMLBodyPart(html));
        } catch (final MessagingException e) {
            throw new RuntimeException("Unable to add TextBody to MimeMessage", e);
        }
    }

    /**
     * Sets the html text alt.
     * @param html
     *            the html
     * @param text
     *            the text
     */
    public void setHTMLTextAlt(final String html, final String text) {
        final MimeBodyPart mixedBodyPart = new MimeBodyPart();

        final MimeBodyPart relatedBodyPart = new MimeBodyPart();

        final MimeMultipart alternativeMultiPart = new MimeMultipart(
                ContentType.ALTERNATIVE.getValue());

        try {
            // Text must be the first or some HTML capable clients will fail to
            // render HTML bodyPart.
            alternativeMultiPart.addBodyPart(buildTextBodyPart(text));
            alternativeMultiPart.addBodyPart(buildHTMLBodyPart(html));

            relatedBodyPart.setContent(alternativeMultiPart);

            this.relatedMultipart.addBodyPart(relatedBodyPart);

            mixedBodyPart.setContent(this.relatedMultipart);

            this.rootMultipart.addBodyPart(mixedBodyPart);
        } catch (final MessagingException e) {
            throw new RuntimeException("Unable to build HTML+Text Email", e);
        }
    }

    /**
     * Sets the calendar.
     * @param body
     *            the body
     * @param invite
     *            the invite
     */
    public void setCalendar(final String body, final AttachmentPart invite) {
        try {
            this.rootMultipart.addBodyPart(buildHTMLBodyPart(body));
            this.rootMultipart.addBodyPart(invite);
        } catch (final MessagingException e) {
            throw new RuntimeException("Unable to add Calendar Body to MimeMessage", e);
        }
    }

    /**
     * Builds the text body part.
     * @param text
     *            the text
     * @return the mime body part
     */
    private MimeBodyPart buildTextBodyPart(final String text) {
        final MimeBodyPart textBodyPart = new MimeBodyPart();

        try {
            textBodyPart.setDisposition(ContentDisposition.INLINE.headerValue());
            textBodyPart.setText(text, this.charset);
        } catch (final MessagingException e) {
            throw new RuntimeException("Unable to build TextBodyPart", e);
        }

        return textBodyPart;
    }

    /**
     * Builds the html body part.
     * @param html
     *            the html
     * @return the mime body part
     */
    private MimeBodyPart buildHTMLBodyPart(final String html) {
        final MimeBodyPart htmlBodyPart = new MimeBodyPart();

        try {
            htmlBodyPart.setDisposition(ContentDisposition.INLINE.headerValue());
            htmlBodyPart.setText(html, this.charset, "html");
        } catch (final MessagingException e) {
            throw new RuntimeException("Unable to build HTMLBodyPart", e);
        }

        return htmlBodyPart;
    }

    /**
     * Adds the attachment.
     * @param emailAttachment
     *            the email attachment
     */
    public void addAttachment(final BaseAttachment emailAttachment) {
        final AttachmentPart attachment = new AttachmentPart(emailAttachment.getBytes(),
                emailAttachment.getContentId(), emailAttachment.getFileName(),
                emailAttachment.getMimeType(), emailAttachment.getHeaders(),
                emailAttachment.getContentDisposition());
        this.attachments.put(attachment.getAttachmentFileName(), attachment);
    }

    /**
     * Adds the attachments.
     * @param emailAttachments
     *            the email attachments
     */
    public void addAttachments(final Collection<BaseAttachment> emailAttachments) {
        for (final BaseAttachment ea : emailAttachments) {
            addAttachment(ea);
        }
    }

    /**
     * Gets the attachments.
     * @return the attachments
     */
    public Map<String, AttachmentPart> getAttachments() {
        return this.attachments;
    }

    /**
     * Gets the root mime message.
     * @return the root mime message
     */
    public SMTPMessage getRootMimeMessage() {
        return this.rootMimeMessage;
    }

    /**
     * Gets the finalized message.
     * @return the finalized message
     */
    public SMTPMessage getFinalizedMessage() {
        addAttachmentsToMessage();
        return getRootMimeMessage();
    }

    /**
     * Adds the attachments to message.
     */
    private void addAttachmentsToMessage() {
        for (final AttachmentPart a : this.attachments.values()) {
            if (a.getContentDisposition() == ContentDisposition.ATTACHMENT) {
                try {
                    this.rootMultipart.addBodyPart(a);
                } catch (final MessagingException e) {
                    throw new RuntimeException("Unable to Add STANDARD Attachment: "
                            + a.getAttachmentFileName(), e);
                }
            } else if (a.getContentDisposition() == ContentDisposition.INLINE) {
                try {
                    if (this.relatedMultipart.getCount() > 0) {
                        this.relatedMultipart.addBodyPart(a);
                    } else {
                        this.rootMultipart.addBodyPart(a);
                    }
                } catch (final MessagingException e) {
                    throw new RuntimeException("Unable to Add INLINE Attachment: "
                            + a.getAttachmentFileName(), e);
                }
            } else {
                throw new RuntimeException("Unsupported Attachment Content Disposition");
            }
        }
    }
}
