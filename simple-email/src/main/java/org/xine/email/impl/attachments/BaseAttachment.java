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
import org.xine.email.api.EmailAttachment;
import org.xine.email.api.Header;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

/**
 * The Class BaseAttachment.
 */
public class BaseAttachment implements EmailAttachment {

    /** The content id. */
    private String contentId;

    /** The file name. */
    private String fileName;

    /** The mime type. */
    private String mimeType;

    /** The content disposition. */
    private ContentDisposition contentDisposition;

    /** The headers. */
    private List<Header> headers = new ArrayList<>();

    /** The bytes. */
    private byte[] bytes;

    /**
     * Instantiates a new base attachment.
     * @param fileName
     *            the file name
     * @param mimeType
     *            the mime type
     * @param contentDisposition
     *            the content disposition
     * @param bytes
     *            the bytes
     */
    public BaseAttachment(final String fileName, final String mimeType,
            final ContentDisposition contentDisposition, final byte[] bytes) {
        this();
        this.fileName = fileName;
        this.mimeType = mimeType;
        this.contentDisposition = contentDisposition;
        this.bytes = bytes;
    }

    /**
     * Instantiates a new base attachment.
     * @param fileName
     *            the file name
     * @param mimeType
     *            the mime type
     * @param contentDisposition
     *            the content disposition
     * @param bytes
     *            the bytes
     * @param contentClass
     *            the content class
     */
    public BaseAttachment(final String fileName, final String mimeType,
            final ContentDisposition contentDisposition, final byte[] bytes,
            final String contentClass) {
        this(fileName, mimeType, contentDisposition, bytes);
        addHeader(new Header("Content-Class", contentClass));
    }

    /**
     * Instantiates a new base attachment.
     */
    public BaseAttachment() {
        this.contentId = UUID.randomUUID().toString();
    }

    /*
     * (non-Javadoc)
     * @see org.xine.email.api.EmailAttachment#getContentId()
     */
    @Override
    @XmlElement
    public String getContentId() {
        return this.contentId;
    }

    /**
     * Sets the contenet id.
     * @param contentId
     *            the new contenet id
     */
    public void setContenetId(final String contentId) {
        this.contentId = contentId;
    }

    /*
     * (non-Javadoc)
     * @see org.xine.email.api.EmailAttachment#getFileName()
     */
    @Override
    @XmlElement
    public String getFileName() {
        return this.fileName;
    }

    /**
     * Sets the file name.
     * @param fileName
     *            the new file name
     */
    public void setFileName(final String fileName) {
        this.fileName = fileName;
    }

    /*
     * (non-Javadoc)
     * @see org.xine.email.api.EmailAttachment#getMimeType()
     */
    @Override
    @XmlElement
    public String getMimeType() {
        return this.mimeType;
    }

    /**
     * Sets the mime type.
     * @param mimeType
     *            the new mime type
     */
    public void setMimeType(final String mimeType) {
        this.mimeType = mimeType;
    }

    /*
     * (non-Javadoc)
     * @see org.xine.email.api.EmailAttachment#getContentDisposition()
     */
    @Override
    @XmlElement
    public ContentDisposition getContentDisposition() {
        return this.contentDisposition;
    }

    /**
     * Sets the content disposition.
     * @param contentDisposition
     *            the new content disposition
     */
    public void setContentDisposition(final ContentDisposition contentDisposition) {
        this.contentDisposition = contentDisposition;
    }

    /*
     * (non-Javadoc)
     * @see org.xine.email.api.EmailAttachment#getHeaders()
     */
    @Override
    @XmlElementWrapper(name = "headers")
    @XmlElement(name = "header")
    public List<Header> getHeaders() {
        return this.headers;
    }

    /**
     * Sets the headers.
     * @param headers
     *            the new headers
     */
    public void setHeaders(final List<Header> headers) {
        this.headers = headers;
    }

    /**
     * Adds the header.
     * @param header
     *            the header
     */
    public void addHeader(final Header header) {
        this.headers.add(header);
    }

    /**
     * Adds the headers.
     * @param headers
     *            the headers
     */
    public void addHeaders(final Collection<Header> headers) {
        headers.addAll(headers);
    }

    /*
     * (non-Javadoc)
     * @see org.xine.email.api.EmailAttachment#getBytes()
     */
    @Override
    @XmlElement
    public byte[] getBytes() {
        return this.bytes;
    }

    /**
     * Sets the bytes.
     * @param bytes
     *            the new bytes
     */
    public void setBytes(final byte[] bytes) {
        this.bytes = bytes;
    }
}
