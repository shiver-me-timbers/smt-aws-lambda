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

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

import static nl.jqno.equalsverifier.Warning.NONFINAL_FIELDS;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static shiver.me.timbers.data.random.RandomStrings.someString;

public class StubbingTest {

    @Test
    public void Can_get_the_request() {

        // Given
        final String expected = someString();

        // When
        final String actual = new Stubbing(expected, someString()).getRequest();

        // Then
        assertThat(actual, is(expected));
    }

    @Test
    public void Can_set_the_request() {

        final Stubbing stubbing = new Stubbing();

        final String expected = someString();

        // Given
        stubbing.setRequest(expected);

        // When
        final String actual = stubbing.getRequest();

        // Then
        assertThat(actual, is(expected));
    }

    @Test
    public void Can_get_the_response() {

        // Given
        final String expected = someString();

        // When
        final String actual = new Stubbing(someString(), expected).getResponse();

        // Then
        assertThat(actual, is(expected));
    }

    @Test
    public void Can_set_the_response() {

        final Stubbing stubbing = new Stubbing();

        final String expected = someString();

        // Given
        stubbing.setResponse(expected);

        // When
        final String actual = stubbing.getResponse();

        // Then
        assertThat(actual, is(expected));
    }

    @Test
    public void Stubbing_has_equality() {
        EqualsVerifier.forClass(Stubbing.class).usingGetClass().suppress(NONFINAL_FIELDS).verify();
    }
}