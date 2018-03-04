package cz.vutbr.fit.openmrdp.model;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Jiri Koudelka
 * @since 01.03.2018.
 */
final class InformationBaseTestService implements InformationBaseService{

    static final RDFTriple TEST_TRIPLE_1 = new RDFTriple("?room", "<name>", "serviceRoom");
    static final RDFTriple TEST_TRIPLE_2 = new RDFTriple("serviceRoom", "<contains>", "broom");
    static final RDFTriple TEST_TRIPLE_3 = new RDFTriple("?room", "<locatedIn>", "?building");

    @Override
    public Set<RDFTriple> loadInformationBase() {
        Set<RDFTriple> testInformationBase = new HashSet<>();

        testInformationBase.add(TEST_TRIPLE_1);
        testInformationBase.add(TEST_TRIPLE_2);
        testInformationBase.add(TEST_TRIPLE_3);

        return testInformationBase;
    }

    @Override
    public void addInformationToBase(RDFTriple triple) {
        //TODO: implement me
    }
}
