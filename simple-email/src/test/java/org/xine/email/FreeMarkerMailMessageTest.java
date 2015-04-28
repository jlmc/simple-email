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

package org.xine.email;

import org.junit.Test;
import org.subethamail.smtp.auth.EasyAuthenticationHandlerFactory;
import org.subethamail.wiser.Wiser;
import org.xine.email.api.ContentDisposition;
import org.xine.email.api.EmailMessage;
import org.xine.email.api.MessagePriority;
import org.xine.email.api.SendFailedException;
import org.xine.email.api.SessionConfig;
import org.xine.email.impl.MailMessageImpl;
import org.xine.email.impl.SimpleMailConfig;
import org.xine.email.impl.attachments.URLAttachment;
import org.xine.email.impl.templating.freemarker.FreeMarkerTemplate;
import org.xine.email.impl.util.EmailAttachmentUtil;
import org.xine.email.impl.util.MailTestUtil;
import org.xine.email.impl.util.MessageConverter;
import org.xine.email.util.SMTPAuthenticator;
import org.xine.email.util.TestMailConfigs;

import com.google.common.io.Resources;

import java.io.IOException;

import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import junit.framework.Assert;

/**
 */
public class FreeMarkerMailMessageTest {

    String fromName = "Seam Framework";
    String fromAddress = "seam@jboss.org";
    String replyToName = "No Reply";
    String replyToAddress = "no-reply@seam-mal.test";
    String toName = "Seamy Seamerson";
    String toAddress = "seamy.seamerson@seam-mail.test";

    @Test
    public void testFreeMarkerTextMailMessage() throws MessagingException, IOException {
        final SessionConfig mailConfig = TestMailConfigs.standardConfig();

        EmailMessage e;

        final String uuid = java.util.UUID.randomUUID().toString();
        final String subjectTemplate = "Text Message from ${version} Mail - " + uuid;
        final Person person = new Person(this.toName, this.toAddress);
        final String version = "Seam 3";
        final String subject = "Text Message from " + version + " Mail - " + uuid;

        final Wiser wiser = new Wiser(mailConfig.getServerPort());
        wiser.setHostname(mailConfig.getServerHost());
        try {
            wiser.start();

            e = new MailMessageImpl(mailConfig)
                    .from(MailTestUtil.getAddressHeader(this.fromName, this.fromAddress))
                    .replyTo(this.replyToAddress)
                    .to(MailTestUtil.getAddressHeader(this.toName, this.toAddress))
                    .subject(new FreeMarkerTemplate(subjectTemplate))
                    .bodyText(
                            new FreeMarkerTemplate(Resources.newInputStreamSupplier(
                                    Resources.getResource("template.text.freemarker")).getInput()))
                    .put("person", person).put("version", version).importance(MessagePriority.HIGH)
                    .send();
        } finally {
            stop(wiser);
        }

        Assert.assertTrue("Didn't receive the expected amount of messages. Expected 1 got "
                + wiser.getMessages().size(), wiser.getMessages().size() == 1);

        final MimeMessage mess = wiser.getMessages().get(0).getMimeMessage();

        Assert.assertEquals(MailTestUtil.getAddressHeader(this.fromName, this.fromAddress),
                mess.getHeader("From", null));
        Assert.assertEquals(MailTestUtil.getAddressHeader(this.replyToAddress),
                mess.getHeader("Reply-To", null));
        Assert.assertEquals(MailTestUtil.getAddressHeader(this.toName, this.toAddress),
                mess.getHeader("To", null));
        Assert.assertEquals("Subject has been modified", subject,
                MimeUtility.unfold(mess.getHeader("Subject", null)));
        Assert.assertEquals(MessagePriority.HIGH.getPriority(), mess.getHeader("Priority", null));
        Assert.assertEquals(MessagePriority.HIGH.getX_priority(),
                mess.getHeader("X-Priority", null));
        Assert.assertEquals(MessagePriority.HIGH.getImportance(),
                mess.getHeader("Importance", null));
        Assert.assertTrue(mess.getHeader("Content-Type", null).startsWith("multipart/mixed"));

        final MimeMultipart mixed = (MimeMultipart) mess.getContent();
        final BodyPart text = mixed.getBodyPart(0);

        Assert.assertTrue(mixed.getContentType().startsWith("multipart/mixed"));
        Assert.assertEquals(1, mixed.getCount());

        Assert.assertTrue("Incorrect Charset: " + e.getCharset(),
                text.getContentType().startsWith("text/plain; charset=" + e.getCharset()));
        Assert.assertEquals(expectedTextBody(person.getName(), version),
                MailTestUtil.getStringContent(text));
        final EmailMessage convertedMessage = MessageConverter.convert(mess);
        Assert.assertEquals(convertedMessage.getSubject(), subject);
    }

