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
import org.xine.email.api.EmailAttachment;
import org.xine.email.api.EmailContact;
import org.xine.email.api.EmailMessage;
import org.xine.email.api.EmailMessageType;
import org.xine.email.api.Header;
import org.xine.email.api.ICalMethod;
import org.xine.email.api.MailContext;
import org.xine.email.api.MailMessage;
import org.xine.email.api.MailTransporter;
import org.xine.email.api.MessagePriority;
import org.xine.email.api.SendFailedException;
import org.xine.email.api.SessionConfig;
import org.xine.email.api.TemplateProvider;
import org.xine.email.impl.attachments.BaseAttachment;
import org.xine.email.impl.attachments.FileAttachment;
import org.xine.email.impl.attachments.InputStreamAttachment;
import org.xine.email.impl.util.EmailAttachmentUtil;
import org.xine.email.impl.util.MailUtility;

import java.io.File;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.mail.Session;
import javax.mail.internet.InternetAddress;

/**
 * The Class MailMessageImpl.
 */
public class MailMessageImpl implements MailMessage {

    /** The email message. */
    private EmailMessage emailMessage;

    /** The mail transporter. */
    private MailTransporter mailTransporter;

    /** The session. */
    private Session session;

    /** The mail config. */
    private SessionConfig mailConfig;

    /** The subject template. */
    private TemplateProvider subjectTemplate;

    /** The text template. */
    private TemplateProvider textTemplate;

    /** The html template. */
    private TemplateProvider htmlTemplate;

    /** The template context. */
    private final Map<String, Object> templateContext = new HashMap<String, Object>();

    /** The templates merged. */
    private boolean templatesMerged;

    /**
     * Instantiates a new mail message impl.
     */
    private MailMessageImpl() {
        this.emailMessage = new EmailMessage();

    }

    /**
     * Instantiates a new mail message impl.
     * @param session
     *            the session
     */
    public MailMessageImpl(final Session session) {
        this();
        this.session = session;
    }

    /**
     * Instantiates a new mail message impl.
     * @param mailTransporter
     *            the mail transporter
     */
    public MailMessageImpl(final MailTransporter mailTransporter) {
        this();
        this.mailTransporter = mailTransporter;
    }

    /**
     * Instantiates a new mail message impl.
     * @param mailConfig
     *            the mail config
     */
    public MailMessageImpl(final SessionConfig mailConfig) {
        this();
        this.mailConfig = mailConfig;
    }

    // Begin Addressing

    /*
     * (non-Javadoc)
     * @see org.xine.email.api.MailMessage#from(java.lang.String[])
     */
    @Override
    public MailMessage from(final String... address) {
        this.emailMessage.getFromAddresses().addAll(MailUtility.internetAddress(address));
        return this;
    }

    /*
     * (non-Javadoc)
     * @see org.xine.email.api.MailMessage#from(javax.mail.internet.InternetAddress)
     */
    @Override
    public MailMessage from(final InternetAddress emailAddress) {
        if (emailAddress != null) {
            this.emailMessage.getFromAddresses().add(emailAddress);
        }
        return this;
    }

    /*
     * (non-Javadoc)
     * @see org.xine.email.api.MailMessage#from(org.xine.email.api.EmailContact)
     */
    @Override
    public MailMessage from(final EmailContact emailContact) {
        if (emailContact != null) {
            this.emailMessage.getFromAddresses().add(MailUtility.internetAddress(emailContact));
        }
        return this;
    }

    /*
     * (non-Javadoc)
     * @see org.xine.email.api.MailMessage#from(java.util.Collection)
     */
    @Override
    public MailMessage from(final Collection<? extends EmailContact> emailContacts) {
        this.emailMessage.getFromAddresses().addAll(MailUtility.internetAddress(emailContacts));
        return this;
    }

    /*
     * (non-Javadoc)
     * @see org.xine.email.api.MailMessage#replyTo(java.lang.String[])
     */
    @Override
    public MailMessage replyTo(final String... address) {
        this.emailMessage.getReplyToAddresses().addAll(MailUtility.internetAddress(address));
        return this;
    }

    /*
     * (non-Javadoc)
     * @see org.xine.email.api.MailMessage#replyTo(javax.mail.internet.InternetAddress)
     */
    @Override
    public MailMessage replyTo(final InternetAddress emailAddress) {
        if (emailAddress != null) {
            this.emailMessage.getReplyToAddresses().add(emailAddress);
        }
        return this;
    }

