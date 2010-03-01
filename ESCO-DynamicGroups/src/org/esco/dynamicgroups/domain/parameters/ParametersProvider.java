/**
 * 
 */
package org.esco.dynamicgroups.domain.parameters;


import java.io.IOException;
import java.io.InputStream;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

import org.apache.log4j.Logger;



/**
 * Contains the user parameters.
 * @author GIP RECIA - A. Deman
 * 17 avr. 08
 */
public class ParametersProvider extends DGParametersSection {

     /** Serial version UID. */
    private static final long serialVersionUID = -186884409502387377L;

       /** Logger. */
    private static final Logger LOGGER = Logger.getLogger(ParametersProvider.class);

    /** Singleton instance. */
    private static ParametersProvider instance;
    
    /** Section for the parameters for the person backend. */
    private DGParametersSection personsParametersSection;
    
    /** Section for the groups system. */
    private DGParametersSection groupsParametersSection;

    /** Section for the reporting. */
    private DGParametersSection reportingParametersSection;
    
    /** Section for the mail parameters. */
    private DGParametersSection mailParametersSection;
    
    /** Section for the common parameters. */
    private DGParametersSection commonsParametersSection;

    /** Properties instance used to initialize this instance. */
    private Properties parametersProperties;
    
    /**
     * Constructor for ParametersProvider.
     * @param configurationFile The configuration file to use.
     * @param personParametersSection The parameters section associated to the persons backend.
     * @param groupsParametersSection The parameters for the groups system.
     * @param reportingParametersSection The parameters for the reporting.
     * @param mailParametersSection the parameters for the mailer.
     * @param commonsParametersSection The common paramters.
     */
    private ParametersProvider(final String configurationFile, 
            final DGParametersSection personParametersSection,
            final DGParametersSection groupsParametersSection,
            final DGParametersSection reportingParametersSection,
            final DGParametersSection mailParametersSection,
            final DGParametersSection commonsParametersSection) {
        setConfigurationFile(configurationFile);
        this.personsParametersSection = personParametersSection;
        this.personsParametersSection.setConfigurationFile(configurationFile);
        
        this.groupsParametersSection = groupsParametersSection;
        this.groupsParametersSection.setConfigurationFile(configurationFile);
        
        this.reportingParametersSection = reportingParametersSection;
        this.reportingParametersSection.setConfigurationFile(configurationFile);
        
        this.mailParametersSection = mailParametersSection;
        this.mailParametersSection.setConfigurationFile(configurationFile);
        
        this.commonsParametersSection = commonsParametersSection;
        this.commonsParametersSection.setConfigurationFile(configurationFile);
                
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Loading ParametersProvider from file " + configurationFile);
        }

        initialize();

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Loaded values: " + this);
        }
        setInstance(this);
    }
    
    /**
     * Gives the logger for this class.
     * @return The logger for this class.
     * @see org.esco.dynamicgroups.domain.parameters.DGParametersSection#getLogger()
     */
    @Override
    protected Logger getLogger() {
        return LOGGER;
    }


    /**
     * Gives the singleton.
     * @return The singleton.
     */
