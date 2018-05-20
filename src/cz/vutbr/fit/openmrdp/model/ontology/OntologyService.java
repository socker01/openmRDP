package cz.vutbr.fit.openmrdp.model.ontology;

import com.sun.istack.internal.NotNull;

/**
 * Service interface used for loading information about ontology.
 *
 * @author Jiri Koudelka
 * @since 06.04.2018.
 */
public interface OntologyService {

    @NotNull
    OntologyInformation loadOntology();
}
