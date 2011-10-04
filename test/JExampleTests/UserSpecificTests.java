/**
 * Calendar framework
 */
package JExampleTests;

import interfaces.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;

import org.junit.*;
import org.junit.runner.RunWith;

import models.AppExceptions;
import models.AppExceptions.*;
import models.AppCalendar;
import models.Calendar;
import models.Event;
import ch.unibe.jexample.*;
import static org.junit.Assert.*;

/**
 * @author Lukas Keller
 * @author Renato Corti
 *
 */

@RunWith(JExample.class)
public class UserSpecificTests extends TestTemplate
{

	IUser userAlpha;
	IUser userBeta;

	@Test
	public AppCalendar simpleTest1()
	{
		AppCalendar app = new AppCalendar();
		return app;
	}

	@Test
	/**
	 * Only for a better coverage ;-)
	 */
	public void createAppCalendarExceptionsClass()
	{
		new AppExceptions();
	}

	@Given("simpleTest1")
	public AppCalendar userAlphaNameShouldBeAlpha(AppCalendar app) throws UsernameAlreadyExistException, UnknownUserException, AccessDeniedException
	{
		app.createUser("Alpha", "123");
		this.userAlpha = app.loginUser("Alpha", "123");

		assertEquals(userAlpha.getName(), "Alpha");
		return app;
	}

	@Given("userAlphaNameShouldBeAlpha")
	public AppCalendar preventCreatingUserWithAlreadyExistingNameTest(AppCalendar app) throws UnknownUserException
	{
		try
		{
			app.createUser("Alpha", "456");
			fail("UsernameAlreadyExistException expected!");
		}
		catch (UsernameAlreadyExistException e)
		{
			assertNotNull(e);
		}

		try
		{
			app.loginUser("Alpha", "456");
			fail("AccessDeniedException expected!");
		}
		catch (AccessDeniedException e)
		{
			assertNotNull(e);
		}

		return app;
	}

	@Given("userAlphaNameShouldBeAlpha")
	public AppCalendar shouldNotRetrieveNonExistentUser(AppCalendar app) throws AccessDeniedException
	{
		try
		{
			app.loginUser("Beta", "abc");
			fail("UnknownUserException expected!");
		}
		catch (UnknownUserException e)
		{
			assertNotNull(e);
		}
		return app;
	}

	@Given("userAlphaNameShouldBeAlpha")
	public AppCalendar shouldNotRetrieveCalendarOrEventsFromFictitiousUser(AppCalendar app) throws UnknownCalendarException, AccessDeniedException, ParseException
	{
		try
		{
			app.getAllCalendarsNamesFromUser("Beta");
			fail("UnknownUserException expected!");
		}
		catch (UnknownUserException e)
		{
			assertNotNull(e);
		}

		try
		{
			app.getUsersCalendarPublicEvents("Beta", "Fictitious Calendar", stringParseToDate("23.9.2011"));
			fail("UnknownUserException expected!");
		}
		catch (UnknownUserException e)
		{
			assertNotNull(e);
		}

		try
		{
			app.getUsersCalendarPublicEventsAtDate("Beta", "Fictitious Calendar", stringParseToDate("23.9.2011"));
			fail("UnknownUserException expected!");
		}
		catch (UnknownUserException e)
		{
			assertNotNull(e);
		}

		return app;
	}

	@Given("userAlphaNameShouldBeAlpha")
	public AppCalendar calendarOwnerShouldBeUserAlpha(AppCalendar app) throws CalendarIsNotUniqueException, UnknownCalendarException
	{
		userAlpha.createNewCalendar("My calendar");
		Calendar myCalendar;

		myCalendar = this.userAlpha.getCalendar("My calendar");

		assertEquals(myCalendar.getOwner(), userAlpha);
		return app;
	}

	@Given("calendarOwnerShouldBeUserAlpha")
	public AppCalendar deleteCalendarFromUserAlpha(AppCalendar app) throws UnknownCalendarException, AccessDeniedException, InvalidDateException, CalendarIsNotUniqueException, ParseException
	{
		userAlpha.createNewCalendar("Short-living calendar");
		userAlpha.createPrivateEvent("Short-living calendar", "My private event", this.stringParseToDate("22.01.2011"), this.stringParseToDate("22.08.2011"));
		userAlpha.createPublicEvent("Short-living calendar", "My public event", this.stringParseToDate("23.01.2011"), this.stringParseToDate("23.08.2011"));

		userAlpha.deleteCalendar("Short-living calendar");

		try
		{
			userAlpha.getCalendar("Short-living calendar");
			fail("UnknownCalendarException expected!");
		}
		catch (UnknownCalendarException e)
		{
			assertNotNull(e);
		}

		assertEquals(1, userAlpha.getAllMyCalendarNames().size());
		return app;
	}

