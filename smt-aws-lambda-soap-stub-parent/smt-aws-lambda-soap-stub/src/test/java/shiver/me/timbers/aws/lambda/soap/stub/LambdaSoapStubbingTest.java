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

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.junit.Before;
import org.junit.Test;

import javax.xml.bind.JAXBException;
import javax.xml.soap.SOAPException;
import java.io.IOException;

import static java.lang.String.format;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static shiver.me.timbers.data.random.RandomStrings.someString;

public class LambdaSoapStubbingTest {

    private Digester digester;
    private StubbingRepository repository;
    private RequestHandler<Stubbing, String> soapStubbing;

    @Before
    public void setUp() {
        digester = mock(Digester.class);
        repository = mock(StubbingRepository.class);
        soapStubbing = new LambdaSoapStubbing(digester, repository);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void Can_call_the_lambda() throws SOAPException, IOException, JAXBException {

        final Stubbing stubbing = mock(Stubbing.class);

        final String request = someString();
        final String hash = someString();

        // Given
        given(stubbing.getRequest()).willReturn(request);
        given(digester.digestSoapRequest(request)).willReturn(hash);

        // When
        final String actual = soapStubbing.handleRequest(stubbing, mock(Context.class));

        // Then
        then(repository).should().save(hash, stubbing);
        assertThat(actual, equalTo(format("SOAP stub saved with hash (%s).", hash)));
    }
}