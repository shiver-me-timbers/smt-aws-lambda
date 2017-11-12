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

import javax.ws.rs.client.WebTarget;
import javax.xml.soap.SOAPMessage;

import static javax.ws.rs.client.Entity.entity;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;

public class RequestStubbing {

    private final Soaps soaps;
    private final SOAPMessage requestMessage;
    private final WebTarget target;

    RequestStubbing(Soaps soaps, SOAPMessage requestMessage, WebTarget target) {
        this.soaps = soaps;
        this.requestMessage = requestMessage;
        this.target = target;
    }

    public void willRespond(Object response) {
        final SOAPMessage responseMessage = soaps.marshalToSOAPMessage(response);
        target.request(APPLICATION_JSON_TYPE).put(
            entity(
                new Stubbing(soaps.toXmlString(requestMessage), soaps.toXmlString(responseMessage)),
                APPLICATION_JSON_TYPE
            ),
            String.class
        );
    }
}
