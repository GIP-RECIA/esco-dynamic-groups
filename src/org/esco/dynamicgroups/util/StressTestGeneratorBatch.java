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
 * Used to generate a stress test.
 * @author GIP RECIA - A. Deman
 * 8 juin 2009
 *
 */
public class StressTestGeneratorBatch implements InitializingBean {

    /** Spring context  file name. */
    private static final String BATCH_CTX_FILE = "classpath:applicationContext.xml";

    /** Name of the bean for the I18N manager. */
    private static final String ST_BEAN = "stressTestGenerator";

    /** Number of groups to generate. */
    private static final int NB_GROUPS = 1000;

    /** Prefix for the group names. */
    private static final String GROUP_PREFIX = "dg_sress_test_3_";

    /** Name of the stem where the groups are created. */
    private static final String ST_ROOT_STEM = "esco:st";

    /** Logger to use. */
    private static final Logger LOGGER = Logger.getLogger(StressTestGeneratorBatch.class);

    /** The candidat definitions. */
    private static final String[] CANDIDAT_DEFINITIONS = {
        "ENTEleveMajeur=O",
        "ENTEleveMajeur=N",
        "ENTEleveEnseignements=MATH*",
        "ENTEleveEnseignements = PHYSIQUE*",
        "And(And(And(ENTEleveStatutEleve=SCOLAIRE, ENTEleveEnseignements=MATHEMATIQUES*), "
        + "ENTPersonStructRattach=ENTStructureSIREN=19370001000013,ou=structures,dc=esco-centre,dc=fr), "
        + "ENTEleveClasses=*$701)",
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
        "OR(ENTEleveClasses=*$601, ENTEleveClasses=*$602)",
        "OR(ENTEleveClasses=*$602, ENTEleveClasses=*$603)",
        "OR(ENTEleveClasses=*$603, ENTEleveClasses=*$604)",
        "OR(ENTEleveClasses=*$604, ENTEleveClasses=*$605)",
        "OR(ENTEleveClasses=*$605, ENTEleveClasses=*$606)",
        "OR(ENTEleveClasses=*$607, ENTEleveClasses=*$608)",
        "OR(ENTEleveClasses=*$609, ENTEleveClasses=*$610)",
        "OR(ENTEleveClasses=*$611, ENTEleveClasses=*$622)",
        "OR(ENTEleveClasses=*$601, ENTEleveClasses=*$701)",
        "OR(ENTEleveClasses=*$602, ENTEleveClasses=*$702)",
        "OR(ENTEleveClasses=*$603, ENTEleveClasses=*$703)",
        "OR(ENTEleveClasses=*$604, ENTEleveClasses=*$704)",
        "OR(ENTEleveClasses=*$605, ENTEleveClasses=*$705)",
        "OR(ENTEleveClasses=*$606, ENTEleveClasses=*$706)",
        "OR(OR(ENTEleveClasses=*$501, ENTEleveClasses=*$601), ENTEleveClasses=*$701)",
        "OR(OR(ENTEleveClasses=*$502, ENTEleveClasses=*$602), ENTEleveClasses=*$702)",
        "OR(OR(ENTEleveClasses=*$503, ENTEleveClasses=*$603), ENTEleveClasses=*$703)",
        "OR(OR(ENTEleveClasses=*$504, ENTEleveClasses=*$604), ENTEleveClasses=*$704)",
        "OR(OR(ENTEleveClasses=*$505, ENTEleveClasses=*$605), ENTEleveClasses=*$705)",
        "OR(OR(ENTEleveClasses=*$506, ENTEleveClasses=*$606), ENTEleveClasses=*$706)",
        "OR(OR(ENTEleveClasses=*$507, ENTEleveClasses=*$607), ENTEleveClasses=*$707)",
        "OR(OR(ENTEleveClasses=*$508, ENTEleveClasses=*$608), ENTEleveClasses=*$708)",
        "OR(OR(ENTEleveClasses=*$509, ENTEleveClasses=*$608), ENTEleveClasses=*$709)",
        "OR(OR(ENTEleveClasses=*$510, ENTEleveClasses=*$610), ENTEleveClasses=*$710)",
        "OR(OR(ENTEleveClasses=*$511, ENTEleveClasses=*$611), ENTEleveClasses=*$711)",
        "OR(OR(ENTEleveClasses=*$512, ENTEleveClasses=*$612), ENTEleveClasses=*$712)",
        "AND(OR(OR(ENTEleveClasses=*$501, ENTEleveClasses=*$601), ENTEleveClasses=*$701), ENTEleveEnseignements=MATH*)",
        "AND(OR(OR(ENTEleveClasses=*$502, ENTEleveClasses=*$602), ENTEleveClasses=*$702), ENTEleveEnseignements=MATH*)",
        "AND(OR(OR(ENTEleveClasses=*$503, ENTEleveClasses=*$603), ENTEleveClasses=*$703), ENTEleveEnseignements=MATH*)",
        "AND(OR(OR(ENTEleveClasses=*$504, ENTEleveClasses=*$604), ENTEleveClasses=*$704), ENTEleveEnseignements=MATH*)",
        "AND(OR(OR(ENTEleveClasses=*$505, ENTEleveClasses=*$605), ENTEleveClasses=*$705), ENTEleveEnseignements=MATH*)",
        "AND(OR(OR(ENTEleveClasses=*$506, ENTEleveClasses=*$606), ENTEleveClasses=*$706), ENTEleveEnseignements=MATH*)",
        "AND(OR(OR(ENTEleveClasses=*$507, ENTEleveClasses=*$607), ENTEleveClasses=*$707), ENTEleveEnseignements=MATH*)",
        "AND(OR(OR(ENTEleveClasses=*$508, ENTEleveClasses=*$608), ENTEleveClasses=*$708), ENTEleveEnseignements=MATH*)",
        "AND(OR(OR(ENTEleveClasses=*$509, ENTEleveClasses=*$609), ENTEleveClasses=*$709), ENTEleveEnseignements=MATH*)",
        "AND(OR(OR(ENTEleveClasses=*$510, ENTEleveClasses=*$610), ENTEleveClasses=*$710), ENTEleveEnseignements=MATH*)",
        "AND(OR(OR(ENTEleveClasses=*$511, ENTEleveClasses=*$611), ENTEleveClasses=*$711), ENTEleveEnseignements=MATH*)",
        "AND(OR(OR(ENTEleveClasses=*$512, ENTEleveClasses=*$612), ENTEleveClasses=*$712), ENTEleveEnseignements=MATH*)",
        "AND(OR(OR(ENTEleveClasses=*$501, ENTEleveClasses=*$601), ENTEleveClasses=*$701), ENTEleveMajeur=N)",
        "AND(OR(OR(ENTEleveClasses=*$502, ENTEleveClasses=*$602), ENTEleveClasses=*$702), ENTEleveMajeur=N)",
        "AND(OR(OR(ENTEleveClasses=*$503, ENTEleveClasses=*$603), ENTEleveClasses=*$703), ENTEleveMajeur=N)",
        "AND(OR(OR(ENTEleveClasses=*$504, ENTEleveClasses=*$604), ENTEleveClasses=*$704), ENTEleveMajeur=N)",
        "AND(OR(OR(ENTEleveClasses=*$505, ENTEleveClasses=*$605), ENTEleveClasses=*$705), ENTEleveMajeur=N)",
        "AND(OR(OR(ENTEleveClasses=*$506, ENTEleveClasses=*$606), ENTEleveClasses=*$706), ENTEleveMajeur=N)",
        "AND(OR(OR(ENTEleveClasses=*$507, ENTEleveClasses=*$607), ENTEleveClasses=*$707), ENTEleveMajeur=N)",
        "AND(OR(OR(ENTEleveClasses=*$508, ENTEleveClasses=*$608), ENTEleveClasses=*$708), ENTEleveMajeur=N)",
        "AND(OR(OR(ENTEleveClasses=*$509, ENTEleveClasses=*$609), ENTEleveClasses=*$709), ENTEleveMajeur=N)",
        "AND(OR(OR(ENTEleveClasses=*$510, ENTEleveClasses=*$610), ENTEleveClasses=*$710), ENTEleveMajeur=N)",
        "AND(OR(OR(ENTEleveClasses=*$511, ENTEleveClasses=*$611), ENTEleveClasses=*$711), ENTEleveMajeur=N)",
        "AND(OR(OR(ENTEleveClasses=*$512, ENTEleveClasses=*$612), ENTEleveClasses=*$712), ENTEleveMajeur=N)",
        "AND(OR(OR(ENTEleveClasses=*$501, ENTEleveClasses=*$601), ENTEleveClasses=*$701), ENTEleveMajeur=O)",
        "AND(OR(OR(ENTEleveClasses=*$502, ENTEleveClasses=*$602), ENTEleveClasses=*$702), ENTEleveMajeur=O)",
        "AND(OR(OR(ENTEleveClasses=*$503, ENTEleveClasses=*$603), ENTEleveClasses=*$703), ENTEleveMajeur=O)",
        "AND(OR(OR(ENTEleveClasses=*$504, ENTEleveClasses=*$604), ENTEleveClasses=*$704), ENTEleveMajeur=O)",
        "AND(OR(OR(ENTEleveClasses=*$505, ENTEleveClasses=*$605), ENTEleveClasses=*$705), ENTEleveMajeur=O)",
        "AND(OR(OR(ENTEleveClasses=*$506, ENTEleveClasses=*$606), ENTEleveClasses=*$706), ENTEleveMajeur=O)",
        "AND(OR(OR(ENTEleveClasses=*$507, ENTEleveClasses=*$607), ENTEleveClasses=*$707), ENTEleveMajeur=O)",
        "AND(OR(OR(ENTEleveClasses=*$508, ENTEleveClasses=*$608), ENTEleveClasses=*$708), ENTEleveMajeur=O)",
        "AND(OR(OR(ENTEleveClasses=*$509, ENTEleveClasses=*$609), ENTEleveClasses=*$709), ENTEleveMajeur=O)",
        "AND(OR(OR(ENTEleveClasses=*$510, ENTEleveClasses=*$610), ENTEleveClasses=*$710), ENTEleveMajeur=O)",
        "AND(OR(OR(ENTEleveClasses=*$511, ENTEleveClasses=*$611), ENTEleveClasses=*$711), ENTEleveMajeur=O)",
        "AND(OR(OR(ENTEleveClasses=*$512, ENTEleveClasses=*$612), ENTEleveClasses=*$712), ENTEleveMajeur=O)",
        "OR(AND(OR(OR(ENTEleveClasses=*$501, ENTEleveClasses=*$601), ENTEleveClasses=*$701), ENTEleveMajeur=N), "
        + "AND(OR(OR(ENTEleveClasses=*$501, ENTEleveClasses=*$601), ENTEleveClasses=*$701), ENTEleveMajeur=O))",
        "OR(AND(OR(OR(ENTEleveClasses=*$502, ENTEleveClasses=*$602), ENTEleveClasses=*$702), ENTEleveMajeur=N), "
        + "AND(OR(OR(ENTEleveClasses=*$502, ENTEleveClasses=*$602), ENTEleveClasses=*$702), ENTEleveMajeur=O))",
        "OR(AND(OR(OR(ENTEleveClasses=*$503, ENTEleveClasses=*$603), ENTEleveClasses=*$703), ENTEleveMajeur=N), "
        + "AND(OR(OR(ENTEleveClasses=*$503, ENTEleveClasses=*$603), ENTEleveClasses=*$703), ENTEleveMajeur=O))",
        "OR(AND(OR(OR(ENTEleveClasses=*$504, ENTEleveClasses=*$604), ENTEleveClasses=*$704), ENTEleveMajeur=N), "
        + "AND(OR(OR(ENTEleveClasses=*$504, ENTEleveClasses=*$604), ENTEleveClasses=*$704), ENTEleveMajeur=O))",
        "OR(AND(OR(OR(ENTEleveClasses=*$505, ENTEleveClasses=*$605), ENTEleveClasses=*$705), ENTEleveMajeur=N), "
        + "AND(OR(OR(ENTEleveClasses=*$505, ENTEleveClasses=*$605), ENTEleveClasses=*$705), ENTEleveMajeur=O))",
        "OR(AND(OR(OR(ENTEleveClasses=*$506, ENTEleveClasses=*$606), ENTEleveClasses=*$706), ENTEleveMajeur=N), "
        + "AND(OR(OR(ENTEleveClasses=*$506, ENTEleveClasses=*$606), ENTEleveClasses=*$706), ENTEleveMajeur=O))",
        "OR(AND(OR(OR(ENTEleveClasses=*$507, ENTEleveClasses=*$607), ENTEleveClasses=*$707), ENTEleveMajeur=N), "
        + "AND(OR(OR(ENTEleveClasses=*$507, ENTEleveClasses=*$607), ENTEleveClasses=*$707), ENTEleveMajeur=O))",
        "OR(AND(OR(OR(ENTEleveClasses=*$508, ENTEleveClasses=*$608), ENTEleveClasses=*$708), ENTEleveMajeur=N), "
        + "AND(OR(OR(ENTEleveClasses=*$508, ENTEleveClasses=*$608), ENTEleveClasses=*$708), ENTEleveMajeur=O))",
        "OR(AND(OR(OR(ENTEleveClasses=*$509, ENTEleveClasses=*$609), ENTEleveClasses=*$709), ENTEleveMajeur=N), "
        + "AND(OR(OR(ENTEleveClasses=*$509, ENTEleveClasses=*$609), ENTEleveClasses=*$709), ENTEleveMajeur=O))",
        "OR(AND(OR(OR(ENTEleveClasses=*$510, ENTEleveClasses=*$610), ENTEleveClasses=*$710), ENTEleveMajeur=N), "
        + "AND(OR(OR(ENTEleveClasses=*$510, ENTEleveClasses=*$610), ENTEleveClasses=*$710), ENTEleveMajeur=O))",
        "OR(AND(OR(OR(ENTEleveClasses=*$511, ENTEleveClasses=*$611), ENTEleveClasses=*$701), ENTEleveMajeur=N), "
        + "AND(OR(OR(ENTEleveClasses=*$511, ENTEleveClasses=*$611), ENTEleveClasses=*$711), ENTEleveMajeur=O))",
        "OR(AND(OR(OR(ENTEleveClasses=*$512, ENTEleveClasses=*$612), ENTEleveClasses=*$712), ENTEleveMajeur=N), "
        + "AND(OR(OR(ENTEleveClasses=*$512, ENTEleveClasses=*$612), ENTEleveClasses=*$712), ENTEleveMajeur=O))",
        "AND(OR(OR(ENTEleveClasses=*$501, ENTEleveClasses=*$601), ENTEleveClasses=*$701), NOT(ENTEleveMajeur=N))",
        "AND(OR(OR(ENTEleveClasses=*$502, ENTEleveClasses=*$602), ENTEleveClasses=*$702), NOT(ENTEleveMajeur=N))",
        "AND(OR(OR(ENTEleveClasses=*$503, ENTEleveClasses=*$603), ENTEleveClasses=*$703), NOT(ENTEleveMajeur=N))",
        "AND(OR(OR(ENTEleveClasses=*$504, ENTEleveClasses=*$604), ENTEleveClasses=*$704), NOT(ENTEleveMajeur=N))",
        "AND(OR(OR(ENTEleveClasses=*$505, ENTEleveClasses=*$605), ENTEleveClasses=*$705), NOT(ENTEleveMajeur=N))",
        "AND(OR(OR(ENTEleveClasses=*$506, ENTEleveClasses=*$606), ENTEleveClasses=*$706), NOT(ENTEleveMajeur=N))",
        "AND(OR(OR(ENTEleveClasses=*$507, ENTEleveClasses=*$607), ENTEleveClasses=*$707), NOT(ENTEleveMajeur=N))",
        "AND(OR(OR(ENTEleveClasses=*$508, ENTEleveClasses=*$608), ENTEleveClasses=*$708), NOT(ENTEleveMajeur=N))",
        "AND(OR(OR(ENTEleveClasses=*$509, ENTEleveClasses=*$609), ENTEleveClasses=*$709), NOT(ENTEleveMajeur=N))",
        "AND(OR(OR(ENTEleveClasses=*$510, ENTEleveClasses=*$610), ENTEleveClasses=*$710), NOT(ENTEleveMajeur=N))",
        "AND(OR(OR(ENTEleveClasses=*$511, ENTEleveClasses=*$611), ENTEleveClasses=*$711), NOT(ENTEleveMajeur=N))",
        "AND(OR(OR(ENTEleveClasses=*$512, ENTEleveClasses=*$612), ENTEleveClasses=*$712), NOT(ENTEleveMajeur=N))",
        "AND(OR(OR(ENTEleveClasses=*$501, ENTEleveClasses=*$601), ENTEleveClasses=*$701), NOT(ENTEleveMajeur=O))",
        "AND(OR(OR(ENTEleveClasses=*$502, ENTEleveClasses=*$602), ENTEleveClasses=*$702), NOT(ENTEleveMajeur=O))",
        "AND(OR(OR(ENTEleveClasses=*$503, ENTEleveClasses=*$603), ENTEleveClasses=*$703), NOT(ENTEleveMajeur=O))",
        "AND(OR(OR(ENTEleveClasses=*$504, ENTEleveClasses=*$604), ENTEleveClasses=*$704), NOT(ENTEleveMajeur=O))",
        "AND(OR(OR(ENTEleveClasses=*$505, ENTEleveClasses=*$605), ENTEleveClasses=*$705), NOT(ENTEleveMajeur=O))",
        "AND(OR(OR(ENTEleveClasses=*$506, ENTEleveClasses=*$606), ENTEleveClasses=*$706), NOT(ENTEleveMajeur=O))",
        "AND(OR(OR(ENTEleveClasses=*$507, ENTEleveClasses=*$607), ENTEleveClasses=*$707), NOT(ENTEleveMajeur=O))",
        "AND(OR(OR(ENTEleveClasses=*$508, ENTEleveClasses=*$608), ENTEleveClasses=*$708), NOT(ENTEleveMajeur=O))",
        "AND(OR(OR(ENTEleveClasses=*$509, ENTEleveClasses=*$609), ENTEleveClasses=*$709), NOT(ENTEleveMajeur=O))",
        "AND(OR(OR(ENTEleveClasses=*$510, ENTEleveClasses=*$610), ENTEleveClasses=*$710), NOT(ENTEleveMajeur=O))",
        "AND(OR(OR(ENTEleveClasses=*$511, ENTEleveClasses=*$611), ENTEleveClasses=*$711), NOT(ENTEleveMajeur=O))",
        "AND(OR(OR(ENTEleveClasses=*$512, ENTEleveClasses=*$612), ENTEleveClasses=*$712), NOT(ENTEleveMajeur=O))",


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
     * Creates the groups and set their members defintion.
     */
    private void createGroups() {
        GrouperSession session = sessionUtil.createSession();
        final int nbMaxAttempts = 3;
        try {


            final Stem stem = StemFinder.findByName(session, ST_ROOT_STEM);
            sessionUtil.stopSession(session);
            final GroupType type = GroupTypeFinder.find(grouperParameters.getGrouperType());
            final String defField = grouperParameters.getGrouperDefinitionField();
            for (int i = 0; i < NB_GROUPS; i++) {

                session = sessionUtil.createSession();
                final String name = ST_ROOT_STEM + ":" + GROUP_PREFIX + i;
                final String ext = GROUP_PREFIX + i;
                LOGGER.debug("Creating group " + ext);

                final Group group = stem.addChildGroup(ext, ext);
                group.setDescription(ext);
                group.addType(type, true);
                final int defIndex = i % validPropositions.size();
                final String def = validPropositions.get(defIndex);
                LOGGER.debug("Definition: " + def + " added to the group: " + name + ".");
                group.setAttribute(defField, def);
                int nbAttempts = 0;
                boolean done = false;
                while (!done) {
                    try {
                        group.store();
                        done = true;
                    } catch (NullPointerException e) {
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
            LOGGER.fatal(e, e);
        } catch (GroupModifyException e) {
            LOGGER.fatal(e, e);
        } catch (AttributeNotFoundException e) {
            LOGGER.fatal(e, e);
        } catch (StemNotFoundException e) {
            LOGGER.fatal(e, e);
        } catch (SchemaException e) {
            LOGGER.fatal(e, e);
        } catch (GroupAddException e) {
            LOGGER.fatal(e, e);
        }



        sessionUtil.stopSession(session);

    }
    /** 
     * Generates the groups.
     */
    public void generate() {
        checkDefinitions();
        createGroups();
       

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



}
