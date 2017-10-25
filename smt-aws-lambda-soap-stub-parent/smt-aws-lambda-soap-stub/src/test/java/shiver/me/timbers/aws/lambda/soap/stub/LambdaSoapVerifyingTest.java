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

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static shiver.me.timbers.data.random.RandomStrings.someString;

public class LambdaSoapVerifyingTest {

    private Digester digester;
    private StubbingRepository repository;
    private RequestHandler<Verifying, String> soapVerifying;

    @Before
    public void setUp() {
        digester = mock(Digester.class);
        repository = mock(StubbingRepository.class);
        soapVerifying = new LambdaSoapVerifying(digester, repository);
    }

    @Test
    public void Can_verify_a_soap_request() {

        final Verifying verifying = mock(Verifying.class);

        final String request = someString();
        final String hash = someString();

        // Given
        given(verifying.getRequest()).willReturn(request);
        given(digester.digestSoapRequest(request)).willReturn(hash);
        given(repository.findCallsByHash(hash)).willReturn(singletonList(someString()));

        // When
        final String actual = soapVerifying.handleRequest(verifying, mock(Context.class));

        // Then
        assertThat(actual, equalTo(format("Verify Success. Request with hash (%s) only called once.", hash)));
    }

    @Test
    public void Can_fail_to_verify_a_soap_request_because_it_is_called_more_than_once() {

        final Verifying verifying = mock(Verifying.class);

        final String request = someString();
        final String hash = someString();

        // Given
        given(verifying.getRequest()).willReturn(request);
        given(digester.digestSoapRequest(request)).willReturn(hash);
        given(repository.findCallsByHash(hash)).willReturn(asList(someString(), someString()));

        // When
        final Throwable actual = catchThrowable(() -> soapVerifying.handleRequest(verifying, mock(Context.class)));

        // Then
        assertThat(actual, instanceOf(VerifyRequestError.class));
        assertThat(actual.getMessage(), equalTo(
            format("Verify Failure. Request with hash (%s) called more than once.", hash)
        ));
    }

    @Test
    public void Can_fail_to_verify_a_soap_request_because_it_is_never_called() {

        final Verifying verifying = mock(Verifying.class);

        final String request = someString();
        final String hash = someString();

        // Given
        given(verifying.getRequest()).willReturn(request);
        given(digester.digestSoapRequest(request)).willReturn(hash);
        given(repository.findCallsByHash(hash)).willReturn(emptyList());

        // When
        final Throwable actual = catchThrowable(() -> soapVerifying.handleRequest(verifying, mock(Context.class)));

        // Then
        assertThat(actual, instanceOf(VerifyRequestError.class));
        assertThat(actual.getMessage(), equalTo(
            format("Verify Failure. Request with hash (%s) was never called.", hash)
        ));
    }
}