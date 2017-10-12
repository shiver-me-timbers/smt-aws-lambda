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

import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import java.io.IOException;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static shiver.me.timbers.aws.lambda.soap.stub.Matchers.hasContent;
import static shiver.me.timbers.data.random.RandomStrings.someString;

public class SoapMessageFactoryTest {

    private MessageFactory messageFactory;
    private SoapMessageFactory factory;

    @Before
    public void setUp() {
        messageFactory = mock(MessageFactory.class);
        factory = new SoapMessageFactory(messageFactory);
    }

    @Test
    public void Instantiation_for_coverage() {
        new SoapMessageFactory();
    }

    @Test
    public void Can_create_a_SOAP_message() throws SOAPException {

        final SOAPMessage expected = mock(SOAPMessage.class);

        // Given
        given(messageFactory.createMessage()).willReturn(expected);

        // When
        final SOAPMessage actual = factory.createMessage();

        // Then
        assertThat(actual, is(expected));
    }

    @Test
    public void Can_create_a_SOAP_message_from_some_XML() throws SOAPException, IOException {

        final String soapRequestXml = someString();

        final SOAPMessage expected = mock(SOAPMessage.class);

        // Given
        given(messageFactory.createMessage(any(MimeHeaders.class), argThat(hasContent(soapRequestXml))))
            .willReturn(expected);

        // When
        final SOAPMessage actual = factory.createMessage(soapRequestXml);

        // Then
        assertThat(actual, is(expected));
    }

    @Test
    public void Can_fail_to_create_a_SOAP_message_from_some_XML() throws SOAPException, IOException {

        final String soapRequestXml = someString();

        final IOException exception = mock(IOException.class);

        // Given
        given(messageFactory.createMessage(any(MimeHeaders.class), argThat(hasContent(soapRequestXml))))
            .willThrow(exception);

        // When
        final Throwable actual = catchThrowable(() -> factory.createMessage(soapRequestXml));

        // Then
        assertThat(actual, instanceOf(SOAPException.class));
        assertThat(actual.getMessage(), equalTo(format("Failed to create a SOAPMessage from: %s", soapRequestXml)));
        assertThat(actual.getCause(), is(exception));
    }
}