    @Test
    public void testTextMailMessageSpecialCharacters() throws MessagingException, IOException {
        final SessionConfig mailConfig = TestMailConfigs.standardConfig();

        EmailMessage e;

        final String uuid = java.util.UUID.randomUUID().toString();
        final String subjectTemplate = "Special Char ü from ${version} Mail - " + uuid;
        final String version = "Seam 3";
        final String subject = "Special Char ü from " + version + " Mail - " + uuid;
        final String specialTextBody = "This is a Text Body with a special character - ü - ${version}";
        final String mergedSpecialTextBody = "This is a Text Body with a special character - ü - "
                + version;

        final String messageId = "1234@seam.test.com";

        final Wiser wiser = new Wiser(mailConfig.getServerPort());
        wiser.setHostname(mailConfig.getServerHost());
        try {
            wiser.start();

            e = new MailMessageImpl(mailConfig)
                    .from(MailTestUtil.getAddressHeader(this.fromName, this.fromAddress))
                    .replyTo(this.replyToAddress)
                    .to(MailTestUtil.getAddressHeader(this.toName, this.toAddress))
                    .subject(new FreeMarkerTemplate(subjectTemplate))
                    .bodyText(new FreeMarkerTemplate(specialTextBody))
                    .importance(MessagePriority.HIGH).messageId(messageId).put("version", version)
                    .send();
        } finally {
            stop(wiser);
        }

        Assert.assertTrue("Didn't receive the expected amount of messages. Expected 1 got "
                + wiser.getMessages().size(), wiser.getMessages().size() == 1);

        final MimeMessage mess = wiser.getMessages().get(0).getMimeMessage();

        Assert.assertEquals("Subject has been modified", subject,
                MimeUtility.decodeText(MimeUtility.unfold(mess.getHeader("Subject", null))));

        final MimeMultipart mixed = (MimeMultipart) mess.getContent();
        final BodyPart text = mixed.getBodyPart(0);

        Assert.assertTrue(mixed.getContentType().startsWith("multipart/mixed"));
        Assert.assertEquals(1, mixed.getCount());

        Assert.assertTrue("Incorrect Charset: " + e.getCharset(),
                text.getContentType().startsWith("text/plain; charset=" + e.getCharset()));
        Assert.assertEquals(mergedSpecialTextBody,
                MimeUtility.decodeText(MailTestUtil.getStringContent(text)));
        final EmailMessage convertedMessage = MessageConverter.convert(mess);
        Assert.assertEquals(convertedMessage.getSubject(), subject);
    }