    /*
     * (non-Javadoc)
     * @see org.xine.email.api.MailMessage#replyTo(org.xine.email.api.EmailContact)
     */
    @Override
    public MailMessage replyTo(final EmailContact emailContact) {
        if (emailContact != null) {
            this.emailMessage.getReplyToAddresses().add(MailUtility.internetAddress(emailContact));
        }
        return this;
    }

    /*
     * (non-Javadoc)
     * @see org.xine.email.api.MailMessage#replyTo(java.util.Collection)
     */
    @Override
    public MailMessage replyTo(final Collection<? extends EmailContact> emailContacts) {
        this.emailMessage.getReplyToAddresses().addAll(MailUtility.internetAddress(emailContacts));
        return this;
    }

    /*
     * (non-Javadoc)
     * @see org.xine.email.api.MailMessage#addHeader(java.lang.String, java.lang.String)
     */
    @Override
    public MailMessage addHeader(final String name, final String value) {
        this.emailMessage.getHeaders().add(new Header(name, value));
        return this;
    }

    /*
     * (non-Javadoc)
     * @see org.xine.email.api.MailMessage#to(java.lang.String[])
     */
    @Override
    public MailMessage to(final String... address) {
        this.emailMessage.getToAddresses().addAll(MailUtility.internetAddress(address));
        return this;
    }

    /*
     * (non-Javadoc)
     * @see org.xine.email.api.MailMessage#to(javax.mail.internet.InternetAddress)
     */
    @Override
    public MailMessage to(final InternetAddress emailAddress) {
        if (emailAddress != null) {
            this.emailMessage.getToAddresses().add(emailAddress);
        }
        return this;
    }

    /*
     * (non-Javadoc)
     * @see org.xine.email.api.MailMessage#to(org.xine.email.api.EmailContact)
     */
    @Override
    public MailMessage to(final EmailContact emailContact) {
        if (emailContact != null) {
            this.emailMessage.getToAddresses().add(MailUtility.internetAddress(emailContact));
        }
        return this;
    }

    /*
     * (non-Javadoc)
     * @see org.xine.email.api.MailMessage#to(java.util.Collection)
     */
    @Override
    public MailMessage to(final Collection<? extends EmailContact> emailContacts) {
        this.emailMessage.getToAddresses().addAll(MailUtility.internetAddress(emailContacts));
        return this;
    }

    /*
     * (non-Javadoc)
     * @see org.xine.email.api.MailMessage#cc(java.lang.String[])
     */
    @Override
    public MailMessage cc(final String... address) {
        this.emailMessage.getCcAddresses().addAll(MailUtility.internetAddress(address));
        return this;
    }

    /*
     * (non-Javadoc)
     * @see org.xine.email.api.MailMessage#cc(javax.mail.internet.InternetAddress)
     */
    @Override
    public MailMessage cc(final InternetAddress emailAddress) {
        if (emailAddress != null) {
            this.emailMessage.getCcAddresses().add(emailAddress);
        }
        return this;
    }

    /*
     * (non-Javadoc)
     * @see org.xine.email.api.MailMessage#cc(org.xine.email.api.EmailContact)
     */
    @Override
    public MailMessage cc(final EmailContact emailContact) {
        if (emailContact != null) {
            this.emailMessage.getCcAddresses().add(MailUtility.internetAddress(emailContact));
        }
        return this;
    }

    /*
     * (non-Javadoc)
     * @see org.xine.email.api.MailMessage#cc(java.util.Collection)
     */
    @Override
    public MailMessage cc(final Collection<? extends EmailContact> emailContacts) {
        this.emailMessage.getCcAddresses().addAll(MailUtility.internetAddress(emailContacts));
        return this;
    }

    /*
     * (non-Javadoc)
     * @see org.xine.email.api.MailMessage#bcc(java.lang.String[])
     */
    @Override
    public MailMessage bcc(final String... address) {
        this.emailMessage.getBccAddresses().addAll(MailUtility.internetAddress(address));
        return this;
    }

