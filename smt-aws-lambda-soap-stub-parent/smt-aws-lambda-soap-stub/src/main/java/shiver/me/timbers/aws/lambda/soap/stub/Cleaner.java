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

import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

class Cleaner {

    private static final String EXTRA_NEW_LINES = "\\n(?:\\s+\\n+)+";

    private final SoapMessages messages;
    private final TransformerFactory transformerFactory;

    Cleaner(SoapMessages messages, TransformerFactory transformerFactory) {
        this.messages = messages;
        this.transformerFactory = transformerFactory;
    }

    String cleanSOAPHeader(String soapRequestXml) {
        try {
            final SOAPMessage message = messages.createMessage(soapRequestXml);
            final SOAPHeader header = message.getSOAPHeader();
            if (header != null) {
                header.detachNode();
            }
            // Make sure to collapse any empty lines.
            return messages.toXmlString(message).replaceAll(EXTRA_NEW_LINES, "\n");
        } catch (SOAPException e) {
            throw new SoapException("Failed to clean out the empty header.", e);
        }
    }

    String cleanNamespaces(String xml) {
        return transform(xml, transformerFactory.createNameSpaceTransformer());
    }

    String cleanIgnoredTags(String xml) {
        return transformerFactory.createTagTransformers().stream().reduce(xml, this::transform, (last, next) -> null)
            .replaceAll(EXTRA_NEW_LINES, "\n");
    }

    private String transform(String xml, Transformer transformer) {
        try {
            final ByteArrayOutputStream stream = new ByteArrayOutputStream();
            transformer.setOutputProperty("omit-xml-declaration", "yes");
            transformer.transform(
                new StreamSource(new ByteArrayInputStream(xml.getBytes())),
                new StreamResult(stream)
            );
            return stream.toString();
        } catch (TransformerException e) {
            throw new XmlException("Failed to remove the namespaces", e);
        }
    }
}
