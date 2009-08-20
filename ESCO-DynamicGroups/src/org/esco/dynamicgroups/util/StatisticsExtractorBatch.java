/**
 * 
 */
package org.esco.dynamicgroups.util;

import com.novell.ldap.LDAPConnection;
import com.novell.ldap.LDAPEntry;
import com.novell.ldap.LDAPException;
import com.novell.ldap.LDAPSearchResults;

import edu.internet2.middleware.grouper.Group;
import edu.internet2.middleware.grouper.GroupFinder;
import edu.internet2.middleware.grouper.GroupType;
import edu.internet2.middleware.grouper.GroupTypeFinder;
import edu.internet2.middleware.grouper.GrouperSession;
import edu.internet2.middleware.grouper.SubjectFinder;
import edu.internet2.middleware.grouper.exception.GrouperSessionException;
import edu.internet2.middleware.grouper.exception.SchemaException;
import edu.internet2.middleware.grouper.exception.SessionException;
import edu.internet2.middleware.grouper.misc.GrouperSessionHandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.esco.dynamicgroups.dao.ldap.LDAPConnectionManager;
import org.esco.dynamicgroups.domain.parameters.GroupsParametersSection;
import org.esco.dynamicgroups.domain.parameters.ParametersProvider;
import org.esco.dynamicgroups.domain.parameters.PersonsParametersSection;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.util.Assert;

/**
 * Callback used to create the groups.
 * @author GIP RECIA - A. Deman
 * 14 août 2009
 *
 */
class ExtractDynamicGroupsCardinalCallback implements GrouperSessionHandler, Serializable {

    /** Serial version UID.*/
    private static final long serialVersionUID = 7657254084831471327L;

    /** Cardinals extractor. */
    private StatisticsExtractorBatch cardinalsExtractor;

    /**
     * Builds an instance of CreateGroupsCallback.
     * @param cardinalsExtractor The StatisticsExtractorBatch to use.
     */
    public ExtractDynamicGroupsCardinalCallback(final StatisticsExtractorBatch cardinalsExtractor) {
        this.cardinalsExtractor = cardinalsExtractor;
    }

    /**
     * Creates the groups.
     * @param session The Grouper session to use.
     * @return null.
     * @throws GrouperSessionException
     * @see edu.internet2.middleware.grouper.misc.GrouperSessionHandler#callback(GrouperSession)
     */
    public Object callback(final GrouperSession session)
    throws GrouperSessionException {
        cardinalsExtractor.extractCardinalsInternal(session);
        return null;
    }
}

/**
 * Used to retrieve for each dynamic group the number of its members 
 * and for each user the number of dynamic groups he belongs to.
 * @author GIP RECIA - A. Deman
 * 14 août 2009
 *
 */
public class StatisticsExtractorBatch implements Serializable, InitializingBean {

    /** Serial version UID.*/
    private static final long serialVersionUID = 1473696481667553700L;


    /** Spring context  file name. */
    private static final String CTX_FILE = "classpath:applicationContext-batch.xml";

    /** Name of the bean. */
    private static final String BEAN_NAME = "statisticsExtractor";

    /** Logger. */
    private static final Logger LOGGER = Logger.getLogger(StatisticsExtractorBatch.class);

    /** The separator to use for the outputs. */
    private String separator;

    /** The user parameters provider. */
    private ParametersProvider parametersProvider;

    /** The grouper parameters. */
    private GroupsParametersSection grouperParameters;

    /** The LDAP Parameters. */
    private PersonsParametersSection ldapParameters;

    /** Name of the file to write the groups members. */
    private String groupsMembersOutputFile;

    /** Name of the file to write the users memberships. */
    private String usersMembershipsOutputFile;

    /** LDAP Connection manager. */
    private LDAPConnectionManager connectionManager;

    /** The LDAP Connection. */
    private transient LDAPConnection connection;

    /**
     * Builds an instance of StatisticsExtractorBatch.
     */
    public StatisticsExtractorBatch() {
        super();
    }


    /**
     * Checks the beans injection.
     * @throws Exception
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(this.parametersProvider, 
                "The property parametersProvider in the class " 
                + getClass().getName() + " can't be null.");

        Assert.notNull(this.separator, 
                "The property separator in the class " 
                + getClass().getName() + " can't be null.");

        Assert.notNull(this.groupsMembersOutputFile, 
                "The property groupsMembersOutputFile in the class " 
                + getClass().getName() + " can't be null.");

        Assert.notNull(this.connectionManager, 
                "The property connectionManager in the class " 
                + getClass().getName() + " can't be null.");

        grouperParameters = (GroupsParametersSection) parametersProvider.getGroupsParametersSection();
        ldapParameters = (PersonsParametersSection) parametersProvider.getPersonsParametersSection();
    }

    /**
     * Gives all the user ids.
     * @return The user ids.
     */
    private Set<String> getUserIds() {
        final String[] uidAtt = new String[]{ldapParameters.getLdapUidAttribute()};
        final Set<String> userIds = new HashSet<String>();
        try {
            
            final LDAPSearchResults result = getConnection().search(ldapParameters.getLdapSearchBase(), 
                    LDAPConnection.SCOPE_SUB, 
                    null, 
                    uidAtt, 
                    false);

            while (result.hasMore()) {
                final LDAPEntry entry = result.next();
                userIds.add(entry.getAttribute(ldapParameters.getLdapUidAttribute()).getStringValue());
            }
        } catch (LDAPException e) {
            LOGGER.error(e, e);
        }
        return userIds;
    }
    /**
  public Set<String> getMembers(final DynamicGroupDefinition definition) {
        final Set<String> userIds = new HashSet<String>();
        if (definition.isValid()) {
            checkConnection();

            final String filter = translateToLdapFilter(definition);

            try {
                final LDAPSearchResults result = getConnection().search(ldapParameters.getLdapSearchBase(), 
                        LDAPConnection.SCOPE_SUB, 
                        filter, 
                        uidAttributeArray, 
                        false, 
                        constraints);

                while (result.hasMore()) {
                    final LDAPEntry entry = result.next();
                    userIds.add(entry.getAttribute(ldapParameters.getLdapUidAttribute()).getStringValue());
                }

            } catch (LDAPException e) {
                translateToLdapFilter(definition);
                LOGGER.error(e, e);
            }
        }
        return userIds;
    }

     */

