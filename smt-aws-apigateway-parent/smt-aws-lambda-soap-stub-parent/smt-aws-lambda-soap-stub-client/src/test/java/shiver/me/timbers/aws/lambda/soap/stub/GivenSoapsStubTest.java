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

import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static shiver.me.timbers.data.random.RandomStrings.someString;
import static shiver.me.timbers.data.random.RandomThings.someThing;
import static shiver.me.timbers.matchers.Matchers.hasField;

public class GivenSoapsStubTest {

    private String stubUrl;
    private Soaps soaps;
    private Client client;
    private GivenSoapStub given;

    @Before
    public void setUp() {
        stubUrl = someString();
        client = mock(Client.class);
        soaps = mock(Soaps.class);
        given = new GivenSoapStub(stubUrl, soaps, client);
    }

    @Test
    public void Can_stub_a_response() throws SOAPException {

        final Object method = someThing();

        final SOAPMessage requestMessage = mock(SOAPMessage.class);
        final WebTarget target = mock(WebTarget.class);

        // Given
        given(soaps.marshalToSOAPMessage(method)).willReturn(requestMessage);
        given(client.target(stubUrl)).willReturn(target);

        // When
        final RequestStubbing actual = given.request(method);

        // Then
        assertThat(actual, hasField("target", target));
        assertThat(actual, hasField("requestMessage", requestMessage));
    }
}