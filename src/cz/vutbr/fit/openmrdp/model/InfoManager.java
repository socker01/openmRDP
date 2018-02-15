package cz.vutbr.fit.openmrdp.model;

import com.sun.istack.internal.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jiri Koudelka
 * @since 15.02.2018.
 */
public final class InfoManager {

    private static List<RDFTriple> informationBase = new ArrayList<>();

    public static void createInformationBase(List<RDFTriple> newInformationBase){
        informationBase.clear();
        informationBase.addAll(newInformationBase);
    }

    public static void addInformationToBase(@NotNull RDFTriple triple){
        informationBase.add(triple);
    }

    static List<RDFTriple> findAllMatchingPatterns(List<String> variables){
        List<RDFTriple> matchingPatterns = new ArrayList<>();

        for (RDFTriple triple : informationBase){
            if (variables.contains(triple.getObject()) || variables.contains(triple.getSubject())){
                matchingPatterns.add(triple);
            }
        }

        return matchingPatterns;
    }
}
