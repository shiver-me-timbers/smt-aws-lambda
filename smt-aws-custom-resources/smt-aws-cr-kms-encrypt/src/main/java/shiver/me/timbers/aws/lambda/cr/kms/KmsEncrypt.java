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

import com.amazonaws.services.kms.AWSKMSClientBuilder;
import shiver.me.timbers.aws.lambda.cr.CustomResourceLambda;

/**
 * @author Karl Bennett
 */
public class KmsEncrypt extends CustomResourceLambda {

    public KmsEncrypt() {
        this(new Base64(), new KmsEncryptService(AWSKMSClientBuilder.defaultClient()));
    }

    KmsEncrypt(Base64 base64, KmsEncryptService service) {
        super(new KmsEncryptHandler(base64, service));
    }
}
