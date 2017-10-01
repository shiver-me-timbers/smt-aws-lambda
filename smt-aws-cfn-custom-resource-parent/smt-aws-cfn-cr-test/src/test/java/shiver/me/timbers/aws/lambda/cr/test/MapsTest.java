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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static shiver.me.timbers.aws.lambda.cr.test.Maps.createMap;
import static shiver.me.timbers.data.random.RandomThings.someThing;

public class MapsTest {

    @Test
    public void Can_generate_a_map_from_a_mustache_template() throws IOException {

        final HashMap<String, Object> values = new HashMap<>();

        final Object one = someThing();
        final Object two = someThing();
        final Object three = someThing();

        // Given
        values.put("one", one);
        values.put("two", two);
        values.put("three", three);

        // When
        final Map<String, Object> actual = createMap("test.json", values);

        // Then
        assertThat(actual.get("one"), equalTo(one.toString()));
        assertThat(actual.get("two"), equalTo(two.toString()));
        assertThat(actual.get("three"), equalTo(three.toString()));
    }
}