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

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static java.lang.String.format;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static shiver.me.timbers.data.random.RandomBooleans.someBoolean;
import static shiver.me.timbers.data.random.RandomStrings.someString;

public class CustomResourceLambdaTest {

    private CustomResourceIoMapper mapper;
    private CustomResourceMappingHandler handler;
    private CustomResourceClient customResourceClient;
    private RequestHandler<Map<String, Object>, String> lambda;

    @Before
    public void setUp() {
        mapper = mock(CustomResourceIoMapper.class);
        handler = mock(CustomResourceMappingHandler.class);
        customResourceClient = mock(CustomResourceClient.class);
        lambda = new CustomResourceLambda(mapper, handler, customResourceClient);
    }

    @Test
    public void Instantiation_test_for_coverage() {
        new CustomResourceLambda(mock(CustomResourceHandler.class));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void Can_handle_a_custom_resource_create() {

        final Map<String, Object> requestMap = mock(Map.class);

        final CustomResourceRequest request = mock(CustomResourceRequest.class);
        final String type = someString();
        final CustomResourceResponse response = mock(CustomResourceResponse.class);
        final String responseURL = someString();

        // Given
        given(mapper.mapRequest(requestMap)).willReturn(request);
        given(request.isCreate()).willReturn(true);
        given(request.getRequestType()).willReturn(type);
        given(handler.create(request)).willReturn(response);
        given(request.getResponseURL()).willReturn(responseURL);

        // When
        final String actual = lambda.handleRequest(requestMap, mock(Context.class));

        // Then
        then(customResourceClient).should().upload(responseURL, response);
        then(handler).should(never()).update(any(CustomResourceRequest.class));
        then(handler).should(never()).delete(any(CustomResourceRequest.class));
        assertThat(actual, equalTo(format(
            "Customer resource (%s) %s has finished.", lambda.getClass().getName(), type
        )));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void Can_handle_a_custom_resource_update() {

        final Map<String, Object> requestMap = mock(Map.class);

        final CustomResourceRequest request = mock(CustomResourceRequest.class);
        final String type = someString();
        final CustomResourceResponse response = mock(CustomResourceResponse.class);
        final String responseURL = someString();

        // Given
        given(mapper.mapRequest(requestMap)).willReturn(request);
        given(request.isUpdate()).willReturn(true);
        given(request.getRequestType()).willReturn(type);
        given(handler.update(request)).willReturn(response);
        given(request.getResponseURL()).willReturn(responseURL);

        // When
        final String actual = lambda.handleRequest(requestMap, mock(Context.class));

        // Then
        then(customResourceClient).should().upload(responseURL, response);
        then(handler).should(never()).create(any(CustomResourceRequest.class));
        then(handler).should(never()).delete(any(CustomResourceRequest.class));
        assertThat(actual, equalTo(format(
            "Customer resource (%s) %s has finished.", lambda.getClass().getName(), type
        )));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void Can_handle_a_custom_resource_delete() {

        final Map<String, Object> requestMap = mock(Map.class);

        final CustomResourceRequest request = mock(CustomResourceRequest.class);
        final String type = someString();
        final CustomResourceResponse response = mock(CustomResourceResponse.class);
        final String responseURL = someString();

        // Given
        given(mapper.mapRequest(requestMap)).willReturn(request);
        given(request.isDelete()).willReturn(true);
        given(request.getRequestType()).willReturn(type);
        given(handler.delete(request)).willReturn(response);
        given(request.getResponseURL()).willReturn(responseURL);

        // When
        final String actual = lambda.handleRequest(requestMap, mock(Context.class));

        // Then
        then(customResourceClient).should().upload(responseURL, response);
        then(handler).should(never()).create(any(CustomResourceRequest.class));
        then(handler).should(never()).update(any(CustomResourceRequest.class));
        assertThat(actual, equalTo(format(
            "Customer resource (%s) %s has finished.", lambda.getClass().getName(), type
        )));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void Cannot_handle_an_unknown_custom_resource_request_type() {

        final Map<String, Object> requestMap = mock(Map.class);

        final CustomResourceRequest request = mock(CustomResourceRequest.class);
        final String type = someString();
        final CustomResourceResponse response = mock(CustomResourceResponse.class);
        final String responseURL = someString();

        // Given
        given(mapper.mapRequest(requestMap)).willReturn(request);
        given(request.isCreate()).willReturn(false);
        given(request.isUpdate()).willReturn(false);
        given(request.isDelete()).willReturn(false);
        given(request.getRequestType()).willReturn(type);
        given(mapper.mapFailureResponse(request, format("Custom Resource request type %s is unknown.", type)))
            .willReturn(response);
        given(request.getResponseURL()).willReturn(responseURL);

        // When
        final String actual = lambda.handleRequest(requestMap, mock(Context.class));

        // Then
        then(customResourceClient).should().upload(responseURL, response);
        then(handler).should(never()).create(any(CustomResourceRequest.class));
        then(handler).should(never()).update(any(CustomResourceRequest.class));
        then(handler).should(never()).delete(any(CustomResourceRequest.class));
        assertThat(actual, equalTo(format(
            "Customer resource (%s) %s has finished.", lambda.getClass().getName(), type
        )));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void Can_handle_a_custom_resource_failure() {

        final Map<String, Object> requestMap = mock(Map.class);

        final CustomResourceRequest request = mock(CustomResourceRequest.class);
        final String type = someString();
        final CustomResourceResponse response = mock(CustomResourceResponse.class);
        final CustomResourceException exception = mock(CustomResourceException.class);
        final String responseURL = someString();

        // Given
        given(mapper.mapRequest(requestMap)).willReturn(request);
        given(request.isCreate()).willReturn(someBoolean());
        given(request.isUpdate()).willReturn(someBoolean());
        given(request.isDelete()).willReturn(true);
        given(request.getRequestType()).willReturn(type);
        given(handler.create(request)).willThrow(exception);
        given(handler.update(request)).willThrow(exception);
        given(handler.delete(request)).willThrow(exception);
        given(mapper.mapFailureResponse(request, exception)).willReturn(response);
        given(request.getResponseURL()).willReturn(responseURL);

        // When
        final String actual = lambda.handleRequest(requestMap, mock(Context.class));

        // Then
        then(customResourceClient).should().upload(responseURL, response);
        assertThat(actual, equalTo(format(
            "Customer resource (%s) %s has finished.", lambda.getClass().getName(), type
        )));
    }
}