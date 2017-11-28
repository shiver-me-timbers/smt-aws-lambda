package shiver.me.timbers.aws.lambda.soap.stub;

import javax.xml.transform.Templates;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.stream.StreamSource;
import java.io.InputStream;

class TemplatesFactory {

    Templates create(InputStream stream) {
        try {
            return javax.xml.transform.TransformerFactory.newInstance().newTemplates(new StreamSource(stream));
        } catch (TransformerConfigurationException e) {
            throw new XsltTemplateException("Failed to create the XSLT template.", e);
        }
    }
}
