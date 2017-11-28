package shiver.me.timbers.aws.lambda.soap.stub;

import org.apache.log4j.Logger;
import shiver.me.timbers.aws.common.IOStreams;

import javax.xml.transform.Templates;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;

class TagTemplatesFactory {

    private final Logger log = Logger.getLogger(getClass());

    private final List<String> tags;
    private final InputStream stream;
    private final IOStreams ioStreams;
    private final TemplatesFactory templatesFactory;

    TagTemplatesFactory(List<String> tags, InputStream stream, IOStreams ioStreams, TemplatesFactory templatesFactory) {
        this.tags = tags;
        this.stream = stream;
        this.ioStreams = ioStreams;
        this.templatesFactory = templatesFactory;
    }

    List<Templates> createAll() {
        try {
            log.info(format("Ignoring tags: %s", tags));
            final String xslt = ioStreams.toString(stream);
            return tags.stream()
                .map(tag -> xslt.replaceAll("\\{TAG_NAME}", tag))
                .map(ioStreams::toStream)
                .map(templatesFactory::create)
                .collect(Collectors.toList());
        } catch (IOException e) {
            throw new XsltTemplateException("Failed to create the XSLT template.", e);
        }
    }
}
