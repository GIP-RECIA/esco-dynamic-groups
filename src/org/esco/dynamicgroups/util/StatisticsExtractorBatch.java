/**
 * 
 */
package org.esco.dynamicgroups.util;

import com.novell.ldap.LDAPAttribute;
import com.novell.ldap.LDAPConnection;
import com.novell.ldap.LDAPEntry;
import com.novell.ldap.LDAPException;
import com.novell.ldap.LDAPSearchResults;

import edu.internet2.middleware.grouper.Group;
import edu.internet2.middleware.grouper.GroupFinder;
import edu.internet2.middleware.grouper.GroupType;
import edu.internet2.middleware.grouper.GroupTypeFinder;
import edu.internet2.middleware.grouper.GrouperSession;
import edu.internet2.middleware.grouper.Member;
import edu.internet2.middleware.grouper.MemberFinder;
import edu.internet2.middleware.grouper.Stem;
import edu.internet2.middleware.grouper.Stem.Scope;
import edu.internet2.middleware.grouper.StemFinder;
import edu.internet2.middleware.grouper.SubjectFinder;
import edu.internet2.middleware.grouper.exception.GrouperSessionException;
import edu.internet2.middleware.grouper.exception.SchemaException;
import edu.internet2.middleware.grouper.exception.SessionException;
import edu.internet2.middleware.grouper.misc.GrouperSessionHandler;
import edu.internet2.middleware.subject.Subject;
import edu.internet2.middleware.subject.SubjectNotFoundException;
import edu.internet2.middleware.subject.SubjectNotUniqueException;

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
 * Callback used to extract the statistics.
 * @author GIP RECIA - A. Deman
 * 14 août 2009
 *
 */
class StatiticsExtractorCallback implements GrouperSessionHandler, Serializable {

    /** Serial version UID.*/
    private static final long serialVersionUID = 7657254084831471327L;

    /** Cardinals extractor. */
    private StatisticsExtractorBatch cardinalsExtractor;

    /** The list of the ids. */
    private Set<String> userIds;

    /**
     * Builds an instance of CreateGroupsCallback.
     * @param cardinalsExtractor The StatisticsExtractorBatch to use.
     */
    public StatiticsExtractorCallback(final StatisticsExtractorBatch cardinalsExtractor) {
        this.cardinalsExtractor = cardinalsExtractor;
    }
    /**
     * Builds an instance of CreateGroupsCallback.
     * @param cardinalsExtractor The StatisticsExtractorBatch to use.
     * @param userIds The list of the ids.
     */
    public StatiticsExtractorCallback(final StatisticsExtractorBatch cardinalsExtractor,
            final Set<String> userIds) {
        this.cardinalsExtractor = cardinalsExtractor;
        this.userIds = userIds;
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
        if (userIds == null) {
            cardinalsExtractor.extractCardinalsInternal(session);
        } else {
            cardinalsExtractor.extractUsersMembershipsCountInternal(session, userIds);
        }
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
        connection = connectionManager.connect();
    }

    /**
     * Gives all the user ids.
     * @return The user ids.
     */
    private Set<String> getUserIds() {
        final String[] uidAtt = new String[]{ldapParameters.getLdapUidAttribute()};
        final Set<String> userIds = new HashSet<String>();
        checkConnection();
        try {

            final LDAPSearchResults result = getConnection().search(ldapParameters.getLdapSearchBase(), 
                    LDAPConnection.SCOPE_SUB, 
                    null, 
                    uidAtt, 
                    false);

            while (result.hasMore()) {
                final LDAPEntry entry = result.next();
                final LDAPAttribute attribute = entry.getAttribute(ldapParameters.getLdapUidAttribute());
                if (attribute != null) {
                    userIds.add(attribute.getStringValue());
                }
            }
        } catch (LDAPException e) {
            LOGGER.error(e, e);
        }
        return userIds;
    }

