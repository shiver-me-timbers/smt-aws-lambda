package shiver.me.timbers.aws.lambda.soap.stub;

import javax.xml.transform.Templates;
import java.io.InputStream;

class NamespaceTemplatesFactory {

    private final TemplatesFactory templatesFactory;
    private final InputStream stream;

    NamespaceTemplatesFactory(TemplatesFactory templatesFactory, InputStream stream) {
        this.templatesFactory = templatesFactory;
        this.stream = stream;
    }

    Templates create() {
        return templatesFactory.create(stream);
    }
}