    /*
     * (non-Javadoc)
     * @see org.xine.email.api.MailMessage#bcc(javax.mail.internet.InternetAddress)
     */
    @Override
    public MailMessage bcc(final InternetAddress emailAddress) {
        if (emailAddress != null) {
            this.emailMessage.getBccAddresses().add(emailAddress);
        }
        return this;
    }

    /*
     * (non-Javadoc)
     * @see org.xine.email.api.MailMessage#bcc(org.xine.email.api.EmailContact)
     */
    @Override
    public MailMessage bcc(final EmailContact emailContact) {
        if (emailContact != null) {
            this.emailMessage.getBccAddresses().add(MailUtility.internetAddress(emailContact));
        }
        return this;
    }

    /*
     * (non-Javadoc)
     * @see org.xine.email.api.MailMessage#bcc(java.util.Collection)
     */
    @Override
    public MailMessage bcc(final Collection<? extends EmailContact> emailContacts) {
        this.emailMessage.getBccAddresses().addAll(MailUtility.internetAddress(emailContacts));
        return this;
    }

    /*
     * (non-Javadoc)
     * @see org.xine.email.api.MailMessage#envelopeFrom(org.xine.email.api.EmailContact)
     */
    @Override
    public MailMessage envelopeFrom(final EmailContact emailContact) {
        if (emailContact != null) {
            this.emailMessage.setEnvelopeFrom(MailUtility.internetAddress(emailContact));
        }
        return this;
    }

    /*
     * (non-Javadoc)
     * @see org.xine.email.api.MailMessage#envelopeFrom(java.lang.String)
     */
    @Override
    public MailMessage envelopeFrom(final String address) {
        if (address != null) {
            this.emailMessage.setEnvelopeFrom(MailUtility.internetAddress(address));
        }
        return this;
    }

    // End Addressing

    /*
     * (non-Javadoc)
     * @see org.xine.email.api.MailMessage#subject(java.lang.String)
     */
    @Override
    public MailMessage subject(final String value) {
        this.emailMessage.setSubject(value);
        return this;
    }

    /*
     * (non-Javadoc)
     * @see org.xine.email.api.MailMessage#deliveryReceipt(java.lang.String)
     */
    @Override
    public MailMessage deliveryReceipt(final String address) {
        this.emailMessage.getDeliveryReceiptAddresses().add(MailUtility.internetAddress(address));
        return this;
    }

    /*
     * (non-Javadoc)
     * @see org.xine.email.api.MailMessage#readReceipt(java.lang.String)
     */
    @Override
    public MailMessage readReceipt(final String address) {
        this.emailMessage.getReadReceiptAddresses().add(MailUtility.internetAddress(address));
        return this;
    }

    /*
     * (non-Javadoc)
     * @see org.xine.email.api.MailMessage#importance(org.xine.email.api.MessagePriority)
     */
    @Override
    public MailMessage importance(final MessagePriority messagePriority) {
        this.emailMessage.setImportance(messagePriority);
        return this;
    }

    /*
     * (non-Javadoc)
     * @see org.xine.email.api.MailMessage#messageId(java.lang.String)
     */
    @Override
    public MailMessage messageId(final String messageId) {
        this.emailMessage.setMessageId(messageId);
        return this;
    }

    /*
     * (non-Javadoc)
     * @see org.xine.email.api.MailMessage#bodyText(java.lang.String)
     */
    @Override
    public MailMessage bodyText(final String text) {
        this.emailMessage.setTextBody(text);
        return this;
    }

    /*
     * (non-Javadoc)
     * @see org.xine.email.api.MailMessage#bodyHtml(java.lang.String)
     */
    @Override
    public MailMessage bodyHtml(final String html) {
        this.emailMessage.setHtmlBody(html);
        return this;
    }

    /*
     * (non-Javadoc)
     * @see org.xine.email.api.MailMessage#bodyHtmlTextAlt(java.lang.String, java.lang.String)
     */
    @Override
    public MailMessage bodyHtmlTextAlt(final String html, final String text) {
        this.emailMessage.setTextBody(text);
        this.emailMessage.setHtmlBody(html);
        return this;
    }

    // Begin Attachments

    /*
     * (non-Javadoc)
     * @see org.xine.email.api.MailMessage#addAttachment(org.xine.email.api.EmailAttachment)
     */
    @Override
    public MailMessage addAttachment(final EmailAttachment attachment) {
        this.emailMessage.addAttachment(attachment);
        return this;
    }

