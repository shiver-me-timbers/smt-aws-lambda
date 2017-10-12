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
import org.junit.Before;
import org.junit.Test;

import static java.lang.String.format;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static shiver.me.timbers.data.random.RandomStrings.someString;

public class StubbingRepositoryTest {

    private Env env;
    private AmazonS3 s3;

    @Before
    public void setUp() {
        env = mock(Env.class);
        s3 = mock(AmazonS3.class);
    }

    @Test
    public void Can_save_a_stub() {

        final String hash = someString();
        final Stubbing stubbing = mock(Stubbing.class);

        final String bucketName = someString();
        final String directory = someString();
        final String request = someString();
        final String response = someString();

        // Given
        given(env.get("S3_BUCKET_NAME")).willReturn(bucketName);
        given(env.get("S3_DIRECTORY_NAME")).willReturn(directory);
        given(stubbing.getRequest()).willReturn(request);
        given(stubbing.getResponse()).willReturn(response);

        // When
        new StubbingRepository(env, s3).save(hash, stubbing);

        // Then
        then(s3).should().putObject(bucketName, format("%s/%s-request.xml", directory, hash), request);
        then(s3).should().putObject(bucketName, format("%s/%s-response.xml", directory, hash), response);
    }

    @Test
    public void Can_find_a_stubbed_response_by_a_hash() {

        final String directory = someString();
        final String hash = someString();

        final String bucketName = someString();
        final String expected = someString();

        // Given
        given(env.get("S3_BUCKET_NAME")).willReturn(bucketName);
        given(env.get("S3_DIRECTORY_NAME")).willReturn(directory);
        given(s3.getObjectAsString(bucketName, format("%s/%s-response.xml", directory, hash))).willReturn(expected);

        // When
        final String actual = new StubbingRepository(env, s3).findResponseByHash(hash);

        // Then
        assertThat(actual, is(expected));
    }
}