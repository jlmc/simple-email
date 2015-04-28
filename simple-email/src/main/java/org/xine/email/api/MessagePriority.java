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
 * The Enum MessagePriority.
 */
public enum MessagePriority {

    /** The low. */
    LOW("5", "non-urgent", "low"),
    /** The normal. */
    NORMAL("3", "normal", "normal"),
    /** The high. */
    HIGH("1", "urgent", "high");

    /** The x priority. */
    private String xPriority;

    /** The priority. */
    private String priority;

    /** The importance. */
    private String importance;

    /**
     * Instantiates a new message priority.
     * @param x_priority
     *            the x_priority
     * @param priority
     *            the priority
     * @param importance
     *            the importance
     */
    private MessagePriority(final String x_priority, final String priority, final String importance) {
        this.xPriority = x_priority;
        this.priority = priority;
        this.importance = importance;
    }

    /**
     * Gets the x_priority.
     * @return the x_priority
     */
    public String getX_priority() {
        return this.xPriority;
    }

    /**
     * Gets the priority.
     * @return the priority
     */
    public String getPriority() {
        return this.priority;
    }

    /**
     * Gets the importance.
     * @return the importance
     */
    public String getImportance() {
        return this.importance;
    }
}
