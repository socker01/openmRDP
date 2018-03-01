package cz.vutbr.fit.openmrdp.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jiri Koudelka
 * @since 01.03.2018.
 */
public final class InformationBaseTestService implements InformationBaseService{

    static final RDFTriple TEST_TRIPLE_1 = new RDFTriple("?room", "<name>", "serviceRoom");
    static final RDFTriple TEST_TRIPLE_2 = new RDFTriple("serviceRoom", "<contains>", "broom");
    static final RDFTriple TEST_TRIPLE_3 = new RDFTriple("?room", "<locatedIn>", "?building");

    @Override
    public List<RDFTriple> loadInformationBase() {
        List<RDFTriple> testInformationBase = new ArrayList<>();

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
