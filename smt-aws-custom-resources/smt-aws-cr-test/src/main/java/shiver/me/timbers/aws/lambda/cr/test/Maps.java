/*
 * Copyright 2017 Karl Bennett
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package shiver.me.timbers.aws.lambda.cr.test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Map;

import static java.lang.ClassLoader.getSystemResourceAsStream;

public class Maps {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static Map<String, Object> createCustomResourceSuccessResponse(Map<String, Object> values) throws IOException {
        return createMap("custom-resource-success-response.mustache", values);
    }

    public static Map<String, Object> createCustomResourceFailedResponse(Map<String, Object> values) throws IOException {
        return createMap("custom-resource-failed-response.mustache", values);
    }

    public static Map<String, Object> createMap(String template, Map<String, Object> values) throws IOException {
        final MustacheFactory mf = new DefaultMustacheFactory();
        final Mustache mustache = mf.compile(new InputStreamReader(getSystemResourceAsStream(template)), template);
        try (final ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            try (final OutputStreamWriter writer = new OutputStreamWriter(outputStream)) {
                mustache.execute(writer, values);
                writer.flush();
                return OBJECT_MAPPER.readValue(
                    new String(outputStream.toByteArray()),
                    new TypeReference<Map<String, Object>>() {
                    }
                );
            }
        }
    }
}