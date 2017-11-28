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

import shiver.me.timbers.aws.common.Env;

import java.io.IOException;

import static shiver.me.timbers.aws.lambda.soap.stub.SoapStubSetup.digester;
import static shiver.me.timbers.aws.lambda.soap.stub.SoapStubSetup.repository;

public class LambdaSoapStub extends SoapStubProxyRequestHandler {

    public LambdaSoapStub() throws IOException {
        this(new Env());
    }

    private LambdaSoapStub(Env env) throws IOException {
        this(digester(env), repository(env));
    }

    private LambdaSoapStub(Digester digester, StubbingRepository repository) {
        super(digester, repository);
    }
}
