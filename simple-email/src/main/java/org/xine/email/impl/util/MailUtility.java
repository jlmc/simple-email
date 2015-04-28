/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in
 * writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific
 * language governing permissions and limitations under the License.
 */

package org.xine.email.impl.util;

import org.xine.email.api.EmailContact;
import org.xine.email.api.EmailMessage;
import org.xine.email.api.EmailMessageType;
import org.xine.email.api.Header;
import org.xine.email.api.InvalidAddressException;
import org.xine.email.api.MailException;
import org.xine.email.api.RecipientType;
import org.xine.email.api.SendFailedException;
import org.xine.email.api.SessionConfig;
import org.xine.email.impl.BaseMailMessage;
import org.xine.email.impl.MailSessionAuthenticator;

import com.sun.mail.smtp.SMTPMessage;

import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * The Class MailUtility.
 */
public final class MailUtility {

    /** The Constant DOMAIN_PROPERTY_KEY. */
    public static final String DOMAIN_PROPERTY_KEY = "org.xine.email.domainName";

    /**
     * Instantiates a new mail utility.
     */
    private MailUtility() {}

    /**
     * Internet address.
     * @param address
     *            the address
     * @return the internet address
     * @throws InvalidAddressException
     *             the invalid address exception
     */
    public static InternetAddress internetAddress(final String address)
            throws InvalidAddressException {
        try {
            return new InternetAddress(address);
        } catch (final AddressException e) {
            throw new InvalidAddressException(
                    "Must be in format of a@b.com or Name<a@b.com> but was: \"" + address + "\"", e);
        }
    }

    /**
     * Internet address.
     * @param addresses
     *            the addresses
     * @return the collection
     * @throws InvalidAddressException
     *             the invalid address exception
     */
    public static Collection<InternetAddress> internetAddress(final String... addresses)
            throws InvalidAddressException {
        final ArrayList<InternetAddress> result = new ArrayList<>();

        for (final String address : addresses) {
            result.add(MailUtility.internetAddress(address));
        }
        return result;
    }

    /**
     * Internet address.
     * @param address
     *            the address
     * @param name
     *            the name
     * @return the internet address
     * @throws InvalidAddressException
     *             the invalid address exception
     */
    public static InternetAddress internetAddress(final String address, final String name)
            throws InvalidAddressException {
        InternetAddress internetAddress;
        try {
            internetAddress = new InternetAddress(address);
            internetAddress.setPersonal(name);
            return internetAddress;
        } catch (final AddressException e) {
            throw new InvalidAddressException(e);
        } catch (final UnsupportedEncodingException e) {
            throw new InvalidAddressException(e);
        }
    }

    /**
     * Internet address.
     * @param emailContact
     *            the email contact
     * @return the internet address
     * @throws InvalidAddressException
     *             the invalid address exception
     */
    public static InternetAddress internetAddress(final EmailContact emailContact)
            throws InvalidAddressException {
        if (Strings.isNullOrBlank(emailContact.getName())) {
            return MailUtility.internetAddress(emailContact.getAddress());
        }
        return MailUtility.internetAddress(emailContact.getAddress(), emailContact.getName());
    }

    /**
     * Internet address.
     * @param emailContacts
     *            the email contacts
     * @return the collection
     * @throws InvalidAddressException
     *             the invalid address exception
     */
    public static Collection<InternetAddress> internetAddress(
            final Collection<? extends EmailContact> emailContacts) throws InvalidAddressException {
        final Set<InternetAddress> internetAddresses = new HashSet<>();

        for (final EmailContact ec : emailContacts) {
            internetAddresses.add(MailUtility.internetAddress(ec));
        }

        return internetAddresses;
    }

    /**
     * Gets the internet addressses.
     * @param emailAddress
     *            the email address
     * @return the internet addressses
     */
    public static InternetAddress[] getInternetAddressses(final InternetAddress emailAddress) {
        final InternetAddress[] internetAddresses = {emailAddress };

        return internetAddresses;
    }

    /**
     * Gets the internet addressses.
     * @param recipients
     *            the recipients
     * @return the internet addressses
     */
    public static InternetAddress[] getInternetAddressses(
            final Collection<InternetAddress> recipients) {
        final InternetAddress[] result = new InternetAddress[recipients.size()];
        recipients.toArray(result);
        return result;
    }

    /**
     * Gets the host name.
     * @return the host name
     */
    public static String getHostName() {
        try {
            final java.net.InetAddress localMachine = java.net.InetAddress.getLocalHost();
            return localMachine.getHostName();
        } catch (final UnknownHostException e) {
            return "localhost";
        }
    }

    /**
     * Gets the internet addressses.
     * @param addresses
     *            the addresses
     * @return the internet addressses
     * @throws InvalidAddressException
     *             the invalid address exception
     */
    public static List<InternetAddress> getInternetAddressses(final Address[] addresses)
            throws InvalidAddressException {
        final List<InternetAddress> result = new ArrayList<>();
        if (addresses != null) {
            for (final Address a : addresses) {
                if (a.getType().equals("rfc822")) {
                    try {
                        result.add(new InternetAddress(a.toString()));
                    } catch (final AddressException e) {
                        throw new InvalidAddressException(e);
                    }
                } else {
                    throw new InvalidAddressException("Not type RFC822");
                }
            }
        }
        return result;
    }

