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

/**
 * @author Karl Bennett
 */
class KmsDecryptHandler extends CreateOrUpdateCustomResourceHandler {

    private final Base64 base64;
    private final KmsDecryptService service;

    KmsDecryptHandler(Base64 base64, KmsDecryptService service) {
        this.base64 = base64;
        this.service = service;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected Map<String, Object> createOrUpdate(CustomResourceRequest request) {
        return (Map) service.decrypt(new KmsDecryptRequest(base64, request));
    }

    @Override
    public Map<String, Object> delete(CustomResourceRequest request) {
        return emptyMap();
    }
}
