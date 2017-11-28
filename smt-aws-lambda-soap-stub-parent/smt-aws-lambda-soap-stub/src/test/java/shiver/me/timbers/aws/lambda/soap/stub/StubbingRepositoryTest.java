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

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import org.junit.Before;
import org.junit.Test;

import java.time.Clock;
import java.time.Instant;
import java.util.List;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static shiver.me.timbers.aws.lambda.soap.stub.StubbingRepository.FORMATTER;
import static shiver.me.timbers.data.random.RandomStrings.someString;

public class StubbingRepositoryTest {

    private AmazonS3 s3;
    private Clock clock;
    private String bucketName;
    private String directory;
    private StubbingRepository repository;

    @Before
    public void setUp() {
        s3 = mock(AmazonS3.class);
        clock = mock(Clock.class);
        bucketName = someString();
        directory = someString();
        repository = new StubbingRepository(bucketName, directory, s3, clock);
    }

    @Test
    public void Can_save_a_stub() {

        final String hash = someString();
        final Stubbing stubbing = mock(Stubbing.class);

        final String request = someString();
        final String response = someString();

        // Given
        given(stubbing.getRequest()).willReturn(request);
        given(stubbing.getResponse()).willReturn(response);

        // When
        repository.save(hash, stubbing);

        // Then
        then(s3).should().putObject(bucketName, format("%s/%s-request.xml", directory, hash), request);
        then(s3).should().putObject(bucketName, format("%s/%s-response.xml", directory, hash), response);
    }

    @Test
    public void Can_find_a_stubbed_response_by_a_hash() {

        final String hash = someString();

        final String expected = someString();

        // Given
        given(s3.getObjectAsString(bucketName, format("%s/%s-response.xml", directory, hash))).willReturn(expected);

        // When
        final String actual = repository.findResponseByHash(hash);

        // Then
        assertThat(actual, is(expected));
    }

    @Test
    public void Can_find_a_call_to_a_stub() {

        final String hash = someString();

        final ObjectListing objects = mock(ObjectListing.class);
        final S3ObjectSummary summary1 = mock(S3ObjectSummary.class);
        final S3ObjectSummary summary2 = mock(S3ObjectSummary.class);
        final S3ObjectSummary summary3 = mock(S3ObjectSummary.class);
        final String call1 = someString();
        final String call2 = someString();
        final String call3 = someString();

        // Given
        given(s3.listObjects(bucketName, format("%s/%s-called-", directory, hash))).willReturn(objects);
        given(objects.getObjectSummaries()).willReturn(asList(summary1, summary2, summary3));
        given(summary1.getKey()).willReturn(call1);
        given(summary2.getKey()).willReturn(call2);
        given(summary3.getKey()).willReturn(call3);

        // When
        final List<String> actual = repository.findCallsByHash(hash);

        // Then
        assertThat(actual, contains(call1, call2, call3));
    }

    @Test
    public void Can_record_a_call_to_the_stub() {

        final String hash = someString();
        final String body = someString();

        final Instant now = Instant.now();

        // Given
        given(clock.instant()).willReturn(now);

        // When
        repository.recordCall(hash, body);

        // Then
        then(s3).should()
            .putObject(bucketName, format("%s/%s-called-%s.xml", directory, hash, FORMATTER.format(now)), body);
    }
}