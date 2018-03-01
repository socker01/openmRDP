package cz.vutbr.fit.openmrdp.model;

import com.sun.istack.internal.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Jiri Koudelka
 * @since 15.02.2018.
 */
public final class InfoManager {

    private boolean initialized = false;
    private List<RDFTriple> informationBase = new ArrayList<>();
    private final InformationBaseService informationBaseService;

    public InfoManager(InformationBaseService informationBaseService){
        this.informationBaseService = informationBaseService;
    }

    public void createInformationBase(){
        if(!initialized)
        {
            this.informationBase = informationBaseService.loadInformationBase();
            initialized = true;
        }
    }

    public void addInformationToBase(@NotNull RDFTriple triple){
        informationBase.add(triple);
        informationBaseService.addInformationToBase(triple);
    }

    Set<RDFTriple> findAllMatchingPatterns(Set<String> variables){
        Set<RDFTriple> matchingPatterns = new HashSet<>();

        for (RDFTriple triple : informationBase){
            if (variables.contains(triple.getObject()) || variables.contains(triple.getSubject())){
                matchingPatterns.add(triple);
            }
        }

        return matchingPatterns;
    }
}
