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

import java.util.Map;

/**
 * @author Karl Bennett
 */
@SuppressWarnings("unchecked")
class KmsEncryptRequest extends CustomResourceRequest {

    KmsEncryptRequest(CustomResourceRequest request) {
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
    }

    String getKmsKeyId() {
        return (String) getResourceProperties().get("KmsKeyId");
    }

    Map<String, String> getParameters() {
        return (Map<String, String>) getResourceProperties().get("Parameters");
    }
}
