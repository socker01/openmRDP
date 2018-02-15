package cz.vutbr.fit.openmrdp.model;

import com.sun.istack.internal.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jiri Koudelka
 * @since 15.02.2018.
 */
public final class InfoManager {

    private List<RDFTriple> informationBase;

    public InfoManager(){
        informationBase = new ArrayList<>();
    }

    public void createInformationBase(){
        //TODO: odkud se bude naplnovat baze znalosti a kde bude ulozena?
    }

    public void addInformationToBase(@NotNull RDFTriple triple){
        informationBase.add(triple);
    }
}
