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

package org.xine.email.impl.templating.velocity;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.RuntimeConstants;
import org.xine.email.api.TemplateProvider;
import org.xine.email.api.TemplatingException;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.Map;

/**
 * The Class VelocityTemplate.
 */
public class VelocityTemplate implements TemplateProvider {

    /** The velocity engine. */
    private final VelocityEngine velocityEngine;

    /** The velocity context. */
    private VelocityContext velocityContext;

    /** The input stream. */
    private final InputStream inputStream;

    /**
     * Instantiates a new velocity template.
     * @param inputStream
     *            the input stream
     */
    public VelocityTemplate(final InputStream inputStream) {
        this.velocityEngine = new VelocityEngine();
        this.velocityEngine.setProperty(RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS,
                "org.apache.velocity.runtime.log.NullLogChute");
        this.inputStream = inputStream;
    }

    /**
     * Instantiates a new velocity template.
     * @param string
     *            the string
     */
    public VelocityTemplate(final String string) {
        this(new ByteArrayInputStream(string.getBytes()));
    }

    /**
     * Instantiates a new velocity template.
     * @param file
     *            the file
     * @throws FileNotFoundException
     *             the file not found exception
     */
    public VelocityTemplate(final File file) throws FileNotFoundException {
        this(new FileInputStream(file));
    }

    /*
     * (non-Javadoc)
     * @see org.xine.email.api.TemplateProvider#merge(java.util.Map)
     */
    @Override
    public String merge(final Map<String, Object> context) {
        final StringWriter writer = new StringWriter();

        this.velocityContext = new VelocityContext(context);

        try {
            this.velocityEngine.evaluate(this.velocityContext, writer, "mailGenerated",
                    new InputStreamReader(this.inputStream));
        } catch (final ResourceNotFoundException e) {
            throw new TemplatingException("Unable to find template", e);
        } catch (final ParseErrorException e) {
            throw new TemplatingException("Unable to find template", e);
        } catch (final MethodInvocationException e) {
            throw new TemplatingException("Error processing method referenced in context", e);
        }

        return writer.toString();
    }
}
