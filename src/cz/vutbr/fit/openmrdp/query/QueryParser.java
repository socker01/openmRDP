package cz.vutbr.fit.openmrdp.query;

import cz.vutbr.fit.openmrdp.exceptions.QuerySyntaxException;

/**
 * @author Jiri Koudelka
 * @since 14.05.2018
 */
//TODO: add test
public final class QueryParser {

    private static final String IDENTIFY_COMMAND = "IDENTIFY";
    private static final String WHERE_COMMAND = "WHERE";

    public static QueryRaw parseQuery(String query) throws QuerySyntaxException {
        checkQuerySyntax(query);

        String variableName = parseVariable(query);
        String conditions = parseConditions(query);

        return new QueryRaw(variableName, conditions);
    }

    private static void checkQuerySyntax(String query) throws QuerySyntaxException {
        if(!query.toUpperCase().contains(IDENTIFY_COMMAND) || !query.toUpperCase().contains(WHERE_COMMAND)){
            throw new QuerySyntaxException("Query does not have expected syntax.");
        }
    }

    private static String parseVariable(String query) {
        query = query.replaceFirst(IDENTIFY_COMMAND, "");
        query = query.substring(1);

        int spaceIndex = query.indexOf(" ");

        return query.substring(0, spaceIndex);
    }

    private static String parseConditions(String query){
        int whereCommandIndex = query.indexOf(WHERE_COMMAND);

        return query.substring(whereCommandIndex + 1 + WHERE_COMMAND.length());
    }
}
