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

import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import java.util.List;
import java.util.stream.Collectors;

class TransformerFactory {

    private final Templates namespaceTemplates;
    private final List<Templates> tagTemplates;

    TransformerFactory(NamespaceTemplatesFactory namespaceTemplatesFactory, TagTemplatesFactory tagTemplatesFactory) {
        namespaceTemplates = namespaceTemplatesFactory.create();
        tagTemplates = tagTemplatesFactory.createAll();
    }

    Transformer createNameSpaceTransformer() {
        return newTransformer(namespaceTemplates);
    }

    List<Transformer> createTagTransformers() {
        return tagTemplates.stream().map(this::newTransformer).collect(Collectors.toList());
    }

    private Transformer newTransformer(Templates templates) {
        try {
            return templates.newTransformer();
        } catch (TransformerConfigurationException e) {
            throw new TransformationException("Failed to create a a Transformer.", e);
        }
    }
}
