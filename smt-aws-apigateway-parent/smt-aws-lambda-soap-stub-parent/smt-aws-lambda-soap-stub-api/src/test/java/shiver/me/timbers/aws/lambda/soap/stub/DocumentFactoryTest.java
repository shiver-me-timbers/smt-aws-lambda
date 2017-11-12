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
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static shiver.me.timbers.aws.lambda.soap.stub.Matchers.hasContent;
import static shiver.me.timbers.data.random.RandomStrings.someString;

public class DocumentFactoryTest {

    private DocumentBuilderFactory builderFactory;
    private DocumentFactory factory;

    @Before
    public void setUp() {
        builderFactory = mock(DocumentBuilderFactory.class);
        factory = new DocumentFactory(builderFactory);
    }

    @Test
    public void Can_create_a_document() throws ParserConfigurationException {

        final DocumentBuilder builder = mock(DocumentBuilder.class);

        final Document expected = mock(Document.class);

        // Given
        given(builderFactory.newDocumentBuilder()).willReturn(builder);
        given(builder.newDocument()).willReturn(expected);

        // When
        final Document actual = factory.create();

        // Then
        assertThat(actual, is(expected));
    }

    @Test
    public void Can_create_a_document_from_xml() throws ParserConfigurationException, IOException, SAXException {

        final String xml = someString();

        final DocumentBuilder builder = mock(DocumentBuilder.class);

        final Document expected = mock(Document.class);

        // Given
        given(builderFactory.newDocumentBuilder()).willReturn(builder);
        given(builder.parse(argThat(hasContent(xml)))).willReturn(expected);

        // When
        final Document actual = factory.create(xml);

        // Then
        assertThat(actual, is(expected));
    }

    @Test
    public void Can_fail_to_read_the_xml() throws ParserConfigurationException, IOException, SAXException {

        final String xml = someString();

        final DocumentBuilder builder = mock(DocumentBuilder.class);

        final IOException exception = mock(IOException.class);

        // Given
        given(builderFactory.newDocumentBuilder()).willReturn(builder);
        given(builder.parse(argThat(hasContent(xml)))).willThrow(exception);

        // When
        final Throwable actual = catchThrowable(() -> factory.create(xml));

        // Then
        assertThat(actual, instanceOf(DocumentException.class));
        assertThat(actual.getMessage(), equalTo(format("Failed to create a Document from: %s", xml)));
        assertThat(actual.getCause(), is(exception));
    }

    @Test
    public void Can_fail_to_parse_the_xml() throws ParserConfigurationException, IOException, SAXException {

        final String xml = someString();

        final DocumentBuilder builder = mock(DocumentBuilder.class);

        final SAXException exception = mock(SAXException.class);

        // Given
        given(builderFactory.newDocumentBuilder()).willReturn(builder);
        given(builder.parse(argThat(hasContent(xml)))).willThrow(exception);

        // When
        final Throwable actual = catchThrowable(() -> factory.create(xml));

        // Then
        assertThat(actual, instanceOf(DocumentException.class));
        assertThat(actual.getMessage(), equalTo(format("Failed to create a Document from: %s", xml)));
        assertThat(actual.getCause(), is(exception));
    }
}