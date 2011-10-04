/**
 * Calendar framework
 */
package JUnitTests;

import interfaces.IUser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

import models.AppCalendar;
import models.AppExceptions.AccessDeniedException;
import models.AppExceptions.UnknownUserException;
import models.AppExceptions.UsernameAlreadyExistException;

import play.test.UnitTest;

/**
 * @author Lukas Keller
 * @author Renato Corti
 *
 */

public class TestTemplate extends UnitTest
{
	@Test
	public void doNothingTest()
	{
		
	}
	
	protected Date stringParseToDate(String strDate) throws ParseException
	{
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

		return sdf.parse(strDate);
	}
	
	protected AppCalendar init() throws UsernameAlreadyExistException, UnknownUserException, AccessDeniedException
	{
		AppCalendar app = new AppCalendar();
		app.createUser("Alpha", "123");
		return app;
	}
}
