package playTests;
/**
 * Calendar framework
 */

import interfaces.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;

import org.junit.*;
import org.junit.runner.RunWith;

import models.*;
import models.AppExceptions;
import models.AppExceptions.*;

import ch.unibe.jexample.*;
import static org.junit.Assert.*;

/**
 * @author Lukas Keller
 * @author Renato Corti
 *
 */
//TODO: FIX IT FOR PLAY TEST!
@RunWith(JExample.class)
public class UserSpecificTests extends TestTemplate
{

	IUser userAlpha;
	IUser userBeta;

	@Test
	public AppCalendar simpleTest1()
	{
		AppCalendar AppCalendar = new AppCalendar();
		return AppCalendar;
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
	public AppCalendar userAlphaNameShouldBeAlpha(AppCalendar AppCalendar) throws UsernameAlreadyExistException, UnknownUserException, AccessDeniedException
	{
		AppCalendar.createUser("Alpha", "123");
		this.userAlpha = AppCalendar.loginUser("Alpha", "123");

		assertEquals(userAlpha.getName(), "Alpha");
		return AppCalendar;
	}

	@Given("userAlphaNameShouldBeAlpha")
	public AppCalendar preventCreatingUserWithAlreadyExistingNameTest(AppCalendar AppCalendar) throws UnknownUserException
	{
		try
		{
			AppCalendar.createUser("Alpha", "456");
			fail("UsernameAlreadyExistException expected!");
		}
		catch (UsernameAlreadyExistException e)
		{
			assertNotNull(e);
		}

		try
		{
			AppCalendar.loginUser("Alpha", "456");
			fail("AccessDeniedException expected!");
		}
		catch (AccessDeniedException e)
		{
			assertNotNull(e);
		}

		return AppCalendar;
	}

	@Given("userAlphaNameShouldBeAlpha")
	public AppCalendar shouldNotRetrieveNonExistentUser(AppCalendar AppCalendar) throws AccessDeniedException
	{
		try
		{
			AppCalendar.loginUser("Beta", "abc");
			fail("UnknownUserException expected!");
		}
		catch (UnknownUserException e)
		{
			assertNotNull(e);
		}
		return AppCalendar;
	}

	@Given("userAlphaNameShouldBeAlpha")
	public AppCalendar shouldNotRetrieveCalendarOrEventsFromFictitiousUser(AppCalendar AppCalendar) throws UnknownCalendarException, AccessDeniedException, ParseException
	{
		try
		{
			AppCalendar.getAllCalendarsNamesFromUser("Beta");
			fail("UnknownUserException expected!");
		}
		catch (UnknownUserException e)
		{
			assertNotNull(e);
		}

		try
		{
			AppCalendar.getUsersCalendarPublicEvents("Beta", "Fictitious Calendar", stringParseToDate("23.9.2011"));
			fail("UnknownUserException expected!");
		}
		catch (UnknownUserException e)
		{
			assertNotNull(e);
		}

		try
		{
			AppCalendar.getUsersCalendarPublicEventsOverview("Beta", "Fictitious Calendar", stringParseToDate("23.9.2011"));
			fail("UnknownUserException expected!");
		}
		catch (UnknownUserException e)
		{
			assertNotNull(e);
		}

		return AppCalendar;
	}

	@Given("userAlphaNameShouldBeAlpha")
	public AppCalendar calendarOwnerShouldBeUserAlpha(AppCalendar AppCalendar) throws CalendarIsNotUniqueException, UnknownCalendarException
	{
		userAlpha.createNewCalendar("My calendar");
		Calendar myCalendar;

		myCalendar = this.userAlpha.getCalendar("My calendar");

		assertEquals(myCalendar.getOwner(), userAlpha);
		return AppCalendar;
	}

	@Given("calendarOwnerShouldBeUserAlpha")
	public AppCalendar deleteCalendarFromUserAlpha(AppCalendar AppCalendar) throws UnknownCalendarException, AccessDeniedException, InvalidDateException, CalendarIsNotUniqueException, ParseException
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
		return AppCalendar;
	}

