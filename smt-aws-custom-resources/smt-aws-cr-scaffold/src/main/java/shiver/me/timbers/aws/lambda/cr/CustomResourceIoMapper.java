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

package shiver.me.timbers.aws.lambda.cr;

import java.util.Map;

import static java.lang.String.format;

/**
 * @author Karl Bennett
 */
class CustomResourceIoMapper {

    @SuppressWarnings("unchecked")
    CustomResourceRequest mapRequest(Map<String, Object> request) {
        return new CustomResourceRequest(
            (String) request.get("RequestType"),
            (String) request.get("RequestId"),
            (String) request.get("ResponseURL"),
            (String) request.get("StackId"),
            (String) request.get("ResourceType"),
            (String) request.get("LogicalResourceId"),
            (String) request.get("PhysicalResourceId"),
            (Map<String, Object>) request.get("ResourceProperties"),
            (Map<String, Object>) request.get("OldResourceProperties")
        );
    }

    CustomResourceResponse mapSuccessResponse(CustomResourceRequest request, Map<String, Object> data) {
        return mapResponse("SUCCESS", request, data, null);
    }

    CustomResourceResponse mapFailureResponse(CustomResourceRequest request, RuntimeException exception) {
        return mapFailureResponse(request, exception.getMessage());
    }

    CustomResourceResponse mapFailureResponse(CustomResourceRequest request, String reason) {
        return mapResponse("FAILED", request, null, reason);
    }

    private CustomResourceResponse mapResponse(
        String status,
        CustomResourceRequest request,
        Map<String, Object> data,
        String reason
    ) {
        return new CustomResourceResponse(
            status,
            request.getRequestId(),
            request.getStackId(),
            request.getLogicalResourceId(),
            createPhysicalResourceId(request),
            data,
            reason
        );
    }

    private String createPhysicalResourceId(CustomResourceRequest request) {
        final String physicalResourceId = request.getPhysicalResourceId();
        return physicalResourceId == null ?
            format("%s:%s", request.getStackId(), request.getLogicalResourceId()) : physicalResourceId;
    }
}