    /*
     * (non-Javadoc)
     * @see org.xine.email.api.MailMessage#addAttachments(java.util.Collection)
     */
    @Override
    public MailMessage addAttachments(final Collection<EmailAttachment> attachments) {
        this.emailMessage.addAttachments(attachments);
        return this;
    }

    /*
     * (non-Javadoc)
     * @see org.xine.email.api.MailMessage#addAttachment(java.lang.String, java.lang.String,
     * org.xine.email.api.ContentDisposition, byte[])
     */
    @Override
    public MailMessage addAttachment(final String fileName, final String mimeType,
            final ContentDisposition contentDispostion, final byte[] bytes) {
        addAttachment(new BaseAttachment(fileName, mimeType, contentDispostion, bytes));
        return this;
    }

    /*
     * (non-Javadoc)
     * @see org.xine.email.api.MailMessage#addAttachment(java.lang.String, java.lang.String,
     * org.xine.email.api.ContentDisposition, java.io.InputStream)
     */
    @Override
    public MailMessage addAttachment(final String fileName, final String mimeType,
            final ContentDisposition contentDispostion, final InputStream inputStream) {
        addAttachment(new InputStreamAttachment(fileName, mimeType, contentDispostion, inputStream));
        return this;
    }

    /*
     * (non-Javadoc)
     * @see org.xine.email.api.MailMessage#addAttachment(org.xine.email.api.ContentDisposition,
     * java.io.File)
     */
    @Override
    public MailMessage addAttachment(final ContentDisposition contentDispostion, final File file) {
        addAttachment(new FileAttachment(contentDispostion, file));
        return this;
    }

    // End Attachments

    // Begin Calendar

    /*
     * (non-Javadoc)
     * @see org.xine.email.api.MailMessage#iCal(java.lang.String, java.lang.String,
     * org.xine.email.api.ICalMethod, byte[])
     */
    @Override
    public MailMessage iCal(final String htmlBody, final String textBody, final ICalMethod method,
            final byte[] bytes) {
        this.emailMessage.setType(EmailMessageType.INVITE_ICAL);
        this.emailMessage.setHtmlBody(htmlBody);
        this.emailMessage.setTextBody(textBody);
        this.emailMessage.addAttachment(new BaseAttachment(null, "text/calendar;method=" + method,
                ContentDisposition.INLINE, bytes, "urn:content-classes:calendarmessage"));
        return this;
    }

    /*
     * (non-Javadoc)
     * @see org.xine.email.api.MailMessage#iCal(java.lang.String, org.xine.email.api.ICalMethod,
     * byte[])
     */
    @Override
    public MailMessage iCal(final String textBody, final ICalMethod method, final byte[] bytes) {
        this.emailMessage.setType(EmailMessageType.INVITE_ICAL);
        this.emailMessage.setTextBody(textBody);
        this.emailMessage.addAttachment(new BaseAttachment(null, "text/calendar;method=" + method,
                ContentDisposition.INLINE, bytes, "urn:content-classes:calendarmessage"));
        return this;
    }

    // End Calendar

    /*
     * (non-Javadoc)
     * @see org.xine.email.api.MailMessage#subject(org.xine.email.api.TemplateProvider)
     */
    @Override
    public MailMessage subject(final TemplateProvider subject) {
        this.subjectTemplate = subject;
        return this;
    }

    /*
     * (non-Javadoc)
     * @see org.xine.email.api.MailMessage#bodyText(org.xine.email.api.TemplateProvider)
     */
    @Override
    public MailMessage bodyText(final TemplateProvider textBody) {
        this.textTemplate = textBody;
        return this;
    }

    /*
     * (non-Javadoc)
     * @see org.xine.email.api.MailMessage#bodyHtml(org.xine.email.api.TemplateProvider)
     */
    @Override
    public MailMessage bodyHtml(final TemplateProvider htmlBody) {
        this.htmlTemplate = htmlBody;
        return this;
    }

    /*
     * (non-Javadoc)
     * @see org.xine.email.api.MailMessage#bodyHtmlTextAlt(org.xine.email.api.TemplateProvider,
     * org.xine.email.api.TemplateProvider)
     */
    @Override
    public MailMessage bodyHtmlTextAlt(final TemplateProvider htmlBody,
            final TemplateProvider textBody) {
        bodyHtml(htmlBody);
        bodyText(textBody);
        return this;
    }

