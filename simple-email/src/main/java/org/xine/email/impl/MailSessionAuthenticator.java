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

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * The Class MailSessionAuthenticator.
 */
public class MailSessionAuthenticator extends Authenticator {

    /** The username. */
    private final String username;

    /** The password. */
    private final String password;

    /**
     * Instantiates a new mail session authenticator.
     * @param username
     *            the username
     * @param password
     *            the password
     */
    public MailSessionAuthenticator(final String username, final String password) {
        this.username = username;
        this.password = password;
    }

    /*
     * (non-Javadoc)
     * @see javax.mail.Authenticator#getPasswordAuthentication()
     */
    @Override
    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(this.username, this.password);
    }
}
