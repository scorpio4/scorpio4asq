package com.scorpio4.asq.sql;
/*
 *

 */

import com.scorpio4.asq.ASQ;
import com.scorpio4.asq.core.Pattern;
import com.scorpio4.asq.core.Term;
import com.scorpio4.oops.ASQException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Fact:Core (c) 2013
 * Module: com.scorpio4.asq.core.sql
 * @author lee
 * Date  : 11/01/2014
 * Time  : 9:39 PM
 *
 *
 * 	Patterns take the form of Table (this) / Field (has) / Alias (that)

 */
public class SelectSQL {
	protected static final Logger log = LoggerFactory.getLogger(SelectSQL.class);
	protected StringBuilder sql = new StringBuilder();

	public SelectSQL() {
	}

	public SelectSQL(ASQ where) throws ASQException {
		build(this.sql, where);
	}

	public SelectSQL(ASQ parent, ASQ where) throws ASQException {
		assert where!=null;
		build(this.sql, where);
		if (parent!=null) SQLSupport.buildParentFilter(this.sql, parent, where);
	}

	protected void build(StringBuilder sql, ASQ asq) throws ASQException {
		sql.append("SELECT ");
		buildProjections(sql, asq);
		buildFrom(sql, asq);
		buildWhere(sql, asq);
	}

	protected void buildProjections(StringBuilder sql, ASQ asq) {
//		log.debug("buildProjections(): " + asq.getSelects());

		if (asq.isDistinct()) {
			sql.append("DISTINCT ");
		}

		Map<String, Pattern> aliased = new HashMap();
		for(Pattern pattern: asq.getPatterns()) {
			aliased.put(pattern.getThat().toString(), pattern);
		}

		boolean first = true;
		for(Term term: asq.getSelects()) {
			if (!first) sql.append(", "); else first=false;

			String id = term.toString();
			Pattern select = aliased.get(id);
			if (select!=null) {
				SQLSupport.buildField(sql, select);
				sql.append(" AS ").append(id);
			}
		}
	}

	protected void buildFrom(StringBuilder sql, ASQ asq) throws ASQException {
		// get unique table names
		Set<String> tables = new HashSet();
		for(Pattern pattern: asq.getPatterns()) {
			tables.add(pattern.getThis().toString());
		}
		if (tables.isEmpty()) throw new ASQException("Missing FROMs");
		sql.append("\n FROM ");

		boolean first = true;
		for(String table: tables) {
			if (!first) sql.append(", "); else first=false;
			sql.append(table);
		}
	}

	protected void buildWhere(StringBuilder sql, ASQ asq) {
		boolean first = true;
		Map<Pattern,Pattern> joins = new HashMap();

		joins = SQLSupport.buildJoins(joins, asq.getPatterns());
		if (joins.isEmpty()) {
			return;
		}

		sql.append("\n WHERE ");
		for(Pattern left: joins.keySet()) {
			Pattern right = joins.get(left);
			if (!first) sql.append(" AND "); else first=false;
			SQLSupport.buildField(sql, left);
			sql.append("=");
			SQLSupport.buildField(sql, right);
		}
	}

	public String toString() {
		return sql.toString();
	}
}
