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

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static shiver.me.timbers.data.random.RandomStrings.someString;

public class KmsEncryptHandlerTest {

    private Base64 base64;
    private KmsEncryptService encryptService;
    private KmsEncryptHandler handler;

    @Before
    public void setUp() {
        base64 = mock(Base64.class);
        encryptService = mock(KmsEncryptService.class);
        handler = new KmsEncryptHandler(base64, encryptService);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void Can_encrypt_some_supplied_values() {

        final CustomResourceRequest request = mock(CustomResourceRequest.class);

        final Map<String, ByteBuffer> encryptedParameters = new HashMap<>();
        final String name1 = someString();
        final String name2 = someString();
        final String name3 = someString();
        final ByteBuffer buffer1 = mock(ByteBuffer.class);
        final ByteBuffer buffer2 = mock(ByteBuffer.class);
        final ByteBuffer buffer3 = mock(ByteBuffer.class);
        final String value1 = someString();
        final String value2 = someString();
        final String value3 = someString();

        // Given
        given(encryptService.encrypt(new KmsEncryptRequest(request))).willReturn(encryptedParameters);
        encryptedParameters.put(name1, buffer1);
        encryptedParameters.put(name2, buffer2);
        encryptedParameters.put(name3, buffer3);
        given(base64.encode(buffer1)).willReturn(value1);
        given(base64.encode(buffer2)).willReturn(value2);
        given(base64.encode(buffer3)).willReturn(value3);

        // When
        final Map<String, Object> actual = handler.createOrUpdate(request);

        // Then
        assertThat(actual, allOf(hasEntry(name1, value1), hasEntry(name2, value2), hasEntry(name3, value3)));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void Can_handle_a_delete() {

        // When
        final Map<String, Object> actual = handler.delete(mock(CustomResourceRequest.class));

        // Then
        assertThat(actual.size(), is(0));
    }
}