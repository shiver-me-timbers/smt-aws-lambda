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

import org.junit.Test;
import shiver.me.timbers.aws.lambda.cr.CustomResourceRequest;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonMap;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static shiver.me.timbers.data.random.RandomStrings.someString;

public class GetParametersResourceRequestTest {

    @Test
    public void Can_get_the_parameter_names() {

        final CustomResourceRequest request = mock(CustomResourceRequest.class);

        final String name1 = someString();
        final String name2 = someString();
        final String name3 = someString();

        // Given
        given(request.getResourceProperties()).willReturn(singletonMap("ParameterNames", asList(name1, name2, name3)));

        // When
        final List<String> actual = new GetParametersResourceRequest(request).getParameterNames();

        // Then
        assertThat(actual, contains(name1, name2, name3));
    }
}