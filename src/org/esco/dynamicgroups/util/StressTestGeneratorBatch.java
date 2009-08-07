/**
 * 
 */
package org.esco.dynamicgroups.util;

import edu.internet2.middleware.grouper.Group;
import edu.internet2.middleware.grouper.GroupType;
import edu.internet2.middleware.grouper.GroupTypeFinder;
import edu.internet2.middleware.grouper.GrouperSession;
import edu.internet2.middleware.grouper.Stem;
import edu.internet2.middleware.grouper.StemFinder;
import edu.internet2.middleware.grouper.exception.AttributeNotFoundException;
import edu.internet2.middleware.grouper.exception.GroupAddException;
import edu.internet2.middleware.grouper.exception.GroupModifyException;
import edu.internet2.middleware.grouper.exception.InsufficientPrivilegeException;
import edu.internet2.middleware.grouper.exception.SchemaException;
import edu.internet2.middleware.grouper.exception.StemAddException;
import edu.internet2.middleware.grouper.exception.StemNotFoundException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.esco.dynamicgroups.dao.grouper.GrouperSessionUtil;
import org.esco.dynamicgroups.domain.definition.DecodedPropositionResult;
import org.esco.dynamicgroups.domain.definition.PropositionCodec;
import org.esco.dynamicgroups.domain.parameters.GroupsParametersSection;
import org.esco.dynamicgroups.domain.parameters.ParametersProvider;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.util.Assert;

/**
 * Used to generate a stress test groups set.
 * @author GIP RECIA - A. Deman
 * 8 juin 2009
 *
 */
public class StressTestGeneratorBatch implements InitializingBean {

    /** Spring context  file name. */
    private static final String CTX_FILE = "classpath:applicationContext.xml";

    /** Logger to use. */
    private static final Logger LOGGER = Logger.getLogger(StressTestGeneratorBatch.class);

    /** Name of the bean. */
    private static final String ST_BEAN = "stressTestGenerator";

    /** Number of groups to generate. */
    private int nbGroups;

    /** Number of groups per stem. */
    private int nbGroupsPerStem;

    /** Prefix for the group names. */
    private String groupsPrefix;

    /** Name of the stem where the groups are created. */
    private String stressTestRootStem;

    /** The valid definitions. */
    private List<String> validPropositions; 

    /** The parameters provider. */
    private ParametersProvider parametersProvider;

    /** The parameters for grouper. */
    private GroupsParametersSection grouperParameters;

    /** The propositionCodec. */
    private PropositionCodec propositionCodec;

    /** The grouper session to use. */
    private GrouperSessionUtil sessionUtil = new GrouperSessionUtil("GrouperSystem");

    /** The file name that contains the definition to use. */
    private String definitionFileName = "dg-stress_test-01.dat";

    /** Used to add 0 in the number used for the group names.*/
    private int padding;
    
    /** The number of errors during the groups creation. */
    private int nbErrors;
    
    /** Number of created groups. */
    private int nbCreatedGroups;

    /**
     * Builds an instance of StressTestGeneratorBatch.
     */
    public StressTestGeneratorBatch() {
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

        Assert.notNull(this.propositionCodec, 
                "The property propositionCodec in the class " 
                + getClass().getName() + " can't be null.");

        grouperParameters = (GroupsParametersSection) parametersProvider.getGroupsParametersSection();

    }

    /**
     * Loads the definitions from the file.
     */
    private void loadDefinitions() {
        final InputStream is = getClass().getClassLoader().getResourceAsStream(definitionFileName);
        final BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String defStr = null;
        validPropositions = new ArrayList<String>();
        int cpt = 0;
        int line = 1;
        try {
            while ((defStr = reader.readLine()) != null) {
                defStr = defStr.trim();
                if (!("".equals(defStr) || defStr.startsWith("//"))) {
                    DecodedPropositionResult dec = propositionCodec.decode(defStr); 
                    if (dec.isValid()) {
                        validPropositions.add(defStr);
                        LOGGER.info("Definion " + defStr + " is valid. Definition : " + cpt++);
                    } else {
                        LOGGER.error(" !!! Line: " + line 
                                + " Definion " + defStr 
                                + " is not valid (Not retained). Definition " + cpt++);
                        LOGGER.error(dec.getErrorMessage());
                    }
                }
                line++;
            }
            reader.close();
        } catch (IOException e) {
            LOGGER.fatal("Error while reading definition file: " + definitionFileName + ".");
            LOGGER.fatal(e, e);
        }
    }

    /**
     * Sleeps 3 seconds.
     */
    private void sleep() {
        final int delay = 3;
        try {
            TimeUnit.SECONDS.sleep(delay);
        } catch (InterruptedException e1) {
            /* */
        }
    }

    /**
     * Adds some 0 before the string that represent a number.
     * @param nb The number.
     * @return The string completed with O.
     */
    private String generateNumberString(final int nb) {
        String str = String.valueOf(nb);
        while (str.length() < padding) {
            str = "0" + str;
        }
        return str;
    }

