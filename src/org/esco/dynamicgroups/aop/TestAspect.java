package org.esco.dynamicgroups.aop;

import org.codehaus.aspectwerkz.joinpoint.JoinPoint;

/**
 * 
 * @author GIP RECIA - A. Deman
 * 18 avr. 08
 *
 */
public class TestAspect {

    /**
     * Constructor for TestAspect.
     */
    public TestAspect() {
        System.out.println("Creation of the aspect " + getClass().getSimpleName());
    }
    
    /**
     * Test advice.
     * @param joinPoint The join point.
     * @throws Throwable 
     */
    void display(final JoinPoint joinPoint) throws Throwable {
        System.out.println("!!!!!!!!!!!! In the aspect "
                + getClass().getSimpleName() + " ==>" + joinPoint);
        joinPoint.proceed();
    }
}
