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

import java.io.Serializable;
import java.io.UnsupportedEncodingException;

import javax.mail.internet.MimeUtility;
import javax.xml.bind.annotation.XmlElement;

/**
 * The Class Header.
 */
public class Header implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The name. */
    private String name;

    /** The value. */
    private String value;

    /**
     * Instantiates a new header.
     */
    public Header() {
        // Required for JAX-B
    }

    /**
     * Instantiates a new header.
     * @param name
     *            the name
     * @param value
     *            the value
     */
    public Header(final String name, final String value) {
        this.name = name;

        try {
            this.value = MimeUtility.fold(name.length() + 2, MimeUtility.encodeText(value));
        } catch (final UnsupportedEncodingException e) {
            throw new RuntimeException("Unable to create header", e);
        }
    }

    /**
     * Gets the name.
     * @return the name
     */
    @XmlElement
    public String getName() {
        return this.name;
    }

    /**
     * Sets the name.
     * @param name
     *            the new name
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Gets the value.
     * @return the value
     */
    @XmlElement
    public String getValue() {
        return this.value;
    }

    /**
     * Sets the value.
     * @param value
     *            the new value
     */
    public void setValue(final String value) {
        this.value = value;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object o) {

        final Header h = (Header) o;

        return this.name.equals(h.getName()) || this.value.equals(h.getValue());
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return this.name.hashCode() + this.value.hashCode();
    }

}
