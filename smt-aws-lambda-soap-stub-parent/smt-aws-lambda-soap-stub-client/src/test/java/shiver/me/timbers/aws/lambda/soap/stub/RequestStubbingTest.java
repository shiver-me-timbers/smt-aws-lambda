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

import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.WebTarget;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import static javax.ws.rs.client.Entity.entity;
import static javax.ws.rs.client.Invocation.Builder;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static shiver.me.timbers.data.random.RandomStrings.someString;
import static shiver.me.timbers.data.random.RandomThings.someThing;

public class RequestStubbingTest {

    private Soaps soaps;
    private SOAPMessage requestMessage;
    private WebTarget target;
    private RequestStubbing stubbing;

    @Before
    public void setUp() {
        soaps = mock(Soaps.class);
        requestMessage = mock(SOAPMessage.class);
        target = mock(WebTarget.class);
        stubbing = new RequestStubbing(soaps, requestMessage, target);
    }

    @Test
    public void Can_stub_a_response() throws SOAPException {

        final Object response = someThing();

        final SOAPMessage responseMessage = mock(SOAPMessage.class);
        final String requestXml = someString();
        final String responseXml = someString();
        final Builder builder = mock(Builder.class);

        // Given
        given(soaps.marshalToSOAPMessage(response)).willReturn(responseMessage);
        given(soaps.toXmlString(requestMessage)).willReturn(requestXml);
        given(soaps.toXmlString(responseMessage)).willReturn(responseXml);
        given(target.request(APPLICATION_JSON_TYPE)).willReturn(builder);

        // When
        stubbing.willRespond(response);

        // Then
        then(builder).should().put(entity(new Stubbing(requestXml, responseXml), APPLICATION_JSON_TYPE), String.class);
    }
}