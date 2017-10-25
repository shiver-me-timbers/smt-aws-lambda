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
import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.apache.log4j.Logger;

import static java.lang.String.format;

abstract class AbstractLambdaSoapStub implements RequestHandler<SoapWrapper, SoapWrapper> {

    private final Logger log = Logger.getLogger(getClass());

    private final Digester digester;
    private final StubbingRepository repository;

    AbstractLambdaSoapStub(Digester digester, StubbingRepository repository) {
        this.digester = digester;
        this.repository = repository;
    }

    @Override
    public SoapWrapper handleRequest(SoapWrapper request, Context context) {
        log.info("START: Soap studded response.");
        final String body = request.getBody();
        log.info(format("REQUEST:\n%s", body));
        final String hash = digester.digestSoapRequest(body);
        log.info(format("Finding stubbed response for hash (%s).", hash));
        final String response = repository.findResponseByHash(hash);
        log.info(format("Saving stub call for hash (%s).", hash));
        repository.recordCall(hash, body);
        log.info(format("END: Returning response with hash (%s).", hash));
        log.info(format("RESPONSE:\n%s", response));
        return new SoapWrapper(response);
    }
}
