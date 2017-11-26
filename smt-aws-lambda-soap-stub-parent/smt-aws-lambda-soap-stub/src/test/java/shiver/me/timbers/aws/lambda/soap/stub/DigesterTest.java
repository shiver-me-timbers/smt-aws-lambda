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

import javax.xml.soap.SOAPException;
import java.security.MessageDigest;

import static com.amazonaws.util.BinaryUtils.toHex;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static shiver.me.timbers.data.random.RandomStrings.someString;

public class DigesterTest {

    private Cleaner cleaner;
    private MessageDigestFactory factory;
    private Digester digester;

    @Before
    public void setUp() {
        cleaner = mock(Cleaner.class);
        factory = mock(MessageDigestFactory.class);
        digester = new Digester(cleaner, factory);
    }

    @Test
    public void Can_create_a_hash_from_a_string() throws SOAPException {

        final String soapRequestXml = someString();

        final String headerCleaned = someString();
        final String xml = someString();
        final MessageDigest digest = mock(MessageDigest.class);

        final byte[] expected = someString().getBytes();

        // Given
        given(cleaner.cleanSOAPHeader(soapRequestXml)).willReturn(headerCleaned);
        given(cleaner.cleanNamespaces(headerCleaned)).willReturn(xml);
        given(factory.create("MD5")).willReturn(digest);
        given(digest.digest(xml.getBytes())).willReturn(expected);

        // When
        final String actual = digester.digestSoapRequest(soapRequestXml);

        // Then
        assertThat(actual, equalTo(toHex(expected)));
    }
}