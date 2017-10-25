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

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class GivenSoapStub {

    private final String stubbingUrl;
    private final Soaps soaps;
    private final Client client;

    public GivenSoapStub(String stubbingUrl) {
        this(
            stubbingUrl,
            new Soaps(
                new Marshals(new DocumentFactory(DocumentBuilderFactory.newInstance()), new JaxbContextFactory()),
                new SoapMessages(new SoapMessageFactory())),
            ClientBuilder.newClient()
        );
    }

    GivenSoapStub(String stubbingUrl, Soaps soaps, Client client) {
        this.stubbingUrl = stubbingUrl;
        this.soaps = soaps;
        this.client = client;
    }

    public RequestStubbing request(Object request) {
        return new RequestStubbing(soaps, soaps.marshalToSOAPMessage(request), client.target(stubbingUrl));
    }
}
