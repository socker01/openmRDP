package cz.vutbr.fit.openmrdp.model;

import com.google.common.collect.Sets;
import cz.vutbr.fit.openmrdp.exceptions.InformationBaseException;
import cz.vutbr.fit.openmrdp.model.base.RDFTriple;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Jiri Koudelka
 * @since 01.03.2018.
 */
public final class InformationBaseProdService implements InformationBaseService {

    private static final String INFORMATION_BASE_ROOT_NODE = "InformationBase";
    private static final String INFORMATION_NODE = "Information";
    private static final String OBJECT_PROPERTY = "Object";
    private static final String PREDICATE_PROPERTY = "Predicate";
    private static final String SUBJECT_PROPERTY = "Subject";
    private static final String INFORMATION_BASE_FILE_PATH = System.getProperty("user.dir") + File.separator + "informationBase.xml";

    @Override
    public Set<RDFTriple> loadInformationBase() {
        File xmlFile = new File(INFORMATION_BASE_FILE_PATH);
        if (!xmlFile.exists()) {
            return Sets.newHashSet();
        }

        Document document = getDocument(xmlFile);

        Element rootNode = document.getRootElement();
        List list = rootNode.getChildren(INFORMATION_NODE);

        return loadTriplesFromDocument(list);
    }

    private Document getDocument(File xmlFile) {
        SAXBuilder builder = new SAXBuilder();

        Document document;
        try {
            document = builder.build(xmlFile);
        } catch (JDOMException | IOException e) {
            throw new InformationBaseException("Exception during loading information base file.", e.getCause());
        }

        return document;
    }

    private Set<RDFTriple> loadTriplesFromDocument(List informationElements) {
        Set<RDFTriple> loadedTriples = new HashSet<>();

        for (Object nodeObject : informationElements) {
            Element node = (Element) nodeObject;

            String subject = node.getChildText(SUBJECT_PROPERTY);
            String predicate = node.getChildText(PREDICATE_PROPERTY);
            String object = node.getChildText(OBJECT_PROPERTY);

            RDFTriple loadedTriple = new RDFTriple(subject, predicate, object);

            loadedTriples.add(loadedTriple);
        }

        return loadedTriples;
    }

    @Override
    public void addInformationToBase(RDFTriple triple) {
        File xmlFile = new File(INFORMATION_BASE_FILE_PATH);
        if (!xmlFile.exists()) {
            createNewInformationBaseFile(triple);
        }

        Document document = getDocument(xmlFile);

        Element rootNode = document.getRootElement();
        List list = rootNode.getChildren(INFORMATION_NODE);

        //TODO: fix this warning
        list.add(createNewXMLElement(triple));
        writeChangesToFile(document);
    }

    private Element createNewXMLElement(RDFTriple triple) {
        Element newInformationElement = new Element(INFORMATION_NODE);

        Element subject = new Element(SUBJECT_PROPERTY);
        subject.setText(triple.getSubject());

        Element predicate = new Element(PREDICATE_PROPERTY);
        predicate.setText(triple.getPredicate());

        Element object = new Element(OBJECT_PROPERTY);
        object.setText(triple.getObject());

        newInformationElement.addContent(subject);
        newInformationElement.addContent(predicate);
        newInformationElement.addContent(object);

        return newInformationElement;
    }

    private void writeChangesToFile(Document document) {
        XMLOutputter xmlOutputter = new XMLOutputter();
        xmlOutputter.setFormat(Format.getPrettyFormat());

        File xmlFile = new File(INFORMATION_BASE_FILE_PATH);
        try {
            if (!xmlFile.exists()) {
                if (!xmlFile.createNewFile()) {
                    throw new IOException("The output file cannot be created.");
                }

            }
            xmlOutputter.output(document, new FileWriter(INFORMATION_BASE_FILE_PATH));
        } catch (IOException e) {
            throw new InformationBaseException("Exception during storing information base file.", e.getCause());
        }
    }

    private void createNewInformationBaseFile(RDFTriple triple) {
        Element informationBaseElement = new Element(INFORMATION_BASE_ROOT_NODE);
        Document document = new Document(informationBaseElement);

        Element newInformationElement = createNewXMLElement(triple);
        document.getRootElement().addContent(newInformationElement);

        writeChangesToFile(document);
    }
}
