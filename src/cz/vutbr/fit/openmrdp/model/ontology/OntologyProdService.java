package cz.vutbr.fit.openmrdp.model.ontology;

import com.sun.istack.internal.NotNull;
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
 * The production implementation of the {@link OntologyService}. This implementation loads information from ontology.xml file.
 *
 * @author Jiri Koudelka
 * @since 12.04.2018
 */
public final class OntologyProdService implements OntologyService {

    private static final String LEVEL_UP_PREDICATE = "LevelUpPredicate";
    private static final String LEVEL_DOWN_PREDICATE = "LevelDownPredicate";
    private static final String TRANSITIVE_PREDICATES = "TransitivePredicates";
    private static final String TRANSITIVE_PREDICATE = "TransitivePredicate";
    private static final String PARENT = "Parent";
    private static final String TRANSITIVE_RELATION = "TransitiveRelation";
    private static final String DELIMITER = "Delimiter";
    private static final String ONTOLOGY_FILE_PATH = System.getProperty("user.dir") + File.separator + "ontology.xml";

    @NotNull
    @Override
    public OntologyInformation loadOntology() {
        try {
            File ontologyXml = new File(ONTOLOGY_FILE_PATH);
            if (!ontologyXml.exists()) {
                return createEmptyOntology();
            }

            Document document = getDocument(ontologyXml);

            Element rootNode = document.getRootElement();
            String levelUpPredicate = rootNode.getChildText(LEVEL_UP_PREDICATE);
            String levelDownPredicate = rootNode.getChildText(LEVEL_DOWN_PREDICATE);
            String delimiter = rootNode.getChildText(DELIMITER);

            Element transitivePredicatesNode = rootNode.getChild(TRANSITIVE_PREDICATES);
            List transitivePredicatesList = transitivePredicatesNode.getChildren(TRANSITIVE_PREDICATE);

            List<Pair<String, String>> transitivePredicates = createTransitivePredicates(transitivePredicatesList);

            return new OntologyInformation.Builder()
                    .withDelimiter(delimiter)
                    .withLevelDownPredicate(levelDownPredicate)
                    .withLevelUpPredicate(levelUpPredicate)
                    .withTransitivePredicates(transitivePredicates)
                    .build();
        } catch (OntologyException oe) {
            return new OntologyInformation.Builder()
                    .withDelimiter(null)
                    .withLevelDownPredicate(null)
                    .withLevelUpPredicate(null)
                    .withTransitivePredicates(null)
                    .build();
        }
    }

    @NotNull
    private List<Pair<String, String>> createTransitivePredicates(@NotNull List transitivePredicatesList) {
        List<Pair<String, String>> transitivePredicates = new ArrayList<>();

        for (Object transitivePredicate : transitivePredicatesList) {
            Element transPredicateElement = (Element) transitivePredicate;

            String parent = transPredicateElement.getChildText(PARENT);
            String transitiveRelation = transPredicateElement.getChildText(TRANSITIVE_RELATION);

            Pair<String, String> transitiveRelationPair = new Pair<>(parent, transitiveRelation);
            transitivePredicates.add(transitiveRelationPair);
        }

        return transitivePredicates;
    }

    @NotNull
    private OntologyInformation createEmptyOntology() {
        return new OntologyInformation.Builder()
                .withTransitivePredicates(Collections.emptyList())
                .build();
    }

    @NotNull
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
