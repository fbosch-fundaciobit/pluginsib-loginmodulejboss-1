package org.fundaciobit.plugins.loginmodule.ldap;

import org.apache.log4j.Logger;

import java.security.acl.Group;

import java.util.Properties;
import java.util.List;

import javax.security.auth.login.LoginException;

import org.fundaciobit.pluginsib.utils.ldap.LDAPConstants;
import org.fundaciobit.pluginsib.utils.ldap.LDAPUserManager;
import org.jboss.security.SimpleGroup;
import org.jboss.security.SimplePrincipal;

import org.jboss.security.auth.spi.UsernamePasswordLoginModule;

/**
 * 
 * @author anadal
 * 
 */
public class LdapLoginModule extends UsernamePasswordLoginModule {

  protected final Logger log = Logger.getLogger(getClass());

  private LDAPUserManager ldapUserManager = null;

  public LdapLoginModule() {
  }

  private LDAPUserManager getLdapUserManager() {
    if (ldapUserManager == null) {
      Properties ldapProperties = new Properties();
      
      for (String attrib :  LDAPConstants.LDAP_PROPERTIES) {
        String value = (String)this.options.get(attrib);
        if (value == null) {
          log.error("Atribut " + attrib + " val null.", new Exception());
        } else {
          ldapProperties.setProperty(attrib, value);
        }
      }

      ldapUserManager = new LDAPUserManager(ldapProperties);

    }
    return ldapUserManager;
  }

  /**
   * Overriden to return an empty password string as typically one cannot obtain
   * a user's password. We also override the validatePassword so this is ok.
   * 
   * @return and empty password String
   */
  protected String getUsersPassword() throws LoginException {
    return "";
  }

  /**
   * Overriden by subclasses to return the Groups that correspond to the to the
   * role sets assigned to the user. Subclasses should create at least a Group
   * named "Roles" that contains the roles assigned to the user. A second common
   * group is "CallerPrincipal" that provides the application identity of the
   * user rather than the security domain identity.
   * 
   * @return Group[] containing the sets of roles
   */
  protected Group[] getRoleSets() throws LoginException {
    
    String username = getUsername();
    
    log.info("getRoleSets() => User=" + username);
    
    SimpleGroup group = new SimpleGroup("Roles");
    
    try {
      List<String> roles = getLdapUserManager().getRolesOfUser(username);

      if (roles != null) {
        boolean debugEnabled = log.isDebugEnabled();
        for (String rol : roles) {
          if(debugEnabled) {
            log.debug("getRoleSets() => ROL= ]" + rol + "[");
          }
          SimplePrincipal userRoles = new SimplePrincipal(rol);
          group.addMember(userRoles);
        }
      }
      
    } catch (Exception e) {
      log.error("Error getting roles of user: " + e.getMessage(), e);
    }
    
    return new Group[] { group };
  }

  /**
   * Validate the inputPassword by creating a ldap InitialContext with the
   * SECURITY_CREDENTIALS set to the password.
   * 
   * @param inputPassword
   *          the password to validate.
   * @param expectedPassword
   *          ignored
   */
  protected boolean validatePassword(String inputPassword, String expectedPassword) {

    log.debug("validatePassword()");

    boolean isValid = false;
    if (inputPassword != null) {

      try {
        // Validate the password by trying to create an initial context
        String username = getUsername();

        isValid = getLdapUserManager().authenticateUser(username,
            inputPassword);

      } catch (Exception e) {
        log.error("Failed to validate password", e);
      }
    }
    return isValid;
  }
}
