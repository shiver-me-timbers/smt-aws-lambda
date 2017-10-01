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

package shiver.me.timbers.aws.lambda.cr;

import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@SuppressWarnings("unchecked")
public class CreateOrUpdateCustomResourceHandlerTest {

    private Runner runner;
    private CreateOrUpdateCustomResourceHandler handler;

    @Before
    public void setUp() {
        runner = mock(Runner.class);
        handler = new CreateOrUpdateCustomResourceHandler() {
            @Override
            protected Map<String, Object> createOrUpdate(CustomResourceRequest request) {
                return runner.run(request);
            }

            @Override
            public Map<String, Object> delete(CustomResourceRequest request) {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Test
    public void Can_handle_a_create() {

        final CustomResourceRequest request = mock(CustomResourceRequest.class);

        final Map<String, Object> expected = mock(Map.class);

        // Given
        given(runner.run(request)).willReturn(expected);

        // When
        final Map<String, Object> actual = handler.create(request);

        // Then
        assertThat(actual, is(expected));
    }

    @Test
    public void Can_handle_a_update() {

        final CustomResourceRequest request = mock(CustomResourceRequest.class);

        final Map<String, Object> expected = mock(Map.class);

        // Given
        given(runner.run(request)).willReturn(expected);

        // When
        final Map<String, Object> actual = handler.update(request);

        // Then
        assertThat(actual, is(expected));
    }

    private interface Runner {
        Map<String, Object> run(CustomResourceRequest request);
    }
}