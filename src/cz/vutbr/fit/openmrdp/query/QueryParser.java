package cz.vutbr.fit.openmrdp.query;

import com.sun.istack.internal.NotNull;
import cz.vutbr.fit.openmrdp.exceptions.QuerySyntaxException;

/**
 * Parser for parsing of the identify query stored in the {@link String}.
 *
 * @author Jiri Koudelka
 * @since 14.05.2018
 */
public final class QueryParser {

    private static final String IDENTIFY_COMMAND = "IDENTIFY";
    private static final String WHERE_COMMAND = "WHERE";

    /**
     * Parse identify query into the {@link QueryRaw} object
     *
     * @param query - Query in the {@link String} format
     * @return - {@link QueryRaw} object
     * @throws QuerySyntaxException - if the query doesn't have expected syntax
     */
    @NotNull
    public static QueryRaw parseQuery(@NotNull String query) throws QuerySyntaxException {
        checkQuerySyntax(query);

        String variableName = parseVariable(query);
        String conditions = parseConditions(query);

        return new QueryRaw(variableName, conditions);
    }

    private static void checkQuerySyntax(@NotNull String query) throws QuerySyntaxException {
        if(!query.toUpperCase().contains(IDENTIFY_COMMAND) || !query.toUpperCase().contains(WHERE_COMMAND)){
            throw new QuerySyntaxException("Query does not have expected syntax.");
        }
    }

    @NotNull
    private static String parseVariable(@NotNull String query) {
        query = query.replaceFirst(IDENTIFY_COMMAND, "");
        query = query.substring(1);

        int spaceIndex = query.indexOf(" ");

        return query.substring(0, spaceIndex);
    }

    @NotNull
    private static String parseConditions(@NotNull String query){
        int whereCommandIndex = query.indexOf(WHERE_COMMAND);

        return query.substring(whereCommandIndex + 1 + WHERE_COMMAND.length());
    }
}
