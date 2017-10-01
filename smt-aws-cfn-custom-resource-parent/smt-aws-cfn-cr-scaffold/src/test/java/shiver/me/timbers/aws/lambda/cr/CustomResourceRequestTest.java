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
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static shiver.me.timbers.data.random.RandomStrings.someString;

public class CustomResourceRequestTest {

    @Test
    public void Can_check_for_a_create_request() {

        // Given
        final CustomResourceRequest request = request("Create");

        // When
        final boolean actual = request.isCreate();

        // Then
        assertThat(actual, is(true));
        assertThat(request.isUpdate(), is(false));
        assertThat(request.isDelete(), is(false));
    }

    @Test
    public void Can_check_for_an_update_request() {

        // Given
        final CustomResourceRequest request = request("Update");

        // When
        final boolean actual = request.isUpdate();

        // Then
        assertThat(actual, is(true));
        assertThat(request.isCreate(), is(false));
        assertThat(request.isDelete(), is(false));
    }

    @Test
    public void Can_check_for_a_delete_request() {

        // Given
        final CustomResourceRequest request = request("Delete");

        // When
        final boolean actual = request.isDelete();

        // Then
        assertThat(actual, is(true));
        assertThat(request.isCreate(), is(false));
        assertThat(request.isUpdate(), is(false));
    }

    @Test
    public void The_custom_resource_requast_has_equality() {
        EqualsVerifier.forClass(CustomResourceRequest.class).suppress(STRICT_INHERITANCE).verify();
    }

    @Test
    @SuppressWarnings("unchecked")
    public void The_custom_resource_request_has_a_to_string() {

        // Given
        final String requestType = someString();
        final String requestId = someString();
        final String responseURL = someString();
        final String stackId = someString();
        final String resourceType = someString();
        final String logicalResourceId = someString();
        final String physicalResourceId = someString();
        final String resourceProperties = someString();
        final String oldResourceProperties = someString();

        // When
        final String actual = new CustomResourceRequest(
            requestType,
            requestId,
            responseURL,
            stackId,
            resourceType,
            logicalResourceId,
            physicalResourceId,
            mock(Map.class, resourceProperties),
            mock(Map.class, oldResourceProperties)
        ).toString();

        // Then
        assertThat(actual, containsString(requestType));
        assertThat(actual, containsString(requestId));
        assertThat(actual, containsString(responseURL));
        assertThat(actual, containsString(stackId));
        assertThat(actual, containsString(resourceType));
        assertThat(actual, containsString(logicalResourceId));
        assertThat(actual, containsString(physicalResourceId));
        assertThat(actual, containsString(resourceProperties));
        assertThat(actual, containsString(oldResourceProperties));
    }

    private static CustomResourceRequest request(String type) {
        return new CustomResourceRequest(
            type,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null
        );
    }
}