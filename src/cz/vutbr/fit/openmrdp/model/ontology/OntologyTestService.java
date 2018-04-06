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
        String levelUpPredicate = "loc:locatedIn";
        String levelDownPredicate = "loc:contains";

        List<Pair> transitivePairs = createTransitivePairs();

        return new OntologyInformation(levelUpPredicate, levelDownPredicate, transitivePairs);
    }

    private List<Pair> createTransitivePairs(){
        List<Pair> transitivePairs = new ArrayList<>();
        Pair<String, String> transitivePair = new Pair<>("rdf:type", "rdf:subtype");
        transitivePairs.add(transitivePair);

        return transitivePairs;
    }
}
