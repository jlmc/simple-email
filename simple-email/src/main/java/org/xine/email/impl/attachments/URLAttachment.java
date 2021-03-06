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
import java.net.MalformedURLException;
import java.net.URL;

import javax.activation.URLDataSource;

public class URLAttachment extends BaseAttachment {
    public URLAttachment(final String url, final String fileName,
            final ContentDisposition contentDisposition) {
        super();
        URLDataSource uds;
        try {
            uds = new URLDataSource(new URL(url));
            super.setFileName(fileName);
            super.setMimeType(uds.getContentType());
            super.setContentDisposition(contentDisposition);
            super.setBytes(Streams.toByteArray(uds.getInputStream()));
        } catch (final MalformedURLException e) {
            throw new AttachmentException(
                    "Wasn't able to create email attachment from URL: " + url, e);
        } catch (final IOException e) {
            throw new AttachmentException(
                    "Wasn't able to create email attachment from URL: " + url, e);
        }
    }

    public URLAttachment(final String url, final String fileName,
            final ContentDisposition contentDisposition, final String contentClass) {
        this(url, fileName, contentDisposition);
        super.addHeader(new Header("Content-Class", contentClass));
    }
}
