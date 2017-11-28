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

import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class TransformerFactoryTest {

    private TransformerFactory factory;
    private TagTemplatesFactory tagTemplatesFactory;
    private NamespaceTemplatesFactory namespaceTemplatesFactory;

    @Before
    public void setUp() {
        namespaceTemplatesFactory = mock(NamespaceTemplatesFactory.class);
        tagTemplatesFactory = mock(TagTemplatesFactory.class);
        factory = new TransformerFactory(namespaceTemplatesFactory, tagTemplatesFactory);
    }

    @Test
    public void Can_create_a_namespace_transformer() throws TransformerConfigurationException {

        final Templates templates = mock(Templates.class);
        final Transformer expected = mock(Transformer.class);

        // Given
        given(namespaceTemplatesFactory.create()).willReturn(templates);
        given(templates.newTransformer()).willReturn(expected);

        // When
        final Transformer actual = new TransformerFactory(namespaceTemplatesFactory, tagTemplatesFactory)
            .createNameSpaceTransformer();

        // Then
        assertThat(actual, is(expected));
    }

    @Test
    public void Can_fail_to_create_a_namespace_transformer() throws TransformerConfigurationException {

        final Templates templates = mock(Templates.class);

        final TransformerConfigurationException exception = mock(TransformerConfigurationException.class);

        // Given
        given(namespaceTemplatesFactory.create()).willReturn(templates);
        given(templates.newTransformer()).willThrow(exception);

        // When
        final Throwable actual = catchThrowable(
            () -> new TransformerFactory(namespaceTemplatesFactory, tagTemplatesFactory).createNameSpaceTransformer()
        );

        // Then
        assertThat(actual, instanceOf(TransformationException.class));
        assertThat(actual.getMessage(), equalTo("Failed to create a a Transformer."));
        assertThat(actual.getCause(), is(exception));
    }

    @Test
    public void Can_create_some_tag_transformer() throws TransformerConfigurationException {

        final Templates templates1 = mock(Templates.class);
        final Templates templates2 = mock(Templates.class);
        final Templates templates3 = mock(Templates.class);
        final Transformer transformer1 = mock(Transformer.class);
        final Transformer transformer2 = mock(Transformer.class);
        final Transformer transformer3 = mock(Transformer.class);

        // Given
        given(tagTemplatesFactory.createAll()).willReturn(asList(templates1, templates2, templates3));
        given(templates1.newTransformer()).willReturn(transformer1);
        given(templates2.newTransformer()).willReturn(transformer2);
        given(templates3.newTransformer()).willReturn(transformer3);

        // When
        final List<Transformer> actual = new TransformerFactory(namespaceTemplatesFactory, tagTemplatesFactory)
            .createTagTransformers();

        // Then
        assertThat(actual, contains(transformer1, transformer2, transformer3));
    }

    @Test
    public void Can_fail_to_create_a_tag_transformers() throws TransformerConfigurationException {

        final Templates templates1 = mock(Templates.class);
        final Templates templates2 = mock(Templates.class);
        final Transformer transformer1 = mock(Transformer.class);

        final TransformerConfigurationException exception = mock(TransformerConfigurationException.class);

        // Given
        given(tagTemplatesFactory.createAll()).willReturn(asList(templates1, templates2, mock(Templates.class)));
        given(templates1.newTransformer()).willReturn(transformer1);
        given(templates2.newTransformer()).willThrow(exception);

        // When
        final Throwable actual = catchThrowable(
            () -> new TransformerFactory(namespaceTemplatesFactory, tagTemplatesFactory).createTagTransformers()
        );

        // Then
        assertThat(actual, instanceOf(TransformationException.class));
        assertThat(actual.getMessage(), equalTo("Failed to create a a Transformer."));
        assertThat(actual.getCause(), is(exception));
    }
}