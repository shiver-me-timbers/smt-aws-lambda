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

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;

class Marshals {

    private final DocumentFactory documentFactory;
    private final JaxbContextFactory contextFactory;

    Marshals(DocumentFactory documentFactory, JaxbContextFactory contextFactory) {
        this.documentFactory = documentFactory;
        this.contextFactory = contextFactory;
    }

    Document marshalToDocument(Object object) {
        try {
            final Document document = documentFactory.create();
            contextFactory.create(object.getClass()).createMarshaller().marshal(object, document);
            return document;
        } catch (JAXBException | ParserConfigurationException e) {
            throw new SoapException("Failed to marshal to the document.", e);
        }
    }
}