//    public static ParametersProvider instance() {
//        return instance;
//    }

    /**
     * Initialization.
     */
    private void initialize() {
        try {
            final ClassLoader cl = getClass().getClassLoader();
            final InputStream is  = cl.getResourceAsStream(getConfigurationFile());
            final Properties params = new Properties();
            if (is == null) {
                LOGGER.fatal("Unable to load (from classpath) " + getConfigurationFile());
            }


            params.load(is);
            loadFromProperties(params);
            setParametersProperties(params);

        } catch (InvalidPropertiesFormatException e) {
            LOGGER.error(e, e);
        } catch (IOException e) {
            LOGGER.error(e, e);
        }
    }

    /**
     * Loads all the parameter sections.
     * @param params The properties that contains the values to load.
     */
    @Override
    public void loadFromProperties(final Properties params) {
        
        // Loads the parameters for the persons backend.
        personsParametersSection.loadFromProperties(params);
        
        // Loads the groups system paramters.
        groupsParametersSection.loadFromProperties(params);
        
        // loads the reporting parameters
        reportingParametersSection.loadFromProperties(params);
        
        // Loads the mailer parameters.
        mailParametersSection.loadFromProperties(params);
        
        // Load the common parameters.
        commonsParametersSection.loadFromProperties(params);
        
    }

   
    /**
     * Gives the string representation of the instance.
     * @return The String that denotes the values of the instance.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer(getClass().getSimpleName());
        sb.append("#{");
        sb.append("\n\n");
        
        sb.append("Persons section: ");
        sb.append(personsParametersSection);
        sb.append("\n\n");
       
        sb.append("Groups section: ");
        sb.append(groupsParametersSection);
        sb.append("\n\n");
        
        sb.append("Reporting section: ");
        sb.append(reportingParametersSection);
        sb.append("\n\n");

        sb.append("Mail section: ");
        sb.append(mailParametersSection);
        sb.append("\n\n");
        
        sb.append("Common section: ");
        sb.append(commonsParametersSection);
        
        sb.append("}");
        return sb.toString();
    }

    /**
     * Getter for parametersProperties.
     * @return the parametersProperties
     */
    public Properties getParametersProperties() {
        return parametersProperties;
    }

    /**
     * Setter for parametersProperties.
     * @param parametersProperties the parametersProperties to set
     */
    public void setParametersProperties(final Properties parametersProperties) {
        this.parametersProperties = parametersProperties;
    }
   
    /**
     * Getter for instance.
     * @return instance.
     */
    protected static synchronized ParametersProvider getInstance() {
        return instance;
    }

    /**
     * Setter for instance.
     * @param instance the new value for instance.
     */
    protected static synchronized void setInstance(final ParametersProvider instance) {
        ParametersProvider.instance = instance;
    }

    /**
     * Getter for personsParametersSection.
     * @return personsParametersSection.
     */
    public DGParametersSection getPersonsParametersSection() {
        return personsParametersSection;
    }

    /**
     * Setter for personsParametersSection.
     * @param personsParametersSection the new value for personsParametersSection.
     */
    public void setPersonsParametersSection(final DGParametersSection personsParametersSection) {
        this.personsParametersSection = personsParametersSection;
    }

    /**
     * Getter for mailParametersSection.
     * @return mailParametersSection.
     */
    public DGParametersSection getMailParametersSection() {
        return mailParametersSection;
    }

    /**
     * Setter for mailParametersSection.
     * @param mailParametersSection the new value for mailParametersSection.
     */
    public void setMailParametersSection(final DGParametersSection mailParametersSection) {
        this.mailParametersSection = mailParametersSection;
    }

    /**
     * Getter for groupsParametersSection.
     * @return groupsParametersSection.
     */
    public DGParametersSection getGroupsParametersSection() {
        return groupsParametersSection;
    }

    /**
     * Setter for groupsParametersSection.
     * @param groupsParametersSection the new value for groupsParametersSection.
     */
    public void setGroupsParametersSection(final DGParametersSection groupsParametersSection) {
        this.groupsParametersSection = groupsParametersSection;
    }

    /**
     * Getter for reportingParametersSection.
     * @return reportingParametersSection.
     */
    public DGParametersSection getReportingParametersSection() {
        return reportingParametersSection;
    }

    /**
     * Setter for reportingParametersSection.
     * @param reportingParametersSection the new value for reportingParametersSection.
     */
    public void setReportingParametersSection(final DGParametersSection reportingParametersSection) {
        this.reportingParametersSection = reportingParametersSection;
    }

    /**
     * Getter for commonsParametersSection.
     * @return commonsParametersSection.
     */
    public DGParametersSection getCommonsParametersSection() {
        return commonsParametersSection;
    }

    /**
     * Setter for commonsParametersSection.
     * @param commonsParametersSection the new value for commonsParametersSection.
     */
    public void setCommonsParametersSection(final DGParametersSection commonsParametersSection) {
        this.commonsParametersSection = commonsParametersSection;
    }
}
