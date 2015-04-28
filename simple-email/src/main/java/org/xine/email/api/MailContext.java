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

package org.xine.email.api;

import org.xine.email.impl.attachments.BaseAttachment;

import java.util.Map;

/**
 * The Class MailContext.
 */
public class MailContext {

    /** The attachments. */
    private final Map<String, BaseAttachment> attachments;

    /**
     * Instantiates a new mail context.
     * @param attachments
     *            the attachments
     */
    public MailContext(final Map<String, BaseAttachment> attachments) {
        this.attachments = attachments;
    }

    /**
     * Insert.
     * @param fileName
     *            the file name
     * @return the string
     */
    public String insert(final String fileName) {
        BaseAttachment attachment = null;

        attachment = this.attachments.get(fileName);

        if (attachment == null) {
            throw new RuntimeException("Unable to find attachment: " + fileName);
        }

        return "cid:" + attachment.getContentId();
    }
}
