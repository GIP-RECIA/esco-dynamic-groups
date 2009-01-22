package org.esco.dynamicgroups.aop;

import java.io.Serializable;

import org.apache.log4j.Logger;
import org.codehaus.aspectwerkz.joinpoint.JoinPoint;


/**
 * 
 * @author GIP RECIA - A. Deman
 * 18 avr. 08
 *
 */
public class TestAspect implements Serializable {
    /** Serial version UID.*/
    private static final long serialVersionUID = -146983562007946379L;
    
    /** Logger. */
    private static final Logger LOGGER = Logger.getLogger(TestAspect.class);
    /**
     * Constructor for TestAspect.
     */
    public TestAspect() {
        System.out.println("!!!!!!!!!!!!!!!!!!!! Creation of the aspect " + getClass().getSimpleName());
        LOGGER.info("!!!!!!!!!!!!!!!!!!!! Creation of the aspect " + getClass().getSimpleName());
    }

    /**
     * Test advice.
     * @param joinPoint The join point.
     * @return the result of the join point invocation.
     * @throws Throwable 
     */
    public Object display(final JoinPoint joinPoint) throws Throwable {
        System.out.println("!!!!!!!!!!!! In the aspect "
                + getClass().getSimpleName() + " ==>" + joinPoint);
        LOGGER.info("!!!!!!!!!!!! In the aspect "
                + getClass().getSimpleName() + " ==>" + joinPoint);
        return joinPoint.proceed();
    }
    
    /**
     * Test advice.
     * @param joinPoint The join point.
     */
    public void testAfter(final JoinPoint joinPoint) {
        System.out.println("!!!!!!!!!!!! In the aspect "
                + getClass().getSimpleName() + " ==>" + joinPoint);
        LOGGER.warn("!!!!!!!!!!!! In the aspect "
                + getClass().getSimpleName() + " ==>" + joinPoint);
    }


}
