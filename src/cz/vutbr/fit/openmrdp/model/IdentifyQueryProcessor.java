package cz.vutbr.fit.openmrdp.model;

import cz.vutbr.fit.openmrdp.exceptions.QuerySyntaxException;
import cz.vutbr.fit.openmrdp.messages.ContentType;
import cz.vutbr.fit.openmrdp.messages.MessageBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @author Jiri Koudelka
 * @since 15.02.2018.
 */
public final class IdentifyQueryProcessor {

    public static Query processQuery(MessageBody messageBody) throws QuerySyntaxException {
        List<RDFTriple> triples;

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

    private static List<RDFTriple> processSparqlQuery(String query){
        //TODO: implement later
        return null;
    }

    private static List<RDFTriple> processPlantQuery(String query) throws QuerySyntaxException {
        Scanner scanner = new Scanner(query);
        List<RDFTriple> triples = new ArrayList<>();

        while (scanner.hasNextLine()){
            RDFTriple triple = createRDFTripleFromQueryLine(scanner.nextLine());
            triples.add(triple);
        }

        return triples;
    }

    private static RDFTriple createRDFTripleFromQueryLine(String queryLine) throws QuerySyntaxException {
        String subject = queryLine.substring(0, queryLine.indexOf(' '));
        String predicate = queryLine.substring(queryLine.indexOf(' ') + 1, queryLine.lastIndexOf(' '));
        String object = queryLine.substring(queryLine.lastIndexOf(' ') + 1);

        checkSubjectSyntax(subject);
        checkPredicateSyntax(predicate);
        checkObjectSyntax(object);

        return new RDFTriple(subject, predicate, object);
    }

    private static void checkSubjectSyntax(String subject) throws QuerySyntaxException {
        if(subject == null || subject.length() == 0){
            throw new QuerySyntaxException("Query syntax error: Missing subject.");
        }
    }

    private static void checkObjectSyntax(String object) throws QuerySyntaxException {
        if(object == null || object.length() == 0){
            throw new QuerySyntaxException("Query syntax error: Missing object.");
        }
    }

    private static void checkPredicateSyntax(String predicate) throws QuerySyntaxException {
        if(predicate == null || predicate.length() == 0){
            throw new QuerySyntaxException("Query syntax error: Missing predicate.");
        }
    }
}
