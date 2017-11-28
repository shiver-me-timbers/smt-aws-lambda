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

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import shiver.me.timbers.aws.common.Env;

import javax.xml.soap.SOAPException;
import javax.xml.transform.TransformerConfigurationException;
import java.io.IOException;
import java.nio.charset.Charset;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static shiver.me.timbers.aws.lambda.soap.stub.SoapStubSetup.cleaner;

public class ITCleaner {

    private Env env;
    private Cleaner cleaner;

    @Before
    public void setUp() throws TransformerConfigurationException, IOException {
        env = mock(Env.class);
        given(env.getAsList("TAG_NAMES")).willReturn(asList("argument1", "argument3"));
        cleaner = cleaner(
            env,
            new TemplatesFactory(),
            ClassLoader.getSystemResourceAsStream("remove-namespaces.xslt"),
            ClassLoader.getSystemResourceAsStream("remove-tag.xslt")
        );
    }

    @Test
    public void Can_clean_out_a_SOAP_header() throws IOException {

        // Given
        final String soapRequestXml = IOUtils
            .toString(ClassLoader.getSystemResource("soup-with-header.xml"), Charset.forName("UTF-8"));
        final String expected = IOUtils
            .toString(ClassLoader.getSystemResource("soup-without-header.xml"), Charset.forName("UTF-8"));

        // When
        final String actual = cleaner.cleanSOAPHeader(soapRequestXml);

        // Then
        assertThat(actual, equalTo(expected));
    }

    @Test
    public void Can_clean_out_the_namespaces() throws SOAPException, IOException {

        // Given
        final String xml = IOUtils
            .toString(ClassLoader.getSystemResource("soup-without-header.xml"), Charset.forName("UTF-8"));
        final String expected = IOUtils
            .toString(ClassLoader.getSystemResource("soup-without-namespaces.xml"), Charset.forName("UTF-8"));

        // When
        final String actual = cleaner.cleanNamespaces(xml);

        // Then
        assertThat(actual, equalTo(expected));
    }

    @Test
    public void Can_clean_out_the_ignored_tags() throws SOAPException, IOException {

        // Given
        final String xml = IOUtils
            .toString(ClassLoader.getSystemResource("soup-without-namespaces.xml"), Charset.forName("UTF-8"));
        final String expected = IOUtils
            .toString(ClassLoader.getSystemResource("soup-without-tags.xml"), Charset.forName("UTF-8"));

        // When
        final String actual = cleaner.cleanIgnoredTags(xml);

        // Then
        assertThat(actual, equalTo(expected));
    }
}