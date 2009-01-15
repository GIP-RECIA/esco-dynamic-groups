/**
 * 
 */
package org.esco.dynamicgroups.domain;


import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.esco.dynamicgroups.IEntryDTO;
import org.esco.dynamicgroups.dao.db.IDBDAOService;
import org.esco.dynamicgroups.dao.grouper.IGroupsDAOService;
import org.esco.dynamicgroups.domain.beans.DynGroup;
import org.esco.dynamicgroups.domain.beans.DynGroupOccurences;
import org.esco.dynamicgroups.util.ESCODynamicGroupsParameters;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * Implementation of the domain service for the ESCO context.
 * @author GIP RECIA - A. Deman
 * 13 janv. 2009
 *
 */
public class ESCODomainServiceImpl implements IDomainService, InitializingBean {

    /** The dynamic attributes. */
    private String[] dynamicAttributes;
    
    /** The Grouper DAO Service to use. */
    private IGroupsDAOService groupsService;

    /** The Database DAO Service to use. */
    private IDBDAOService daoService;

    /** Logger. */
    private Logger logger = Logger.getLogger(ESCODomainServiceImpl.class);

    /**
     * Builds an instance of ESCODomainServiceImpl.
     */
    public ESCODomainServiceImpl() {
        dynamicAttributes = ESCODynamicGroupsParameters.instance().getLdapSearchAttributesAsArray();
        if (logger.isDebugEnabled()) {
            logger.debug("Creates an instance of ESCODomainServiceImpl - Attributes: " 
                    + Arrays.toString(dynamicAttributes) + ".");
        }
    }
    
    /**
     * Checks the properties after the Spring injection.
     * @throws Exception If one of the injected property is null.
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
     public void afterPropertiesSet() throws Exception {
         
         Assert.notNull(this.groupsService, 
                 "The property groupService in the class " + this.getClass().getName() 
                 + " can't be null.");
         
         Assert.notNull(this.daoService, 
                 "The property daoService in the class " + this.getClass().getName() 
                 + " can't be null.");
     }
   
    /**
     * Updates the dynamic groups for a given user entry.
     * @param entry The user entry used to compute the new groups.
     * @see org.esco.dynamicgroups.domain.IDomainService#updateDynamicGroups(IEntryDTO)
     */
    public void updateDynamicGroups(final IEntryDTO entry) {
        if (logger.isDebugEnabled()) {
            logger.debug("Request to Update the dynamic groups for the user: " + entry.getId());
        }
       
        final Map<String, DynGroupOccurences> candidatGroups = new HashMap<String, DynGroupOccurences>();

        // For each value of attribute, computes the candidat groups and their number of occurences.
        // If the attribute is a and the value is v, a candidat group is a group 
        // wich uses a=v in its definition.
        for (String attributeName : dynamicAttributes) {
            final String[] attributeValues = entry.getAttributeValues(attributeName);
            for (String attributeValue : attributeValues) {
                
                final Set<DynGroup> candidatGroupsForAtt = 
                    daoService.getGroupsForAttributeValue(attributeName, attributeValue);
                
                for (DynGroup candidatGroupForAtt : candidatGroupsForAtt) {

                    if (!candidatGroups.containsKey(candidatGroupForAtt.getGroupName())) {
                        candidatGroups.put(candidatGroupForAtt.getGroupName(), 
                                new DynGroupOccurences(candidatGroupForAtt));
                    }
                    candidatGroups.get(candidatGroupForAtt.getGroupName()).incrementOccurences();
                }
            }
        }

        // The retained groups are those which the number of occurences is equal to
        // the number of attributes used in their definition (the group definition are in a conjonctive form).
        //final Subject subject = SubjectFinder.findById(entry.getId());
        final Map<String, DynGroup> retainedCandidatGroups = new HashMap<String, DynGroup>(); 
        for (String candidatGroup : candidatGroups.keySet()) {
            final DynGroupOccurences dynGroupOcc = candidatGroups.get(candidatGroup);
            if (dynGroupOcc.getOcurrences() == dynGroupOcc.getGroup().getAttributesNb()) {
                retainedCandidatGroups.put(candidatGroup, dynGroupOcc.getGroup());
            }
        }
        
        if (logger.isTraceEnabled()) {
            logger.trace("Number of candidat groups: " + candidatGroups.size());
            logger.trace("Number of retained candidat groups: " + retainedCandidatGroups.size());
            logger.trace("All the candidat Groups:" + candidatGroups);
            logger.trace("Retained candidat Groups:" + retainedCandidatGroups);
        }
        
        // Updates the memberships effectively.
        groupsService.updateMemberships(entry.getId(), retainedCandidatGroups);
    }
}
