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

import java.nio.ByteBuffer;
import java.util.Map;

import static java.util.Map.Entry;
import static java.util.stream.Collectors.toMap;

/**
 * @author Karl Bennett
 */
class KmsEncryptService {

    private final AWSKMS awskms;

    KmsEncryptService(AWSKMS awskms) {
        this.awskms = awskms;
    }

    Map<String, ByteBuffer> encrypt(KmsEncryptRequest request) {
        final String kmsKeyId = request.getKmsKeyId();
        return request.getParameters().entrySet().parallelStream()
            .collect(toMap(Entry::getKey, entry -> encrypt(kmsKeyId, entry.getValue())));
    }

    private ByteBuffer encrypt(String kmsKeyId, String value) {
        return awskms.encrypt(
            new EncryptRequest().withKeyId(kmsKeyId).withPlaintext(ByteBuffer.wrap(value.getBytes()))
        ).getCiphertextBlob();
    }
}