	@Given("calendarOwnerShouldBeUserAlpha")
	public AppCalendar eventShouldBePrivate (AppCalendar AppCalendar) throws UnknownCalendarException, AccessDeniedException, InvalidDateException, UnknownEventException, ParseException
	{
		this.userAlpha.createPrivateEvent("My calendar", "My private event", this.stringParseToDate("22.01.2011"), this.stringParseToDate("22.08.2011"));

		Event myPrivateEvent = this.userAlpha.getCalendar("My calendar").getEvent("My private event", this.stringParseToDate("22.01.2011"));

		assertTrue(myPrivateEvent.isPrivate());
		return AppCalendar;
	}

	@Given("calendarOwnerShouldBeUserAlpha")
	public AppCalendar eventShouldBePublic(AppCalendar AppCalendar) throws UnknownCalendarException, AccessDeniedException, InvalidDateException, UnknownEventException, ParseException
	{
		this.userAlpha.createPublicEvent("My calendar", "My public event", this.stringParseToDate("23.01.2011"), this.stringParseToDate("23.08.2011"));

		Event myPublicEvent = this.userAlpha.getCalendar("My calendar").getEvent("My public event", this.stringParseToDate("23.01.2011"));

		assertTrue(myPublicEvent.isPublic());
		return AppCalendar;
	}

	@Given("userAlphaNameShouldBeAlpha")
	public AppCalendar deleteUserAlpha(AppCalendar AppCalendar) throws AccessDeniedException
	{
		try
		{
			AppCalendar.deleteUser("Alpha", "123");
			AppCalendar.loginUser("Alpha", "123");
			fail("UnknownUserException expected!");
		}
		catch (UnknownUserException e)
		{
			assertNotNull(e);
		}
		return AppCalendar;
	}

	@Given("eventShouldBePrivate")
	public AppCalendar userBetaShouldNotAccessForeignUserAcount(AppCalendar AppCalendar)
	{
		try
		{
			AppCalendar.loginUser("Alpha", "***");
			fail("Unauthorized access permitted!");
		}
		catch(Exception e)
		{
			assertNotNull(e);
		}
		return AppCalendar;
	}

	@Given("userBetaShouldNotAccessForeignUserAcount")
	public AppCalendar changeUserPasswordTest(AppCalendar AppCalendar) throws UsernameAlreadyExistException, UnknownUserException, AccessDeniedException
	{
		AppCalendar.createUser("Beta", "abc");
		this.userBeta = AppCalendar.loginUser("Beta", "abc");

		AppCalendar.changePassword("Beta", "abc", "xyz");
		IUser userBetaNew = AppCalendar.loginUser("Beta", "xyz");
		assertEquals(this.userBeta, userBetaNew);
		return AppCalendar;
	}

	@Given("eventShouldBePublic")
	public AppCalendar userBetaShouldSeePublicEventsFromForeignCalendar(AppCalendar AppCalendar) throws UnknownUserException, UnknownCalendarException, AccessDeniedException, ParseException
	{
		Iterator<IEvent> iteratorPublicEvents = AppCalendar.getUsersCalendarPublicEvents("Alpha", "My calendar", this.stringParseToDate("22.01.2011"));

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

		ArrayList<IEvent> arrayListPublicEvents = AppCalendar.getUsersCalendarPublicEventsOverview("Alpha", "My calendar", this.stringParseToDate("22.01.2011"));
		assertEquals("My public event", arrayListPublicEvents.get(0).getEventName());
		assertEquals(this.stringParseToDate("23.01.2011"), arrayListPublicEvents.get(0).getStartDate());
		assertEquals(this.stringParseToDate("23.08.2011"), arrayListPublicEvents.get(0).getEndDate());
		assertEquals(1, arrayListPublicEvents.size());
		return AppCalendar;
	}

	@Given("eventShouldBePrivate")
	public AppCalendar invalidCalendarTest(AppCalendar AppCalendar)
	{
		try
		{
			userAlpha.getCalendar("No calendar");
		}
		catch(UnknownCalendarException e)
		{
			assertNotNull(e);
		}
		return AppCalendar;
	}

	@Given("eventShouldBePrivate")
	public AppCalendar invalidEventStringButValidDateTest(AppCalendar AppCalendar) throws UnknownCalendarException, ParseException
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
		return AppCalendar;
	}

	@Given("eventShouldBePrivate")
	public AppCalendar validEventStringButInvalidDateTest(AppCalendar AppCalendar) throws UnknownCalendarException, ParseException
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
		return AppCalendar;
	}
}