    @Test
    public void testFreeMarkerHTMLMailMessage() throws MessagingException, IOException {
        final SessionConfig mailConfig = TestMailConfigs.standardConfig();

        final String subject = "HTML Message from Seam Mail - "
                + java.util.UUID.randomUUID().toString();
        final Person person = new Person(this.toName, this.toAddress);
        final String version = "Seam 3";
        EmailMessage emailMessage;

        final Wiser wiser = new Wiser(mailConfig.getServerPort());
        wiser.setHostname(mailConfig.getServerHost());
        try {
            wiser.start();

            emailMessage = new MailMessageImpl(mailConfig)
                    .from(MailTestUtil.getAddressHeader(this.fromName, this.fromAddress))
                    .replyTo(MailTestUtil.getAddressHeader(this.replyToName, this.replyToAddress))
                    .to(person)
                    .subject(subject)
                    .bodyHtml(
                            new FreeMarkerTemplate(Resources.newInputStreamSupplier(
                                    Resources.getResource("template.html.freemarker")).getInput()))
                    .put("person", person)
                    .put("version", version)
                    .importance(MessagePriority.HIGH)
                    .addAttachment(
                                            new URLAttachment(
                                    "http://design.jboss.org/seam/logo/final/seam_mail_85px.png",
                                    "seamLogo.png", ContentDisposition.INLINE)).send();
        } finally {
            stop(wiser);
        }

        Assert.assertTrue("Didn't receive the expected amount of messages. Expected 1 got "
                + wiser.getMessages().size(), wiser.getMessages().size() == 1);

        final MimeMessage mess = wiser.getMessages().get(0).getMimeMessage();

        Assert.assertEquals(MailTestUtil.getAddressHeader(this.fromName, this.fromAddress),
                mess.getHeader("From", null));
        Assert.assertEquals(MailTestUtil.getAddressHeader(this.replyToName, this.replyToAddress),
                mess.getHeader("Reply-To", null));
        Assert.assertEquals(MailTestUtil.getAddressHeader(this.toName, this.toAddress),
                mess.getHeader("To", null));
        Assert.assertEquals("Subject has been modified", subject,
                MimeUtility.unfold(mess.getHeader("Subject", null)));
        Assert.assertEquals(MessagePriority.HIGH.getPriority(), mess.getHeader("Priority", null));
        Assert.assertEquals(MessagePriority.HIGH.getX_priority(),
                mess.getHeader("X-Priority", null));
        Assert.assertEquals(MessagePriority.HIGH.getImportance(),
                mess.getHeader("Importance", null));
        Assert.assertTrue(mess.getHeader("Content-Type", null).startsWith("multipart/mixed"));

        final MimeMultipart mixed = (MimeMultipart) mess.getContent();
        final MimeMultipart related = (MimeMultipart) mixed.getBodyPart(0).getContent();
        final BodyPart html = related.getBodyPart(0);
        final BodyPart attachment1 = related.getBodyPart(1);

        Assert.assertTrue(mixed.getContentType().startsWith("multipart/mixed"));
        Assert.assertEquals(1, mixed.getCount());

        Assert.assertTrue(related.getContentType().startsWith("multipart/related"));
        Assert.assertEquals(2, related.getCount());

        Assert.assertTrue(html.getContentType().startsWith("text/html"));
        Assert.assertEquals(
                expectedHtmlBody(emailMessage, person.getName(), person.getEmail(), version),
                MailTestUtil.getStringContent(html));

        Assert.assertTrue(attachment1.getContentType().startsWith("image/png;"));
        Assert.assertEquals("seamLogo.png", attachment1.getFileName());
        final EmailMessage convertedMessage = MessageConverter.convert(mess);
        Assert.assertEquals(convertedMessage.getSubject(), subject);
    }

