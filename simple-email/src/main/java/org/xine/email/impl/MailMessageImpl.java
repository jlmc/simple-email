/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in
 * writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific
 * language governing permissions and limitations under the License.
 */

package org.xine.email.impl;

import java.io.File;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.mail.Session;
import javax.mail.internet.InternetAddress;

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

public class MailMessageImpl implements MailMessage {

    private EmailMessage emailMessage;
    private MailTransporter mailTransporter;
    private Session session;
    private SessionConfig mailConfig;
    private TemplateProvider subjectTemplate;
    private TemplateProvider textTemplate;
    private TemplateProvider htmlTemplate;
    private final Map<String, Object> templateContext = new HashMap<String, Object>();
    private boolean templatesMerged;

    private MailMessageImpl() {
        this.emailMessage = new EmailMessage();

    }

    public MailMessageImpl(final Session session) {
        this();
        this.session = session;
    }

    public MailMessageImpl(final MailTransporter mailTransporter) {
        this();
        this.mailTransporter = mailTransporter;
    }

    public MailMessageImpl(final SessionConfig mailConfig) {
        this();
        this.mailConfig = mailConfig;
    }

    @Override
    public MailMessage from(final String... address) {
        this.from(MailUtility.internetAddress(address));
        return this;
    }

    @Override
    public MailMessage from(final InternetAddress... emailAddress) {
        if (emailAddress != null) {
            emailMessage.addFrom(emailAddress);
        }
        return this;
    }

    @Override
    public MailMessage from(final EmailContact emailContact) {
        if (emailContact != null) {
            this.from(MailUtility.internetAddress(emailContact));
        }
        return this;
    }

    @Override
    public MailMessage from(final Collection<? extends EmailContact> emailContacts) {
        this.from(MailUtility.internetAddress(emailContacts));
        return this;
    }

    @Override
    public MailMessage replyTo(final String... address) {
        this.replyTo(MailUtility.internetAddress(address));
        return this;
    }

    @Override
    public MailMessage replyTo(final InternetAddress... emailAddress) {
        if (emailAddress != null) {
            this.emailMessage.addReplyTo(emailAddress);
        }
        return this;
    }

    @Override
    public MailMessage replyTo(final EmailContact emailContact) {
        if (emailContact != null) {
            this.replyTo(MailUtility.internetAddress(emailContact));
        }
        return this;
    }

    @Override
    public MailMessage replyTo(final Collection<? extends EmailContact> emailContacts) {
        this.replyTo(MailUtility.internetAddress(emailContacts));
        return this;
    }

    @Override
    public MailMessage addHeader(final String name, final String value) {
        this.emailMessage.addHeader(new Header(name, value));
        return this;
    }

    @Override
    public MailMessage to(final String... address) {
        this.to(MailUtility.internetAddress(address));
        return this;
    }

    @Override
    public MailMessage to(final InternetAddress... emailAddress) {
        if (emailAddress != null) {
            this.emailMessage.addTo(emailAddress);
        }
        return this;
    }

    @Override
    public MailMessage to(final EmailContact emailContact) {
        if (emailContact != null) {
            this.to(MailUtility.internetAddress(emailContact));
        }
        return this;
    }

    @Override
    public MailMessage to(final Collection<? extends EmailContact> emailContacts) {
        this.to(MailUtility.internetAddress(emailContacts));
        return this;
    }

    @Override
    public MailMessage cc(final String... address) {
        this.cc(MailUtility.internetAddress(address));
        return this;
    }

    @Override
    public MailMessage cc(final InternetAddress... emailAddress) {
        if (emailAddress != null) {
            this.emailMessage.addCc(emailAddress);
        }
        return this;
    }

    @Override
    public MailMessage cc(final EmailContact emailContact) {
        if (emailContact != null) {
            this.cc(MailUtility.internetAddress(emailContact));
        }
        return this;
    }

    @Override
    public MailMessage cc(final Collection<? extends EmailContact> emailContacts) {
        this.cc(MailUtility.internetAddress(emailContacts));
        return this;
    }

    @Override
    public MailMessage bcc(final String... address) {
        this.bcc(MailUtility.internetAddress(address));
        return this;
    }

    @Override
    public MailMessage bcc(final InternetAddress... emailAddress) {
        if (emailAddress != null) {
            this.emailMessage.addBcc(emailAddress);
        }
        return this;
    }

    @Override
    public MailMessage bcc(final EmailContact emailContact) {
        if (emailContact != null) {
            this.bcc(MailUtility.internetAddress(emailContact));
        }
        return this;
    }

    @Override
    public MailMessage bcc(final Collection<? extends EmailContact> emailContacts) {
        this.bcc(MailUtility.internetAddress(emailContacts));
        return this;
    }

    @Override
    public MailMessage envelopeFrom(final EmailContact emailContact) {
        if (emailContact != null) {
            this.envelopeFrom(MailUtility.internetAddress(emailContact));
        }
        return this;
    }

