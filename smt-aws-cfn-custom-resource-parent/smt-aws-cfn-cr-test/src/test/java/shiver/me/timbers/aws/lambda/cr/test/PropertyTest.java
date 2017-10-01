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

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Before;
import org.junit.Test;

import static nl.jqno.equalsverifier.Warning.NONFINAL_FIELDS;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static shiver.me.timbers.data.random.RandomBooleans.someBoolean;
import static shiver.me.timbers.data.random.RandomStrings.someString;
import static shiver.me.timbers.data.random.RandomThings.someThing;

public class PropertyTest {

    private String name;
    private Object value;
    private Property property;
    private Boolean last;

    @Before
    public void setUp() {
        name = someString();
        value = someThing();
        last = someBoolean();
        property = new Property(name, value).withLast(last);
    }

    @Test
    public void Can_get_the_property_name() {

        // When
        final String actual = property.getName();

        // Then
        assertThat(actual, is(name));
    }

    @Test
    public void Can_get_the_property_value() {

        // When
        final Object actual = property.getValue();

        // Then
        assertThat(actual, is(value));
    }

    @Test
    public void Can_get_the_property_last_flag() {

        // When
        final boolean actual = property.isLast();

        // Then
        assertThat(actual, is(last));
    }

    @Test
    public void The_property_has_equality() {
        EqualsVerifier.forClass(Property.class).suppress(NONFINAL_FIELDS).verify();
    }
}