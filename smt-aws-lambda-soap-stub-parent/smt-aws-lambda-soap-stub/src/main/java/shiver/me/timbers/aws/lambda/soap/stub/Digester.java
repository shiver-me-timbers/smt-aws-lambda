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

import org.apache.log4j.Logger;

import static com.amazonaws.util.BinaryUtils.toHex;
import static java.lang.String.format;

class Digester {

    private final Logger log = Logger.getLogger(getClass());

    private final Cleaner cleaner;
    private final MessageDigestFactory factory;

    Digester(Cleaner cleaner, MessageDigestFactory factory) {
        this.cleaner = cleaner;
        this.factory = factory;
    }

    String digestSoapRequest(String soapRequestXml) {
        final String xml = cleaner.cleanNamespaces(cleaner.cleanEmptySOAPHeader(soapRequestXml));
        log.info(format("Digesting XML:\n%s", xml));
        return toHex(factory.create("MD5").digest(xml.getBytes()));
    }
}
