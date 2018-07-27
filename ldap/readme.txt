

S'ha d'obrir el fitxer login-config.xml i afegir dins un bloc <authentication> 
de <application-policy> el següent text (Important ajustar totes les propietats):


	  <login-module code="org.fundaciobit.plugins.loginmodule.ldap.LdapLoginModule" flag="sufficient" >
		<module-option name="ldap.host_url">ldap://ldap.ibit.org:389</module-option>
		<module-option name="ldap.security_principal">lectorldap</module-option>
		<module-option name="ldap.security_credentials">[PUT PASSWORD]</module-option>
		<module-option name="ldap.security_authentication">simple</module-option>
		<module-option name="ldap.users_context_dn">cn=Users,dc=ibitnet,dc=lan</module-option>
		<module-option name="ldap.search_scope">onelevel</module-option>
		<module-option name="ldap.search_filter">(|(memberOf=CN=@PFI_ADMIN,CN=Users,DC=ibitnet,DC=lan)(memberOf=CN=@PFI_USER,CN=Users,DC=ibitnet,DC=lan))</module-option>
		<module-option name="ldap.prefix_role_match_memberof">CN=@</module-option>
		<module-option name="ldap.suffix_role_match_memberof">,CN=Users,DC=ibitnet,DC=lan</module-option>
		<module-option name="ldap.attribute.username">sAMAccountName</module-option>
		<module-option name="ldap.attribute.mail">mail</module-option>
		<module-option name="ldap.attribute.administration_id">postOfficeBox</module-option>
		<module-option name="ldap.attribute.name">givenName</module-option>
		<module-option name="ldap.attribute.surname">sn</module-option>
		<module-option name="ldap.attribute.telephone">telephoneNumber</module-option>
		<module-option name="ldap.attribute.memberof">memberOf</module-option>
	  </login-module>