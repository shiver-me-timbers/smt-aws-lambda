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

package shiver.me.timbers.aws.lambda.cr.ssm;

import org.junit.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;
import static shiver.me.timbers.aws.lambda.cr.ssm.TestParameters.parameter;
import static shiver.me.timbers.data.random.RandomStrings.someAlphanumericString;
import static shiver.me.timbers.data.random.RandomStrings.someString;

public class ParametersTest {

    @Test
    public void Can_get_the_parameters_names() {

        // Given
        final String name1 = someAlphanumericString(8);
        final String name2 = someAlphanumericString(8);
        final String name3 = someAlphanumericString(8);

        // When
        final List<String> actual = new Parameters(
            asList(parameter(name1, someString()), parameter(name2, someString()), parameter(name3, someString()))
        ).getNames();

        // Then
        assertThat(actual, contains(name1, name2, name3));
    }
}