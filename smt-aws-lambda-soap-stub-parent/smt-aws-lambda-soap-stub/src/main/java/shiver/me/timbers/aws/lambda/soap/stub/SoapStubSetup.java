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

import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import shiver.me.timbers.aws.common.Env;
import shiver.me.timbers.aws.common.IOStreams;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.time.Clock;

class SoapStubSetup {

    static Digester digester(Env env) throws FileNotFoundException {
        // The AWS Lambda JAR is extracted into chroot, so we must use a standard file lookup to access JAR resources.
        return new Digester(
            cleaner(
                env,
                new TemplatesFactory(),
                new FileInputStream("remove-namespaces.xslt"),
                new FileInputStream("remove-tag.xslt")
            ),
            new MessageDigestFactory()
        );
    }

    static Cleaner cleaner(
        Env env,
        TemplatesFactory templatesFactory,
        InputStream namespaceXsltStream,
        InputStream tagXsltStream
    ) {
        return new Cleaner(
            new SoapMessages(new SoapMessageFactory()),
            new TransformerFactory(
                new NamespaceTemplatesFactory(
                    templatesFactory,
                    namespaceXsltStream
                ),
                new TagTemplatesFactory(
                    env.getAsList("TAG_NAMES"),
                    tagXsltStream,
                    new IOStreams(),
                    templatesFactory
                )
            )
        );
    }

    static StubbingRepository repository(Env env) {
        return new StubbingRepository(
            env.get("S3_BUCKET_NAME"),
            env.get("S3_DIRECTORY_NAME"),
            AmazonS3ClientBuilder.defaultClient(), Clock.systemDefaultZone()
        );
    }
}
