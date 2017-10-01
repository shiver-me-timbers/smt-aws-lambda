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
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static shiver.me.timbers.data.random.RandomIntegers.someInteger;
import static shiver.me.timbers.data.random.RandomStrings.someString;

public class CustomResourceClientTest {

    private HttpURLConnectionFactory factory;
    private ObjectMapper mapper;
    private CustomResourceClient service;

    @Before
    public void setUp() {
        factory = mock(HttpURLConnectionFactory.class);
        mapper = mock(ObjectMapper.class);
        service = new CustomResourceClient(factory, mapper);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void Can_upload_a_response() throws IOException {

        final String responseURL = someString();
        final CustomResourceResponse response = mock(CustomResourceResponse.class);

        final HttpURLConnection connection = mock(HttpURLConnection.class);
        final OutputStream stream = mock(OutputStream.class);

        // Given
        given(factory.createConnection(responseURL)).willReturn(connection);
        given(connection.getOutputStream()).willReturn(stream);
        given(connection.getResponseCode()).willReturn(200);

        // When
        service.upload(responseURL, response);

        // Then
        final InOrder order = inOrder(connection, mapper, stream);
        order.verify(connection).setDoOutput(true);
        order.verify(connection).setRequestMethod("PUT");
        order.verify(connection).getOutputStream();
        order.verify(mapper).writeValue(stream, response);
        order.verify(stream).close();
    }

    @Test
    @SuppressWarnings("unchecked")
    public void Can_handle_a_failed_upload() throws IOException {

        final String responseURL = someString();
        final CustomResourceResponse response = mock(CustomResourceResponse.class);

        final HttpURLConnection connection = mock(HttpURLConnection.class);
        final OutputStream stream = mock(OutputStream.class);

        final IOException exception = mock(IOException.class);
        final String status = someString();
        final int responseCode = someInteger();

        // Given
        given(factory.createConnection(responseURL)).willReturn(connection);
        given(connection.getOutputStream()).willReturn(stream);
        given(connection.getResponseCode()).willReturn(responseCode);
        given(response.getStatus()).willReturn(status);

        // When
        final Throwable actual = catchThrowable(() -> service.upload(responseURL, response));

        // Then
        assertThat(actual, instanceOf(UploadException.class));
        assertThat(actual.getMessage(), equalTo(format(
            "Failed to upload the %s custom resource response to: %s HTTP Status: %s",
            status, responseURL, responseCode
        )));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void Can_handle_a_broken_upload() throws IOException {

        final String responseURL = someString();
        final CustomResourceResponse response = mock(CustomResourceResponse.class);

        final HttpURLConnection connection = mock(HttpURLConnection.class);
        final OutputStream stream = mock(OutputStream.class);

        final IOException exception = mock(IOException.class);
        final String status = someString();

        // Given
        given(factory.createConnection(responseURL)).willReturn(connection);
        given(connection.getOutputStream()).willReturn(stream);
        willThrow(exception).given(mapper).writeValue(stream, response);
        given(response.getStatus()).willReturn(status);

        // When
        final Throwable actual = catchThrowable(() -> service.upload(responseURL, response));

        // Then
        assertThat(actual, instanceOf(UploadException.class));
        assertThat(actual.getMessage(), equalTo(format(
            "Failed to upload the %s custom resource response to: %s", status, responseURL
        )));
        assertThat(actual.getCause(), is(exception));
    }
}