	@Given("calendarOwnerShouldBeUserAlpha")
	public AppCalendar eventShouldBePrivate (AppCalendar app) throws UnknownCalendarException, AccessDeniedException, InvalidDateException, UnknownEventException, ParseException
	{
		this.userAlpha.createPrivateEvent("My calendar", "My private event", this.stringParseToDate("22.01.2011"), this.stringParseToDate("22.08.2011"));

		Event myPrivateEvent = this.userAlpha.getCalendar("My calendar").getEvent("My private event", this.stringParseToDate("22.01.2011"));

		assertTrue(myPrivateEvent.isPrivate());
		return app;
	}

	@Given("calendarOwnerShouldBeUserAlpha")
	public AppCalendar eventShouldBePublic(AppCalendar app) throws UnknownCalendarException, AccessDeniedException, InvalidDateException, UnknownEventException, ParseException
	{
		this.userAlpha.createPublicEvent("My calendar", "My public event", this.stringParseToDate("23.01.2011"), this.stringParseToDate("23.08.2011"));

		Event myPublicEvent = this.userAlpha.getCalendar("My calendar").getEvent("My public event", this.stringParseToDate("23.01.2011"));

		assertTrue(myPublicEvent.isPublic());
		return app;
	}

	@Given("userAlphaNameShouldBeAlpha")
	public AppCalendar deleteUserAlpha(AppCalendar app) throws AccessDeniedException
	{
		try
		{
			app.deleteUser("Alpha", "123");
			app.loginUser("Alpha", "123");
			fail("UnknownUserException expected!");
		}
		catch (UnknownUserException e)
		{
			assertNotNull(e);
		}
		return app;
	}

	@Given("eventShouldBePrivate")
	public AppCalendar userBetaShouldNotAccessForeignUserAcount(AppCalendar app)
	{
		try
		{
			app.loginUser("Alpha", "***");
			fail("Unauthorized access permitted!");
		}
		catch(Exception e)
		{
			assertNotNull(e);
		}
		return app;
	}

	@Given("userBetaShouldNotAccessForeignUserAcount")
	public AppCalendar changeUserPasswordTest(AppCalendar app) throws UsernameAlreadyExistException, UnknownUserException, AccessDeniedException
	{
		app.createUser("Beta", "abc");
		this.userBeta = app.loginUser("Beta", "abc");

		app.changePassword("Beta", "abc", "xyz");
		IUser userBetaNew = app.loginUser("Beta", "xyz");
		assertEquals(this.userBeta, userBetaNew);
		return app;
	}

	@Given("eventShouldBePublic")
	public AppCalendar userBetaShouldSeePublicEventsFromForeignCalendar(AppCalendar app) throws UnknownUserException, UnknownCalendarException, AccessDeniedException, ParseException
	{
		Iterator<IEvent> iteratorPublicEvents = app.getUsersCalendarPublicEvents("Alpha", "My calendar", this.stringParseToDate("22.01.2011"));

		int i = 0;
		while(iteratorPublicEvents.hasNext())
		{
			IEvent eventInQuestion = iteratorPublicEvents.next();
			assertEquals(eventInQuestion.getEventName(), "My public event");
			assertEquals(eventInQuestion.getStartDate(), this.stringParseToDate("23.01.2011"));
			assertEquals(eventInQuestion.getEndDate(), this.stringParseToDate("23.08.2011"));
			i++;
		}
		assertEquals(1, i);

		ArrayList<IEvent> arrayListPublicEvents = app.getUsersCalendarPublicEventsAtDate("Alpha", "My calendar", this.stringParseToDate("22.01.2011"));
		assertEquals("My public event", arrayListPublicEvents.get(0).getEventName());
		assertEquals(this.stringParseToDate("23.01.2011"), arrayListPublicEvents.get(0).getStartDate());
		assertEquals(this.stringParseToDate("23.08.2011"), arrayListPublicEvents.get(0).getEndDate());
		assertEquals(1, arrayListPublicEvents.size());
		return app;
	}

	@Given("eventShouldBePrivate")
	public AppCalendar invalidCalendarTest(AppCalendar app)
	{
		try
		{
			userAlpha.getCalendar("No calendar");
		}
		catch(UnknownCalendarException e)
		{
			assertNotNull(e);
		}
		return app;
	}

	@Given("eventShouldBePrivate")
	public AppCalendar invalidEventStringButValidDateTest(AppCalendar app) throws UnknownCalendarException, ParseException
	{
		Calendar calendarAlpha = userAlpha.getCalendar("My calendar");
		try
		{
			calendarAlpha.getEvent("No event", this.stringParseToDate("22.01.2011"));
			fail("Expected UnknownEventException!");
		}
		catch(UnknownEventException e)
		{
			assertNotNull(e);
		}
		return app;
	}

	@Given("eventShouldBePrivate")
	public AppCalendar validEventStringButInvalidDateTest(AppCalendar app) throws UnknownCalendarException, ParseException
	{
		Calendar calendarAlpha = userAlpha.getCalendar("My calendar");
		try
		{
			calendarAlpha.getEvent("My public event", this.stringParseToDate("20.01.2011"));
			fail("Expected UnknownEventException!");
		}
		catch(UnknownEventException e)
		{
			assertNotNull(e);
		}
		return app;
	}
}
