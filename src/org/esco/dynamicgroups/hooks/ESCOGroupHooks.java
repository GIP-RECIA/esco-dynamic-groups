/**
 * 
 */
package org.esco.dynamicgroups.hooks;

import java.io.Serializable;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.esco.dynamicgroups.domain.DomainServiceProviderForHooks;
import org.esco.dynamicgroups.domain.IDomainService;
import org.esco.dynamicgroups.domain.parameters.GroupsParametersSection;
import org.esco.dynamicgroups.domain.parameters.ParametersProvider;
import org.esco.dynamicgroups.domain.parameters.ParametersProviderForHooks;
import org.esco.dynamicgroups.domain.reporting.statistics.IStatisticsManager;
import org.esco.dynamicgroups.domain.reporting.statistics.StatisticsManagerProviderForHooks;

import edu.internet2.middleware.grouper.Group;
import edu.internet2.middleware.grouper.GroupType;
import edu.internet2.middleware.grouper.hooks.GroupHooks;
import edu.internet2.middleware.grouper.hooks.beans.HooksContext;
import edu.internet2.middleware.grouper.hooks.beans.HooksGroupBean;

/**
 * Dynamic groups hook.
 * This hook managed the creation of the dynamic groups.
 * @author GIP RECIA - A. Deman
 * 9 February. 2009
 *
 */
public class ESCOGroupHooks extends GroupHooks implements Serializable {

    /** Serial version UID. */
    private static final long serialVersionUID = 8623167847413079614L;

    /** Logger. */
    private static final Logger LOGGER = Logger.getLogger(ESCOGroupHooks.class);

    /** The dynamic type to use. */
    private String dynamicType;

    /** The definition field for the dynamic groups. */
    private String definitionField;

    /** The statistics manager.*/
    private transient IStatisticsManager statisticsManager;

    /** The domain service, used to handle the
     *  operations associated to the dynamic groups. */
    private transient IDomainService domainService;

    /**
     * Builds an instance of ESCOGroupHooks.
     */
    public ESCOGroupHooks() {
        final ParametersProvider parametersProvider = ParametersProviderForHooks.instance().getParametersProvider();

        final GroupsParametersSection grouperParameters = 
            (GroupsParametersSection) parametersProvider.getGroupsParametersSection();
        dynamicType = grouperParameters.getGrouperType();
        definitionField = grouperParameters.getGrouperDefinitionField();
        statisticsManager = StatisticsManagerProviderForHooks.instance().getStatisticsManager();
        domainService = DomainServiceProviderForHooks.instance().getDomainService();

        if (LOGGER.isInfoEnabled()) {
            final StringBuilder sb = new StringBuilder("Creation of an hooks of class: ");
            sb.append(getClass().getSimpleName());
            sb.append(" - dynamic type: ");
            sb.append(dynamicType);
            sb.append(" Definition field: ");
            sb.append(definitionField);
            sb.append(".");
            LOGGER.info(sb.toString());
        }
    }

    /**
     * Post delete hook point.
     * Handle the complete deletion of a dynamic group
     * @param hooksContext The hook context.
     * @param postDeleteBean The available Grouper information.
     */
    @Override
    public void groupPostDelete(final HooksContext hooksContext, 
            final HooksGroupBean postDeleteBean) {

        final Group group = postDeleteBean.getGroup();

        if (isDynamicGroup(group)) {
            domainService.handleDeletedGroup(group.getUuid());
            statisticsManager.handleDeletedGroup(group.getName());

        }
    }
    
 
    /**
     * Tests if a group is dynamic.
     * @param group The group to test.
     * @return True if the group has the dynamic type.
     */
    protected boolean isDynamicGroup(final Group group) {
        final Iterator<GroupType> it = group.getTypes().iterator(); 
        while (it.hasNext()) {
            if (dynamicType.equals(it.next().getName())) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("The group " + group.getName() + " is dynamic.");
                }
                return true;
            }
        }
        return false;
    }
}