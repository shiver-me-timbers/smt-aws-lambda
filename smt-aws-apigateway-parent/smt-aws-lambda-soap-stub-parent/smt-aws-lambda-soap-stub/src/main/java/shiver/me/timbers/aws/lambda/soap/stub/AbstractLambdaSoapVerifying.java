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

import java.util.List;

import static java.lang.String.format;

abstract class AbstractLambdaSoapVerifying implements RequestHandler<Verifying, String> {

    private final Logger log = Logger.getLogger(getClass());

    private final Digester digester;
    private final StubbingRepository repository;

    AbstractLambdaSoapVerifying(Digester digester, StubbingRepository repository) {
        this.digester = digester;
        this.repository = repository;
    }

    @Override
    public String handleRequest(Verifying verifying, Context context) {
        log.info("START: Starting verification.");
        log.info(format("REQUEST:\n%s", verifying.getRequest()));
        final String hash = digester.digestSoapRequest(verifying.getRequest());
        final List<String> calls = repository.findCallsByHash(hash);
        if (calledMoreThanOnce(calls)) {
            log.info(format("END: Verify for (%s) has failed. Request called more than once.", hash));
            throw new VerifyRequestError(format("Verify Failure. Request with hash (%s) called more than once.", hash));
        }
        if (neverCalled(calls)) {
            log.info(format("END: Verify for (%s) has failed. Request never called.", hash));
            throw new VerifyRequestError(format("Verify Failure. Request with hash (%s) was never called.", hash));
        }
        log.info(format("END: Verify for (%s) has succeeded.", hash));
        return format("Verify Success. Request with hash (%s) only called once.", hash);
    }

    private static boolean calledMoreThanOnce(List<String> calls) {
        return calls.size() > 1;
    }

    private static boolean neverCalled(List<String> calls) {
        return calls.size() < 1;
    }
}
