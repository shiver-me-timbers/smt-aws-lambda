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

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static shiver.me.timbers.data.random.RandomStrings.someString;

public class LambdaSoapStubTest {

    private Digester digester;
    private StubbingRepository repository;
    private RequestHandler<SoapWrapper, SoapWrapper> soapStub;

    @Before
    public void setUp() {
        digester = mock(Digester.class);
        repository = mock(StubbingRepository.class);
        soapStub = new LambdaSoapStub(digester, repository);
    }

    @Test
    public void Can_handle_a_soap_request() {

        final SoapWrapper request = mock(SoapWrapper.class);

        final String body = someString();
        final String hash = someString();
        final String response = someString();

        // Given
        given(request.getBody()).willReturn(body);
        given(digester.digestSoapRequest(body)).willReturn(hash);
        given(repository.findResponseByHash(hash)).willReturn(response);

        // When
        final SoapWrapper actual = soapStub.handleRequest(request, mock(Context.class));

        // Then
        then(repository).should().recordCall(hash, body);
        assertThat(actual, equalTo(new SoapWrapper(response)));
    }
}