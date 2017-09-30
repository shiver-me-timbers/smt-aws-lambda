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

import com.amazonaws.services.simplesystemsmanagement.model.Parameter;
import org.junit.Before;
import org.junit.Test;
import shiver.me.timbers.aws.lambda.cr.CustomResourceRequest;

import java.util.Map;

import static java.util.Collections.singletonList;
import static java.util.Collections.singletonMap;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static shiver.me.timbers.data.random.RandomStrings.someString;

public class GetParametersHandlerTest {

    private ParametersService parametersService;
    private GetParametersHandler handler;

    @Before
    public void setUp() {
        parametersService = mock(ParametersService.class);
        handler = new GetParametersHandler(parametersService);
    }

    @Test
    public void Can_handle_a_create_or_update_request() {

        final CustomResourceRequest request = mock(CustomResourceRequest.class);

        final Parameter parameter = mock(Parameter.class);
        final Parameters parameters = new Parameters(singletonList(parameter));
        final String name = someString();
        final String value = someString();

        final Map<String, Object> expected = singletonMap(name, value);

        // Given
        given(parametersService.getParameters(new GetParametersResourceRequest(request))).willReturn(parameters);
        given(parameter.getName()).willReturn(name);
        given(parameter.getValue()).willReturn(value);

        // When
        final Map<String, Object> actual = handler.createOrUpdate(request);

        // Then
        assertThat(actual, is(expected));
    }

    @Test
    public void Can_handle_a_delete_request() {

        // Given
        final CustomResourceRequest request = mock(CustomResourceRequest.class);

        // When
        final Map<String, Object> actual = handler.delete(request);

        // Then
        then(parametersService).should(never()).getParameters(any(GetParametersResourceRequest.class));
        assertThat(actual.size(), is(0));
    }
}