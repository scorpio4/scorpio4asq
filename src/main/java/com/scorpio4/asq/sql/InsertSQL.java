package com.scorpio4.asq.sql;

import com.scorpio4.asq.ASQ;
import com.scorpio4.asq.core.Pattern;
import com.scorpio4.oops.ASQException;

import java.util.Collection;

/**
 * Created by lee on 19/11/14.
 */
public class InsertSQL extends QueriesSQL {

	public InsertSQL(ASQ where) throws ASQException {
		build(where);
	}

	public InsertSQL(ASQ parent, ASQ where) throws ASQException {
		build(where);
//		SQLSupport.buildParentFilter(this.sql, parent, where);
	}

	protected void build(StringBuilder sql, String table, Collection<Pattern> patterns) {
		if (sql.length()==0) {
			sql.append("INSERT INTO ").append(table);
		}
		StringBuilder fields = new StringBuilder();
		StringBuilder values  = new StringBuilder();

		for(Pattern pattern: patterns) {
			String fieldName = pattern.getHas().toString();
			if (fields.length()>0) fields.append(",");
			fields.append(fieldName);

			String aliasName = pattern.getThat().toString();
			if (values.length()>0) values.append(",");
			values.append("${").append(aliasName).append("}");
		}
		sql.append(" (").append(fields).append(") VALUES (").append(values).append(");");
	}
}
