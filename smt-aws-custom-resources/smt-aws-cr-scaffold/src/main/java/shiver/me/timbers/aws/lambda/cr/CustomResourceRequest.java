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

/**
 * @author Karl Bennett
 */
public class CustomResourceRequest {

    private final String requestType;
    private final String requestId;
    private final String responseURL;
    private final String stackId;
    private final String resourceType;
    private final String logicalResourceId;
    private final String physicalResourceId;
    private final Map<String, Object> resourceProperties;
    private final Map<String, Object> oldResourceProperties;

    public CustomResourceRequest(
        String requestType,
        String requestId,
        String responseURL,
        String stackId,
        String resourceType,
        String logicalResourceId,
        String physicalResourceId,
        Map<String, Object> resourceProperties,
        Map<String, Object> oldResourceProperties
    ) {
        this.requestType = requestType;
        this.requestId = requestId;
        this.responseURL = responseURL;
        this.stackId = stackId;
        this.resourceType = resourceType;
        this.logicalResourceId = logicalResourceId;
        this.physicalResourceId = physicalResourceId;
        this.resourceProperties = resourceProperties;
        this.oldResourceProperties = oldResourceProperties;
    }

    public String getRequestType() {
        return requestType;
    }

    boolean isCreate() {
        return "Create".equals(requestType);
    }

    boolean isUpdate() {
        return "Update".equals(requestType);
    }

    boolean isDelete() {
        return "Delete".equals(requestType);
    }

    public String getRequestId() {
        return requestId;
    }

    public String getResponseURL() {
        return responseURL;
    }

    public String getStackId() {
        return stackId;
    }

    public String getResourceType() {
        return resourceType;
    }

    public String getLogicalResourceId() {
        return logicalResourceId;
    }

    public String getPhysicalResourceId() {
        return physicalResourceId;
    }

    public Map<String, Object> getResourceProperties() {
        return resourceProperties;
    }

    public Map<String, Object> getOldResourceProperties() {
        return oldResourceProperties;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CustomResourceRequest)) return false;

        CustomResourceRequest that = (CustomResourceRequest) o;

        if (requestType != null ? !requestType.equals(that.requestType) : that.requestType != null) return false;
        if (requestId != null ? !requestId.equals(that.requestId) : that.requestId != null) return false;
        if (responseURL != null ? !responseURL.equals(that.responseURL) : that.responseURL != null) return false;
        if (stackId != null ? !stackId.equals(that.stackId) : that.stackId != null) return false;
        if (resourceType != null ? !resourceType.equals(that.resourceType) : that.resourceType != null) return false;
        if (logicalResourceId != null ? !logicalResourceId.equals(that.logicalResourceId) : that.logicalResourceId != null)
            return false;
        if (physicalResourceId != null ? !physicalResourceId.equals(that.physicalResourceId) : that.physicalResourceId != null)
            return false;
        if (resourceProperties != null ? !resourceProperties.equals(that.resourceProperties) : that.resourceProperties != null)
            return false;
        return oldResourceProperties != null ? oldResourceProperties.equals(that.oldResourceProperties) : that.oldResourceProperties == null;
    }

    @Override
    public int hashCode() {
        int result = requestType != null ? requestType.hashCode() : 0;
        result = 31 * result + (requestId != null ? requestId.hashCode() : 0);
        result = 31 * result + (responseURL != null ? responseURL.hashCode() : 0);
        result = 31 * result + (stackId != null ? stackId.hashCode() : 0);
        result = 31 * result + (resourceType != null ? resourceType.hashCode() : 0);
        result = 31 * result + (logicalResourceId != null ? logicalResourceId.hashCode() : 0);
        result = 31 * result + (physicalResourceId != null ? physicalResourceId.hashCode() : 0);
        result = 31 * result + (resourceProperties != null ? resourceProperties.hashCode() : 0);
        result = 31 * result + (oldResourceProperties != null ? oldResourceProperties.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "CustomResourceRequest{" +
            "requestType='" + requestType + '\'' +
            ", requestId='" + requestId + '\'' +
            ", responseURL='" + responseURL + '\'' +
            ", stackId='" + stackId + '\'' +
            ", resourceType='" + resourceType + '\'' +
            ", logicalResourceId='" + logicalResourceId + '\'' +
            ", physicalResourceId='" + physicalResourceId + '\'' +
            ", resourceProperties=" + resourceProperties +
            ", oldResourceProperties=" + oldResourceProperties +
            '}';
    }
}
