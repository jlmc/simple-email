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

package org.xine.email.impl.templating.freemarker;

import org.xine.email.api.TemplateProvider;
import org.xine.email.api.TemplatingException;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

public class FreeMarkerTemplate implements TemplateProvider {
    private final Configuration configuration;
    private final Map<String, Object> rootMap = new HashMap<>();
    private final InputStream inputStream;

    public FreeMarkerTemplate(final InputStream inputStream) {
        this.inputStream = inputStream;
        this.configuration = new Configuration();
        this.configuration.setObjectWrapper(new DefaultObjectWrapper());
    }

    public FreeMarkerTemplate(final String string) {
        this(new ByteArrayInputStream(string.getBytes()));
    }

    public FreeMarkerTemplate(final File file) throws FileNotFoundException {
        this(new FileInputStream(file));
    }

    @Override
    public String merge(final Map<String, Object> context) {
        this.rootMap.putAll(context);

        final StringWriter writer = new StringWriter();

        try {
            final Template template = new Template("mailGenerated", new InputStreamReader(
                    this.inputStream), this.configuration);
            template.process(this.rootMap, writer);
        } catch (final IOException e) {
            throw new TemplatingException("Error creating template", e);
        } catch (final TemplateException e) {
            throw new TemplatingException("Error rendering output", e);
        }

        return writer.toString();
    }
}
