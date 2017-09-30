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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.singletonMap;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static shiver.me.timbers.aws.lambda.cr.Maps.createCustomResourceFailedResponse;
import static shiver.me.timbers.aws.lambda.cr.Maps.createCustomResourceRequest;
import static shiver.me.timbers.aws.lambda.cr.Maps.createCustomResourceSuccessResponse;
import static shiver.me.timbers.aws.lambda.cr.test.Properties.properties;
import static shiver.me.timbers.aws.lambda.cr.test.Properties.property;
import static shiver.me.timbers.data.random.RandomStrings.someAlphanumericString;

public class CustomResourceIoMapperTest {

    private static final String SUCCESS = "SUCCESS";

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private CustomResourceIoMapper mapper;

    @Before
    public void setUp() {
        this.mapper = new CustomResourceIoMapper();
    }

    @Test
    public void Can_map_a_custom_resource_request() throws IOException {

        final HashMap<String, Object> values = new HashMap<>();

        final String requestType = someAlphanumericString(8);
        final String responseUrl = someAlphanumericString(21);
        final String stackId = someAlphanumericString(34);
        final String resourceType = someAlphanumericString(34);
        final String requestId = someAlphanumericString(34);
        final String logicalResourceId = someAlphanumericString(34);
        final String physicalResourceId = someAlphanumericString(34);
        final String name1 = someAlphanumericString(3);
        final String name2 = someAlphanumericString(5);
        final String value1 = someAlphanumericString(5);
        final String value2 = someAlphanumericString(3);

        // Given
        values.put("RequestType", requestType);
        values.put("RequestId", requestId);
        values.put("ResponseURL", responseUrl);
        values.put("StackId", stackId);
        values.put("ResourceType", resourceType);
        values.put("LogicalResourceId", logicalResourceId);
        values.put("PhysicalResourceId", physicalResourceId);
        values.put("ResourceProperties", properties(property(name1, value1)));
        values.put("OldResourceProperties", properties(property(name2, value2)));

        // When
        final CustomResourceRequest actual = mapper.mapRequest(createCustomResourceRequest(values));

        // Then
        assertThat(actual.getRequestType(), equalTo(requestType));
        assertThat(actual.getRequestId(), equalTo(requestId));
        assertThat(actual.getResponseURL(), equalTo(responseUrl));
        assertThat(actual.getStackId(), equalTo(stackId));
        assertThat(actual.getResourceType(), equalTo(resourceType));
        assertThat(actual.getLogicalResourceId(), equalTo(logicalResourceId));
        assertThat(actual.getPhysicalResourceId(), equalTo(physicalResourceId));
        assertThat(actual.getResourceProperties(), equalTo(singletonMap(name1, value1)));
        assertThat(actual.getOldResourceProperties(), equalTo(singletonMap(name2, value2)));
    }

    @Test
    public void Can_map_a_custom_resource_success_response_with_data() throws IOException {

        final CustomResourceRequest request = mock(CustomResourceRequest.class);

        final HashMap<String, Object> values = new HashMap<>();
        final String requestId = someAlphanumericString(13);
        final String stackId = someAlphanumericString(21);
        final String logicalResourceId = someAlphanumericString(13);
        final String physicalResourceId = someAlphanumericString(21);
        final String name1 = someAlphanumericString(3);
        final String name2 = someAlphanumericString(5);
        final String value1 = someAlphanumericString(5);
        final String value2 = someAlphanumericString(3);
        final HashMap<String, Object> data = new HashMap<>();

        // Given
        data.put(name1, value1);
        data.put(name2, value2);
        given(request.getRequestId()).willReturn(requestId);
        given(request.getStackId()).willReturn(stackId);
        given(request.getLogicalResourceId()).willReturn(logicalResourceId);
        given(request.getPhysicalResourceId()).willReturn(physicalResourceId);
        values.put("Status", SUCCESS);
        values.put("RequestId", requestId);
        values.put("StackId", stackId);
        values.put("LogicalResourceId", logicalResourceId);
        values.put("PhysicalResourceId", physicalResourceId);
        values.put("Data", properties(property(name1, value1), property(name2, value2)));

        // When
        final CustomResourceResponse actual = mapper.mapSuccessResponse(request, data);

        // Then
        assertThat(toMap(actual), equalTo(createCustomResourceSuccessResponse(values)));
    }

    @Test
    public void Can_map_a_custom_resource_failed_response() throws IOException {

        final CustomResourceRequest request = mock(CustomResourceRequest.class);
        final RuntimeException exception = mock(RuntimeException.class);

        final HashMap<String, Object> values = new HashMap<>();
        final String requestId = someAlphanumericString(13);
        final String stackId = someAlphanumericString(21);
        final String logicalResourceId = someAlphanumericString(13);
        final String physicalResourceId = someAlphanumericString(21);
        final String message = someAlphanumericString(3);

        // Given
        given(request.getRequestId()).willReturn(requestId);
        given(request.getStackId()).willReturn(stackId);
        given(request.getLogicalResourceId()).willReturn(logicalResourceId);
        given(request.getPhysicalResourceId()).willReturn(physicalResourceId);
        given(exception.getMessage()).willReturn(message);
        values.put("Status", "FAILED");
        values.put("RequestId", requestId);
        values.put("StackId", stackId);
        values.put("LogicalResourceId", logicalResourceId);
        values.put("PhysicalResourceId", physicalResourceId);
        values.put("NoData", true);
        values.put("Reason", message);

        // When
        final CustomResourceResponse actual = mapper.mapFailureResponse(request, exception);

        // Then
        assertThat(toMap(actual), equalTo(createCustomResourceFailedResponse(values)));
    }

    private static Map toMap(CustomResourceResponse actual) {
        return OBJECT_MAPPER.convertValue(actual, Map.class);
    }
}