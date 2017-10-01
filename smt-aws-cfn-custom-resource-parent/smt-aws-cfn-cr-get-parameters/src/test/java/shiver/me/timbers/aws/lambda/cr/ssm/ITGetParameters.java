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

package shiver.me.timbers.aws.lambda.cr.ssm;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagement;
import com.amazonaws.services.simplesystemsmanagement.model.GetParametersRequest;
import com.amazonaws.services.simplesystemsmanagement.model.GetParametersResult;
import com.amazonaws.services.simplesystemsmanagement.model.Parameter;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import shiver.me.timbers.aws.lambda.cr.test.CustomResourceConfiguration;
import shiver.me.timbers.aws.lambda.cr.test.UploadHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static shiver.me.timbers.aws.lambda.cr.ssm.Maps.createGetParametersResourceRequest;
import static shiver.me.timbers.aws.lambda.cr.ssm.TestParameters.parameter;
import static shiver.me.timbers.aws.lambda.cr.test.Maps.createCustomResourceFailedResponse;
import static shiver.me.timbers.aws.lambda.cr.test.Maps.createCustomResourceSuccessResponse;
import static shiver.me.timbers.aws.lambda.cr.test.Properties.properties;
import static shiver.me.timbers.aws.lambda.cr.test.Properties.property;
import static shiver.me.timbers.data.random.RandomStrings.someAlphanumericString;
import static shiver.me.timbers.data.random.RandomThings.someThing;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = CustomResourceConfiguration.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class ITGetParameters {

    @LocalServerPort
    private int port;

    @Autowired
    private AWSSimpleSystemsManagement simpleSystemsManagement;

    @Autowired
    private UploadHandler uploadHandler;

    @Autowired
    private GetParameters lambda;

    @After
    public void tearDown() {
        reset(uploadHandler);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void Can_get_the_requested_parameters() throws IOException {

        final HashMap<String, Object> request = new HashMap<>();

        final String type = someThing("Create", "Update");
        final String stackId = someAlphanumericString(34);
        final String resourceType = someAlphanumericString(34);
        final String requestId = someAlphanumericString(34);
        final String logicalResourceId = someAlphanumericString(34);
        final String physicalResourceId = someAlphanumericString(34);
        final String name1 = someAlphanumericString(3);
        final String name2 = someAlphanumericString(5);
        final String name3 = someAlphanumericString(8);
        final String value1 = someAlphanumericString(8);
        final String value2 = someAlphanumericString(5);
        final String value3 = someAlphanumericString(3);
        final List<String> names = asList(name1, name2, name3);
        final GetParametersResult result = mock(GetParametersResult.class);
        final Parameter parameter1 = parameter(name1, value1);
        final Parameter parameter2 = parameter(name2, value2);
        final Parameter parameter3 = parameter(name3, value3);
        final HashMap<String, Object> response = new HashMap<>();

        // Given
        request.put("RequestType", type);
        request.put("RequestId", requestId);
        request.put("ResponseURL", format("http://localhost:%s/upload", port));
        request.put("StackId", stackId);
        request.put("ResourceType", resourceType);
        request.put("LogicalResourceId", logicalResourceId);
        request.put("PhysicalResourceId", physicalResourceId);
        request.put("ParameterNames", properties(property(name1, null), property(name2, null), property(name3, null)));
        given(simpleSystemsManagement.getParameters(new GetParametersRequest().withNames(names))).willReturn(result);
        given(result.getParameters()).willReturn(asList(parameter1, parameter2, parameter3));
        given(result.getInvalidParameters()).willReturn(emptyList());
        response.put("Status", "SUCCESS");
        response.put("RequestId", requestId);
        response.put("StackId", stackId);
        response.put("LogicalResourceId", logicalResourceId);
        response.put("PhysicalResourceId", physicalResourceId);
        response.put("Data", properties(property(name1, value1), property(name2, value2), property(name3, value3)));

        // When
        final String actual = lambda.handleRequest(createGetParametersResourceRequest(request), mock(Context.class));

        // Then
        then(uploadHandler).should().upload(createCustomResourceSuccessResponse(response));
        assertThat(actual, equalTo(format(
            "Customer resource (%s) %s has finished.", GetParameters.class.getName(), type
        )));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void Can_fail_to_get_the_requested_parameters() throws IOException {

        final HashMap<String, Object> request = new HashMap<>();

        final String type = someThing("Create", "Update");
        final String stackId = someAlphanumericString(34);
        final String resourceType = someAlphanumericString(34);
        final String requestId = someAlphanumericString(34);
        final String logicalResourceId = someAlphanumericString(34);
        final String physicalResourceId = someAlphanumericString(34);
        final String name1 = someAlphanumericString(3);
        final String name2 = someAlphanumericString(5);
        final String name3 = someAlphanumericString(8);
        final String value1 = someAlphanumericString(8);
        final String value2 = someAlphanumericString(5);
        final List<String> names = asList(name1, name2, name3);
        final GetParametersResult result = mock(GetParametersResult.class);
        final Parameter parameter1 = parameter(name1, value1);
        final Parameter parameter2 = parameter(name2, value2);
        final HashMap<String, Object> response = new HashMap<>();

        // Given
        request.put("RequestType", type);
        request.put("RequestId", requestId);
        request.put("ResponseURL", format("http://localhost:%s/upload", port));
        request.put("StackId", stackId);
        request.put("ResourceType", resourceType);
        request.put("LogicalResourceId", logicalResourceId);
        request.put("PhysicalResourceId", physicalResourceId);
        request.put("ParameterNames", properties(property(name1, null), property(name2, null), property(name3, null)));
        given(simpleSystemsManagement.getParameters(new GetParametersRequest().withNames(names))).willReturn(result);
        given(result.getParameters()).willReturn(asList(parameter1, parameter2));
        given(result.getInvalidParameters()).willReturn(singletonList(name3));
        response.put("Status", "FAILED");
        response.put("RequestId", requestId);
        response.put("StackId", stackId);
        response.put("LogicalResourceId", logicalResourceId);
        response.put("PhysicalResourceId", physicalResourceId);
        response.put("Reason", format("Invalid parameters: %s", singletonList(name3)));

        // When
        final String actual = lambda.handleRequest(createGetParametersResourceRequest(request), mock(Context.class));

        // Then
        then(uploadHandler).should().upload(createCustomResourceFailedResponse(response));
        assertThat(actual, equalTo(format(
            "Customer resource (%s) %s has finished.", GetParameters.class.getName(), type
        )));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void Can_handle_a_delete() throws IOException {

        final HashMap<String, Object> request = new HashMap<>();

        final String type = "Delete";
        final String stackId = someAlphanumericString(34);
        final String resourceType = someAlphanumericString(34);
        final String requestId = someAlphanumericString(34);
        final String logicalResourceId = someAlphanumericString(34);
        final String physicalResourceId = someAlphanumericString(34);
        final String name1 = someAlphanumericString(3);
        final String name2 = someAlphanumericString(5);
        final String name3 = someAlphanumericString(8);
        final String value1 = someAlphanumericString(8);
        final String value2 = someAlphanumericString(5);
        final String value3 = someAlphanumericString(3);
        final List<String> names = asList(name1, name2, name3);
        final GetParametersResult result = mock(GetParametersResult.class);
        final Parameter parameter1 = parameter(name1, value1);
        final Parameter parameter2 = parameter(name2, value2);
        final Parameter parameter3 = parameter(name3, value3);
        final HashMap<String, Object> response = new HashMap<>();

        // Given
        request.put("RequestType", type);
        request.put("RequestId", requestId);
        request.put("ResponseURL", format("http://localhost:%s/upload", port));
        request.put("StackId", stackId);
        request.put("ResourceType", resourceType);
        request.put("LogicalResourceId", logicalResourceId);
        request.put("PhysicalResourceId", physicalResourceId);
        request.put("ParameterNames", properties(property(name1, null), property(name2, null), property(name3, null)));
        given(simpleSystemsManagement.getParameters(new GetParametersRequest().withNames(names))).willReturn(result);
        given(result.getParameters()).willReturn(asList(parameter1, parameter2, parameter3));
        given(result.getInvalidParameters()).willReturn(emptyList());
        response.put("Status", "SUCCESS");
        response.put("RequestId", requestId);
        response.put("StackId", stackId);
        response.put("LogicalResourceId", logicalResourceId);
        response.put("PhysicalResourceId", physicalResourceId);

        // When
        final String actual = lambda.handleRequest(createGetParametersResourceRequest(request), mock(Context.class));

        // Then
        then(uploadHandler).should().upload(createCustomResourceSuccessResponse(response));
        assertThat(actual, equalTo(format(
            "Customer resource (%s) %s has finished.", GetParameters.class.getName(), type
        )));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void Can_handle_an_unkown_request_type() throws IOException {

        final HashMap<String, Object> request = new HashMap<>();

        final String type = someAlphanumericString(5);
        final String stackId = someAlphanumericString(34);
        final String resourceType = someAlphanumericString(34);
        final String requestId = someAlphanumericString(34);
        final String logicalResourceId = someAlphanumericString(34);
        final String physicalResourceId = someAlphanumericString(34);
        final String name1 = someAlphanumericString(3);
        final String name2 = someAlphanumericString(5);
        final String name3 = someAlphanumericString(8);
        final String value1 = someAlphanumericString(8);
        final String value2 = someAlphanumericString(5);
        final List<String> names = asList(name1, name2, name3);
        final GetParametersResult result = mock(GetParametersResult.class);
        final Parameter parameter1 = parameter(name1, value1);
        final Parameter parameter2 = parameter(name2, value2);
        final HashMap<String, Object> response = new HashMap<>();

        // Given
        request.put("RequestType", type);
        request.put("RequestId", requestId);
        request.put("ResponseURL", format("http://localhost:%s/upload", port));
        request.put("StackId", stackId);
        request.put("ResourceType", resourceType);
        request.put("LogicalResourceId", logicalResourceId);
        request.put("PhysicalResourceId", physicalResourceId);
        request.put("ParameterNames", properties(property(name1, null), property(name2, null), property(name3, null)));
        given(simpleSystemsManagement.getParameters(new GetParametersRequest().withNames(names))).willReturn(result);
        given(result.getParameters()).willReturn(asList(parameter1, parameter2));
        given(result.getInvalidParameters()).willReturn(singletonList(name3));
        response.put("Status", "FAILED");
        response.put("RequestId", requestId);
        response.put("StackId", stackId);
        response.put("LogicalResourceId", logicalResourceId);
        response.put("PhysicalResourceId", physicalResourceId);
        response.put("Reason", format("Custom Resource request type %s is unknown.", type));

        // When
        final String actual = lambda.handleRequest(createGetParametersResourceRequest(request), mock(Context.class));

        // Then
        then(uploadHandler).should().upload(createCustomResourceFailedResponse(response));
        assertThat(actual, equalTo(format(
            "Customer resource (%s) %s has finished.", GetParameters.class.getName(), type
        )));
    }
}
