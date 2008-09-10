package org.esco.dynamicgroups.aop;

import org.apache.log4j.Logger;
import org.codehaus.aspectwerkz.joinpoint.JoinPoint;


/**
 * 
 * @author GIP RECIA - A. Deman
 * 18 avr. 08
 *
 */
public class TestAspect {
    /** Logger. */
    private static final Logger LOGGER = Logger.getLogger(TestAspect.class);
    /**
     * Constructor for TestAspect.
     */
    public TestAspect() {
        LOGGER.info("Creation of the aspect " + getClass().getSimpleName());
    }

    /**
     * Test advice.
     * @param joinPoint The join point.
     * @return the result of the join point invocation.
     * @throws Throwable 
     */
    public Object display(final JoinPoint joinPoint) throws Throwable {
        LOGGER.info("!!!!!!!!!!!! In the aspect "
                + getClass().getSimpleName() + " ==>" + joinPoint);
        return joinPoint.proceed();
    }


}
