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

package shiver.me.timbers.aws.lambda.cr.test;

import org.junit.Test;

import java.util.List;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static shiver.me.timbers.aws.lambda.cr.test.Properties.properties;
import static shiver.me.timbers.aws.lambda.cr.test.Properties.property;
import static shiver.me.timbers.data.random.RandomStrings.someString;

public class PropertiesTest {

    @Test
    public void Can_create_a_property() {

        // Given
        final String name = someString();
        final String value = someString();

        // When
        final Property actual = property(name, value);

        // Then
        assertThat(actual.getName(), is(name));
        assertThat(actual.getValue(), is(value));
    }

    @Test
    public void Can_create_some_properties_with_the_last_property_tagged() {

        // Given
        final Property property1 = property(someString(), someString());
        final Property property2 = property(someString(), someString());
        final Property property3 = property(someString(), someString());

        // When
        final List<Property> actual = properties(property1, property2, property3);

        // Then
        assertThat(actual, contains(property1, property2, property3.withLast(true)));
    }
}