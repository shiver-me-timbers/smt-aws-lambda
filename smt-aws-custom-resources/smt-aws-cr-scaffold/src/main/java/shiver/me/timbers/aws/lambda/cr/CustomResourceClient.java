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

package shiver.me.timbers.aws.lambda.cr;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;

import static java.lang.String.format;

/**
 * @author Karl Bennett
 */
class CustomResourceClient {

    private final HttpURLConnectionFactory factory;
    private final ObjectMapper mapper;

    CustomResourceClient(HttpURLConnectionFactory factory, ObjectMapper mapper) {
        this.factory = factory;
        this.mapper = mapper;
    }

    void upload(String responseURL, CustomResourceResponse response) {
        try {
            final HttpURLConnection connection = factory.createConnection(responseURL);
            connection.setDoOutput(true);
            connection.setRequestMethod("PUT");
            try (final OutputStream stream = connection.getOutputStream()) {
                mapper.writeValue(stream, response);
            }
            checkHttpStatusCode(connection.getResponseCode(), response.getStatus(), responseURL);
        } catch (IOException e) {
            throw new UploadException(
                format("Failed to upload the %s custom resource response to: %s", response.getStatus(), responseURL),
                e
            );
        }
    }

    private static void checkHttpStatusCode(int responseCode, String status, String responseURL) {
        if (responseCode != 200) {
            throw new UploadException(
                format(
                    "Failed to upload the %s custom resource response to: %s HTTP Status: %s",
                    status,
                    responseURL,
                    responseCode
                )
            );
        }
    }
}
