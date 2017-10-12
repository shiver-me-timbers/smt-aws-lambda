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

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.w3c.dom.Document;

import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willAnswer;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static shiver.me.timbers.data.random.RandomStrings.someString;

public class SoapMessagesTest {

    private SoapMessageFactory messageFactory;
    private SoapMessages messages;

    @Before
    public void setUp() {
        messageFactory = mock(SoapMessageFactory.class);
        messages = new SoapMessages(messageFactory);
    }

    @Test
    public void Can_create_a_SOAP_message_from_some_XML() throws SOAPException {

        final String soapRequestXml = someString();

        final SOAPMessage expected = mock(SOAPMessage.class);

        // Given
        given(messageFactory.createMessage(soapRequestXml)).willReturn(expected);

        // When
        final SOAPMessage actual = messages.createMessage(soapRequestXml);

        // Then
        assertThat(actual, is(expected));
    }

    @Test
    public void Can_fail_to_create_a_SOAP_message_from_some_XML() throws SOAPException {

        final String soapRequestXml = someString();

        final SOAPException exception = mock(SOAPException.class);

        // Given
        given(messageFactory.createMessage(soapRequestXml)).willThrow(exception);

        // When
        final Throwable actual = catchThrowable(() -> messages.createMessage(soapRequestXml));

        // Then
        assertThat(actual, instanceOf(SoapException.class));
        assertThat(actual.getMessage(), equalTo(format("Failed to create a SOAPMessage from: %s", soapRequestXml)));
        assertThat(actual.getCause(), is(exception));
    }

    @Test
    public void Can_wrap_a_document_in_a_SOAP_envelope() throws SOAPException {

        final Document document = mock(Document.class);

        final SOAPMessage message = mock(SOAPMessage.class);
        final SOAPBody body = mock(SOAPBody.class);

        // Given
        given(messageFactory.createMessage()).willReturn(message);
        given(message.getSOAPBody()).willReturn(body);

        // When
        final SOAPMessage actual = messages.wrapInSoapEnvelope(document);

        // Then
        then(body).should().addDocument(document);
        assertThat(actual, is(message));
    }

    @Test
    public void Can_fail_to_wrap_a_document_in_a_SOAP_envelope() throws SOAPException {

        final SOAPException exception = mock(SOAPException.class);

        // Given
        given(messageFactory.createMessage()).willThrow(exception);

        // When
        final Throwable actual = catchThrowable(() -> messages.wrapInSoapEnvelope(mock(Document.class)));

        // Then
        assertThat(actual, instanceOf(SoapException.class));
        assertThat(actual.getMessage(), equalTo("Failed to wrap the document in a SOAP envelope."));
        assertThat(actual.getCause(), is(exception));
    }

    @Test
    public void Can_convert_a_SOAP_message_to_a_string() throws IOException, SOAPException {

        final SOAPMessage message = mock(SOAPMessage.class);

        final String expected = someString();

        // Given
        willAnswer(new Write(0, expected)).given(message).writeTo(any(OutputStream.class));

        // When
        final String actual = messages.toXmlString(message);

        // Then
        assertThat(actual, is(expected));
    }

    @Test
    public void Can_fail_to_write_the_SOAP_message_to_a_string() throws IOException, SOAPException {

        final SOAPMessage message = mock(SOAPMessage.class);

        final IOException exception = mock(IOException.class);

        // Given
        willThrow(exception).given(message).writeTo(any(OutputStream.class));

        // When
        final Throwable actual = catchThrowable(() -> messages.toXmlString(message));

        // Then
        assertThat(actual, instanceOf(SoapException.class));
        assertThat(actual.getMessage(), equalTo("Failed to convert the SOAP message to a string."));
        assertThat(actual.getCause(), is(exception));
    }

    @Test
    public void Can_fail_to_convert_the_SOAP_message_to_a_string() throws IOException, SOAPException {

        final SOAPMessage message = mock(SOAPMessage.class);

        final SOAPException exception = mock(SOAPException.class);

        // Given
        willThrow(exception).given(message).writeTo(any(OutputStream.class));

        // When
        final Throwable actual = catchThrowable(() -> messages.toXmlString(message));

        // Then
        assertThat(actual, instanceOf(SoapException.class));
        assertThat(actual.getMessage(), equalTo("Failed to convert the SOAP message to a string."));
        assertThat(actual.getCause(), is(exception));
    }

    private static class Write implements Answer<Void> {

        private final int index;
        private final String string;

        private Write(int index, String string) {
            this.index = index;
            this.string = string;
        }

        @Override
        public Void answer(InvocationOnMock invocationOnMock) throws Throwable {
            IOUtils.write(string, invocationOnMock.getArgumentAt(index, OutputStream.class), Charset.forName("UTF-8"));
            return null;
        }
    }
}