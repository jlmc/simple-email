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
import org.xine.email.api.EmailContact;
import org.xine.email.api.InvalidAddressException;
import org.xine.email.api.MailMessage;
import org.xine.email.impl.BasicEmailContact;
import org.xine.email.impl.MailMessageImpl;
import org.xine.email.util.TestMailConfigs;

import java.util.ArrayList;
import java.util.Collection;

/**
 */
public class InternetAddressTest {
    @Test
    public void validAddresses() {
        final MailMessage m = new MailMessageImpl(TestMailConfigs.standardConfig());

        final BasicEmailContact seam = new BasicEmailContact("seam@domain.test");
        final BasicEmailContact seamey = new BasicEmailContact("seamey@domain.test");

        final Collection<EmailContact> addresses = new ArrayList<EmailContact>();
        addresses.add(seamey);

        m.from("seam@domain.test", "Seam Seamerson<seam@domain.test>");
        m.from("Seam Seamerson<seam@domain.test>");
        m.from("seam@domain.test");
        m.from(seam);
        m.from(addresses);

        m.to("seam@domain.test", "Seam Seamerson<seam@domain.test>");
        m.to("Seam Seamerson<seam@domain.test>");
        m.to("seam@domain.test");
        m.to(seam);
        m.to(addresses);

        m.cc("seam@domain.test", "Seam Seamerson<seam@domain.test>");
        m.cc("Seam Seamerson<seam@domain.test>");
        m.cc("seam@domain.test");
        m.cc(seam);
        m.cc(addresses);

        m.bcc("seam@domain.test", "Seam Seamerson<seam@domain.test>");
        m.bcc("Seam Seamerson<seam@domain.test>");
        m.bcc("seam@domain.test");
        m.bcc(seam);
        m.bcc(addresses);

        m.replyTo("seam@domain.test", "Seam Seamerson<seam@domain.test>");
        m.replyTo("Seam Seamerson<seam@domain.test>");
        m.replyTo("seam@domain.test");
        m.replyTo(seam);
        m.replyTo(addresses);
    }

    @Test(expected = InvalidAddressException.class)
    public void invalidFromSimpleAddresses() {
        final MailMessage m = new MailMessageImpl(TestMailConfigs.standardConfig());

        m.from("woo foo @bar.com");
    }

    @Test(expected = InvalidAddressException.class)
    public void invalidFromFullAddresses() {
        final MailMessage m = new MailMessageImpl(TestMailConfigs.standardConfig());

        m.from("foo @bar.com", "Woo");
    }

    @Test(expected = InvalidAddressException.class)
    public void invalidToSimpleAddresses() {
        final MailMessage m = new MailMessageImpl(TestMailConfigs.standardConfig());

        m.to("woo foo @bar.com");
    }

    @Test(expected = InvalidAddressException.class)
    public void invalidToFullAddresses() {
        final MailMessage m = new MailMessageImpl(TestMailConfigs.standardConfig());

        m.to("foo @bar.com", "Woo");
    }

    @Test(expected = InvalidAddressException.class)
    public void invalidCcSimpleAddresses() {
        final MailMessage m = new MailMessageImpl(TestMailConfigs.standardConfig());

        m.cc("woo foo @bar.com");
    }

    @Test(expected = InvalidAddressException.class)
    public void invalidCcFullAddresses() {
        final MailMessage m = new MailMessageImpl(TestMailConfigs.standardConfig());

        m.cc("foo @bar.com", "Woo");
    }

    @Test(expected = InvalidAddressException.class)
    public void invalidBccSimpleAddresses() {
        final MailMessage m = new MailMessageImpl(TestMailConfigs.standardConfig());

        m.bcc("woo foo @bar.com");
    }

    @Test(expected = InvalidAddressException.class)
    public void invalidbccFullAddresses() {
        final MailMessage m = new MailMessageImpl(TestMailConfigs.standardConfig());

        m.bcc("foo @bar.com", "Woo");
    }

    @Test(expected = InvalidAddressException.class)
    public void invalidReplyToSimpleAddresses() {
        final MailMessage m = new MailMessageImpl(TestMailConfigs.standardConfig());

        m.replyTo("woo foo @bar.com");
    }

    @Test(expected = InvalidAddressException.class)
    public void invalidReplyToFullAddresses() {
        final MailMessage m = new MailMessageImpl(TestMailConfigs.standardConfig());

        m.replyTo("foo @bar.com", "Woo");
    }

    @Test(expected = InvalidAddressException.class)
    public void invalidDeliveryReceipt() {
        final MailMessage m = new MailMessageImpl(TestMailConfigs.standardConfig());

        m.deliveryReceipt("woo foo @bar.com");
    }

    @Test(expected = InvalidAddressException.class)
    public void invalidReadReceipt() {
        final MailMessage m = new MailMessageImpl(TestMailConfigs.standardConfig());

        m.readReceipt("woo foo @bar.com");
    }
}
