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
import com.amazonaws.services.s3.model.S3ObjectSummary;
import org.apache.log4j.Logger;

import java.time.Clock;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

class StubbingRepository {

    static final DateTimeFormatter FORMATTER = DateTimeFormatter
        .ofPattern("yyyy-MM-dd-HH-mm-ss-SSSS").withZone(ZoneId.systemDefault());

    private final Logger log = Logger.getLogger(getClass());

    private final String bucketName;
    private final String directory;
    private final AmazonS3 s3;
    private final Clock clock;

    StubbingRepository(Env env, AmazonS3 s3, Clock clock) {
        this.bucketName = env.get("S3_BUCKET_NAME");
        this.directory = env.get("S3_DIRECTORY_NAME");
        this.s3 = s3;
        this.clock = clock;
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

    List<String> findCallsByHash(String hash) {
        return s3.listObjects(bucketName, format("%s/%s-called-", directory, hash)).getObjectSummaries()
            .stream().map(S3ObjectSummary::getKey).collect(toList());
    }

    void recordCall(String hash, String body) {
        s3.putObject(
            bucketName,
            format("%s/%s-called-%s.xml", directory, hash, FORMATTER.format(clock.instant())),
            body
        );
    }
}
