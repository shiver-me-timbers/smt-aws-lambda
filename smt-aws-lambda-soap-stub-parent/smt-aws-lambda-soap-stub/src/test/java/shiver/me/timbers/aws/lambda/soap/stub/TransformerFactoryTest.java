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

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class TransformerFactoryTest {

    private Templates templates;
    private TransformerFactory factory;

    @Before
    public void setUp() {
        templates = mock(Templates.class);
        factory = new TransformerFactory(templates);
    }

    @Test
    public void Can_create_a_transformer() throws TransformerConfigurationException {

        final Transformer expected = mock(Transformer.class);

        // Given
        given(templates.newTransformer()).willReturn(expected);

        // When
        final Transformer actual = factory.createTransformer();

        // Then
        assertThat(actual, is(expected));
    }

    @Test
    public void Can_fail_to_create_a_transformer() throws TransformerConfigurationException {

        final TransformerConfigurationException exception = mock(TransformerConfigurationException.class);

        // Given
        given(templates.newTransformer()).willThrow(exception);

        // When
        final Throwable actual = catchThrowable(() -> factory.createTransformer());

        // Then
        assertThat(actual, instanceOf(TransformationException.class));
        assertThat(actual.getMessage(), equalTo("Failed to create a a Transformer."));
        assertThat(actual.getCause(), is(exception));
    }
}