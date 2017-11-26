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

import com.amazonaws.services.lambda.runtime.Context;
import org.apache.log4j.Logger;
import shiver.me.timbers.aws.apigateway.proxy.DeserialisedProxyRequestHandler;
import shiver.me.timbers.aws.apigateway.proxy.ProxyRequest;
import shiver.me.timbers.aws.apigateway.proxy.ProxyResponse;

import static java.lang.String.format;

class SoapStubbingProxyRequestHandler implements DeserialisedProxyRequestHandler<Stubbing, String> {

    private final Logger log = Logger.getLogger(getClass());

    private final Digester digester;
    private final StubbingRepository repository;

    SoapStubbingProxyRequestHandler(Digester digester, StubbingRepository repository) {
        this.digester = digester;
        this.repository = repository;
    }

    @Override
    public ProxyResponse<String> handleRequest(ProxyRequest<Stubbing> request, Context context) {
        log.info("START: Setting up stub.");
        final Stubbing stubbing = request.getBody();
        log.info(format("REQUEST:\n%s", stubbing.getRequest()));
        log.info(format("RESPONSE:\n%s", stubbing.getResponse()));
        final String hash = digester.digestSoapRequest(stubbing.getRequest());
        repository.save(hash, stubbing);
        log.info(format("END: Stub (%s) has been setup.", hash));
        return new StubbingProxyResponse(format("SOAP stub saved with hash (%s).", hash));
    }
}
