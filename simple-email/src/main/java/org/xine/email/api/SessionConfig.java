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

/**
 * Bean which holds Mail Session configuration options.
 */
public interface SessionConfig {

    /**
     * Gets the server host.
     * @return the server host
     */
    String getServerHost();

    /**
     * Gets the server port.
     * @return the server port
     */
    Integer getServerPort();

    /**
     * Gets the domain name.
     * @return the domain name
     */
    String getDomainName();

    /**
     * Gets the username.
     * @return the username
     */
    String getUsername();

    /**
     * Gets the password.
     * @return the password
     */
    String getPassword();

    /**
     * Gets the enable tls.
     * @return the enable tls
     */
    Boolean getEnableTls();

    /**
     * Gets the require tls.
     * @return the require tls
     */
    Boolean getRequireTls();

    /**
     * Gets the enable ssl.
     * @return the enable ssl
     */
    Boolean getEnableSsl();

    /**
     * Gets the auth.
     * @return the auth
     */
    Boolean getAuth();

    /**
     * Gets the jndi session name.
     * @return the jndi session name
     */
    String getJndiSessionName();
}
