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

package shiver.me.timbers.aws.lambda.cr.kms;

import com.amazonaws.services.kms.AWSKMS;
import com.amazonaws.services.kms.model.EncryptRequest;
import com.amazonaws.services.kms.model.EncryptResult;
import com.amazonaws.services.lambda.runtime.Context;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import shiver.me.timbers.aws.lambda.cr.test.CustomResourceConfiguration;
import shiver.me.timbers.aws.lambda.cr.test.UploadHandler;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;

import static java.lang.String.format;
import static java.nio.ByteBuffer.wrap;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static shiver.me.timbers.aws.lambda.cr.kms.Maps.createKmsEncryptResourceRequest;
import static shiver.me.timbers.aws.lambda.cr.test.Maps.createCustomResourceSuccessResponse;
import static shiver.me.timbers.aws.lambda.cr.test.Properties.properties;
import static shiver.me.timbers.aws.lambda.cr.test.Properties.property;
import static shiver.me.timbers.data.random.RandomStrings.someAlphanumericString;
import static shiver.me.timbers.data.random.RandomThings.someThing;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = CustomResourceConfiguration.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class ITKmsEncrypt {

    @LocalServerPort
    private int port;

    @Autowired
    private AWSKMS awskms;

    @Autowired
    private Base64 base64;

    @Autowired
    private UploadHandler uploadHandler;

    @Autowired
    private KmsEncrypt lambda;

    @After
    public void tearDown() {
        reset(uploadHandler);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void Can_encrypt_some_values() throws IOException {

        final HashMap<String, Object> request = new HashMap<>();

        final String type = someThing("Create", "Update");
        final String stackId = someAlphanumericString(34);
        final String resourceType = someAlphanumericString(34);
        final String requestId = someAlphanumericString(34);
        final String logicalResourceId = someAlphanumericString(34);
        final String physicalResourceId = someAlphanumericString(34);
        final String keyId = someAlphanumericString(13);
        final String name1 = someAlphanumericString(3);
        final String name2 = someAlphanumericString(5);
        final String name3 = someAlphanumericString(8);
        final String value1 = someAlphanumericString(8);
        final String value2 = someAlphanumericString(5);
        final String value3 = someAlphanumericString(3);
        final EncryptResult result1 = mock(EncryptResult.class);
        final EncryptResult result2 = mock(EncryptResult.class);
        final EncryptResult result3 = mock(EncryptResult.class);
        final ByteBuffer buffer1 = ByteBuffer.wrap(someAlphanumericString(21).getBytes());
        final ByteBuffer buffer2 = ByteBuffer.wrap(someAlphanumericString(21).getBytes());
        final ByteBuffer buffer3 = ByteBuffer.wrap(someAlphanumericString(21).getBytes());
        final HashMap<String, Object> response = new HashMap<>();

        // Given
        request.put("RequestType", type);
        request.put("RequestId", requestId);
        request.put("ResponseURL", format("http://localhost:%s/upload", port));
        request.put("StackId", stackId);
        request.put("ResourceType", resourceType);
        request.put("LogicalResourceId", logicalResourceId);
        request.put("PhysicalResourceId", physicalResourceId);
        request.put("KmsKeyId", keyId);
        request.put("Parameters", properties(
            property(name1, value1), property(name2, value2), property(name3, value3)
        ));
        given(awskms.encrypt(new EncryptRequest().withKeyId(keyId).withPlaintext(wrap(value1.getBytes()))))
            .willReturn(result1);
        given(awskms.encrypt(new EncryptRequest().withKeyId(keyId).withPlaintext(wrap(value2.getBytes()))))
            .willReturn(result2);
        given(awskms.encrypt(new EncryptRequest().withKeyId(keyId).withPlaintext(wrap(value3.getBytes()))))
            .willReturn(result3);
        given(result1.getCiphertextBlob()).willReturn(buffer1);
        given(result2.getCiphertextBlob()).willReturn(buffer2);
        given(result3.getCiphertextBlob()).willReturn(buffer3);
        response.put("Status", "SUCCESS");
        response.put("RequestId", requestId);
        response.put("StackId", stackId);
        response.put("LogicalResourceId", logicalResourceId);
        response.put("PhysicalResourceId", physicalResourceId);
        response.put(
            "Data", properties(
                property(name1, base64.encode(buffer1)),
                property(name2, base64.encode(buffer2)),
                property(name3, base64.encode(buffer3))
            )
        );

        // When
        final String actual = lambda.handleRequest(createKmsEncryptResourceRequest(request), mock(Context.class));

        // Then
        then(uploadHandler).should().upload(createCustomResourceSuccessResponse(response));
        assertThat(actual, equalTo(format(
            "Customer resource (%s) %s has finished.", KmsEncrypt.class.getName(), type
        )));
    }
}