    @Test
    public void testFreeMarkerHTMLTextAltMailMessage() throws MessagingException, IOException {
        final SessionConfig mailConfig = TestMailConfigs.standardConfig();

        final String subject = "HTML+Text Message from Seam Mail - "
                + java.util.UUID.randomUUID().toString();
        final Person person = new Person(this.toName, this.toAddress);
        final String version = "Seam 3";
        EmailMessage emailMessage;

        final Wiser wiser = new Wiser(mailConfig.getServerPort());
        wiser.setHostname(mailConfig.getServerHost());
        try {
            wiser.start();

            emailMessage = new MailMessageImpl(mailConfig)
                    .from(MailTestUtil.getAddressHeader(this.fromName, this.fromAddress))
                    .to(MailTestUtil.getAddressHeader(person.getName(), person.getEmail()))
                    .subject(subject)
                    .put("person", person)
                    .put("version", version)
                    .bodyHtmlTextAlt(
                            new FreeMarkerTemplate(Resources.newInputStreamSupplier(
                                    Resources.getResource("template.html.freemarker")).getInput()),
                            new FreeMarkerTemplate(Resources.newInputStreamSupplier(
                                    Resources.getResource("template.text.freemarker")).getInput()))
                    .importance(MessagePriority.LOW)
                    .deliveryReceipt(this.fromAddress)
                    .readReceipt("seam.test")
                    .addAttachment(
                            "template.html.freemarker",
                            "text/html",
                            ContentDisposition.ATTACHMENT,
                            Resources.newInputStreamSupplier(
                                    Resources.getResource("template.html.freemarker")).getInput())
                    .addAttachment(
                            new URLAttachment(
                                    "http://design.jboss.org/seam/logo/final/seam_mail_85px.png",
                                    "seamLogo.png", ContentDisposition.INLINE)).send();
        } finally {
            stop(wiser);
        }

        Assert.assertTrue("Didn't receive the expected amount of messages. Expected 1 got "
                + wiser.getMessages().size(), wiser.getMessages().size() == 1);

        final MimeMessage mess = wiser.getMessages().get(0).getMimeMessage();

        Assert.assertEquals(MailTestUtil.getAddressHeader(this.fromName, this.fromAddress),
                mess.getHeader("From", null));
        Assert.assertEquals(MailTestUtil.getAddressHeader(this.toName, this.toAddress),
                mess.getHeader("To", null));
        Assert.assertEquals("Subject has been modified", subject,
                MimeUtility.unfold(mess.getHeader("Subject", null)));
        Assert.assertEquals(MessagePriority.LOW.getPriority(), mess.getHeader("Priority", null));
        Assert.assertEquals(MessagePriority.LOW.getX_priority(), mess.getHeader("X-Priority", null));
        Assert.assertEquals(MessagePriority.LOW.getImportance(), mess.getHeader("Importance", null));
        Assert.assertTrue(mess.getHeader("Content-Type", null).startsWith("multipart/mixed"));

        final MimeMultipart mixed = (MimeMultipart) mess.getContent();
        final MimeMultipart related = (MimeMultipart) mixed.getBodyPart(0).getContent();
        final MimeMultipart alternative = (MimeMultipart) related.getBodyPart(0).getContent();
        final BodyPart attachment = mixed.getBodyPart(1);
        final BodyPart inlineAttachment = related.getBodyPart(1);

        final BodyPart textAlt = alternative.getBodyPart(0);
        final BodyPart html = alternative.getBodyPart(1);

        Assert.assertTrue(mixed.getContentType().startsWith("multipart/mixed"));
        Assert.assertEquals(2, mixed.getCount());

        Assert.assertTrue(related.getContentType().startsWith("multipart/related"));
        Assert.assertEquals(2, related.getCount());

        Assert.assertTrue(html.getContentType().startsWith("text/html"));
        Assert.assertEquals(
                expectedHtmlBody(emailMessage, person.getName(), person.getEmail(), version),
                MailTestUtil.getStringContent(html));

        Assert.assertTrue(textAlt.getContentType().startsWith("text/plain"));
        Assert.assertEquals(expectedTextBody(person.getName(), version),
                MailTestUtil.getStringContent(textAlt));

        Assert.assertTrue(attachment.getContentType().startsWith("text/html"));
        Assert.assertEquals("template.html.freemarker", attachment.getFileName());

        Assert.assertTrue(inlineAttachment.getContentType().startsWith("image/png;"));
        Assert.assertEquals("seamLogo.png", inlineAttachment.getFileName());
        final EmailMessage convertedMessage = MessageConverter.convert(mess);
        Assert.assertEquals(convertedMessage.getSubject(), subject);
    }

