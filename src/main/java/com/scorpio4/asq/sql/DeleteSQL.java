package com.scorpio4.asq.sql;

import com.scorpio4.asq.ASQ;
import com.scorpio4.asq.core.Pattern;
import com.scorpio4.oops.ASQException;

import java.util.Collection;

/**
 * Created by lee on 25/11/14.
 */
public class DeleteSQL extends QueriesSQL {

	public DeleteSQL() {
	}

	public DeleteSQL(ASQ where) throws ASQException {
		build(where);
	}

	public DeleteSQL(ASQ parent, ASQ where) throws ASQException {
		build(where);
//		SQLSupport.buildParentFilter(this.sql, parent, where);
	}

	protected void build(StringBuilder sql, String table, Collection<Pattern> patterns) {
		if (sql.length()==0) {
			sql.append("DELETE FROM ").append(table);
			sql.append(" WHERE ");
		}
		// TODO: get PKs
		// build WHERE clause with PK constraints
	}
}
