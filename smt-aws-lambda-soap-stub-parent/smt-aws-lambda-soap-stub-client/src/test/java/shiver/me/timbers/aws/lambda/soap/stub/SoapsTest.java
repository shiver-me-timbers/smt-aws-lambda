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
import org.w3c.dom.Document;

import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import java.io.IOException;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static shiver.me.timbers.data.random.RandomStrings.someString;
import static shiver.me.timbers.data.random.RandomThings.someThing;

public class SoapsTest {

    private Marshals marshals;
    private SoapMessages messages;
    private Soaps soaps;

    @Before
    public void setUp() {
        marshals = mock(Marshals.class);
        messages = mock(SoapMessages.class);
        soaps = new Soaps(marshals, messages);
    }

    @Test
    public void Can_marshal_an_object_to_a_SOAP_message() {

        final Object object = someThing();

        final Document document = mock(Document.class);
        final SOAPMessage expected = mock(SOAPMessage.class);

        // Given
        given(marshals.marshalToDocument(object)).willReturn(document);
        given(messages.wrapInSoapEnvelope(document)).willReturn(expected);

        // When
        final SOAPMessage actual = soaps.marshalToSOAPMessage(object);

        // Then
        assertThat(actual, is(expected));
    }

    @Test
    public void Can_convert_a_SOAP_message_to_a_string() throws IOException, SOAPException {

        final SOAPMessage message = mock(SOAPMessage.class);

        final String expected = someString();

        // Given
        given(messages.toXmlString(message)).willReturn(expected);

        // When
        final String actual = soaps.toXmlString(message);

        // Then
        assertThat(actual, is(expected));
    }
}