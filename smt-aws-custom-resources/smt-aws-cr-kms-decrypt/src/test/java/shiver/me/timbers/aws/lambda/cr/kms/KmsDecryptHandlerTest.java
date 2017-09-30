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

public class KmsDecryptHandlerTest {

    private Base64 base64;
    private KmsDecryptService encryptService;
    private KmsDecryptHandler handler;

    @Before
    public void setUp() {
        base64 = mock(Base64.class);
        encryptService = mock(KmsDecryptService.class);
        handler = new KmsDecryptHandler(base64, encryptService);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void Can_encrypt_some_supplied_values() {

        final CustomResourceRequest request = mock(CustomResourceRequest.class);

        final Map<String, String> expected = mock(Map.class);

        // Given
        given(encryptService.decrypt(new KmsDecryptRequest(base64, request))).willReturn(expected);

        // When
        final Map<String, Object> actual = handler.createOrUpdate(request);

        // Then
        assertThat(actual, is(expected));
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