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

package shiver.me.timbers.aws.lambda.cr.parameters;

import com.amazonaws.services.simplesystemsmanagement.model.Parameter;
import shiver.me.timbers.aws.lambda.cr.CreateOrUpdateCustomResourceHandler;
import shiver.me.timbers.aws.lambda.cr.CustomResourceRequest;

import java.util.Map;

import static java.util.Collections.emptyMap;
import static java.util.stream.Collectors.toMap;

/**
 * @author Karl Bennett
 */
class GetParametersHandler extends CreateOrUpdateCustomResourceHandler {

    private final ParametersService parametersService;

    GetParametersHandler(ParametersService parametersService) {
        this.parametersService = parametersService;
    }

    @Override
    protected Map<String, Object> createOrUpdate(CustomResourceRequest request) {
        return parametersService.getParameters(new GetParametersResourceRequest(request)).stream()
            .collect(toMap(Parameter::getName, Parameter::getValue));
    }

    @Override
    public Map<String, Object> delete(CustomResourceRequest request) {
        return emptyMap();
    }
}
