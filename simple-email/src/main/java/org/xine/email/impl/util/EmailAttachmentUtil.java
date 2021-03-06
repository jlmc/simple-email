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

import org.xine.email.impl.attachments.BaseAttachment;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * The Class EmailAttachmentUtil.
 */
public class EmailAttachmentUtil {

    /**
     * Gets the email attachment map.
     * @param attachments
     *            the attachments
     * @return the email attachment map
     */
    public static Map<String, BaseAttachment> getEmailAttachmentMap(
            final Collection<BaseAttachment> attachments) {
        final Map<String, BaseAttachment> emailAttachmentMap = new HashMap<>();

        for (final BaseAttachment ea : attachments) {
            if (!Strings.isNullOrBlank(ea.getFileName())) {
                emailAttachmentMap.put(ea.getFileName(), ea);
            }
        }
        return emailAttachmentMap;
    }
}
