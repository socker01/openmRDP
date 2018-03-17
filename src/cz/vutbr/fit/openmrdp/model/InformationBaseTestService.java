package cz.vutbr.fit.openmrdp.model;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Jiri Koudelka
 * @since 01.03.2018.
 */
public final class InformationBaseTestService implements InformationBaseService{

    static final RDFTriple TEST_TRIPLE_1 = new RDFTriple("urn:uuid:drill", "<loc:locatedIn>", "urn:uuid:room1");
    static final RDFTriple TEST_TRIPLE_2 = new RDFTriple("urn:uuid:box1", "<loc:contains>", "urn:uuid:fuel1");
    static final RDFTriple TEST_TRIPLE_3 = new RDFTriple("urn:uuid:box1", "<loc:locatedIn>", "urn:uuid:room1");
    static final RDFTriple TEST_TRIPLE_4 = new RDFTriple("urn:uuid:fuel1", "<loc:locatedIn>", "urn:uuid:box1");
    static final RDFTriple TEST_TRIPLE_5 = new RDFTriple("urn:uuid:fuel1", "<loc:locatedIn>", "urn:uuid:room1");
    static final RDFTriple TEST_TRIPLE_6 = new RDFTriple("urn:uuid:fuel1", "is", "flammable");

    @Override
    public Set<RDFTriple> loadInformationBase() {
        Set<RDFTriple> testInformationBase = new HashSet<>();

        testInformationBase.add(TEST_TRIPLE_1);
        testInformationBase.add(TEST_TRIPLE_2);
        testInformationBase.add(TEST_TRIPLE_3);
        testInformationBase.add(TEST_TRIPLE_4);
        testInformationBase.add(TEST_TRIPLE_5);


        return testInformationBase;
    }

    @Override
    public void addInformationToBase(RDFTriple triple) {
        //TODO: implement me
    }
}
