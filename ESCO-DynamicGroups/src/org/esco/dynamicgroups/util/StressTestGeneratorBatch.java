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
import edu.internet2.middleware.grouper.exception.StemNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;
import org.esco.dynamicgroups.dao.grouper.GrouperSessionUtil;
import org.esco.dynamicgroups.domain.definition.DecodedPropositionResult;
import org.esco.dynamicgroups.domain.definition.IProposition;
import org.esco.dynamicgroups.domain.definition.PropositionCodec;
import org.esco.dynamicgroups.domain.parameters.GroupsParametersSection;
import org.esco.dynamicgroups.domain.parameters.ParametersProvider;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.util.Assert;

/**
 * Used to generate a stress test.
 * @author GIP RECIA - A. Deman
 * 8 juin 2009
 *
 */
public class StressTestGeneratorBatch implements InitializingBean {
    
    /** Spring context  file name. */
    private static final String BATCH_CTX_FILE = "classpath:applicationContext-batch.xml";
    
    /** Name of the bean for the I18N manager. */
    private static final String ST_BEAN = "stressTestGenerator";
    
    /** Number of groups to generate. */
    private static final int NB_GROUPS = 300;
    
    /** Prefix for the group names. */
    private static final String GROUP_PREFIX = "dg_sress_test_";
    
    /** Name of the stem where the groups are created. */
    private static final String ST_ROOT_STEM = "esco:st";
    
    /** Logger to use. */
    private static final Logger LOGGER = Logger.getLogger(StressTestGeneratorBatch.class);
    
    /** The candidat definitions. */
    private static final String[] CANDIDAT_DEFINITIONS = {
        "ENTEleveMajeur=0",
        "ENTEleveEnseignements=MATHEMATIQUES",
        "ENTEleveEnseignements=MATH*",
        "ENTEleveEnseignements = PHYSIQUE*",
        "And(And(And(ENTEleveStatutEleve=SCOLAIRE, ENTEleveEnseignements=MATHEMATIQUES*), "
        + "ENTPersonStructRattach=ENTStructureSIREN=19370001000013,ou=structures,dc=esco-centre,dc=fr), "
        + "ENTEleveClasses=*$701)",
        "ENTEleveEnseignements = *MODULE*",
        "ENTEleveClasses=*$601",
        "ENTEleveClasses=*$602",
        "ENTEleveClasses=*$603",
        "ENTEleveClasses=*$604",
        "ENTEleveClasses=*$605",
        "ENTEleveClasses=*$606",
        "ENTEleveClasses=*$607",
        "ENTEleveClasses=*$608",
        "ENTEleveClasses=*$609",
        "ENTEleveClasses=*$610",
        "ENTEleveClasses=*$611",
        "ENTEleveClasses=*$612",
        "ENTEleveClasses=*$701",
        "ENTEleveClasses=*$702",
        "ENTEleveClasses=*$703",
        "ENTEleveClasses=*$704",
        "ENTEleveClasses=*$705",
        "ENTEleveClasses=*$706",
        "ENTEleveClasses=*$707",
        "ENTEleveClasses=*$708",
        "ENTEleveClasses=*$709",
        "ENTEleveClasses=*$710",
        "ENTEleveClasses=*$711",
        "ENTEleveClasses=*$712",
    };
    
    /** The valid definitions. */
    private List<String> validPropositions; 
    
    /** The parameters provider. */
    /** */
    private ParametersProvider parametersProvider;
    
    /** The parameters for grouper. */
    private GroupsParametersSection grouperParameters;
    
    /** The propositionCodec. */
    private PropositionCodec propositionCodec;
    
    /** The grouper session to use. */
    private GrouperSessionUtil sessionUtil = new GrouperSessionUtil("GrouperSystem");
    

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
     * Generates the groups.
     */
    public void generate() {
        checkDefinitions();
        final Random rand = new Random();
        final GrouperSession session = sessionUtil.createSession();
        try {
            final Stem stem = StemFinder.findByName(session, ST_ROOT_STEM);
            final GroupType type = GroupTypeFinder.find(grouperParameters.getGrouperType());
            final String defField = grouperParameters.getGrouperDefinitionField();
            for (int i = 0; i < NB_GROUPS; i++) {
                final String ext = GROUP_PREFIX + i;
                LOGGER.debug("Creating group" + ext);
                final Group group = stem.addChildGroup(ext, ext);
                group.addType(type, true);
                final int defIndex = rand.nextInt(validPropositions.size());
                final String def = validPropositions.get(defIndex);
                LOGGER.debug("Creating group" + ext + " with definition: " + def);
                group.setAttribute(defField, def);
                group.store();
                
            }
            
            
        } catch (StemNotFoundException e) {
          LOGGER.fatal(e, e);
        } catch (GroupAddException e) {
            LOGGER.fatal(e, e);
        } catch (InsufficientPrivilegeException e) {
            LOGGER.fatal(e, e);
        } catch (GroupModifyException e) {
            LOGGER.fatal(e, e);
        } catch (SchemaException e) {
            LOGGER.fatal(e, e);
        } catch (AttributeNotFoundException e) {
            LOGGER.fatal(e, e);
        }
        
        
        
        sessionUtil.stopSession(session);
        
    }
    
    /**
     * Checks the defintions.
     */
    private void checkDefinitions() {
        validPropositions = new ArrayList<String>();
        for (String def : CANDIDAT_DEFINITIONS) {
            DecodedPropositionResult dec = propositionCodec.decode(def); 
            if (dec.isValid()) {
                validPropositions.add(def);
                LOGGER.info("Definion " + def + " is valid.");
            } else {
                LOGGER.error("Definion " + def + " is not valid (Not retained).");
                LOGGER.error(dec.getErrorMessage());
            }
        }
    }

    /**
     * @param args
     */
    public static void main(final String[] args) {
        final ThreadLocal<ApplicationContext> appCtx = new ThreadLocal<ApplicationContext>();
        appCtx.set(new FileSystemXmlApplicationContext(BATCH_CTX_FILE));
        final BeanFactory beanFactory = appCtx.get();
        final StressTestGeneratorBatch st = (StressTestGeneratorBatch) beanFactory.getBean(ST_BEAN);   
        st.generate();
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


 
}
