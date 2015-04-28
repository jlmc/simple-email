/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in
 * writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific
 * language governing permissions and limitations under the License.
 */

package org.xine.email.api;

import org.xine.email.impl.attachments.BaseAttachment;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.mail.internet.InternetAddress;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Stores information about an EmailMessage while it is being built and after sending.
 */
@XmlRootElement
@XmlType(propOrder = {"messageId", "importance", "charset", "fromAddresses", "replyToAddresses",
        "toAddresses", "ccAddresses", "bccAddresses", "envelopeFrom", "deliveryReceiptAddresses",
        "readReceiptAddresses", "subject", "textBody", "htmlBody", "headers", "rootContentType",
        "type", "attachments" })
public class EmailMessage {

    /** The charset. */
    private String charset = Charset.defaultCharset().name();

    /** The root content type. */
    private ContentType rootContentType = ContentType.MIXED;

    /** The type. */
    private EmailMessageType type = EmailMessageType.STANDARD;

    /** The message id. */
    private String messageId;

    /** The from addresses. */
    private List<InternetAddress> fromAddresses = new ArrayList<>();

    /** The reply to addresses. */
    private List<InternetAddress> replyToAddresses = new ArrayList<>();

    /** The to addresses. */
    private final List<InternetAddress> toAddresses = new ArrayList<>();

    /** The cc addresses. */
    private List<InternetAddress> ccAddresses = new ArrayList<>();

    /** The bcc addresses. */
    private List<InternetAddress> bccAddresses = new ArrayList<>();

    /** The envelope from. */
    private InternetAddress envelopeFrom;

    /** The headers. */
    private List<Header> headers = new ArrayList<>();

    /** The subject. */
    private String subject;

    /** The text body. */
    private String textBody;

    /** The html body. */
    private String htmlBody;

    /** The attachments. */
    private List<BaseAttachment> attachments = new ArrayList<>();

    /** The delivery receipt addresses. */
    private List<InternetAddress> deliveryReceiptAddresses = new ArrayList<>();

    /** The read receipt addresses. */
    private List<InternetAddress> readReceiptAddresses = new ArrayList<>();

    /** The importance. */
    private MessagePriority importance = MessagePriority.NORMAL;

    /**
     * Get the charset used to encode the EmailMessage.
     * @return charset of the EmailMessage
     */
    @XmlElement
    public String getCharset() {
        return this.charset;
    }

    /**
     * Override the default charset of the JVM.
     * @param charset
     *            the new charset
     */
    public void setCharset(final String charset) {
        this.charset = charset;
    }

    /**
     * Get the Root Mime ContentType of the EmailMessage.
     * @return Root Mime ContentType of the EmailMessage
     */
    @XmlElement
    public ContentType getRootContentType() {
        return this.rootContentType;
    }

    /**
     * Set the Root Mime ContentType of the EmailMessage.
     * @param rootContentType
     *            SubType to set
     */
    public void setRootContentType(final ContentType rootContentType) {
        this.rootContentType = rootContentType;
    }

    /**
     * Get the current EmailMessageType of the EmailMessage.
     * @return EmailMessageType of this EmailMessage
     */
    @XmlElement
    public EmailMessageType getType() {
        return this.type;
    }

    /**
     * Sets the EmailMessageType of the EmailMessage.
     * @param type
     *            EmailMessageType to set on the EmailMessage
     */
    public void setType(final EmailMessageType type) {
        this.type = type;
    }

    /**
     * Gets the Message-ID of the EmailMeassage. This nulled after sending.
     * @return Message-ID of the EmailMeassage
     */
    @XmlElement
    public String getMessageId() {
        return this.messageId;
    }

    /**
     * Sets the Message-ID for the EmailMeassage. Should be in RFC822 format
     * @param messageId
     *            Globally unique Message-ID example 1234.5678@test.com
     */
    public void setMessageId(final String messageId) {
        this.messageId = messageId;
    }

    /**
     * Get the Collection of FROM addresses on the EmailMeassage.
     * @return Collection of InternetAddresses addresses
     */
    @XmlElement
    public List<InternetAddress> getFromAddresses() {
        return this.fromAddresses;
    }

    /**
     * Sets the from addresses.
     * @param fromAddresses
     *            the new from addresses
     */
    public void setFromAddresses(final List<InternetAddress> fromAddresses) {
        this.fromAddresses = fromAddresses;
    }

    /**
     * Get the Collection of REPLY-TO addresses on the EmailMeassage.
     * @return Collection of InternetAddresses addresses
     */
    @XmlElement
    public List<InternetAddress> getReplyToAddresses() {
        return this.replyToAddresses;
    }

    /**
     * Sets the reply to addresses.
     * @param replyToAddresses
     *            the new reply to addresses
     */
    public void setReplyToAddresses(final List<InternetAddress> replyToAddresses) {
        this.replyToAddresses = replyToAddresses;
    }

    /**
     * Get the Collection of TO addresses on the EmailMeassage.
     * @return Collection of InternetAddresses addresses
     */
    @XmlElement
    public List<InternetAddress> getToAddresses() {
        return this.toAddresses;
    }

    /**
     * Get the Collection of CC addresses on the EmailMeassage.
     * @return Collection of InternetAddresses addresses
     */
    @XmlElement
    public List<InternetAddress> getCcAddresses() {
        return this.ccAddresses;
    }

    /**
     * Sets the cc addresses.
     * @param ccAddresses
     *            the new cc addresses
     */
    public void setCcAddresses(final List<InternetAddress> ccAddresses) {
        this.ccAddresses = ccAddresses;
    }