    /*
     * (non-Javadoc)
     * @see org.xine.email.api.MailMessage#charset(java.lang.String)
     */
    @Override
    public MailMessage charset(final String charset) {
        this.emailMessage.setCharset(charset);
        return this;
    }

    /*
     * (non-Javadoc)
     * @see org.xine.email.api.MailMessage#contentType(org.xine.email.api.ContentType)
     */
    @Override
    public MailMessage contentType(final ContentType contentType) {
        this.emailMessage.setRootContentType(contentType);
        return this;
    }

    /*
     * (non-Javadoc)
     * @see org.xine.email.api.MailMessage#put(java.lang.String, java.lang.Object)
     */
    @Override
    public MailMessage put(final String key, final Object value) {
        this.templateContext.put(key, value);
        return this;
    }

    /*
     * (non-Javadoc)
     * @see org.xine.email.api.MailMessage#put(java.util.Map)
     */
    @Override
    public MailMessage put(final Map<String, Object> values) {
        this.templateContext.putAll(values);
        return this;
    }

    /*
     * (non-Javadoc)
     * @see org.xine.email.api.MailMessage#getEmailMessage()
     */
    @Override
    public EmailMessage getEmailMessage() {
        return this.emailMessage;
    }

    /*
     * (non-Javadoc)
     * @see org.xine.email.api.MailMessage#setEmailMessage(org.xine.email.api.EmailMessage)
     */
    @Override
    public void setEmailMessage(final EmailMessage emailMessage) {
        this.emailMessage = emailMessage;
    }

    /**
     * Sets the mail transporter.
     * @param mailTransporter
     *            the new mail transporter
     */
    public void setMailTransporter(final MailTransporter mailTransporter) {
        this.mailTransporter = mailTransporter;
    }

    /**
     * Sets the session.
     * @param session
     *            the new session
     */
    public void setSession(final Session session) {
        this.session = session;
    }

    /*
     * (non-Javadoc)
     * @see org.xine.email.api.MailMessage#mergeTemplates()
     */
    @Override
    public EmailMessage mergeTemplates() {

        put("mailContext",
                new MailContext(EmailAttachmentUtil.getEmailAttachmentMap(this.emailMessage
                        .getAttachments())));

        if (this.subjectTemplate != null) {
            this.emailMessage.setSubject(this.subjectTemplate.merge(this.templateContext));
        }

        if (this.textTemplate != null) {
            this.emailMessage.setTextBody(this.textTemplate.merge(this.templateContext));
        }

        if (this.htmlTemplate != null) {
            this.emailMessage.setHtmlBody(this.htmlTemplate.merge(this.templateContext));
        }

        this.templatesMerged = true;

        return this.emailMessage;
    }

    /**
     * Send.
     * @param mailTransporter
     *            the mail transporter
     * @return the email message
     * @throws SendFailedException
     *             the send failed exception
     */
    public EmailMessage send(final MailTransporter mailTransporter) throws SendFailedException {
        if (!this.templatesMerged) {
            mergeTemplates();
        }

        try {
            mailTransporter.send(this.emailMessage);
        } catch (final Exception e) {
            throw new SendFailedException("Send Failed", e);
        }

        return this.emailMessage;
    }

    /**
     * Send.
     * @param session
     *            the session
     * @return the email message
     * @throws SendFailedException
     *             the send failed exception
     */
    private EmailMessage send(final Session session) throws SendFailedException {
        return send(new MailTransporterImpl(session));
    }

    /**
     * Send.
     * @param mailConfig
     *            the mail config
     * @return the email message
     */
    public EmailMessage send(final SessionConfig mailConfig) {
        return send(MailUtility.createSession(mailConfig));
    }

    /*
     * (non-Javadoc)
     * @see org.xine.email.api.MailMessage#send()
     */
    @Override
    public EmailMessage send() throws SendFailedException {
        if (this.mailTransporter != null) {
            return send(this.mailTransporter);
        } else if (this.session != null) {
            return send(this.session);
        } else if (this.mailConfig != null) {
            return send(this.mailConfig);
        } else {
            throw new SendFailedException(
                    "No Resource availiable to send. How was this constructed?");
        }
    }
}
