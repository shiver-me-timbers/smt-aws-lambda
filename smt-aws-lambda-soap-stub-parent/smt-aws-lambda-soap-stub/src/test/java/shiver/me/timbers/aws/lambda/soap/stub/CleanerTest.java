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
import org.mockito.InOrder;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.w3c.dom.NodeList;

import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamResult;
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
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static shiver.me.timbers.data.random.RandomStrings.someString;

public class CleanerTest {

    private SoapMessages messages;
    private Cleaner cleaner;
    private TransformerFactory transformerFactory;

    @Before
    public void setUp() {
        messages = mock(SoapMessages.class);
        transformerFactory = mock(TransformerFactory.class);
        cleaner = new Cleaner(messages, transformerFactory);
    }

    @Test
    public void Can_clean_out_a_SOAP_header() throws SOAPException {

        final String soapRequestXml = someString();

        final SOAPMessage message = mock(SOAPMessage.class);
        final SOAPHeader header = mock(SOAPHeader.class);
        final NodeList nodes = mock(NodeList.class);

        final String start = someString();
        final String end = someString();
        final String expected = format("%s\n%s", start, end);

        // Given
        given(messages.createMessage(soapRequestXml)).willReturn(message);
        given(message.getSOAPHeader()).willReturn(header);
        given(messages.toXmlString(message)).willReturn(format("%s\n   \n%s", start, end));

        // When
        final String actual = cleaner.cleanSOAPHeader(soapRequestXml);

        // Then
        final InOrder order = inOrder(header, messages);
        order.verify(header).detachNode();
        order.verify(messages).toXmlString(message);
        assertThat(actual, is(expected));
    }

    @Test
    public void Can_ignore_no_header() throws SOAPException {

        final String soapRequestXml = someString();

        final SOAPMessage message = mock(SOAPMessage.class);

        final String start = someString();
        final String end = someString();
        final String expected = format("%s\n%s", start, end);

        // Given
        given(messages.createMessage(soapRequestXml)).willReturn(message);
        given(message.getSOAPHeader()).willReturn(null);
        given(messages.toXmlString(message)).willReturn(format("%s\n   \n%s", start, end));

        // When
        final String actual = cleaner.cleanSOAPHeader(soapRequestXml);

        // Then
        then(messages).should().toXmlString(message);
        assertThat(actual, is(expected));
    }

    @Test
    public void Can_fail_to_clean_out_an_empty_SOAP_header() throws SOAPException {

        final String soapRequestXml = someString();

        final SOAPMessage message = mock(SOAPMessage.class);

        final SOAPException exception = mock(SOAPException.class);

        // Given
        given(messages.createMessage(soapRequestXml)).willReturn(message);
        given(message.getSOAPHeader()).willThrow(exception);

        // When
        final Throwable actual = catchThrowable(() -> cleaner.cleanSOAPHeader(soapRequestXml));

        // Then
        assertThat(actual, instanceOf(SoapException.class));
        assertThat(actual.getMessage(), equalTo("Failed to clean out the empty header."));
        assertThat(actual.getCause(), is(exception));
    }

    @Test
    public void Can_clean_out_the_namespaces() throws TransformerException {

        final String xml = someString();

        final Transformer transformer = mock(Transformer.class);

        final String expected = someString();

        // Given
        given(transformerFactory.createTransformer()).willReturn(transformer);
        willAnswer(new Write(expected)).given(transformer).transform(any(Source.class), any(Result.class));

        // When
        final String actual = cleaner.cleanNamespaces(xml);

        // Then
        final InOrder order = inOrder(transformer);
        order.verify(transformer).setOutputProperty("omit-xml-declaration", "yes");
        order.verify(transformer).transform(any(Source.class), any(Result.class));
        assertThat(actual, is(expected));
    }

    @Test
    public void Can_fail_to_clean_out_the_namespaces() throws TransformerException {

        final String xml = someString();

        final Transformer transformer = mock(Transformer.class);

        final TransformerException exception = mock(TransformerException.class);

        // Given
        given(transformerFactory.createTransformer()).willReturn(transformer);
        willThrow(exception).given(transformer).transform(any(Source.class), any(Result.class));

        // When
        final Throwable actual = catchThrowable(() -> cleaner.cleanNamespaces(xml));

        // Then
        assertThat(actual, instanceOf(XmlException.class));
        assertThat(actual.getMessage(), equalTo("Failed to remove the namespaces"));
        assertThat(actual.getCause(), is(exception));
    }

    private static class Write implements Answer<Void> {

        private final String string;

        private Write(String string) {
            this.string = string;
        }

        @Override
        public Void answer(InvocationOnMock invocationOnMock) throws Throwable {
            IOUtils.write(
                string,
                invocationOnMock.getArgumentAt(1, StreamResult.class).getOutputStream(),
                Charset.forName("UTF-8")
            );
            return null;
        }
    }
}