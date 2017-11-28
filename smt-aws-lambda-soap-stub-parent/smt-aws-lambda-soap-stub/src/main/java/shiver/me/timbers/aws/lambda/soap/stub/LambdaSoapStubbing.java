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

package shiver.me.timbers.aws.lambda.soap.stub;

import com.fasterxml.jackson.databind.ObjectMapper;
import shiver.me.timbers.aws.apigateway.proxy.JsonProxyRequestHandler;
import shiver.me.timbers.aws.common.Env;

import java.io.IOException;

import static shiver.me.timbers.aws.lambda.soap.stub.SoapStubSetup.digester;
import static shiver.me.timbers.aws.lambda.soap.stub.SoapStubSetup.repository;

public class LambdaSoapStubbing extends JsonProxyRequestHandler<Stubbing, String> {

    public LambdaSoapStubbing() throws IOException {
        this(new Env());
    }

    private LambdaSoapStubbing(Env env) throws IOException {
        super(Stubbing.class, new ObjectMapper(), new SoapStubbingProxyRequestHandler(digester(env), repository(env)));
    }
}
