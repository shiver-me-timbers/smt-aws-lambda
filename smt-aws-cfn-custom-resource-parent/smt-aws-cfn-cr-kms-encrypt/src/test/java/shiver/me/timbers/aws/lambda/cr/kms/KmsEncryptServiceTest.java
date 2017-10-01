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

public class KmsEncryptServiceTest {

    @Test
    public void Can_encrypt_some_values() {

        final AWSKMS awskms = mock(AWSKMS.class);
        final KmsEncryptRequest request = mock(KmsEncryptRequest.class);

        final String kmsKeyId = someString();
        final Map<String, String> parameters = new HashMap<>();
        final String name1 = someString();
        final String name2 = someString();
        final String name3 = someString();
        final String value1 = someString();
        final String value2 = someString();
        final String value3 = someString();
        final EncryptResult result1 = mock(EncryptResult.class);
        final EncryptResult result2 = mock(EncryptResult.class);
        final EncryptResult result3 = mock(EncryptResult.class);
        final ByteBuffer blob1 = mock(ByteBuffer.class);
        final ByteBuffer blob2 = mock(ByteBuffer.class);
        final ByteBuffer blob3 = mock(ByteBuffer.class);

        // Given
        given(request.getKmsKeyId()).willReturn(kmsKeyId);
        given(request.getParameters()).willReturn(parameters);
        parameters.put(name1, value1);
        parameters.put(name2, value2);
        parameters.put(name3, value3);
        given(awskms.encrypt(request(kmsKeyId, value1))).willReturn(result1);
        given(awskms.encrypt(request(kmsKeyId, value2))).willReturn(result2);
        given(awskms.encrypt(request(kmsKeyId, value3))).willReturn(result3);
        given(result1.getCiphertextBlob()).willReturn(blob1);
        given(result2.getCiphertextBlob()).willReturn(blob2);
        given(result3.getCiphertextBlob()).willReturn(blob3);

        // When
        final Map<String, ByteBuffer> actual = new KmsEncryptService(awskms).encrypt(request);

        // Then
        assertThat(actual, allOf(hasEntry(name1, blob1), hasEntry(name2, blob2), hasEntry(name3, blob3)));

    }

    private static EncryptRequest request(String kmsKeyId, String value1) {
        return new EncryptRequest().withKeyId(kmsKeyId).withPlaintext(ByteBuffer.wrap(value1.getBytes()));
    }
}