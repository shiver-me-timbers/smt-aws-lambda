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

package shiver.me.timbers.aws.lambda.cr.ssm;

import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagement;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagementClientBuilder;
import shiver.me.timbers.aws.lambda.cr.CustomResourceLambda;

/**
 * @author Karl Bennett
 */
public class GetParameters extends CustomResourceLambda {

    public GetParameters() {
        this(AWSSimpleSystemsManagementClientBuilder.defaultClient());
    }

    GetParameters(AWSSimpleSystemsManagement awsSimpleSystemsManagement) {
        super(new GetParametersHandler(new ParametersService(awsSimpleSystemsManagement)));
    }
}
