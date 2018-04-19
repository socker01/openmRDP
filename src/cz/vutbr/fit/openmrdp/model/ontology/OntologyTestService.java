package cz.vutbr.fit.openmrdp.model.ontology;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jiri Koudelka
 * @since 06.04.2018.
 */
public class OntologyTestService implements OntologyService {

    @Override
    public OntologyInformation loadOntology() {
        String levelUpPredicate = "<loc:locatedIn>";
        String levelDownPredicate = "<loc:contains>";
        String delimiter = "\\";

        List<Pair<String, String>> transitivePairs = createTransitivePairs();

        return new OntologyInformation.Builder()
                .withTransitivePredicates(transitivePairs)
                .withLevelUpPredicate(levelUpPredicate)
                .withLevelDownPredicate(levelDownPredicate)
                .withDelimiter(delimiter)
                .build();
    }

    private List<Pair<String, String>> createTransitivePairs(){
        List<Pair<String, String>> transitivePairs = new ArrayList<>();

        Pair<String, String> transitivePair1 = new Pair<>("rdf:type", "rdf:subtype");
        Pair<String, String> transitivePair2 = new Pair<>("rdf:subtype", "rdf:subsubtype");
        Pair<String, String> transitivePair3 = new Pair<>("rdf:type", "rdf:has");

        transitivePairs.add(transitivePair1);
        transitivePairs.add(transitivePair2);
        transitivePairs.add(transitivePair3);

        return transitivePairs;
    }
}
