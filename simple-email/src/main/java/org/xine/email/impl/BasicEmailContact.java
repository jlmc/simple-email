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

import org.xine.email.api.EmailContact;

/**
 * The Class BasicEmailContact.
 */
public class BasicEmailContact implements EmailContact {

    /** The address. */
    private String address;

    /** The name. */
    private String name;

    /**
     * Instantiates a new basic email contact.
     * @param address
     *            the address
     */
    public BasicEmailContact(final String address) {
        this.address = address;
    }

    /**
     * Instantiates a new basic email contact.
     * @param address
     *            the address
     * @param name
     *            the name
     */
    public BasicEmailContact(final String address, final String name) {
        this.address = address;
        this.name = name;
    }

    /*
     * (non-Javadoc)
     * @see org.xine.email.api.EmailContact#getAddress()
     */
    @Override
    public String getAddress() {
        return this.address;
    }

    /**
     * Sets the address.
     * @param address
     *            the new address
     */
    public void setAddress(final String address) {
        this.address = address;
    }

    /*
     * (non-Javadoc)
     * @see org.xine.email.api.EmailContact#getName()
     */
    @Override
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

    /*
     * (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object o) {
        final EmailContact e = (EmailContact) o;

        return toString().equals(e.toString());
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        if (this.name == null || this.name.length() == 0) {
            return this.address;
        } else {
            return this.name + " <" + this.address + ">";
        }
    }
}
