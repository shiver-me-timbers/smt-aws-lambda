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

package shiver.me.timbers.aws.lambda.cr.parameters;

import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagement;
import com.amazonaws.services.simplesystemsmanagement.model.GetParametersRequest;
import com.amazonaws.services.simplesystemsmanagement.model.GetParametersResult;
import com.amazonaws.services.simplesystemsmanagement.model.Parameter;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static shiver.me.timbers.aws.lambda.cr.parameters.TestParameters.parameter;
import static shiver.me.timbers.data.random.RandomStrings.someString;

public class ParametersServiceTest {

    private AWSSimpleSystemsManagement simpleSystemsManagement;
    private ParametersService service;

    @Before
    public void setUp() {
        simpleSystemsManagement = mock(AWSSimpleSystemsManagement.class);
        service = new ParametersService(simpleSystemsManagement);
    }

    @Test
    public void Can_retrieve_the_requested_parameters() {

        final GetParametersResourceRequest request = mock(GetParametersResourceRequest.class);

        final String name1 = someString(3);
        final String name2 = someString(5);
        final String name3 = someString(8);
        final GetParametersResult result = mock(GetParametersResult.class);
        final String value1 = someString(8);
        final String value2 = someString(5);
        final String value3 = someString(3);

        final List<Parameter> expected = asList(parameter(name1, value1), parameter(name2, value2), parameter(name3, value3));

        // Given
        given(request.getParameterNames()).willReturn(asList(name1, name2, name3));
        given(simpleSystemsManagement.getParameters(new GetParametersRequest().withNames(name1, name2, name3)))
            .willReturn(result);
        given(result.getParameters()).willReturn(expected);
        given(result.getInvalidParameters()).willReturn(emptyList());

        // When
        final Parameters actual = service.getParameters(request);

        // Then
        assertThat(actual, contains(expected.toArray(new Parameter[expected.size()])));
    }

    @Test
    public void Can_fail_retrieve_the_requested_parameters() {

        final GetParametersResourceRequest request = mock(GetParametersResourceRequest.class);

        final String name1 = someString(3);
        final String name2 = someString(5);
        final String name3 = someString(8);
        final GetParametersResult result = mock(GetParametersResult.class);
        final String value1 = someString(8);
        final String value2 = someString(5);
        final List<String> invalidParameters = singletonList(name3);

        // Given
        given(request.getParameterNames()).willReturn(asList(name1, name2, name3));
        given(simpleSystemsManagement.getParameters(new GetParametersRequest().withNames(name1, name2, name3)))
            .willReturn(result);
        given(result.getParameters()).willReturn(asList(parameter(name1, value1), parameter(name2, value2)));
        given(result.getInvalidParameters()).willReturn(invalidParameters);

        // When
        final Throwable actual = catchThrowable(() -> service.getParameters(request));

        // Then
        assertThat(actual, instanceOf(InvalidParametersException.class));
        assertThat(actual.getMessage(), equalTo(format("Invalid parameters: %s", invalidParameters)));
    }
}