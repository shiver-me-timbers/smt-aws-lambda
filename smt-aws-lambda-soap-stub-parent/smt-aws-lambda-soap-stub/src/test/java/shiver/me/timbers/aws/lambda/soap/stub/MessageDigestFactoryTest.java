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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static shiver.me.timbers.data.random.RandomStrings.someString;

public class MessageDigestFactoryTest {

    private MessageDigestFactory factory;

    @Before
    public void setUp() {
        factory = new MessageDigestFactory();
    }

    @Test
    public void Can_create_a_message_digest() {

        // When
        final MessageDigest actual = factory.create("MD5");

        // Then
        assertThat(actual, not(nullValue()));
    }

    @Test
    public void Can_fail_to_create_a_message_digest() {

        // When
        final Throwable actual = catchThrowable(() -> factory.create(someString()));

        // Then
        assertThat(actual, instanceOf(MessageDigestException.class));
        assertThat(actual.getMessage(), equalTo("Failed to create the MessageDigest."));
        assertThat(actual.getCause(), instanceOf(NoSuchAlgorithmException.class));
    }
}