    @Test
    public void testSMTPSessionAuthentication() throws MessagingException, IOException {
        final SimpleMailConfig mailConfig = TestMailConfigs.gmailConfig();

        final String subject = "HTML+Text Message from Seam Mail - "
                + java.util.UUID.randomUUID().toString();
        final Person person = new Person(this.toName, this.toAddress);
        mailConfig.setServerHost("localHost");
        mailConfig.setServerPort(8978);

        final Wiser wiser = new Wiser(mailConfig.getServerPort());
        wiser.getServer().setAuthenticationHandlerFactory(
                new EasyAuthenticationHandlerFactory(new SMTPAuthenticator("test", "test12!")));
        try {
            wiser.start();

            new MailMessageImpl(mailConfig)
                    .from(this.fromAddress)
                    .to(person.getEmail())
                    .subject(subject)
                    .put("person", person)
                    .put("version", "Seam 3")
                    .bodyHtmlTextAlt(
                            new FreeMarkerTemplate(Resources.newInputStreamSupplier(
                                    Resources.getResource("template.html.freemarker")).getInput()),
                            new FreeMarkerTemplate(Resources.newInputStreamSupplier(
                                    Resources.getResource("template.text.freemarker")).getInput()))
                    .importance(MessagePriority.LOW)
                    .deliveryReceipt(this.fromAddress)
                    .readReceipt("seam.test")
                    .addAttachment(
                            "template.html.freemarker",
                            "text/html",
                            ContentDisposition.ATTACHMENT,
                            Resources.newInputStreamSupplier(
                                    Resources.getResource("template.html.freemarker")).getInput())
                    .addAttachment(
                                    new URLAttachment(
                                    "http://design.jboss.org/seam/logo/final/seam_mail_85px.png",
                                    "seamLogo.png", ContentDisposition.INLINE)).send();
        } finally {
            stop(wiser);
        }

        Assert.assertTrue("Didn't receive the expected amount of messages. Expected 1 got "
                + wiser.getMessages().size(), wiser.getMessages().size() == 1);

        final MimeMessage mess = wiser.getMessages().get(0).getMimeMessage();

        Assert.assertEquals("Subject has been modified", subject,
                MimeUtility.unfold(mess.getHeader("Subject", null)));
        final EmailMessage convertedMessage = MessageConverter.convert(mess);
        Assert.assertEquals(convertedMessage.getSubject(), subject);
    }

    @Test(expected = SendFailedException.class)
    public void testFreeMarkerTextMailMessageSendFailed() throws IOException {
        final SessionConfig mailConfig = TestMailConfigs.standardConfig();

        final String uuid = java.util.UUID.randomUUID().toString();
        final String subject = "Text Message from $version Mail - " + uuid;
        final Person person = new Person(this.toName, this.toAddress);
        final String version = "Seam 3";

        // Port is two off so this should fail
        final Wiser wiser = new Wiser(mailConfig.getServerPort() + 2);
        wiser.setHostname(mailConfig.getServerHost());
        try {
            wiser.start();

            person.setName(this.toName);
            person.setEmail(this.toAddress);

            new MailMessageImpl(mailConfig)
                    .from(this.fromAddress)
                    .replyTo(this.replyToAddress)
                    .to(this.toAddress)
                    .subject(new FreeMarkerTemplate(subject))
                    .bodyText(
                            new FreeMarkerTemplate(Resources.newInputStreamSupplier(
                                    Resources.getResource("template.text.freemarker")).getInput()))
                    .put("person", person).put("version", version).importance(MessagePriority.HIGH)
                    .send();
        } finally {
            stop(wiser);
        }
    }

    /**
     * Wiser takes a fraction of a second to shutdown, so let it finish.
     */
    protected void stop(final Wiser wiser) {
        wiser.stop();
        try {
            Thread.sleep(100);
        } catch (final InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static String expectedHtmlBody(final EmailMessage emailMessage, final String name,
            final String email, final String version) {

        final StringBuilder sb = new StringBuilder();

        sb.append("<html xmlns=\"http://www.w3.org/1999/xhtml\">" + "\r\n");
        sb.append("<body>" + "\r\n");
        sb.append("<p><b>Dear <a href=\"mailto:" + email + "\">" + name + "</a>,</b></p>" + "\r\n");
        sb.append("<p>This is an example <i>HTML</i> email sent by " + version
                + " and FreeMarker.</p>" + "\r\n");
        sb.append("<p><img src=\"cid:"
                + EmailAttachmentUtil.getEmailAttachmentMap(emailMessage.getAttachments())
                        .get("seamLogo.png").getContentId() + "\" /></p>" + "\r\n");
        sb.append("<p>It has an alternative text body for mail readers that don't support html.</p>"
                + "\r\n");
        sb.append("</body>" + "\r\n");
        sb.append("</html>");

        return sb.toString();
    }

    private static String expectedTextBody(final String name, final String version) {
        final StringBuilder sb = new StringBuilder();

        sb.append("Hello " + name + ",\r\n");
        sb.append("\r\n");
        sb.append("This is the alternative text body for mail readers that don't support html. This was sent with "
                + version + " and FreeMarker.");

        return sb.toString();
    }
}
