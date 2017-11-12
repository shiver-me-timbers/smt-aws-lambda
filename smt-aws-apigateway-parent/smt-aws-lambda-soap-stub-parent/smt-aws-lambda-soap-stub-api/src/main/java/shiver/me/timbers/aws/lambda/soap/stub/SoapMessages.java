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

import org.w3c.dom.Document;

import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static java.lang.String.format;

class SoapMessages {

    private final SoapMessageFactory messageFactory;

    SoapMessages(SoapMessageFactory messageFactory) {
        this.messageFactory = messageFactory;
    }

    SOAPMessage createMessage(String soapRequestXml) {
        try {
            return messageFactory.createMessage(soapRequestXml);
        } catch (SOAPException e) {
            throw new SoapException(format("Failed to create a SOAPMessage from: %s", soapRequestXml), e);
        }
    }

    SOAPMessage wrapInSoapEnvelope(Document document) {
        try {
            final SOAPMessage message = messageFactory.createMessage();
            message.getSOAPBody().addDocument(document);
            return message;
        } catch (SOAPException e) {
            throw new SoapException("Failed to wrap the document in a SOAP envelope.", e);
        }
    }

    String toXmlString(SOAPMessage message) {
        try {
            final ByteArrayOutputStream stream = new ByteArrayOutputStream();
            message.writeTo(stream);
            return stream.toString();
        } catch (SOAPException | IOException e) {
            throw new SoapException("Failed to convert the SOAP message to a string.", e);
        }
    }
}
