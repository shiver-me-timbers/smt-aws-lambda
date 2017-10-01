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

package shiver.me.timbers.aws.lambda.cr.kms;

import org.junit.Test;

import java.nio.ByteBuffer;

import static java.util.Base64.getEncoder;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static shiver.me.timbers.data.random.RandomStrings.someString;

public class Base64Test {

    @Test
    public void Can_encode_a_byte_buffer() {

        // Given
        final byte[] bytes = someString().getBytes();
        final ByteBuffer buffer = ByteBuffer.wrap(bytes);

        // When
        final String actual = new Base64().encode(buffer);

        // Then
        assertThat(actual, equalTo(new String(getEncoder().encode(bytes))));
    }
}