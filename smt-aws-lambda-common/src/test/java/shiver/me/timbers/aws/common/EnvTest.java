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

package shiver.me.timbers.aws.common;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static shiver.me.timbers.data.random.RandomStrings.someAlphanumericString;

public class EnvTest {

    private Env env;

    @Before
    public void setUp() {
        env = new Env();
    }

    @Test
    public void Can_get_an_environment_variable() {

        // When
        final String actual = env.get("HOME");

        // Then
        assertThat(actual, not(nullValue()));
    }

    @Test
    public void Can_get_an_environment_variable_as_a_list() {

        // When
        final List<String> actual = env.getAsList("TEST_LIST");

        // Then
        assertThat(actual, contains("one", "two", "three"));
    }

    @Test
    public void Can_get_an_environment_variable_as_a_list_if_it_does_not() {

        // When
        final List<String> actual = env.getAsList(someAlphanumericString(10));

        // Then
        assertThat(actual, empty());
    }
}