package domain.login.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses(
		{ 
			LoginCustomerTest.class,
			LoginCustomerUITest.class 
		})
public class LoginCustomerAllTests {

}
