package cz.vutbr.fit.openmrdp.model.informationbase;

import com.sun.istack.internal.NotNull;
import cz.vutbr.fit.openmrdp.model.base.RDFTriple;

import java.util.HashSet;
import java.util.Set;

/**
 * Test implementation of the {@link InformationBaseService} interface.
 *
 * Do not use this implementation in the production code.
 *
 * @author Jiri Koudelka
 * @since 01.03.2018.
 */
public final class InformationBaseTestService implements InformationBaseService {

    private static final RDFTriple TEST_TRIPLE_1 = new RDFTriple("urn:uuid:drill1", "<loc:locatedIn>", "urn:uuid:room1");
    private static final RDFTriple TEST_TRIPLE_2 = new RDFTriple("urn:uuid:box1", "<loc:locatedIn>", "urn:uuid:room1");
    private static final RDFTriple TEST_TRIPLE_3 = new RDFTriple("urn:uuid:fuel1", "<loc:locatedIn>", "urn:uuid:box1");
    private static final RDFTriple TEST_TRIPLE_4 = new RDFTriple("urn:uuid:fuel1", "<loc:locatedIn>", "urn:uuid:room1");
    private static final RDFTriple TEST_TRIPLE_5 = new RDFTriple("fuel:chemicalFuel", "rdf:subtype", "mat:inflammableThing");
    private static final RDFTriple TEST_TRIPLE_6 = new RDFTriple("urn:uuid:room1", "<loc:contains>", "urn:uuid:drill1");
    private static final RDFTriple TEST_TRIPLE_7 = new RDFTriple("urn:uuid:room1", "<loc:contains>", "urn:uuid:box1");
    private static final RDFTriple TEST_TRIPLE_8 = new RDFTriple("urn:uuid:room1", "<loc:contains>", "urn:uuid:fuel1");
    private static final RDFTriple TEST_TRIPLE_9 = new RDFTriple("urn:uuid:surface1", "rdf:type", "fur:steelShelf");
    private static final RDFTriple TEST_TRIPLE_10 = new RDFTriple("urn:uuid:drill1", "task:drilling", "urn:uuid:surface1");
    private static final RDFTriple TEST_TRIPLE_11 = new RDFTriple("urn:uuid:fuel1", "rdf:type", "fur:chemicalFuel");
    private static final RDFTriple TEST_TRIPLE_12 = new RDFTriple("fur:steelShelf", "rdf:subtype", "mat:metallicThing");
    private static final RDFTriple TEST_TRIPLE_14 = new RDFTriple("urn:uuid:fuel1", "rdf:type", "mat:inflammableThing");
    private static final RDFTriple TEST_TRIPLE_15 = new RDFTriple("urn:uuid:box1", "<loc:contains>", "urn:uuid:fuel1");

    private Set<RDFTriple> addedInformation = new HashSet<>();

    @NotNull
    @Override
    public Set<RDFTriple> loadInformationBase() {
        Set<RDFTriple> testInformationBase = new HashSet<>();

        testInformationBase.add(TEST_TRIPLE_1);
        testInformationBase.add(TEST_TRIPLE_2);
        testInformationBase.add(TEST_TRIPLE_3);
        testInformationBase.add(TEST_TRIPLE_4);
        testInformationBase.add(TEST_TRIPLE_5);
        testInformationBase.add(TEST_TRIPLE_6);
        testInformationBase.add(TEST_TRIPLE_7);
        testInformationBase.add(TEST_TRIPLE_8);
        testInformationBase.add(TEST_TRIPLE_9);
        testInformationBase.add(TEST_TRIPLE_10);
        testInformationBase.add(TEST_TRIPLE_11);
        testInformationBase.add(TEST_TRIPLE_12);
        testInformationBase.add(TEST_TRIPLE_14);
        testInformationBase.add(TEST_TRIPLE_15);
        testInformationBase.addAll(addedInformation);


        return testInformationBase;
    }

    @Override
    public void addInformationToBase(@NotNull RDFTriple triple) {
        addedInformation.add(triple);
    }

    @Override
    public void removeInformationFromBase(@NotNull RDFTriple triple) {
        addedInformation.remove(triple);
    }
}
