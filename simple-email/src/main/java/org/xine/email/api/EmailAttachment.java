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

import java.util.Collection;

/**
 * The Interface EmailAttachment.
 */
public interface EmailAttachment {

    /**
     * Gets the content id.
     * @return the content id
     */
    public String getContentId();

    /**
     * Gets the file name.
     * @return the file name
     */
    public String getFileName();

    /**
     * Gets the mime type.
     * @return the mime type
     */
    public String getMimeType();

    /**
     * Gets the content disposition.
     * @return the content disposition
     */
    public ContentDisposition getContentDisposition();

    /**
     * Gets the headers.
     * @return the headers
     */
    public Collection<Header> getHeaders();

    /**
     * Gets the bytes.
     * @return the bytes
     */
    public byte[] getBytes();
}
