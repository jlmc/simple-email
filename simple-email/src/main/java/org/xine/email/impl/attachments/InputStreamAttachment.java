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

package org.xine.email.impl.attachments;

import org.xine.email.api.AttachmentException;
import org.xine.email.api.ContentDisposition;
import org.xine.email.api.Header;
import org.xine.email.impl.util.Streams;

import java.io.IOException;
import java.io.InputStream;

public class InputStreamAttachment extends BaseAttachment {
    public InputStreamAttachment(final String fileName, final String mimeType,
            final ContentDisposition contentDisposition, final InputStream inputStream) {
        super();

        try {
            super.setFileName(fileName);
            super.setMimeType(mimeType);
            super.setContentDisposition(contentDisposition);
            super.setBytes(Streams.toByteArray(inputStream));
        } catch (final IOException e) {
            throw new AttachmentException("Wasn't able to create email attachment from InputStream");
        }
    }

    public InputStreamAttachment(final String fileName, final String mimeType,
            final ContentDisposition contentDisposition, final InputStream inputStream,
            final String contentClass) {
        this(fileName, mimeType, contentDisposition, inputStream);
        super.addHeader(new Header("Content-Class", contentClass));
    }
}
