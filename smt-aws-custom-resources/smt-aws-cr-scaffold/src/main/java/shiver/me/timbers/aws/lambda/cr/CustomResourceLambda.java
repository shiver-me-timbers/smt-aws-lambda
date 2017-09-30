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

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;

import java.util.Map;

import static java.lang.String.format;

/**
 * @author Karl Bennett
 */
public class CustomResourceLambda implements RequestHandler<Map<String, Object>, String> {

    private Logger log = Logger.getLogger(getClass());

    private final CustomResourceIoMapper mapper;
    private final CustomResourceMappingHandler handler;
    private final CustomResourceClient client;

    public CustomResourceLambda(CustomResourceHandler handler) {
        this(new CustomResourceIoMapper(), handler);
    }

    private CustomResourceLambda(CustomResourceIoMapper mapper, CustomResourceHandler handler) {
        this(
            mapper,
            new CustomResourceMappingHandler(mapper, handler),
            new CustomResourceClient(new HttpURLConnectionFactory(), new ObjectMapper())
        );
    }

    CustomResourceLambda(
        CustomResourceIoMapper mapper,
        CustomResourceMappingHandler handler,
        CustomResourceClient client
    ) {
        this.mapper = mapper;
        this.handler = handler;
        this.client = client;
    }

    @Override
    public String handleRequest(Map<String, Object> requestMap, Context context) {
        log.info(format("START: The custom resource has been started with request: %s", requestMap));
        final CustomResourceRequest request = mapper.mapRequest(requestMap);

        final CustomResourceResponse response = handle(request);

        log.info(format("RESULT: Uploading the %s response to the custom resource S3 bucket.", response.getStatus()));
        client.upload(request.getResponseURL(), response);

        log.info("END: The custom resource has finished.");
        return format(
            "Customer resource (%s) %s has finished.", getClass().getName(), request.getRequestType()
        );
    }

    private CustomResourceResponse handle(CustomResourceRequest request) {
        try {
            if (request.isCreate()) {
                log.info("CREATE: Handling the custom resource CREATE request.");
                return handler.create(request);
            }
            if (request.isUpdate()) {
                log.info("CREATE: Handling the custom resource UPDATE request.");
                return handler.update(request);
            }
            if (request.isDelete()) {
                log.info("CREATE: Handling the custom resource DELETE request.");
                return handler.delete(request);
            }
        } catch (RuntimeException e) {
            log.error(format("FAILED: The custom resource %s failed", request.getRequestType()), e);
            return mapper.mapFailureResponse(request, e);
        }
        final String type = request.getRequestType();
        log.error(format("FAILED: The custom resource %s request type is unknown", type));
        return mapper.mapFailureResponse(request, format("Custom Resource request type %s is unknown.", type));
    }
}
