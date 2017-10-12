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
import org.apache.log4j.Logger;

import java.util.HashMap;

import static java.lang.String.format;

class StubbingRepository {

    private final Logger log = Logger.getLogger(getClass());

    private final String bucketName;
    private final String directory;
    private final AmazonS3 s3;

    StubbingRepository(Env env, AmazonS3 s3) {
        this.bucketName = env.get("S3_BUCKET_NAME");
        this.directory = env.get("S3_DIRECTORY_NAME");
        this.s3 = s3;
    }

    void save(String hash, Stubbing stubbing) {
        final HashMap<String, String> map = new HashMap<>();
        map.put("request", stubbing.getRequest());
        map.put("response", stubbing.getResponse());
        map.entrySet().parallelStream().forEach(entry -> {
            final String key = format("%s/%s-%s.xml", directory, hash, entry.getKey());
            log.info(format("Saving the stubbing to the (%s) S3 bucket with the key (%s).", bucketName, key));
            s3.putObject(
                bucketName, key, entry.getValue()
            );
        });
    }

    String findResponseByHash(String hash) {
        final String key = format("%s/%s-response.xml", directory, hash);
        log.info(format("Getting object from bucket (%s) with key (%s).", bucketName, key));
        return s3.getObjectAsString(bucketName, key);
    }
}
