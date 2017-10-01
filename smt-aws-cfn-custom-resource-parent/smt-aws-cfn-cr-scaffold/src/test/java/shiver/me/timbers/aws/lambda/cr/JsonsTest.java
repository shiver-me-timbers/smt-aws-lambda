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

package shiver.me.timbers.aws.lambda.cr;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static shiver.me.timbers.data.random.RandomStrings.someString;
import static shiver.me.timbers.data.random.RandomThings.someThing;

public class JsonsTest {

    private ObjectMapper objectMapper;
    private Jsons jsons;

    @Before
    public void setUp() {
        objectMapper = mock(ObjectMapper.class);
        jsons = new Jsons(objectMapper);
    }

    @Test
    public void Can_convert_an_object_to_json() throws JsonProcessingException {

        final Object object = someThing();

        final String expected = someString();

        // Given
        given(objectMapper.writeValueAsString(object)).willReturn(expected);

        // When
        final String actual = jsons.toJson(object);

        // Then
        assertThat(actual, is(expected));
    }

    @Test
    public void Can_fail_to_convert_an_object_to_json() throws JsonProcessingException {

        final Object object = someThing();

        final JsonProcessingException exception = mock(JsonProcessingException.class);

        // Given
        given(objectMapper.writeValueAsString(object)).willThrow(exception);

        // When
        final Throwable actual = catchThrowable(() -> jsons.toJson(object));

        // Then
        assertThat(actual, instanceOf(JsonConversionException.class));
        assertThat(actual.getCause(), is(exception));
    }
}