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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@RestController
public class UploadController {

    private final ObjectMapper mapper;
    private final UploadHandler uploadHandler;

    public UploadController(ObjectMapper mapper, UploadHandler uploadHandler) {
        this.mapper = mapper;
        this.uploadHandler = uploadHandler;
    }

    @RequestMapping(path = "/upload", method = PUT)
    public void upload(HttpServletRequest request) throws IOException {
        uploadHandler.upload(mapper.readValue(request.getInputStream(), new TypeReference<Map<String, Object>>() {
        }));
    }
}
