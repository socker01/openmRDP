package cz.vutbr.fit.openmrdp.model;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RDFTriple triple = (RDFTriple) o;
        return Objects.equals(subject, triple.subject) &&
                Objects.equals(predicate, triple.predicate) &&
                Objects.equals(object, triple.object);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subject, predicate, object);
    }
}
