package cz.vutbr.fit.openmrdp.model.base;

import com.google.common.base.Preconditions;
import com.sun.istack.internal.NotNull;

import java.util.Objects;

/**
 * Object which represents RDF triple
 *
 * @author Jiri Koudelka
 * @since 15.02.2018.
 */
public final class RDFTriple {

    @NotNull
    private final String subject;
    @NotNull
    private final String predicate;
    @NotNull
    private final String object;

    public RDFTriple(@NotNull String subject, @NotNull String predicate, @NotNull String object) {
        this.subject = Preconditions.checkNotNull(subject);
        this.predicate = Preconditions.checkNotNull(predicate);
        this.object = Preconditions.checkNotNull(object);
    }

    @NotNull
    public String getSubject() {
        return subject;
    }

    @NotNull
    public String getPredicate() {
        return predicate;
    }

    @NotNull
    public String getObject() {
        return object;
    }

    public boolean isSubjectVariable() {
        return subject.startsWith("?");
    }

    public boolean isObjectVariable() {
        return object.startsWith("?");
    }

    public boolean hasTwoVariables() {
        return isObjectVariable() && isSubjectVariable();
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
