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

import shiver.me.timbers.aws.lambda.cr.CustomResourceRequest;

import java.nio.ByteBuffer;
import java.util.Map;

import static java.util.Map.Entry;
import static java.util.stream.Collectors.toMap;

/**
 * @author Karl Bennett
 */
@SuppressWarnings("unchecked")
class KmsDecryptRequest extends CustomResourceRequest {

    private final Base64 base64;

    KmsDecryptRequest(Base64 base64, CustomResourceRequest request) {
        super(
            request.getRequestType(),
            request.getRequestId(),
            request.getResponseURL(),
            request.getStackId(),
            request.getResourceType(),
            request.getLogicalResourceId(),
            request.getPhysicalResourceId(),
            request.getResourceProperties(),
            request.getOldResourceProperties()
        );
        this.base64 = base64;
    }

    Map<String, ByteBuffer> getParameters() {
        return ((Map<String, String>) getResourceProperties().get("Parameters")).entrySet().stream()
            .collect(toMap(Entry::getKey, this::decode));
    }

    private ByteBuffer decode(Entry<String, String> entry) {
        return base64.decode(entry.getValue());
    }
}
