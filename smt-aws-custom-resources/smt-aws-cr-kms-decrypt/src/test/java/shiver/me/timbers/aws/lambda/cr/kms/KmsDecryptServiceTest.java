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

import com.amazonaws.services.kms.AWSKMS;
import com.amazonaws.services.kms.model.DecryptRequest;
import com.amazonaws.services.kms.model.DecryptResult;
import com.amazonaws.services.kms.model.EncryptRequest;
import com.amazonaws.services.kms.model.EncryptResult;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasEntry;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static shiver.me.timbers.data.random.RandomStrings.someString;

public class KmsDecryptServiceTest {

    @Test
    public void Can_encrypt_some_values() {

        final AWSKMS awskms = mock(AWSKMS.class);
        final KmsDecryptRequest request = mock(KmsDecryptRequest.class);

        final Map<String, ByteBuffer> parameters = new HashMap<>();
        final String name1 = someString();
        final String name2 = someString();
        final String name3 = someString();
        final ByteBuffer blob1 = mock(ByteBuffer.class);
        final ByteBuffer blob2 = mock(ByteBuffer.class);
        final ByteBuffer blob3 = mock(ByteBuffer.class);
        final DecryptResult result1 = mock(DecryptResult.class);
        final DecryptResult result2 = mock(DecryptResult.class);
        final DecryptResult result3 = mock(DecryptResult.class);
        final String value1 = someString();
        final String value2 = someString();
        final String value3 = someString();

        // Given
        given(request.getParameters()).willReturn(parameters);
        parameters.put(name1, blob1);
        parameters.put(name2, blob2);
        parameters.put(name3, blob3);
        given(awskms.decrypt(request(blob1))).willReturn(result1);
        given(awskms.decrypt(request(blob2))).willReturn(result2);
        given(awskms.decrypt(request(blob3))).willReturn(result3);
        given(result1.getPlaintext()).willReturn(ByteBuffer.wrap(value1.getBytes()));
        given(result2.getPlaintext()).willReturn(ByteBuffer.wrap(value2.getBytes()));
        given(result3.getPlaintext()).willReturn(ByteBuffer.wrap(value3.getBytes()));

        // When
        final Map<String, String> actual = new KmsDecryptService(awskms).decrypt(request);

        // Then
        assertThat(actual, allOf(hasEntry(name1, value1), hasEntry(name2, value2), hasEntry(name3, value3)));

    }

    private static DecryptRequest request(ByteBuffer value) {
        return new DecryptRequest().withCiphertextBlob(value);
    }
}