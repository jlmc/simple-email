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
import org.subethamail.wiser.Wiser;
import org.xine.email.api.ContentDisposition;
import org.xine.email.api.EmailMessage;
import org.xine.email.api.InvalidAddressException;
import org.xine.email.api.MessagePriority;
import org.xine.email.api.SendFailedException;
import org.xine.email.api.SessionConfig;
import org.xine.email.impl.MailMessageImpl;
import org.xine.email.impl.attachments.URLAttachment;
import org.xine.email.impl.util.MailTestUtil;
import org.xine.email.impl.util.MailUtility;
import org.xine.email.impl.util.MessageConverter;
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
public class MailMessageTest {

    String fromName = "Seam Framework";
    String fromAddress = "seam@jboss.org";
    String replyToName = "No Reply";
    String replyToAddress = "no-reply@seam-mal.test";
    String toName = "Seamy Seamerson";
    String toAddress = "seamy.seamerson@seam-mail.test";
    String ccName = "Red Hatty";
    String ccAddress = "red.hatty@jboss.org";

    String htmlBody = "<html><body><b>Hello</b> World!</body></html>";
    String textBody = "This is a Text Body!";

    private static final String ENVELOPE_FROM_ADDRESS = "ef@jboss.org";

    @Test
    public void testTextMailMessage() throws MessagingException, IOException {
        final SessionConfig mailConfig = TestMailConfigs.standardConfig();

        final String subject = "Text Message from Seam Mail - "
                + java.util.UUID.randomUUID().toString();
        final String messageId = "1234@seam.test.com";

        EmailMessage e;

        final Wiser wiser = new Wiser(mailConfig.getServerPort());
        wiser.setHostname(mailConfig.getServerHost());
        try {
            wiser.start();

            e = new MailMessageImpl(mailConfig)
                    .from(MailTestUtil.getAddressHeader(this.fromName, this.fromAddress))
                    .replyTo(this.replyToAddress)
                    .to(MailTestUtil.getAddressHeader(this.toName, this.toAddress))
                    .subject(subject).bodyText(this.textBody).importance(MessagePriority.HIGH)
                    .messageId(messageId).envelopeFrom(ENVELOPE_FROM_ADDRESS).send();
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
        Assert.assertEquals(messageId,
                MailUtility.headerStripper(mess.getHeader("Message-ID", null)));

        final MimeMultipart mixed = (MimeMultipart) mess.getContent();
        final BodyPart text = mixed.getBodyPart(0);

        Assert.assertTrue(mixed.getContentType().startsWith("multipart/mixed"));
        Assert.assertEquals(1, mixed.getCount());

        Assert.assertTrue("Incorrect Charset: " + e.getCharset(),
                text.getContentType().startsWith("text/plain; charset=" + e.getCharset()));
        Assert.assertEquals(this.textBody, MailTestUtil.getStringContent(text));
        final EmailMessage convertedMessage = MessageConverter.convert(mess);
        Assert.assertEquals(convertedMessage.getSubject(), subject);
    }

    @Test
    public void testTextMailMessageSpecialCharacters() throws MessagingException, IOException {
        final SessionConfig mailConfig = TestMailConfigs.standardConfig();

        final String subject = "Sometimes subjects have speical characters like ü - "
                + java.util.UUID.randomUUID().toString();
        final String specialTextBody = "This is a Text Body with a special character - ü";

        EmailMessage e;

        final String messageId = "1234@seam.test.com";

        final Wiser wiser = new Wiser(mailConfig.getServerPort());
        wiser.setHostname(mailConfig.getServerHost());
        try {
            wiser.start();

            e = new MailMessageImpl(mailConfig)
                    .from(MailTestUtil.getAddressHeader(this.fromName, this.fromAddress))
                    .replyTo(this.replyToAddress)
                    .to(MailTestUtil.getAddressHeader(this.toName, this.toAddress))
                    .subject(subject).bodyText(specialTextBody).importance(MessagePriority.HIGH)
                    .messageId(messageId).send();
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
        Assert.assertEquals(specialTextBody,
                MimeUtility.decodeText(MailTestUtil.getStringContent(text)));
        final EmailMessage convertedMessage = MessageConverter.convert(mess);
        Assert.assertEquals(convertedMessage.getSubject(), subject);
    }

    @Test
    public void testHTMLMailMessage() throws MessagingException, IOException {
        final SessionConfig mailConfig = TestMailConfigs.standardConfig();

        final String subject = "HTML Message from Seam Mail - "
                + java.util.UUID.randomUUID().toString();
        final Person person = new Person(this.toName, this.toAddress);

        EmailMessage e;

        final Wiser wiser = new Wiser(mailConfig.getServerPort());
        wiser.setHostname(mailConfig.getServerHost());
        try {
            wiser.start();
            e = new MailMessageImpl(mailConfig)
                    .from(MailTestUtil.getAddressHeader(this.fromName, this.fromAddress))
                    .replyTo(MailTestUtil.getAddressHeader(this.replyToName, this.replyToAddress))
                    .to(person)
                    .subject(subject)
                    .bodyHtml(this.htmlBody)
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
        Assert.assertEquals(e.getMessageId(),
                MailUtility.headerStripper(mess.getHeader("Message-ID", null)));
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
        Assert.assertEquals(this.htmlBody, MailTestUtil.getStringContent(html));

        Assert.assertTrue(attachment1.getContentType().startsWith("image/png;"));
        Assert.assertEquals("seamLogo.png", attachment1.getFileName());
        final EmailMessage convertedMessage = MessageConverter.convert(mess);
        Assert.assertEquals(convertedMessage.getSubject(), subject);
    }

    @Test
    public void testHTMLTextAltMailMessage() throws MessagingException, IOException {
        final SessionConfig mailConfig = TestMailConfigs.standardConfig();
        final String subject = "HTML+Text Message from Seam Mail - "
                + java.util.UUID.randomUUID().toString();
        final Person person = new Person(this.toName, this.toAddress);
        final Wiser wiser = new Wiser(mailConfig.getServerPort());
        wiser.setHostname(mailConfig.getServerHost());
        try {
            wiser.start();

            new MailMessageImpl(mailConfig)
                    .from(MailTestUtil.getAddressHeader(this.fromName, this.fromAddress))
                    .to(person)
                    .subject(subject)
                    .bodyHtmlTextAlt(this.htmlBody, this.textBody)
                    .importance(MessagePriority.LOW)
            .deliveryReceipt(this.fromAddress)
                    .readReceipt("seam.test")
                    .addAttachment(
                            "template.text.velocity",
                            "text/plain",
                            ContentDisposition.ATTACHMENT,
                    Resources.newInputStreamSupplier(
                                    Resources.getResource("template.text.velocity")).getInput())
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
        Assert.assertEquals(this.htmlBody, MailTestUtil.getStringContent(html));

        Assert.assertTrue(textAlt.getContentType().startsWith("text/plain"));
        Assert.assertEquals(this.textBody, MailTestUtil.getStringContent(textAlt));

        Assert.assertTrue(attachment.getContentType().startsWith("text/plain"));
        Assert.assertEquals("template.text.velocity", attachment.getFileName());

        Assert.assertTrue(inlineAttachment.getContentType().startsWith("image/png;"));
        Assert.assertEquals("seamLogo.png", inlineAttachment.getFileName());
        final EmailMessage convertedMessage = MessageConverter.convert(mess);
        Assert.assertEquals(convertedMessage.getSubject(), subject);
    }

    @Test
    public void testTextMailMessageLongFields() throws MessagingException, IOException {
        final SessionConfig mailConfig = TestMailConfigs.standardConfig();

        final String subject = "Sometimes it is important to have a really long subject even if nobody is going to read it - "
                + java.util.UUID.randomUUID().toString();

        final String longFromName = "FromSometimesPeopleHaveNamesWhichAreALotLongerThanYouEverExpectedSomeoneToHaveSoItisGoodToTestUpTo100CharactersOrSo YouKnow?";
        final String longFromAddress = "sometimesPeopleHaveNamesWhichAreALotLongerThanYouEverExpectedSomeoneToHaveSoItisGoodToTestUpTo100CharactersOrSo@jboss.org";
        final String longToName = "ToSometimesPeopleHaveNamesWhichAreALotLongerThanYouEverExpectedSomeoneToHaveSoItisGoodToTestUpTo100CharactersOrSo YouKnow?";
        final String longToAddress = "toSometimesPeopleHaveNamesWhichAreALotLongerThanYouEverExpectedSomeoneToHaveSoItisGoodToTestUpTo100CharactersOrSo.seamerson@seam-mail.test";
        final String longCcName = "CCSometimesPeopleHaveNamesWhichAreALotLongerThanYouEverExpectedSomeoneToHaveSoItisGoodToTestUpTo100CharactersOrSo YouKnow? Hatty";
        final String longCcAddress = "cCSometimesPeopleHaveNamesWhichAreALotLongerThanYouEverExpectedSomeoneToHaveSoItisGoodToTestUpTo100CharactersOrSo.hatty@jboss.org";

        EmailMessage e;

        final Wiser wiser = new Wiser(mailConfig.getServerPort());
        wiser.setHostname(mailConfig.getServerHost());
        try {
            wiser.start();

            e = new MailMessageImpl(mailConfig)
                    .from(MailTestUtil.getAddressHeader(longFromName, longFromAddress))
                    .to(MailTestUtil.getAddressHeader(longToName, longToAddress))
                    .cc(MailTestUtil.getAddressHeader(longCcName, longCcAddress)).subject(subject)
                    .bodyText(this.textBody).importance(MessagePriority.HIGH).send();
        } finally {
            stop(wiser);
        }

        Assert.assertTrue("Didn't receive the expected amount of messages. Expected 2 got "
                + wiser.getMessages().size(), wiser.getMessages().size() == 2);

        final MimeMessage mess = wiser.getMessages().get(0).getMimeMessage();

        Assert.assertEquals(MailTestUtil.getAddressHeader(longFromName, longFromAddress),
                mess.getHeader("From", null));
        Assert.assertEquals(MailTestUtil.getAddressHeader(longToName, longToAddress),
                mess.getHeader("To", null));
        Assert.assertEquals(MailTestUtil.getAddressHeader(longCcName, longCcAddress),
                mess.getHeader("CC", null));
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
        Assert.assertEquals(this.textBody, MailTestUtil.getStringContent(text));
        final EmailMessage convertedMessage = MessageConverter.convert(mess);
        Assert.assertEquals(convertedMessage.getSubject(), subject);
    }

    @Test(expected = SendFailedException.class)
    public void testTextMailMessageSendFailed() {
        final SessionConfig mailConfig = TestMailConfigs.standardConfig();

        final String subject = "Text Message from Seam Mail - "
                + java.util.UUID.randomUUID().toString();
        final String messageId = "1234@seam.test.com";

        // Port is one off so this should fail
        final Wiser wiser = new Wiser(mailConfig.getServerPort() + 1);
        wiser.setHostname(mailConfig.getServerHost());

        try {
            wiser.start();

            new MailMessageImpl(mailConfig)
                    .from(MailTestUtil.getAddressHeader(this.fromName, this.fromAddress))
                    .replyTo(this.replyToAddress).to(this.toAddress).subject(subject)
                    .bodyText(this.textBody).importance(MessagePriority.HIGH).messageId(messageId)
                    .send();
        } finally {
            stop(wiser);
        }
    }

    @Test(expected = InvalidAddressException.class)
    public void testTextMailMessageInvalidAddress() throws SendFailedException {
        final SessionConfig mailConfig = TestMailConfigs.standardConfig();

        final String subject = "Text Message from Seam Mail - "
                + java.util.UUID.randomUUID().toString();

        final String messageId = "1234@seam.test.com";

        // Port is one off so this should fail
        final Wiser wiser = new Wiser(mailConfig.getServerPort() + 1);
        wiser.setHostname(mailConfig.getServerHost());

        try {
            wiser.start();

            new MailMessageImpl(mailConfig).from("seam seamerson@test.com", this.fromName)
                    .replyTo(this.replyToAddress).to(this.toAddress, this.toName).subject(subject)
                    .bodyText(this.textBody).importance(MessagePriority.HIGH).messageId(messageId)
                    .send();
        } finally {
            stop(wiser);
        }
    }

    @Test
    public void testTextMailMessageUsingPerson() throws MessagingException, IOException {
        final SessionConfig mailConfig = TestMailConfigs.standardConfig();

        final String subject = "Text Message from Seam Mail - "
                + java.util.UUID.randomUUID().toString();
        final Person person = new Person(this.toName, this.toAddress);
        final String messageId = "1234@seam.test.com";

        EmailMessage e;

        final Wiser wiser = new Wiser(mailConfig.getServerPort());
        wiser.setHostname(mailConfig.getServerHost());
        try {
            wiser.start();

            e = new MailMessageImpl(mailConfig)
                    .from(MailTestUtil.getAddressHeader(this.fromName, this.fromAddress))
                    .replyTo(this.replyToAddress).to(person).subject(subject)
                    .bodyText(this.textBody).importance(MessagePriority.HIGH).messageId(messageId)
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
        Assert.assertEquals(messageId,
                MailUtility.headerStripper(mess.getHeader("Message-ID", null)));

        final MimeMultipart mixed = (MimeMultipart) mess.getContent();
        final BodyPart text = mixed.getBodyPart(0);

        Assert.assertTrue(mixed.getContentType().startsWith("multipart/mixed"));
        Assert.assertEquals(1, mixed.getCount());

        Assert.assertTrue("Incorrect Charset: " + e.getCharset(),
                text.getContentType().startsWith("text/plain; charset=" + e.getCharset()));
        Assert.assertEquals(this.textBody, MailTestUtil.getStringContent(text));
        final EmailMessage convertedMessage = MessageConverter.convert(mess);
        Assert.assertEquals(convertedMessage.getSubject(), subject);
    }

    @Test
    public void testTextMailMessageUsingDefaultSession() throws MessagingException, IOException {
        final SessionConfig mailConfig = TestMailConfigs.standardConfig();
        final Person person = new Person(this.toName, this.toAddress);
        final String subject = "Text Message from Seam Mail - "
                + java.util.UUID.randomUUID().toString();

        final String messageId = "1234@seam.test.com";

        EmailMessage e;

        final Wiser wiser = new Wiser(mailConfig.getServerPort());
        wiser.setHostname(mailConfig.getServerHost());
        try {
            wiser.start();
            e = new MailMessageImpl(mailConfig)
                    .from(MailTestUtil.getAddressHeader(this.fromName, this.fromAddress))
                    .replyTo(this.replyToAddress).to(person).subject(subject)
                    .bodyText(this.textBody).importance(MessagePriority.HIGH).messageId(messageId)
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
        Assert.assertEquals(messageId,
                MailUtility.headerStripper(mess.getHeader("Message-ID", null)));

        final MimeMultipart mixed = (MimeMultipart) mess.getContent();
        final BodyPart text = mixed.getBodyPart(0);

        Assert.assertTrue(mixed.getContentType().startsWith("multipart/mixed"));
        Assert.assertEquals(1, mixed.getCount());

        Assert.assertTrue("Incorrect Charset: " + e.getCharset(),
                text.getContentType().startsWith("text/plain; charset=" + e.getCharset()));
        Assert.assertEquals(this.textBody, MailTestUtil.getStringContent(text));
        final EmailMessage convertedMessage = MessageConverter.convert(mess);
        Assert.assertEquals(convertedMessage.getSubject(), subject);
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
}
