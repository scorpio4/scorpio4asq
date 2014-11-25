package com.scorpio4.asq.sql;

import com.scorpio4.asq.ASQ;
import com.scorpio4.asq.core.Pattern;
import com.scorpio4.oops.ASQException;

import java.util.Collection;

/**
 * Created by lee on 19/11/14.
 */
public class UpdateSQL extends QueriesSQL {

	public UpdateSQL() {
	}

	public UpdateSQL(ASQ where) throws ASQException {
		build(where);
	}

	public UpdateSQL(ASQ parent, ASQ where) throws ASQException {
		build(where);
//		SQLSupport.buildParentFilter(this.sql, parent, where);
	}

	protected void build(StringBuilder sql, String table, Collection<Pattern> patterns) {
		if (sql.length()==0) {
			sql.append("UPDATE ").append(table);
			sql.append(" SET ");
		}
		for(Pattern pattern: patterns) {
			String fieldName = pattern.getHas().toString();
			String aliasName = pattern.getThat().toString();

			sql.append(fieldName).append(" = '${").append(aliasName).append("}', ");
		}
	}
}