    @Override
    public MailMessage envelopeFrom(final String address) {
        if (address != null) {
            this.envelopeFrom(MailUtility.internetAddress(address));
        }
        return this;
    }

    private void envelopeFrom(final InternetAddress internetAddress) {
        this.emailMessage.setEnvelopeFrom(internetAddress);
    }

    @Override
    public MailMessage subject(final String value) {
        this.emailMessage.setSubject(value);
        return this;
    }

    @Override
    public MailMessage deliveryReceipt(final String address) {
        this.emailMessage.getDeliveryReceiptAddresses().add(MailUtility.internetAddress(address));
        return this;
    }

    @Override
    public MailMessage readReceipt(final String address) {
        this.emailMessage.getReadReceiptAddresses().add(MailUtility.internetAddress(address));
        return this;
    }

    @Override
    public MailMessage importance(final MessagePriority messagePriority) {
        this.emailMessage.setImportance(messagePriority);
        return this;
    }

    @Override
    public MailMessage messageId(final String messageId) {
        this.emailMessage.setMessageId(messageId);
        return this;
    }

    @Override
    public MailMessage bodyText(final String text) {
        this.emailMessage.setTextBody(text);
        return this;
    }

    @Override
    public MailMessage bodyHtml(final String html) {
        this.emailMessage.setHtmlBody(html);
        return this;
    }

    @Override
    public MailMessage bodyHtmlTextAlt(final String html, final String text) {
        this.emailMessage.setTextBody(text);
        this.emailMessage.setHtmlBody(html);
        return this;
    }

    @Override
    public MailMessage addAttachment(final EmailAttachment attachment) {
        this.emailMessage.addAttachment(attachment);
        return this;
    }

    @Override
    public MailMessage addAttachments(final Collection<EmailAttachment> attachments) {
        this.emailMessage.addAttachments(attachments);
        return this;
    }

    @Override
    public MailMessage addAttachment(final String fileName, final String mimeType,
            final ContentDisposition contentDispostion, final byte[] bytes) {
        addAttachment(new BaseAttachment(fileName, mimeType, contentDispostion, bytes));
        return this;
    }

    @Override
    public MailMessage addAttachment(final String fileName, final String mimeType,
            final ContentDisposition contentDispostion, final InputStream inputStream) {
        addAttachment(new InputStreamAttachment(fileName, mimeType, contentDispostion, inputStream));
        return this;
    }

    @Override
    public MailMessage addAttachment(final ContentDisposition contentDispostion, final File file) {
        addAttachment(new FileAttachment(contentDispostion, file));
        return this;
    }

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

    @Override
    public MailMessage iCal(final String textBody, final ICalMethod method, final byte[] bytes) {
        this.emailMessage.setType(EmailMessageType.INVITE_ICAL);
        this.emailMessage.setTextBody(textBody);
        this.emailMessage.addAttachment(new BaseAttachment(null, "text/calendar;method=" + method,
                ContentDisposition.INLINE, bytes, "urn:content-classes:calendarmessage"));
        return this;
    }

    @Override
    public MailMessage subject(final TemplateProvider subject) {
        this.subjectTemplate = subject;
        return this;
    }

    @Override
    public MailMessage bodyText(final TemplateProvider textBody) {
        this.textTemplate = textBody;
        return this;
    }

    @Override
    public MailMessage bodyHtml(final TemplateProvider htmlBody) {
        this.htmlTemplate = htmlBody;
        return this;
    }

    @Override
    public MailMessage bodyHtmlTextAlt(final TemplateProvider htmlBody,
            final TemplateProvider textBody) {
        bodyHtml(htmlBody);
        bodyText(textBody);
        return this;
    }

    @Override
    public MailMessage charset(final String charset) {
        this.emailMessage.setCharset(charset);
        return this;
    }

    @Override
    public MailMessage contentType(final ContentType contentType) {
        this.emailMessage.setRootContentType(contentType);
        return this;
    }

    @Override
    public MailMessage put(final String key, final Object value) {
        this.templateContext.put(key, value);
        return this;
    }

    @Override
    public MailMessage put(final Map<String, Object> values) {
        this.templateContext.putAll(values);
        return this;
    }

    @Override
    public EmailMessage getEmailMessage() {
        return this.emailMessage;
    }

    @Override
    public void setEmailMessage(final EmailMessage emailMessage) {
        this.emailMessage = emailMessage;
    }

    public void setMailTransporter(final MailTransporter mailTransporter) {
        this.mailTransporter = mailTransporter;
    }

    public void setSession(final Session session) {
        this.session = session;
    }

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

    private EmailMessage send(final Session session) throws SendFailedException {
        return send(new MailTransporterImpl(session));
    }

    public EmailMessage send(final SessionConfig mailConfig) {
        return send(MailUtility.createSession(mailConfig));
    }

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
