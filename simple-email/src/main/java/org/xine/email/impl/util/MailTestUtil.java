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

package org.xine.email.impl.util;

import java.io.IOException;

import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMultipart;

/**
 * The Class MailTestUtil.
 */
public class MailTestUtil {

    /**
     * Gets the address header.
     * @param address
     *            the address
     * @return the address header
     */
    public static String getAddressHeader(final String address) {
        return address;
    }

    /**
     * Gets the address header.
     * @param name
     *            the name
     * @param address
     *            the address
     * @return the address header
     */
    public static String getAddressHeader(final String name, final String address) {
        return name + " <" + address + ">";
    }

    /**
     * Gets the string content.
     * @param mmp
     *            the mmp
     * @param index
     *            the index
     * @return the string content
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     * @throws MessagingException
     *             the messaging exception
     */
    public static String getStringContent(final MimeMultipart mmp, final int index)
            throws IOException, MessagingException {
        return getStringContent(mmp.getBodyPart(index));
    }

    /**
     * Gets the string content.
     * @param bodyPart
     *            the body part
     * @return the string content
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     * @throws MessagingException
     *             the messaging exception
     */
    public static String getStringContent(final BodyPart bodyPart) throws IOException,
            MessagingException {
        return (String) bodyPart.getContent();
    }
}
