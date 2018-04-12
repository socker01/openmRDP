package cz.vutbr.fit.openmrdp.model.ontology;

import cz.vutbr.fit.openmrdp.exceptions.OntologyException;
import javafx.util.Pair;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Jiri Koudelka
 * @since 12.04.2018
 */
//TODO: add exception checks
public final class OntologyProdService implements OntologyService {

    private static final String LEVEL_UP_PREDICATE = "LevelUpPredicate";
    private static final String LEVEL_DOWN_PREDICATE = "LevelDownPredicate";
    private static final String TRANSITIVE_PREDICATES = "TransitivePredicates";
    private static final String TRANSITIVE_PREDICATE = "TransitivePredicate";
    private static final String PARENT = "Parent";
    private static final String TRANSITIVE_RELATION = "TransitiveRelation";
    private static final String ONTOLOGY_FILE_PATH = System.getProperty("user.dir") + File.separator + "ontology.xml";

    @Override
    public OntologyInformation loadOntology() {
        File ontologyXml = new File(ONTOLOGY_FILE_PATH);
        if (!ontologyXml.exists()) {
            return createEmptyOntology();
        }

        Document document = getDocument(ontologyXml);

        Element rootNode = document.getRootElement();
        String levelUpPredicate = rootNode.getChildText(LEVEL_UP_PREDICATE);
        String levelDownPredicate = rootNode.getChildText(LEVEL_DOWN_PREDICATE);

        Element transitivePredicatesNode = rootNode.getChild(TRANSITIVE_PREDICATES);
        List transitivePredicatesList = transitivePredicatesNode.getChildren(TRANSITIVE_PREDICATE);

        List<Pair<String, String>> transitivePredicates = createTransitivePredicates(transitivePredicatesList);

        return new OntologyInformation(levelUpPredicate, levelDownPredicate, transitivePredicates);
    }

    private List<Pair<String, String>> createTransitivePredicates(List transitivePredicatesList) {
        List<Pair<String, String>> transitivePredicates = new ArrayList<>();

        for (Object transitivePredicate : transitivePredicatesList){
            Element transPredElement = (Element) transitivePredicate;

            String parent = transPredElement.getChildText(PARENT);
            String transitiveRelation = transPredElement.getChildText(TRANSITIVE_RELATION);

            Pair<String, String> transitiveRelationPair = new Pair<>(parent, transitiveRelation);
            transitivePredicates.add(transitiveRelationPair);
        }

        return transitivePredicates;
    }

    private OntologyInformation createEmptyOntology() {
        return new OntologyInformation(null, null, Collections.emptyList());
    }

    private Document getDocument(File ontologyXml) {
        SAXBuilder builder = new SAXBuilder();

        Document document;
        try {
            document = builder.build(ontologyXml);
        } catch (JDOMException | IOException e) {
            throw new OntologyException("Exception during loading ontology file.", e.getCause());
        }

        return document;
    }
}
