package com.scorpio4.asq.sql;

import com.scorpio4.asq.ASQ;
import com.scorpio4.asq.core.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lee on 19/11/14.
 */
public abstract class QueriesSQL {
	Map<String, StringBuilder> queries = new HashMap();
	Map<String, Collection<Pattern>> tablePatterns = new HashMap();
	protected final Logger log = LoggerFactory.getLogger(getClass());

	protected Collection<Pattern> getPatterns(String table) {
		if (!tablePatterns.containsKey(table)) {
			tablePatterns.put(table, new ArrayList() );
		}
		return tablePatterns.get(table);
	}

	protected StringBuilder getSQL(String query) {
		if (!queries.containsKey(query)) {
			StringBuilder sql = new StringBuilder();
			queries.put(query, sql);
		}
		return queries.get(query);
	}

	protected Collection<StringBuilder> getSQL() {
		return queries.values();
	}

	public String toString() {
		StringBuilder sql = new StringBuilder();
		for(String table: queries.keySet()) {
			StringBuilder q = queries.get(table);
			sql.append("# ").append(table).append("\n");
			sql.append(q).append("\n");
		}
		return sql.toString();
	}

	protected void build(ASQ where) {
		for(Pattern pattern: where.getPatterns()) {
			String table = pattern.getThis().toString();
			Collection<Pattern> patterns = getPatterns(table);
			patterns.add(pattern);
		}

		for(String table: tablePatterns.keySet()) {
			Collection<Pattern> patterns = getPatterns(table);
			StringBuilder sql = getSQL(table);
			build(sql, table, patterns);
		}
	}

	protected abstract void build(StringBuilder sql, String table, Collection<Pattern> patterns);
}
