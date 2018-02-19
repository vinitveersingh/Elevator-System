/*
 * This file was automatically generated by EvoSuite
 * Fri Dec 15 23:32:13 EST 2017
 */

package main;

import static org.evosuite.runtime.EvoAssertions.verifyException;
import static org.junit.Assert.fail;

import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class) @EvoRunnerParameters(useVNET = true, separateClassLoader = false, useJEE = true) 
public class Motor_ESTest extends Motor_ESTest_scaffolding {

  @Test(timeout = 100000)
  public void test0()  throws Throwable  {
      // Undeclared exception!
      try { 
        Motor.move((Elevator) null);
        fail("Expecting exception: NullPointerException");
      
      } catch(NullPointerException e) {
         //
         // no message in exception (getMessage() returned null)
         //
         verifyException("main.Motor", e);
      }
  }

  @Test(timeout = 100000)
  public void test1()  throws Throwable  {
      Elevator elevator0 = new Elevator(0);
      Motor motor0 = new Motor(elevator0);
  }
}
