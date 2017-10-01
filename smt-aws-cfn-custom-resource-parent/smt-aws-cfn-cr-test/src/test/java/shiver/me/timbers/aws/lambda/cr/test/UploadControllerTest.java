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

package shiver.me.timbers.aws.lambda.cr.test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;

public class UploadControllerTest {

    @Test
    @SuppressWarnings("unchecked")
    public void Can_handle_a_test_custom_resource_upload() throws IOException {

        final ObjectMapper mapper = mock(ObjectMapper.class);
        final UploadHandler uploadHandler = mock(UploadHandler.class);
        final HttpServletRequest request = mock(HttpServletRequest.class);

        final ServletInputStream stream = mock(ServletInputStream.class);

        final Map<String, Object> map = mock(Map.class);

        // Given
        given(request.getInputStream()).willReturn(stream);
        given(mapper.readValue(eq(stream), any(TypeReference.class))).willReturn(map);

        // When
        new UploadController(mapper, uploadHandler).upload(request);

        // Then
        then(uploadHandler).should().upload(map);
    }
}