    /**
     * Counts for each user the number of its memberships to dynamic groups.
     * @param session The grouper session.
     * @param userIds The list of the ids.
     */
    protected void extractUsersMembershipsCountInternal(final GrouperSession session, final Set<String> userIds) {
        PrintWriter pw = null;
        try {
            final GroupType type = GroupTypeFinder.find(grouperParameters.getGrouperType());
            pw = new PrintWriter(new File(usersMembershipsOutputFile));
            for (final String userId : userIds) {
                final Subject subject = SubjectFinder.findById(userId);
                final Member member = MemberFinder.findBySubject(session, subject);
                final Set<Group> groups = member.getImmediateGroups();

                // Count the number of dynamic groups that the current user belongs to.
                int membershipTouDGCount = 0;
                for (final Group group : groups) {
                    if (group.getTypes().contains(type)) {
                        membershipTouDGCount++;
                    }
                }

                // Prints the values into the file.
                final StringBuilder sb = new StringBuilder();
                sb.append(userId);
                sb.append(separator);
                sb.append(subject.getName());
                sb.append(separator);
                sb.append(membershipTouDGCount);
                LOGGER.info(sb.toString());
                pw.println(sb.toString());
            }

        } catch (IllegalArgumentException e) {
            LOGGER.fatal(e, e);
        } catch (FileNotFoundException e) {
            LOGGER.fatal(e, e);
        } catch (SubjectNotFoundException e) {
            LOGGER.error(e, e);
        } catch (SubjectNotUniqueException e) {
            LOGGER.error(e, e);
        } catch (SchemaException e) {
            LOGGER.fatal(e, e);
        } finally {
            if (pw != null) {
                pw.close();
            }
        }  
    }

    /**
     * Extract the cardinals of the group.
     * @param session The grouper session to use.
     */
    protected void extractCardinalsInternalOld(final GrouperSession session) {
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
     * Process the dynamic groups under a stem an call recursively on the child stems.
     * @param session The Grouper session.
     * @param type The custom type associated to the dynamic groups.
     * @param pw The print writer.
     * @param stem The stem to process.
     */
    protected void processStem(final GrouperSession session, 
            final GroupType type, final PrintWriter pw, final Stem stem) {
        
        LOGGER.debug("Processing stem " + stem.getName());
        final Set<Stem> children = stem.getChildStems(Scope.ONE);

        // Process the children
        for (Stem child : children) {
            processStem(session, type, pw, child);
        }

        // Process the groups.
        final Set<Group> groups = stem.getChildGroups(Scope.ONE);
        for (Group group : groups) {
            if (group.getTypes().contains(type)) {
                final StringBuilder sb = new StringBuilder();
                sb.append(group.getName());
                sb.append(separator);
                sb.append(group.getImmediateMembers().size());

                LOGGER.info(sb.toString());
                pw.println(sb.toString());
            }
        }
        pw.flush();
        LOGGER.debug("Finished stem " + stem.getName());
    }

    /**
     * Extract the cardinals of the group.
     * @param session The grouper session to use.
     */
    protected void extractCardinalsInternal(final GrouperSession session) {
        PrintWriter pw = null;
        try {
            final Stem root = StemFinder.findRootStem(session);

            pw = new PrintWriter(new File(groupsMembersOutputFile));
            final GroupType type = GroupTypeFinder.find(grouperParameters.getGrouperType());
            processStem(session, type, pw, root);
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
            GrouperSession.callbackGrouperSession(session, new StatiticsExtractorCallback(this));

        } catch (SessionException e) {
            LOGGER.error(e, e);
        } finally {
            GrouperSession.stopQuietly(session);
        }
    }
    /**
     * Extracts the memberships count.
     */
    public void  extractMembershipsCount() {
        GrouperSession session = null;
        try {

            final Set<String> userIds = getUserIds();
            session = GrouperSession.start(SubjectFinder.findRootSubject(), false);
            GrouperSession.callbackGrouperSession(session, new StatiticsExtractorCallback(this, userIds));

        } catch (SessionException e) {
            LOGGER.error(e, e);
        } finally {
            GrouperSession.stopQuietly(session);
        }
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
        extractor.extractMembershipsCount();
    }
}
