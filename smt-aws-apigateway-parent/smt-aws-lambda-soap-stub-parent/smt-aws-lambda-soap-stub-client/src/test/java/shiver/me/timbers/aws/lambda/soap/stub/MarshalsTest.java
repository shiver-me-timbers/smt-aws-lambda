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

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.ParserConfigurationException;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static shiver.me.timbers.data.random.RandomThings.someThing;

public class MarshalsTest {

    private DocumentFactory documentFactory;
    private JaxbContextFactory contextFactory;
    private Marshals marshals;

    @Before
    public void setUp() {
        documentFactory = mock(DocumentFactory.class);
        contextFactory = mock(JaxbContextFactory.class);
        marshals = new Marshals(documentFactory, contextFactory);
    }

    @Test
    public void Can_marshal_an_object_to_a_document() throws JAXBException, ParserConfigurationException {

        final Object object = someThing();

        final Document document = mock(Document.class);
        final JAXBContext context = mock(JAXBContext.class);
        final Marshaller marshaller = mock(Marshaller.class);

        // Given
        given(documentFactory.create()).willReturn(document);
        given(contextFactory.create(object.getClass())).willReturn(context);
        given(context.createMarshaller()).willReturn(marshaller);

        // When
        final Document actual = marshals.marshalToDocument(object);

        // Then
        then(marshaller).should().marshal(object, document);
        assertThat(actual, is(document));
    }

    @Test
    public void Can_fail_to_create_the_document_to_marshal_to() throws JAXBException, ParserConfigurationException {

        final Object object = someThing();

        final ParserConfigurationException exception = mock(ParserConfigurationException.class);

        // Given
        given(documentFactory.create()).willThrow(exception);

        // When
        final Throwable actual = catchThrowable(() -> marshals.marshalToDocument(object));

        // Then
        assertThat(actual, instanceOf(SoapException.class));
        assertThat(actual.getMessage(), equalTo("Failed to marshal to the document."));
        assertThat(actual.getCause(), is(exception));
    }

    @Test
    public void Can_fail_to_create_the_JAXB_context() throws JAXBException, ParserConfigurationException {

        final Object object = someThing();

        final JAXBException exception = mock(JAXBException.class);

        // Given
        given(documentFactory.create()).willReturn(mock(Document.class));
        given(contextFactory.create(object.getClass())).willThrow(exception);

        // When
        final Throwable actual = catchThrowable(() -> marshals.marshalToDocument(object));

        // Then
        assertThat(actual, instanceOf(SoapException.class));
        assertThat(actual.getMessage(), equalTo("Failed to marshal to the document."));
        assertThat(actual.getCause(), is(exception));
    }
}