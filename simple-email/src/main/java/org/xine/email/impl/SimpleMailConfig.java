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

import org.xine.email.api.SessionConfig;

import java.io.Serializable;

/**
 * Bean which holds Mail Session configuration options.
 */
public class SimpleMailConfig implements Serializable, SessionConfig {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The server host. */
    private String serverHost = "localhost";

    /** The server port. */
    private Integer serverPort = 25;

    /** The domain name. */
    private String domainName;

    /** The username. */
    private String username;

    /** The password. */
    private String password;

    /** The enable tls. */
    private Boolean enableTls = Boolean.FALSE;

    /** The require tls. */
    private Boolean requireTls = Boolean.FALSE;

    /** The enable ssl. */
    private Boolean enableSsl = Boolean.FALSE;

    /** The auth. */
    private Boolean auth = Boolean.FALSE;

    /** The jndi session name. */
    private String jndiSessionName;

    /*
     * (non-Javadoc)
     * @see org.xine.email.api.SessionConfig#getServerHost()
     */
    @Override
    public String getServerHost() {
        return this.serverHost;
    }

    /**
     * Sets the server host.
     * @param serverHost
     *            the new server host
     */
    public void setServerHost(final String serverHost) {
        this.serverHost = serverHost;
    }

    /*
     * (non-Javadoc)
     * @see org.xine.email.api.SessionConfig#getServerPort()
     */
    @Override
    public Integer getServerPort() {
        return this.serverPort;
    }

    /**
     * Sets the server port.
     * @param serverPort
     *            the new server port
     */
    public void setServerPort(final Integer serverPort) {
        this.serverPort = serverPort;
    }

    /*
     * (non-Javadoc)
     * @see org.xine.email.api.SessionConfig#getDomainName()
     */
    @Override
    public String getDomainName() {
        return this.domainName;
    }

    /**
     * Sets the domain name.
     * @param domainName
     *            the new domain name
     */
    public void setDomainName(final String domainName) {
        this.domainName = domainName;
    }

    /*
     * (non-Javadoc)
     * @see org.xine.email.api.SessionConfig#getUsername()
     */
    @Override
    public String getUsername() {
        return this.username;
    }

    /**
     * Sets the username.
     * @param username
     *            the new username
     */
    public void setUsername(final String username) {
        this.username = username;
    }

    /*
     * (non-Javadoc)
     * @see org.xine.email.api.SessionConfig#getPassword()
     */
    @Override
    public String getPassword() {
        return this.password;
    }

    /**
     * Sets the password.
     * @param password
     *            the new password
     */
    public void setPassword(final String password) {
        this.password = password;
    }

    /*
     * (non-Javadoc)
     * @see org.xine.email.api.SessionConfig#getEnableTls()
     */
    @Override
    public Boolean getEnableTls() {
        return this.enableTls;
    }

    /**
     * Sets the enable tls.
     * @param enableTls
     *            the new enable tls
     */
    public void setEnableTls(final Boolean enableTls) {
        this.enableTls = enableTls;
    }

    /*
     * (non-Javadoc)
     * @see org.xine.email.api.SessionConfig#getRequireTls()
     */
    @Override
    public Boolean getRequireTls() {
        return this.requireTls;
    }

    /**
     * Sets the require tls.
     * @param requireTls
     *            the new require tls
     */
    public void setRequireTls(final Boolean requireTls) {
        this.requireTls = requireTls;
    }

    /*
     * (non-Javadoc)
     * @see org.xine.email.api.SessionConfig#getEnableSsl()
     */
    @Override
    public Boolean getEnableSsl() {
        return this.enableSsl;
    }

    /**
     * Sets the enable ssl.
     * @param enableSsl
     *            the new enable ssl
     */
    public void setEnableSsl(final Boolean enableSsl) {
        this.enableSsl = enableSsl;
    }

    /*
     * (non-Javadoc)
     * @see org.xine.email.api.SessionConfig#getAuth()
     */
    @Override
    public Boolean getAuth() {
        return this.auth;
    }

    /**
     * Sets the auth.
     * @param auth
     *            the new auth
     */
    public void setAuth(final Boolean auth) {
        this.auth = auth;
    }

    /*
     * (non-Javadoc)
     * @see org.xine.email.api.SessionConfig#getJndiSessionName()
     */
    @Override
    public String getJndiSessionName() {
        return this.jndiSessionName;
    }

    /**
     * Sets the jndi session name.
     * @param jndiSessionName
     *            the new jndi session name
     */
    public void setJndiSessionName(final String jndiSessionName) {
        this.jndiSessionName = jndiSessionName;
    }

    /**
     * Checks if is valid.
     * @return true, if is valid
     */
    public boolean isValid() {

        if (this.jndiSessionName != null && !this.jndiSessionName.trim().isEmpty()) {
            return true;
        }

        if (this.serverHost == null || this.serverHost.trim().isEmpty()) {
            return false;
        }

        if (this.serverPort == 0) {
            return false;
        }

        return true;
    }

}
