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

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.singletonMap;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static shiver.me.timbers.data.random.RandomStrings.someString;

public class KmsDecryptRequestTest {

    private CustomResourceRequest request;
    private Base64 base64;

    @Before
    public void setUp() {
        request = mock(CustomResourceRequest.class);
        base64 = mock(Base64.class);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void Can_get_the_parameters_to_decrypt() {

        final Map<String, String> parameters = new HashMap<>();
        final String name1 = someString();
        final String name2 = someString();
        final String name3 = someString();
        final String encode1 = someString();
        final String encode2 = someString();
        final String encode3 = someString();
        final ByteBuffer value1 = mock(ByteBuffer.class);
        final ByteBuffer value2 = mock(ByteBuffer.class);
        final ByteBuffer value3 = mock(ByteBuffer.class);

        // Given
        given(request.getResourceProperties()).willReturn(singletonMap("Parameters", parameters));
        parameters.put(name1, encode1);
        parameters.put(name2, encode2);
        parameters.put(name3, encode3);
        given(base64.decode(encode1)).willReturn(value1);
        given(base64.decode(encode2)).willReturn(value2);
        given(base64.decode(encode3)).willReturn(value3);

        // When
        final Map<String, ByteBuffer> actual = new KmsDecryptRequest(base64, request).getParameters();

        // Then
        assertThat(actual, allOf(hasEntry(name1, value1), hasEntry(name2, value2), hasEntry(name3, value3)));
    }

    private static ByteBuffer wrap(String encrypt1) {
        return ByteBuffer.wrap(encrypt1.getBytes());
    }
}