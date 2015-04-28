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

import org.xine.email.api.ContentDisposition;
import org.xine.email.api.Header;

import java.util.ArrayList;
import java.util.Collection;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.util.ByteArrayDataSource;

/**
 * The Class AttachmentPart.
 */
public class AttachmentPart extends MimeBodyPart {

    /** The uid. */
    private final String uid;

    /**
     * Instantiates a new attachment part.
     * @param dataSource
     *            the data source
     * @param uid
     *            the uid
     * @param fileName
     *            the file name
     * @param headers
     *            the headers
     * @param contentDisposition
     *            the content disposition
     */
    public AttachmentPart(final DataSource dataSource, final String uid, final String fileName,
            final Collection<Header> headers, final ContentDisposition contentDisposition) {
        super();

        this.uid = uid;

        try {
            setContentID("<" + uid + ">");
        } catch (final MessagingException e1) {
            throw new RuntimeException("Unable to set unique content-id on attachment");
        }

        setData(dataSource);

        if (fileName != null) {
            try {
                setFileName(fileName);
            } catch (final MessagingException e) {
                throw new RuntimeException("Unable to get FileName on attachment");
            }
        }

        if (headers != null) {
            for (final Header header : headers) {
                try {
                    addHeader(header.getName(), header.getValue());

                } catch (final MessagingException e) {
                    throw new RuntimeException("Unable to add Content-Class Header");
                }
            }
        }

        setContentDisposition(contentDisposition);
    }

    /**
     * Instantiates a new attachment part.
     * @param bytes
     *            the bytes
     * @param uid
     *            the uid
     * @param fileName
     *            the file name
     * @param mimeType
     *            the mime type
     * @param headers
     *            the headers
     * @param contentDisposition
     *            the content disposition
     */
    public AttachmentPart(final byte[] bytes, final String uid, final String fileName,
            final String mimeType, final Collection<Header> headers,
            final ContentDisposition contentDisposition) {
        this(getByteArrayDataSource(bytes, mimeType), uid, fileName, headers, contentDisposition);
    }

    /**
     * Instantiates a new attachment part.
     * @param bytes
     *            the bytes
     * @param uid
     *            the uid
     * @param fileName
     *            the file name
     * @param mimeType
     *            the mime type
     * @param contentDisposition
     *            the content disposition
     */
    public AttachmentPart(final byte[] bytes, final String uid, final String fileName,
            final String mimeType, final ContentDisposition contentDisposition) {
        this(getByteArrayDataSource(bytes, mimeType), uid, fileName, new ArrayList<Header>(),
                contentDisposition);
    }

    /**
     * Gets the attachment file name.
     * @return the attachment file name
     */
    public String getAttachmentFileName() {
        try {
            return getFileName();
        } catch (final MessagingException e) {
            throw new RuntimeException("Unable to get File Name from attachment");
        }
    }

    /**
     * Gets the content disposition.
     * @return the content disposition
     */
    public ContentDisposition getContentDisposition() {
        try {
            return ContentDisposition.mapValue(getDisposition());
        } catch (final MessagingException e) {
            throw new RuntimeException("Unable to get Content-Dispostion on attachment");
        }
    }

    /**
     * Gets the uid.
     * @return the uid
     */
    public String getUid() {
        return this.uid;
    }

    /**
     * Sets the content disposition.
     * @param contentDisposition
     *            the new content disposition
     */
    public void setContentDisposition(final ContentDisposition contentDisposition) {
        try {
            setDisposition(contentDisposition.headerValue());
        } catch (final MessagingException e) {
            throw new RuntimeException("Unable to set Content-Dispostion on attachment");
        }
    }

    /**
     * Sets the data.
     * @param datasource
     *            the new data
     */
    private void setData(final DataSource datasource) {
        try {
            setDataHandler(new DataHandler(datasource));
        } catch (final MessagingException e) {
            throw new RuntimeException("Unable to set Data on attachment");
        }
    }

    /**
     * Gets the byte array data source.
     * @param bytes
     *            the bytes
     * @param mimeType
     *            the mime type
     * @return the byte array data source
     */
    private static ByteArrayDataSource getByteArrayDataSource(final byte[] bytes,
            final String mimeType) {
        final ByteArrayDataSource bads = new ByteArrayDataSource(bytes, mimeType);
        return bads;
    }
}
