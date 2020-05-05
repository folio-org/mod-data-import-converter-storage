package org.folio.dao.sql;

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.SPACE;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import org.apache.commons.lang3.StringUtils;
import org.folio.cql2pgjson.CQL2PgJSON;
import org.folio.cql2pgjson.model.SqlSelect;


public class SelectBuilder {
  private final StringBuilder query;

  /**
   * Creates a sql select builder instance. It takes a part of select statement (before <i>where</i> keyword)
   * and then you are able to build conditions for that select statement.
   * <br>
   * <br>
   * For example:
   * "SELECT detail_id, detail_type, detail FROM associations_view"
   *
   * @param selectStatement
   */
  public SelectBuilder(String selectStatement) {
    this.query = new StringBuilder(selectStatement);
  }

  /**
   * Puts a value in quotes.
   * <br>
   * <br>
   * 123 -> '123'
   *
   * @param value a value.
   * @return a value in quotes.
   */
  public static String putInQuotes(String value) {
    return format("'%s'", value);
  }

  /**
   * Parses a cql query to a sql query.
   * <br>
   * <br>
   * For example: associations_view.detail stores an array of jsons. We need to specify a jsonb field of the
   * associations_view and the first element of an jsonb array (<i>associations_view.detail -> (0)</i>)
   * where will be performed searching.<br>
   * <br>
   * cql query <i>"name=testActionProfile"</i> produces the following result:
   * <br>
   * <br>
   * lower(f_unaccent(associations_view.detail -> (0) ->> 'name')) ~ lower(f_unaccent('(^|[[:punct:]]|[[:space:]]|(?=[[:punct:]]|[[:space:]]))testActionProfile($|[[:punct:]]|[[:space:]]|(?<=[[:punct:]]|[[:space:]]))'))
   * <br>
   * <br>
   * If a table filed stores json (not array of jsons) we need to specify only this field (<i>action_profiles.jsonb</i>)
   *
   * @param jsonField a jsonb filed.
   * @param query     a cql query.
   * @return sql query.
   */
  public static String parseQuery(String jsonField, String query) {
    StringBuilder parsedQuery = new StringBuilder();
    if (isNotBlank(query)) {
      try {
        //here is jsonField is a jsonb array field and (0) is first element in the array, so this way we search in a json.
        SqlSelect select = new CQL2PgJSON(jsonField).toSql(query);
        parsedQuery
          .append("(")
          .append(select.getWhere())
          .append(")");
        if (StringUtils.isNotEmpty(select.getOrderBy())) {
          parsedQuery
            .append(SPACE)
            .append("ORDER BY")
            .append(SPACE)
            .append(select.getOrderBy());
        }
      } catch (Exception e) {
        throw new IllegalStateException(format("Can not parse the cql query: %s", query), e);
      }
    }
    return parsedQuery.toString();
  }

  /**
   * Appends <i>" WHERE"</i> to a select statement.
   *
   * @return a select statement with "WHERE" sql keyword
   */
  public SelectBuilder where() {
    if (isNotEmpty(query)) {
      query.append(SPACE);
      query.append("WHERE");
    }
    return this;
  }

  /**
   * Appends <i>" some_field=some-value"</i> to a select statement.
   * <br>
   * Do nothing if a select statement is empty or a value is blank.
   *
   * @param field a table field.
   * @param value a value
   * @return a select statement with equality condition of field:value
   */
  public SelectBuilder equals(String field, String value) {
    if (isNotEmpty(query)) {
      query.append(SPACE);
      query.append(field);
      query.append("=");
      query.append(value);
    }
    return this;
  }

  /**
   * Appends <i>" AND"</i> to a select statement.
   * <br>
   * Do nothing if a select statement is empty or the select statement ends with "AND" sql keyword.
   *
   * @return a select statement with "AND" sql keyword
   */
  public SelectBuilder and() {
    if (isNotEmpty(query)) {
      query.append(SPACE);
      query.append("AND");
    }
    return this;
  }

  /**
   * Appends a query to a select statement.
   *
   * @param query a sql query
   * @return a select statement
   */
  public SelectBuilder appendQuery(String query) {
    if (isNotEmpty(this.query)) {
      this.query.append(SPACE);
      this.query.append(query);
    }
    return this;
  }

  /**
   * Appends <i>" LIMIT some-value"</i> to a select statement.
   *
   * @param limit a limit value
   * @return a select statement with "LIMIT"
   */
  public SelectBuilder limit(int limit) {
    if (isNotEmpty(query)) {
      query.append(SPACE);
      query.append("LIMIT");
      query.append(SPACE);
      query.append(limit);
    }
    return this;
  }

  /**
   * Appends <i>" OFFSET some-value"</i> to a select statement.
   *
   * @param offset an offset value
   * @return a select statement with "OFFSET"
   */
  public SelectBuilder offset(int offset) {
    if (isNotEmpty(query)) {
      query.append(SPACE);
      query.append("OFFSET");
      query.append(SPACE);
      query.append(offset);
    }
    return this;
  }

  @Override
  public String toString() {
    return query.toString();
  }

  private boolean isEmpty(StringBuilder sqlQuery) {
    return sqlQuery.length() == 0;
  }

  private boolean isNotEmpty(StringBuilder sqlQuery) {
    return !isEmpty(sqlQuery);
  }
}
