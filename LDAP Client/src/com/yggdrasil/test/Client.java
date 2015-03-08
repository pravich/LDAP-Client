package com.yggdrasil.test;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

public class Client {
	public static void main(String[] args) {
		Hashtable env = new Hashtable();
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL, "ldap://192.168.10.101:389");
		env.put(Context.SECURITY_AUTHENTICATION, "simple");
		env.put(Context.SECURITY_PRINCIPAL, "uid=admin,ou=staff,dc=yggdrasil,dc=com");
		env.put(Context.SECURITY_CREDENTIALS, "admin");
		
		DirContext ctx = null;
		NamingEnumeration results = null;
		System.out.println("Begin.");
		try {
			ctx = new InitialDirContext(env);
			
//			System.out.println(ctx.getAttributes("uid=user01,ou=user,dc=yggdrasil,dc=com"));
			
			SearchControls controls = new SearchControls();
			controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
			results = ctx.search("dc=yggdrasil,dc=com", "uid=user01", controls);
			while(results.hasMore()) {
				SearchResult searchResult = (SearchResult) results.next();
				Attributes attributes = searchResult.getAttributes();
				Attribute attr = attributes.get("cn");
				String cn = (String)attr.get();
				System.out.println(" Person Common Name = " + attributes.get("cn"));
				System.out.println(" Person Display Name = " + attributes.get("displayName"));
				System.out.println(" Person logonhours = " + attributes.get("logonhours"));
				System.out.println(" Person MemberOf = " + attributes.get("memberOf"));				
			}
		} catch (Throwable e) {
			e.printStackTrace();
		} finally {
			System.out.println("closing result");
			if (results != null) {
				try {
					results.close();
				} catch(Exception e) {
				}
			}
			System.out.println("closing context");
			if (ctx != null) {
				try {
					ctx.close();
				} catch(Exception e) {
				}
			}
		}
		System.out.println("End.");
	}
}
