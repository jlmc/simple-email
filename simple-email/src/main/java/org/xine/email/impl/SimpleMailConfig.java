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

import java.io.Serializable;

import org.xine.email.api.SessionConfig;

/**
 * Bean which holds Mail Session configuration options.
 */
public class SimpleMailConfig implements Serializable, SessionConfig {

    private static final long serialVersionUID = 1L;

    private String serverHost = "localhost";
    private Integer serverPort = 25;
    private String domainName;
    private String username;
    private String password;
    private Boolean enableTls = Boolean.FALSE;
    private Boolean requireTls = Boolean.FALSE;
    private Boolean enableSsl = Boolean.FALSE;
    private Boolean auth = Boolean.FALSE;
    private String jndiSessionName;

    @Override
    public String getServerHost() {
        return this.serverHost;
    }

    public void setServerHost(final String serverHost) {
        this.serverHost = serverHost;
    }

    @Override
    public Integer getServerPort() {
        return this.serverPort;
    }

    public void setServerPort(final Integer serverPort) {
        this.serverPort = serverPort;
    }

    @Override
    public String getDomainName() {
        return this.domainName;
    }

    public void setDomainName(final String domainName) {
        this.domainName = domainName;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    @Override
    public Boolean getEnableTls() {
        return this.enableTls;
    }

    public void setEnableTls(final Boolean enableTls) {
        this.enableTls = enableTls;
    }

    @Override
    public Boolean getRequireTls() {
        return this.requireTls;
    }

    public void setRequireTls(final Boolean requireTls) {
        this.requireTls = requireTls;
    }

    @Override
    public Boolean getEnableSsl() {
        return this.enableSsl;
    }

    public void setEnableSsl(final Boolean enableSsl) {
        this.enableSsl = enableSsl;
    }

    @Override
    public Boolean getAuth() {
        return this.auth;
    }

    public void setAuth(final Boolean auth) {
        this.auth = auth;
    }

    @Override
    public String getJndiSessionName() {
        return this.jndiSessionName;
    }

    public void setJndiSessionName(final String jndiSessionName) {
        this.jndiSessionName = jndiSessionName;
    }

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
