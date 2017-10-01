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

import shiver.me.timbers.aws.lambda.cr.CreateOrUpdateCustomResourceHandler;
import shiver.me.timbers.aws.lambda.cr.CustomResourceRequest;

import java.util.Map;

import static java.util.Collections.emptyMap;
import static java.util.Map.Entry;
import static java.util.stream.Collectors.toMap;

/**
 * @author Karl Bennett
 */
class KmsEncryptHandler extends CreateOrUpdateCustomResourceHandler {

    private final Base64 base64;
    private final KmsEncryptService service;

    KmsEncryptHandler(Base64 base64, KmsEncryptService service) {
        this.base64 = base64;
        this.service = service;
    }

    @Override
    protected Map<String, Object> createOrUpdate(CustomResourceRequest request) {
        return service.encrypt(new KmsEncryptRequest(request)).entrySet().stream()
            .collect(toMap(Entry::getKey, entry -> base64.encode(entry.getValue())));
    }

    @Override
    public Map<String, Object> delete(CustomResourceRequest request) {
        return emptyMap();
    }
}
