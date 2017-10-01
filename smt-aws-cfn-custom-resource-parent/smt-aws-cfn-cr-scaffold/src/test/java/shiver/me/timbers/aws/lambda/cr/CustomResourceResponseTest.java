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

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

import java.util.Map;

import static nl.jqno.equalsverifier.Warning.STRICT_INHERITANCE;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static shiver.me.timbers.data.random.RandomStrings.someString;

public class CustomResourceResponseTest {


    @Test
    public void The_custom_resource_response_has_equality() {
        EqualsVerifier.forClass(CustomResourceResponse.class).suppress(STRICT_INHERITANCE).verify();
    }

    @Test
    @SuppressWarnings("unchecked")
    public void The_custom_resource_response_has_a_to_string() {

        // Given
        final String status = someString();
        final String requestId = someString();
        final String stackId = someString();
        final String logicalResourceId = someString();
        final String physicalResourceId = someString();
        final String data = someString();
        final String reason = someString();

        // When
        final String actual = new CustomResourceResponse(
            status,
            requestId,
            stackId,
            logicalResourceId,
            physicalResourceId,
            mock(Map.class, data),
            reason
        ).toString();

        // Then
        assertThat(actual, containsString(status));
        assertThat(actual, containsString(requestId));
        assertThat(actual, containsString(stackId));
        assertThat(actual, containsString(logicalResourceId));
        assertThat(actual, containsString(physicalResourceId));
        assertThat(actual, containsString(data));
        assertThat(actual, containsString(reason));
    }
}