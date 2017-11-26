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

import org.hamcrest.Matcher;
import org.junit.Test;

import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static shiver.me.timbers.data.random.RandomStrings.someString;

public class StubProxyResponseTest {

    @Test
    public void Can_create_a_stub_proxy_response() {

        // Given
        final String expected = someString();

        // When
        final StubProxyResponse actual = new StubProxyResponse(expected);

        // Then
        assertThat(actual.getStatusCode(), is(200));
        assertThat(actual.getHeaders(), (Matcher) hasEntry("Content-Type", "text/xml"));
        assertThat(actual.getBody(), is(expected));
    }
}