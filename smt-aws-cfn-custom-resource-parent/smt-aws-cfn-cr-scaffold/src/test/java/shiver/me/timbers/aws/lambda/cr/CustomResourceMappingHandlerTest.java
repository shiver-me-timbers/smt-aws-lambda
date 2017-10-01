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
public class CustomResourceMappingHandlerTest {

    private CustomResourceIoMapper mapper;
    private CustomResourceHandler handler;
    private CustomResourceMappingHandler mappingHandler;

    @Before
    public void setUp() {
        mapper = mock(CustomResourceIoMapper.class);
        handler = mock(CustomResourceHandler.class);
        mappingHandler = new CustomResourceMappingHandler(mapper, handler);
    }

    @Test
    public void Can_handle_a_create() {

        final CustomResourceRequest request = mock(CustomResourceRequest.class);

        final Map<String, Object> data = mock(Map.class);

        final CustomResourceResponse expected = mock(CustomResourceResponse.class);

        // Given
        given(handler.create(request)).willReturn(data);
        given(mapper.mapSuccessResponse(request, data)).willReturn(expected);

        // When
        final CustomResourceResponse actual = mappingHandler.create(request);

        // Then
        assertThat(actual, is(expected));
    }

    @Test
    public void Can_handle_an_update() {

        final CustomResourceRequest request = mock(CustomResourceRequest.class);

        final Map<String, Object> data = mock(Map.class);

        final CustomResourceResponse expected = mock(CustomResourceResponse.class);

        // Given
        given(handler.update(request)).willReturn(data);
        given(mapper.mapSuccessResponse(request, data)).willReturn(expected);

        // When
        final CustomResourceResponse actual = mappingHandler.update(request);

        // Then
        assertThat(actual, is(expected));
    }

    @Test
    public void Can_handle_a_delete() {

        final CustomResourceRequest request = mock(CustomResourceRequest.class);

        final Map<String, Object> data = mock(Map.class);

        final CustomResourceResponse expected = mock(CustomResourceResponse.class);

        // Given
        given(handler.delete(request)).willReturn(data);
        given(mapper.mapSuccessResponse(request, data)).willReturn(expected);

        // When
        final CustomResourceResponse actual = mappingHandler.delete(request);

        // Then
        assertThat(actual, is(expected));
    }
}