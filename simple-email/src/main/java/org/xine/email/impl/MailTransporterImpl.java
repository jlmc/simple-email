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

import org.xine.email.api.EmailMessage;
import org.xine.email.api.MailTransporter;
import org.xine.email.impl.util.MailUtility;

import javax.mail.Session;

/**
 * The Class MailTransporterImpl.
 */
public class MailTransporterImpl implements MailTransporter {

    /** The session. */
    private final Session session;

    /**
     * Instantiates a new mail transporter impl.
     * @param session
     *            the session
     */
    public MailTransporterImpl(final Session session) {
        this.session = session;
    }

    /*
     * (non-Javadoc)
     * @see org.xine.email.api.MailTransporter#send(org.xine.email.api.EmailMessage)
     */
    @Override
    public EmailMessage send(final EmailMessage emailMessage) {
        MailUtility.send(emailMessage, this.session);
        return emailMessage;
    }

}