    /**
     * Extract the cardinals of the group.
     * @param session The grouper session to use.
     */
    protected void extractCardinalsInternal(final GrouperSession session) {
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new File(groupsMembersOutputFile));
            final GroupType type = GroupTypeFinder.find(grouperParameters.getGrouperType());
            final Set<Group> groups = GroupFinder.findAllByType(session, type);
            for (Group group : groups) {
                final StringBuilder sb = new StringBuilder();
                sb.append(group.getName());
                sb.append(separator);
                sb.append(group.getImmediateMembers().size());

                LOGGER.info(sb.toString());
                pw.println(sb.toString());
            }
        } catch (SchemaException e) {
            LOGGER.fatal(e, e);
        } catch (IllegalArgumentException e) {
            LOGGER.fatal(e, e);
        } catch (FileNotFoundException e) {
            LOGGER.fatal(e, e);
        } finally {
            if (pw != null) {
                pw.close();
            }
        }
    }

    /**
     * Extract the cardinals.
     */
    public void  extractCardinals() {
        GrouperSession session = null;
        try {


            session = GrouperSession.start(SubjectFinder.findRootSubject(), false);
            GrouperSession.callbackGrouperSession(session, new ExtractDynamicGroupsCardinalCallback(this));

        } catch (SessionException e) {
            LOGGER.error(e, e);
        } finally {
            GrouperSession.stopQuietly(session);
        }
    }

    /**
     * Main method.
     * @param args The user arguments.
     */
    public static void main(final String[] args) {

        final ThreadLocal<ApplicationContext> appCtx = new ThreadLocal<ApplicationContext>();
        appCtx.set(new FileSystemXmlApplicationContext(CTX_FILE));
        final BeanFactory beanFactory = appCtx.get();

        final StatisticsExtractorBatch extractor = 
            (StatisticsExtractorBatch) beanFactory.getBean(BEAN_NAME);
        extractor.extractCardinals();
    }

    /**
     * Getter for separator.
     * @return separator.
     */
    public String getSeparator() {
        return separator;
    }

    /**
     * Setter for separator.
     * @param separator the new value for separator.
     */
    public void setSeparator(final String separator) {
        this.separator = separator;
    }

    /**
     * Getter for parametersProvider.
     * @return parametersProvider.
     */
    public ParametersProvider getParametersProvider() {
        return parametersProvider;
    }

    /**
     * Setter for parametersProvider.
     * @param parametersProvider the new value for parametersProvider.
     */
    public void setParametersProvider(final ParametersProvider parametersProvider) {
        this.parametersProvider = parametersProvider;
    }

    /**
     * Getter for grouperParameters.
     * @return grouperParameters.
     */
    public GroupsParametersSection getGrouperParameters() {
        return grouperParameters;
    }

    /**
     * Setter for grouperParameters.
     * @param grouperParameters the new value for grouperParameters.
     */
    public void setGrouperParameters(final GroupsParametersSection grouperParameters) {
        this.grouperParameters = grouperParameters;
    }


    /**
     * Getter for groupsMembersOutputFile.
     * @return groupsMembersOutputFile.
     */
    public String getGroupsMembersOutputFile() {
        return groupsMembersOutputFile;
    }


    /**
     * Setter for groupsMembersOutputFile.
     * @param groupsMembersOutputFile the new value for outputFile.
     */
    public void setGroupsMembersOutputFile(final String groupsMembersOutputFile) {
        this.groupsMembersOutputFile = groupsMembersOutputFile;
    }


    /**
     * Getter for connectionManager.
     * @return connectionManager.
     */
    public LDAPConnectionManager getConnectionManager() {
        return connectionManager;
    }


    /**
     * Setter for connectionManager.
     * @param connectionManager the new value for connectionManager.
     */
    public void setConnectionManager(final LDAPConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }


    /**
     * Getter for usersMembershipsOutputFile.
     * @return usersMembershipsOutputFile.
     */
    public String getUsersMembershipsOutputFile() {
        return usersMembershipsOutputFile;
    }


    /**
     * Setter for usersMembershipsOutputFile.
     * @param usersMembershipsOutputFile the new value for usersMembershipsOutputFile.
     */
    public void setUsersMembershipsOutputFile(final String usersMembershipsOutputFile) {
        this.usersMembershipsOutputFile = usersMembershipsOutputFile;
    }

    /**
     * Checks the connection and tries to reconnect if needed.
     */
    private void checkConnection() {
        synchronized (connection) {
            if (!connectionManager.isActiveConnection(connection)) {
                connection = connectionManager.connect();
            }
        }
    } 
    /**
     * Getter for connection.
     * @return connection.
     */
    protected LDAPConnection getConnection() {
        synchronized (connection) {
            return connection;
        }
    }
}
