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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * @author Karl Bennett
 */
@JsonInclude(NON_NULL)
public class CustomResourceResponse {

    private final String status;
    private final String requestId;
    private final String stackId;
    private final String logicalResourceId;
    private final String physicalResourceId;
    private final Map<String, Object> data;
    private final String reason;

    public CustomResourceResponse(
        @JsonProperty(value = "Status", required = true) String status,
        @JsonProperty(value = "RequestId", required = true) String requestId,
        @JsonProperty(value = "StackId", required = true) String stackId,
        @JsonProperty(value = "LogicalResourceId", required = true) String logicalResourceId,
        @JsonProperty(value = "PhysicalResourceId", required = true) String physicalResourceId,
        @JsonProperty("Data") Map<String, Object> data,
        @JsonProperty("Reason") String reason
    ) {
        this.status = status;
        this.requestId = requestId;
        this.stackId = stackId;
        this.logicalResourceId = logicalResourceId;
        this.physicalResourceId = physicalResourceId;
        this.data = data;
        this.reason = reason;
    }

    @JsonProperty("Status")
    public String getStatus() {
        return status;
    }

    @JsonProperty("RequestId")
    public String getRequestId() {
        return requestId;
    }

    @JsonProperty("StackId")
    public String getStackId() {
        return stackId;
    }

    @JsonProperty("LogicalResourceId")
    public String getLogicalResourceId() {
        return logicalResourceId;
    }

    @JsonProperty("PhysicalResourceId")
    public String getPhysicalResourceId() {
        return physicalResourceId;
    }

    @JsonProperty("Data")
    public Map<String, Object> getData() {
        return data;
    }

    @JsonProperty("Reason")
    public String getReason() {
        return reason;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CustomResourceResponse)) return false;

        CustomResourceResponse that = (CustomResourceResponse) o;

        if (status != null ? !status.equals(that.status) : that.status != null) return false;
        if (requestId != null ? !requestId.equals(that.requestId) : that.requestId != null) return false;
        if (stackId != null ? !stackId.equals(that.stackId) : that.stackId != null) return false;
        if (logicalResourceId != null ? !logicalResourceId.equals(that.logicalResourceId) : that.logicalResourceId != null)
            return false;
        if (physicalResourceId != null ? !physicalResourceId.equals(that.physicalResourceId) : that.physicalResourceId != null)
            return false;
        if (data != null ? !data.equals(that.data) : that.data != null) return false;
        return reason != null ? reason.equals(that.reason) : that.reason == null;
    }

    @Override
    public int hashCode() {
        int result = status != null ? status.hashCode() : 0;
        result = 31 * result + (requestId != null ? requestId.hashCode() : 0);
        result = 31 * result + (stackId != null ? stackId.hashCode() : 0);
        result = 31 * result + (logicalResourceId != null ? logicalResourceId.hashCode() : 0);
        result = 31 * result + (physicalResourceId != null ? physicalResourceId.hashCode() : 0);
        result = 31 * result + (data != null ? data.hashCode() : 0);
        result = 31 * result + (reason != null ? reason.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "CustomResourceResponse{" +
            "status='" + status + '\'' +
            ", requestId='" + requestId + '\'' +
            ", stackId='" + stackId + '\'' +
            ", logicalResourceId='" + logicalResourceId + '\'' +
            ", physicalResourceId='" + physicalResourceId + '\'' +
            ", data=" + data +
            ", reason='" + reason + '\'' +
            '}';
    }
}
