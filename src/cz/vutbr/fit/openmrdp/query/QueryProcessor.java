package cz.vutbr.fit.openmrdp.query;

import com.sun.istack.internal.NotNull;
import cz.vutbr.fit.openmrdp.exceptions.QuerySyntaxException;
import cz.vutbr.fit.openmrdp.messages.ContentType;
import cz.vutbr.fit.openmrdp.messages.MessageBody;
import cz.vutbr.fit.openmrdp.model.base.RDFTriple;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * Processor which process {@link MessageBody} and create the {@link Query} object.
 *
 * @author Jiri Koudelka
 * @since 15.02.2018.
 */
final class QueryProcessor {

    /**
     * Process {@link MessageBody} and generate appropriate {@link Query} object
     *
     * @param messageBody - {@link MessageBody} object
     * @return - {@link Query} object
     * @throws QuerySyntaxException - if the query, stored in the messageBody, doesn't have expected syntax
     */
    @NotNull
    static Query processQuery(@NotNull MessageBody messageBody) throws QuerySyntaxException {
        Set<RDFTriple> triples;

        try{
            if (messageBody.getContentType() == ContentType.SPARQL_QUERY){
                triples = processSparqlQuery(messageBody.getQuery());
            } else {
                triples = processPlantQuery(messageBody.getQuery());
            }
        } catch (StringIndexOutOfBoundsException sio){
            throw new QuerySyntaxException("General query syntax error.");
        }

        return new Query(triples, messageBody.getContentType());
    }

    /**
     * Implementation of processing of the SPARQL queries will be implemented in the next version
     *
     * @param query - query to process
     * @return - {@link Set} of {@link RDFTriple}
     */
    @SuppressWarnings("unused")
    private static Set<RDFTriple> processSparqlQuery(@NotNull String query){
        throw new UnsupportedOperationException("The library does not support SPARQL query yet");
    }

    @NotNull
    private static Set<RDFTriple> processPlantQuery(@NotNull String query) throws QuerySyntaxException {
        Scanner scanner = new Scanner(query);
        Set<RDFTriple> triples = new HashSet<>();

        while (scanner.hasNextLine()){
            String line = scanner.nextLine();
            if(!line.equals("")){
                RDFTriple triple = createRDFTripleFromQueryLine(line);
                triples.add(triple);
            }
        }

        return triples;
    }

    @NotNull
    private static RDFTriple createRDFTripleFromQueryLine(@NotNull String queryLine) throws QuerySyntaxException {
        String subject = queryLine.substring(0, queryLine.indexOf(' '));
        String predicate = queryLine.substring(queryLine.indexOf(' ') + 1, queryLine.lastIndexOf(' '));
        String object = queryLine.substring(queryLine.lastIndexOf(' ') + 1);

        subject = subject.replaceAll("[\u0000-\u001f]", "");
        predicate = predicate.replaceAll("[\u0000-\u001f]", "");
        object = object.replaceAll("[\u0000-\u001f]", "");

        checkSubjectSyntax(subject);
        checkPredicateSyntax(predicate);
        checkObjectSyntax(object);

        return new RDFTriple(subject, predicate, object);
    }

    private static void checkSubjectSyntax(@NotNull String subject) throws QuerySyntaxException {
        if(subject == null || subject.length() == 0){
            throw new QuerySyntaxException("Query syntax error: Missing subject.");
        }
    }

    private static void checkObjectSyntax(@NotNull String object) throws QuerySyntaxException {
        if(object == null || object.length() == 0){
            throw new QuerySyntaxException("Query syntax error: Missing object.");
        }
    }

    private static void checkPredicateSyntax(@NotNull String predicate) throws QuerySyntaxException {
        if(predicate == null || predicate.length() == 0){
            throw new QuerySyntaxException("Query syntax error: Missing predicate.");
        }
    }
}