    /**
     * Get the Collection of BCC addresses on the EmailMeassage.
     * @return Collection of InternetAddresses addresses
     */
    @XmlElement
    public List<InternetAddress> getBccAddresses() {
        return this.bccAddresses;
    }

    /**
     * Sets the bcc addresses.
     * @param bccAddresses
     *            the new bcc addresses
     */
    public void setBccAddresses(final List<InternetAddress> bccAddresses) {
        this.bccAddresses = bccAddresses;
    }

    /**
     * Gets the "Envelope From" address which is used for error messages.
     * @return the envelope from
     */
    @XmlElement
    public InternetAddress getEnvelopeFrom() {
        return this.envelopeFrom;
    }

    /**
     * Sets the "Envelope From" address which is used for error messages.
     * @param address
     *            the new envelope from
     */
    public void setEnvelopeFrom(final InternetAddress address) {
        this.envelopeFrom = address;
    }

    /**
     * Get a Collection of additional headers added to the EmailMessage.
     * @return Collection of Header
     */
    @XmlElementWrapper(name = "headers")
    @XmlElement(name = "header")
    public List<Header> getHeaders() {
        return this.headers;
    }

    /**
     * Sets the headers.
     * @param headers
     *            the new headers
     */
    public void setHeaders(final List<Header> headers) {
        this.headers = headers;
    }

    /**
     * Get the Subject of the EmailMessage.
     * @return The Subject
     */
    @XmlElement
    public String getSubject() {
        return this.subject;
    }

    /**
     * Sets the Subject on the EmailMessage.
     * @param subject
     *            Subject to be set
     */
    public void setSubject(final String subject) {
        this.subject = subject;
    }

    /**
     * Get the Text Body of the EmailMessage.
     * @return The EmailMessage Text Body.
     */
    @XmlElement
    public String getTextBody() {
        return this.textBody;
    }

    /**
     * Set the Text Body of the EmailMessage.
     * @param textBody
     *            Text Body to be set
     */
    public void setTextBody(final String textBody) {
        this.textBody = textBody;
    }

    /**
     * Get the HTML Body of the EmailMessage.
     * @return The EmailMessage HTML Body.
     */
    @XmlElement
    public String getHtmlBody() {
        return this.htmlBody;
    }

    /**
     * Set the HTML Body of the EmailMessage.
     * @param htmlBody
     *            HTML Body to be set
     */
    public void setHtmlBody(final String htmlBody) {
        this.htmlBody = htmlBody;
    }

    /**
     * Get the collection of InternetAddress which are Delivery Reciept addresses.
     * @return Collection of InternetAddress
     */
    @XmlElement
    public List<InternetAddress> getDeliveryReceiptAddresses() {
        return this.deliveryReceiptAddresses;
    }

    /**
     * Sets the delivery receipt addresses.
     * @param deliveryReceiptAddresses
     *            the new delivery receipt addresses
     */
    public void setDeliveryReceiptAddresses(final List<InternetAddress> deliveryReceiptAddresses) {
        this.deliveryReceiptAddresses = deliveryReceiptAddresses;
    }

    /**
     * Get the collection of InternetAddress which are Read Receipt addresses.
     * @return Collection of InternetAddress
     */
    public List<InternetAddress> getReadReceiptAddresses() {
        return this.readReceiptAddresses;
    }

    /**
     * Sets the read receipt addresses.
     * @param readReceiptAddresses
     *            the new read receipt addresses
     */
    public void setReadReceiptAddresses(final List<InternetAddress> readReceiptAddresses) {
        this.readReceiptAddresses = readReceiptAddresses;
    }

    /**
     * Get the Current Importance of the EmailMessage. Default is normal. No Header added
     * @return MessagePriority of EmailMessage
     */
    public MessagePriority getImportance() {
        return this.importance;
    }

    /**
     * Sets the MessagePriority of the EmailMessage.
     * @param importance
     *            MessagePriority to be set.
     */
    public void setImportance(final MessagePriority importance) {
        this.importance = importance;
    }

    /**
     * Adds an BaseAttachment to the EmailMessage.
     * @param attachment
     *            EmailAttachment to be added
     */
    public void addAttachment(final BaseAttachment attachment) {
        this.attachments.add(attachment);
    }

    /**
     * Adds an EmailAttachment to the EmailMessage.
     * @param attachment
     *            EmailAttachment to be added
     */
    public void addAttachment(final EmailAttachment attachment) {
        final BaseAttachment ba = new BaseAttachment(attachment.getFileName(),
                attachment.getMimeType(), attachment.getContentDisposition(), attachment.getBytes());
        this.attachments.add(ba);
    }

    /**
     * Adds a Collection of EmailAttachment to the EmailMessage.
     * @param emailAttachments
     *            Collection of EmailAttachment
     */
    public void addAttachments(final Collection<EmailAttachment> emailAttachments) {
        for (final EmailAttachment e : emailAttachments) {
            addAttachment(e);
        }
    }

    /**
     * Gets a Collection representing all the Attachments on the EmailMessage.
     * @return Collection of EmailAttachment
     */
    @XmlElementWrapper(name = "attachments")
    @XmlElement(name = "attachment")
    public List<BaseAttachment> getAttachments() {
        return this.attachments;
    }

    /**
     * Sets the attachments.
     * @param attachments
     *            the new attachments
     */
    public void setAttachments(final List<BaseAttachment> attachments) {
        this.attachments = attachments;
    }
}
