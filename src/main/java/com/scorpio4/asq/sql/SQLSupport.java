package com.scorpio4.asq.sql;

import com.scorpio4.asq.ASQ;
import com.scorpio4.asq.core.Pattern;
import com.scorpio4.asq.core.Term;
import com.scorpio4.oops.ASQException;

import java.util.*;

/**
 * Created by lee on 19/11/14.
 */
public class SQLSupport {

	public static void buildParentFilter(StringBuilder sql, ASQ parent, ASQ asq) throws ASQException {
		Map<Pattern, Pattern> joins = buildJoins(new HashMap(), asq.getPatterns());
		boolean hasWhere = !joins.isEmpty();

		if (!hasWhere) sql.append(" WHERE ");

		Map<String,Boolean> seen = new HashMap();

		for(Term parentSelect: parent.getSelects() ) {
			for(Pattern select: asq.getPatterns() ) {
				String that = select.getThat().toString();
				boolean done = seen.containsKey(that);
				if (!done && parentSelect.equals(select.getThat()) ) {

					if (hasWhere) sql.append(" AND ");
					buildField(sql, select);
					sql.append(" = '${").append(that).append("}'");
					hasWhere = true;
					seen.put(that, true);
				}
			}
		}
	}

	public static Map<String, Pattern> getIntersects(ASQ parent, ASQ asq) throws ASQException {
		Map<String,Pattern> seen = new HashMap();

		for(Term parentSelect: parent.getSelects() ) {
			for(Pattern select: asq.getPatterns() ) {
				String that = select.getThat().toString();
				boolean done = seen.containsKey(that);
				if (!done && parentSelect.equals(select.getThat()) ) {
					seen.put(that, select);
				}
			}
		}
		return seen;
	}


	// joins have matching aliases ('that' Terms) but different tables ('this' Terms)
	public static  Map<Pattern, Pattern> buildJoins(Map<Pattern, Pattern> joins, Collection<Pattern> patterns) {
//		log.debug("Join Patterns: "+patterns);

		if (patterns==null||patterns.size()<2) {
//			log.debug("Too few patterns to Join: "+patterns);
			return joins;
		}

		for(Pattern left: patterns) {
			for(Pattern right: patterns) {
				if (left.getThat().toString().equals(right.getThat().toString()) &&
						!left.getThis().toString().equals(right.getThis().toString()) &&
						!joins.containsKey(right) ) {
					joins.put(left, right);
				}
			}
			// joins are often nested
			buildJoins(joins, left.getNested());
		}
		return joins;
	}

	public static void buildField(StringBuilder sql, Pattern pattern) {
		sql.append(pattern.getThis()).
				append(".").
				append(pattern.getHas());
	}

}
