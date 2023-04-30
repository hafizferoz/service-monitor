package com.monitor.service.config;

import org.hibernate.MappingException;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.identity.IdentityColumnSupportImpl;

public class SQLiteIdentityColumnSupport extends IdentityColumnSupportImpl {
	 
    public SQLiteIdentityColumnSupport(Dialect dialect) {
    	super();
	}

	@Override
    public boolean supportsIdentityColumns() {
        return true;
    }
 
    @Override
    public String getIdentitySelectString(String table, String column, int type) 
      throws MappingException {
        return "select last_insert_rowid()";
    }
 
    @Override
    public String getIdentityColumnString(int type) throws MappingException {
        return "not null";
    }
}