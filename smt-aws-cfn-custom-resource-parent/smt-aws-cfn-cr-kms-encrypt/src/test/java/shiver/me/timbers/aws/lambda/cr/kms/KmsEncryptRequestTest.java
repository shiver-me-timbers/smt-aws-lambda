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

package shiver.me.timbers.aws.lambda.cr.kms;

import org.junit.Before;
import org.junit.Test;
import shiver.me.timbers.aws.lambda.cr.CustomResourceRequest;

import java.util.Map;

import static java.util.Collections.singletonMap;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static shiver.me.timbers.data.random.RandomStrings.someString;

public class KmsEncryptRequestTest {

    private CustomResourceRequest request;

    @Before
    public void setUp() {
        request = mock(CustomResourceRequest.class);
    }

    @Test
    public void Can_get_the_kms_key_id() {

        final String expected = someString();

        // Given
        given(request.getResourceProperties()).willReturn(singletonMap("KmsKeyId", expected));

        // When
        final String actual = new KmsEncryptRequest(request).getKmsKeyId();

        // Then
        assertThat(actual, is(expected));
    }

    @Test
    public void Can_get_the_parameters_to_encrypt() {

        final Map expected = mock(Map.class);

        // Given
        given(request.getResourceProperties()).willReturn(singletonMap("Parameters", expected));

        // When
        final Map<String, String> actual = new KmsEncryptRequest(request).getParameters();

        // Then
        assertThat(actual, is(expected));
    }
}