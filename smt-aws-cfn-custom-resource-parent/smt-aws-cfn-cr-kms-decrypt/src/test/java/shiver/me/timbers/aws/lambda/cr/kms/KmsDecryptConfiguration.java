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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.mockito.Mockito.mock;

@Configuration
public class KmsDecryptConfiguration {

    @Bean
    public AWSKMS awskms() {
        return mock(AWSKMS.class);
    }

    @Bean
    public Base64 base64() {
        return new Base64();
    }

    @Bean
    public KmsDecrypt kmsDecrypt(Base64 base64, AWSKMS awskms) {
        return new KmsDecrypt(base64, new KmsDecryptService(awskms));
    }
}