    /**
     * Gets the headers.
     * @param allHeaders
     *            the all headers
     * @return the headers
     */
    public static List<Header> getHeaders(final Enumeration<?> allHeaders) {
        final List<Header> result = new LinkedList<>();
        while (allHeaders.hasMoreElements()) {
            final javax.mail.Header h = (javax.mail.Header) allHeaders.nextElement();
            result.add(new Header(h.getName(), h.getValue()));
        }
        return result;
    }

    /**
     * Creates the session.
     * @param mailConfig
     *            the mail config
     * @return the session
     */
    public static Session createSession(final SessionConfig mailConfig) {

        if (!Strings.isNullOrBlank(mailConfig.getJndiSessionName())) {
            try {
                return InitialContext.doLookup(mailConfig.getJndiSessionName());
            } catch (final NamingException e) {
                throw new MailException("Unable to lookup JNDI JavaMail Session", e);
            }
        }

        Session session;

        final Properties props = new Properties();

        if (!Strings.isNullOrBlank(mailConfig.getServerHost()) && mailConfig.getServerPort() > 0) {
            props.setProperty("mail.smtp.host", mailConfig.getServerHost());
            props.setProperty("mail.smtp.port", mailConfig.getServerPort().toString());
            props.setProperty("mail.smtp.starttls.enable", mailConfig.getEnableTls().toString());
            props.setProperty("mail.smtp.starttls.required", mailConfig.getRequireTls().toString());
            props.setProperty("mail.smtp.ssl.enable", mailConfig.getEnableSsl().toString());
            props.setProperty("mail.smtp.auth", mailConfig.getAuth().toString());
        } else {
            throw new MailException("Server Host and Server  Port must be set in MailConfig");
        }

        if (!Strings.isNullOrBlank(mailConfig.getDomainName())) {
            props.put(MailUtility.DOMAIN_PROPERTY_KEY, mailConfig.getDomainName());
        }

        if (mailConfig.getUsername() != null && mailConfig.getUsername().length() != 0
                && mailConfig.getPassword() != null && mailConfig.getPassword().length() != 0) {
            final MailSessionAuthenticator authenticator = new MailSessionAuthenticator(
                    mailConfig.getUsername(), mailConfig.getPassword());

            session = Session.getInstance(props, authenticator);
        } else {
            session = Session.getInstance(props, null);
        }

        return session;
    }

    /**
     * Header stripper.
     * @param header
     *            the header
     * @return the string
     */
    public static String headerStripper(final String header) {
        if (!Strings.isNullOrBlank(header)) {
            final String s = header.trim();

            if (s.matches("^<.*>$")) {
                return header.substring(1, header.length() - 1);
            }
            return header;
        }
        return header;
    }

    /**
     * Creates the mime message.
     * @param e
     *            the e
     * @param session
     *            the session
     * @return the SMTP message
     */
    public static SMTPMessage createMimeMessage(final EmailMessage e, final Session session) {
        final BaseMailMessage b = new BaseMailMessage(session, e.getCharset(),
                e.getRootContentType());

        if (!Strings.isNullOrBlank(e.getMessageId())) {
            b.setMessageID(e.getMessageId());
        }

        b.setFrom(e.getFromAddresses());
        b.addRecipients(RecipientType.TO, e.getToAddresses());
        b.addRecipients(RecipientType.CC, e.getCcAddresses());
        b.addRecipients(RecipientType.BCC, e.getBccAddresses());
        b.setReplyTo(e.getReplyToAddresses());
        b.addDeliveryRecieptAddresses(e.getDeliveryReceiptAddresses());
        b.addReadRecieptAddresses(e.getReadReceiptAddresses());
        b.setImportance(e.getImportance());
        b.addHeaders(e.getHeaders());
        b.setEnvelopeFrom(MailUtility.nullSafeAddress(e.getEnvelopeFrom()));

        if (e.getSubject() != null) {
            b.setSubject(e.getSubject());
        }

        if (e.getType() == EmailMessageType.STANDARD) {

            if (e.getHtmlBody() != null && e.getTextBody() != null) {
                b.setHTMLTextAlt(e.getHtmlBody(), e.getTextBody());
            } else if (e.getTextBody() != null) {
                b.setText(e.getTextBody());
            } else if (e.getHtmlBody() != null) {
                b.setHTML(e.getHtmlBody());
            }

            b.addAttachments(e.getAttachments());
        } else if (e.getType() == EmailMessageType.INVITE_ICAL) {
            if (e.getHtmlBody() != null) {
                b.setHTMLNotRelated(e.getHtmlBody());
            } else {
                b.setText(e.getTextBody());
            }
            b.addAttachments(e.getAttachments());
        } else {
            throw new SendFailedException("Unsupported Message Type: " + e.getType());
        }

        return b.getFinalizedMessage();
    }

    /**
     * Null safe address.
     * @param value
     *            the value
     * @return the string
     */
    public static String nullSafeAddress(final InternetAddress value) {
        return value != null ? value.getAddress() : null;
    }

    /**
     * Send.
     * @param e
     *            the e
     * @param session
     *            the session
     * @throws SendFailedException
     *             the send failed exception
     */
    public static void send(final EmailMessage e, final Session session) throws SendFailedException {
        final SMTPMessage msg = MailUtility.createMimeMessage(e, session);
        try {
            Transport.send(msg);
        } catch (final MessagingException e1) {
            throw new SendFailedException("Send Failed", e1);
        }

        try {
            e.setMessageId(MailUtility.headerStripper(msg.getMessageID()));
        } catch (final MessagingException e1) {
            throw new SendFailedException("Unable to read Message-ID from sent message");
        }
    }
}
