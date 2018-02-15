package cz.vutbr.fit.openmrdp.model;

/**
 * @author Jiri Koudelka
 * @since 15.02.2018.
 */
final class RDFTriple {

    private final String subject;
    private final String predicate;
    private final String object;

    RDFTriple(String subject, String predicate, String object) {
        this.subject = subject;
        this.predicate = predicate;
        this.object = object;
    }

    String getSubject() {
        return subject;
    }

    String getPredicate() {
        return predicate;
    }

    String getObject() {
        return object;
    }
}