    /** 
     * Creates the groups and set their members defintion.
     */
    private void createGroups() {
        GrouperSession session = sessionUtil.createSession();
        final int nbMaxAttempts = 3;
        try {


            final Stem stRootStem = StemFinder.findByName(session, stressTestRootStem);
            sessionUtil.stopSession(session);
            final GroupType type = GroupTypeFinder.find(grouperParameters.getGrouperType());
            final String defField = grouperParameters.getGrouperDefinitionField();
            session = sessionUtil.createSession();
            Stem currentStem = null;
            
            for (int i = 0; i < nbGroups; i++) {

                session = sessionUtil.createSession();
                final String cptStr = generateNumberString(i);

                if ((i % nbGroupsPerStem) == 0 || currentStem == null) {
                    currentStem = stRootStem.addChildStem("st_" + cptStr, "st_" + cptStr);
                }

                final String name = stressTestRootStem + ":" + groupsPrefix + cptStr;
                final String ext = groupsPrefix + cptStr;
                LOGGER.debug("Creating group " + ext);

                final Group group = currentStem.addChildGroup(ext, ext);
                group.setDescription(ext);
                group.addType(type, true);
                group.store();
                final int defIndex = i % validPropositions.size();
                final String def = validPropositions.get(defIndex);
                LOGGER.debug("Definition: " + def + ".");
                LOGGER.debug("-> added to the group: " + name + ".");
                group.setAttribute(defField, def);
                int nbAttempts = 0;
                boolean done = false;
                while (!done) {
                    try {
                        group.store();
                        nbCreatedGroups++;
                        done = true;
                    } catch (NullPointerException e) {
                        nbErrors++;
                        nbAttempts++;
                        LOGGER.error("NullPointerException during group.store attempt " 
                                + nbAttempts + "/" + nbMaxAttempts);
                        done = nbAttempts > nbMaxAttempts;
                        sleep();
                        sessionUtil.stopSession(session);
                        session = sessionUtil.createSession();

                    }
                }
                sessionUtil.stopSession(session);
            }

        } catch (InsufficientPrivilegeException e) {
            nbErrors++;
            LOGGER.fatal(e, e);
        } catch (GroupModifyException e) {
            nbErrors++;
            LOGGER.fatal(e, e);
        } catch (AttributeNotFoundException e) {
            nbErrors++;
            LOGGER.fatal(e, e);
        } catch (StemNotFoundException e) {
            nbErrors++;
            LOGGER.fatal(e, e);
        } catch (SchemaException e) {
            nbErrors++;
            LOGGER.fatal(e, e);
        } catch (GroupAddException e) {
            nbErrors++;
            LOGGER.fatal(e, e);
        } catch (StemAddException e) {
            nbErrors++;
            LOGGER.fatal(e, e);
        }

        sessionUtil.stopSession(session);
    }

    /**
     * Checks the parameters.
     */
    private void checkParameters() {

        if ("".equals(definitionFileName)) {
            throw new IllegalArgumentException("Empty string for the definition file name.");
        }

        if ("".equals(groupsPrefix)) {
            throw new IllegalArgumentException("Empty string for the groups prefix.");
        }

        if ("".equals(stressTestRootStem)) {
            throw new IllegalArgumentException("Empty string for the stress test root stem.");
        }

        if (nbGroups <= 0) {
            throw new IllegalArgumentException("Invalid number of groups: " + nbGroups 
                    + " - The number of groups to create must be > 0.");
        }

        if (nbGroupsPerStem <= 0) {
            throw new IllegalArgumentException("Invalid number of groups per stem: " + nbGroupsPerStem 
                    + "The number of groups per stem must be > 0.");
        }
    }

    /** 
     * Generates the groups.
     */
    public void generate() {
        final long top = System.currentTimeMillis();
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("---------------------------------------------------------------");
            LOGGER.info("  Parameters used to generates the groups");
            LOGGER.info("---------------------------------------------------------------");
            LOGGER.info("    Definition file:       " + definitionFileName);
            LOGGER.info("    Root stem:             " + stressTestRootStem);
            LOGGER.info("    Groups prefix:         " + groupsPrefix);
            LOGGER.info("    Number of groups:      " + nbGroups);
            LOGGER.info("    Number of groups/stem: " + nbGroupsPerStem);
            LOGGER.info("---------------------------------------------------------------");
        }
        
        checkParameters();
        loadDefinitions();
        createGroups();
        final double ellapsed = System.currentTimeMillis() - top;
        final int milliToSecondFact = 1000;
        final double nbSeconds = ellapsed / milliToSecondFact;
        
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("---------------------------------------------------------------");
            LOGGER.info("   " + nbCreatedGroups + " groups created in " + nbSeconds + " s." );
            LOGGER.info("   Number of errors: " + nbErrors + "." );
            LOGGER.info("---------------------------------------------------------------");
        }
        
    }

    /**
     * Checks the defintions.
     */
    //    private void checkDefinitions() {
    //        validPropositions = new ArrayList<String>();
    //        for (String def : CANDIDAT_DEFINITIONS) {
    //            DecodedPropositionResult dec = propositionCodec.decode(def); 
    //            if (dec.isValid()) {
    //                validPropositions.add(def);
    //                LOGGER.info("Definion " + def + " is valid.");
    //            } else {
    //                LOGGER.error("Definion " + def + " is not valid (Not retained).");
    //                LOGGER.error(dec.getErrorMessage());
    //            }
    //        }
    //    }

    /**
     * @param args
     */
    public static void main(final String[] args) {
        final ThreadLocal<ApplicationContext> appCtx = new ThreadLocal<ApplicationContext>();
        appCtx.set(new FileSystemXmlApplicationContext(CTX_FILE));
        final BeanFactory beanFactory = appCtx.get();
        //        final IDomainService ds = (IDomainService) beanFactory.getBean("domainService");
        //        final File file = new File("toto.ser");
        //        
        //        final ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
        //        oos.writeObject(ds);
        //        oos.close();
        //        
        //        
        //        final ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
        //        final IDomainService previousInstance = (IDomainService) ois.readObject();
        //        ois.close();
        final StressTestGeneratorBatch st = (StressTestGeneratorBatch) beanFactory.getBean(ST_BEAN); 
        final String usage = 
            "Needed parameters:  definitionFile stressTestRootStem groupsPrefix nbGroups nbGroupsPërStem.";
        final int nbParams = 5;
        if (args.length != nbParams) {
            throw new RuntimeException("Five parameters are needed." + usage);
        }
        int argsPos = 0;
        final String defFile = args[argsPos++].trim();
        final String stRoot = args[argsPos++].trim();
        final String grpPref = args[argsPos++].trim();
        final int nbGrp = Integer.parseInt(args[argsPos++].trim());
        final int nbGrpPerStem = Integer.parseInt(args[argsPos++].trim());

        st.setDefinitionFileName(defFile);
        st.setStressTestRootStem(stRoot);
        st.setGroupsPrefix(grpPref);
        st.setNbGroups(nbGrp);
        st.setNbGroupsPerStem(nbGrpPerStem);

        st.generate();
        System.exit(0);
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
     * Getter for propositionCodec.
     * @return propositionCodec.
     */
    public PropositionCodec getPropositionCodec() {
        return propositionCodec;
    }

    /**
     * Setter for propositionCodec.
     * @param propositionCodec the new value for propositionCodec.
     */
    public void setPropositionCodec(final PropositionCodec propositionCodec) {
        this.propositionCodec = propositionCodec;
    }

    /**
     * Getter for nbGroups.
     * @return nbGroups.
     */
    public int getNbGroups() {
        return nbGroups;
    }

    /**
     * Setter for nbGroups.
     * @param nbGroups the new value for nbGroups.
     */
    public void setNbGroups(final int nbGroups) {
        this.nbGroups = nbGroups;
        final String str = String.valueOf(nbGroups);
        if (str.matches("1O*")) {
            padding = str.length() - 1;
        } else {
            padding = str.length();
        }
    }

    /**
     * Getter for nbGroupsPerStem.
     * @return nbGroupsPerStem.
     */
    public int getNbGroupsPerStem() {
        return nbGroupsPerStem;
    }

    /**
     * Setter for nbGroupsPerStem.
     * @param nbGroupsPerStem the new value for nbGroupsPerStem.
     */
    public void setNbGroupsPerStem(final int nbGroupsPerStem) {
        this.nbGroupsPerStem = nbGroupsPerStem;
    }

    /**
     * Getter for groupsPrefix.
     * @return groupsPrefix.
     */
    public String getGroupsPrefix() {
        return groupsPrefix;
    }

    /**
     * Setter for groupsPrefix.
     * @param groupsPrefix the new value for groupsPrefix.
     */
    public void setGroupsPrefix(final String groupsPrefix) {
        this.groupsPrefix = groupsPrefix;
    }

    /**
     * Getter for stressTestRootStem.
     * @return stressTestRootStem.
     */
    public String getStressTestRootStem() {
        return stressTestRootStem;
    }

    /**
     * Setter for stressTestRootStem.
     * @param stressTestRootStem the new value for stressTestRootStem.
     */
    public void setStressTestRootStem(final String stressTestRootStem) {
        this.stressTestRootStem = stressTestRootStem;
    }

    /**
     * Getter for definitionFileName.
     * @return definitionFileName.
     */
    public String getDefinitionFileName() {
        return definitionFileName;
    }

    /**
     * Setter for definitionFileName.
     * @param definitionFileName the new value for definitionFileName.
     */
    public void setDefinitionFileName(final String definitionFileName) {
        this.definitionFileName = definitionFileName;
    }
}
