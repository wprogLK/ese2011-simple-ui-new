/**
 * Calendar framework
 */
package JUnitTests;

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
//TODO: mod. for play!
public class UserSpecificTests extends TestTemplate
{
	@Test
	/**
	 * Only for a better coverage ;-)
	 */
	public void createAppCalendarExceptionsClass()
	{
		new AppExceptions();
	}

	@Test
	public void userAlphaNameShouldBeAlpha() throws UsernameAlreadyExistException, UnknownUserException, AccessDeniedException
	{
		AppCalendar app = new AppCalendar();
		
		app.createUser("Alpha", "123");
		IUser userAlpha = app.loginUser("Alpha", "123");

		assertEquals(userAlpha.getName(), "Alpha");
	}

	@Test
	public void preventCreatingUserWithAlreadyExistingNameTest() throws UnknownUserException, UsernameAlreadyExistException, AccessDeniedException
	{
		//PART 1
		
		AppCalendar app = initCalendarAndUserAlpha();
		
		//PART 2
		
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

		
	}

	@Test
	public void shouldNotRetrieveNonExistentUser() throws AccessDeniedException, UsernameAlreadyExistException, UnknownUserException
	{
		//PART 1
		AppCalendar app = initCalendarAndUserAlpha();
		
		//PART 2
		
		try
		{
			app.loginUser("Beta", "abc");
			fail("UnknownUserException expected!");
		}
		catch (UnknownUserException e)
		{
			assertNotNull(e);
		}
		
	}

	private AppCalendar initCalendarAndUserAlpha()throws UsernameAlreadyExistException, UnknownUserException,AccessDeniedException {
		AppCalendar app = new AppCalendar();
		
		app.createUser("Alpha", "123");
		IUser userAlpha = app.loginUser("Alpha", "123");

		assertEquals(userAlpha.getName(), "Alpha");
		return app;
	}

	@Test
	public void shouldNotRetrieveCalendarOrEventsFromFictitiousUser() throws UnknownCalendarException, AccessDeniedException, ParseException, UsernameAlreadyExistException, UnknownUserException
	{
		AppCalendar app = initCalendarAndUserAlpha();
		
		//PART 2
		
		
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

		
	}

	@Test
	public void calendarOwnerShouldBeUserAlpha() throws CalendarIsNotUniqueException, UnknownCalendarException, UsernameAlreadyExistException, UnknownUserException, AccessDeniedException
	{
		AppCalendar app = initCalendarAndUserAlpha();
		
		IUser userAlpha = createMyCalendar(app);
		
	}

	@Test
	public void deleteCalendarFromUserAlpha() throws UnknownCalendarException, AccessDeniedException, InvalidDateException, CalendarIsNotUniqueException, ParseException, UsernameAlreadyExistException, UnknownUserException
	{
		AppCalendar app = initCalendarAndUserAlpha();
		
		IUser userAlpha = createMyCalendar(app);
		
		//PART 3
		
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
		
	}

	@Test
	public void eventShouldBePrivate () throws UnknownCalendarException, AccessDeniedException, InvalidDateException, UnknownEventException, ParseException, CalendarIsNotUniqueException, UsernameAlreadyExistException, UnknownUserException
	{
		AppCalendar app = initCalendarAndUserAlpha();
		
		IUser userAlpha = createMyCalendar(app);
		
		//PART 3
		
		createMyPrivateEvent(userAlpha);
		
	}

	@Test
	public void eventShouldBePublic() throws UnknownCalendarException, AccessDeniedException, InvalidDateException, UnknownEventException, ParseException, CalendarIsNotUniqueException, UsernameAlreadyExistException, UnknownUserException
	{
		AppCalendar app = initCalendarAndUserAlpha();
		
		IUser userAlpha = createMyCalendar(app);
		
		//PART 3
		
		userAlpha.createPublicEvent("My calendar", "My public event", this.stringParseToDate("23.01.2011"), this.stringParseToDate("23.08.2011"));

		Event myPublicEvent = userAlpha.getCalendar("My calendar").getEvent("My public event", this.stringParseToDate("23.01.2011"));

		assertTrue(myPublicEvent.isPublic());
	}

	@Test
	public void deleteUserAlpha() throws AccessDeniedException, UsernameAlreadyExistException, UnknownUserException, CalendarIsNotUniqueException, UnknownCalendarException
	{
		AppCalendar app = initCalendarAndUserAlpha();
		
		IUser userAlpha = createMyCalendar(app);
		
		//PART 3
		
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
		
	}

	@Test
	public void userBetaShouldNotAccessForeignUserAcount() throws UsernameAlreadyExistException, UnknownUserException, AccessDeniedException, CalendarIsNotUniqueException, UnknownCalendarException, InvalidDateException, ParseException, UnknownEventException
	{
		AppCalendar app = initCalendarAndUserAlpha();
		
		IUser userAlpha = createMyCalendar(app);
		
		//PART 3
		
		createMyPrivateEvent(userAlpha);
		
		//PART 4
		
		try
		{
			app.loginUser("Alpha", "***");
			fail("Unauthorized access permitted!");
		}
		catch(Exception e)
		{
			assertNotNull(e);
		}
		
	}

	@Test
	public void changeUserPasswordTest() throws UsernameAlreadyExistException, UnknownUserException, AccessDeniedException, InvalidDateException, UnknownCalendarException, ParseException, CalendarIsNotUniqueException, UnknownEventException
	{
		AppCalendar app = initCalendarAndUserAlpha();
		
		IUser userAlpha = createMyCalendar(app);
		
		//PART 3
		
		createMyPrivateEvent(userAlpha);
		
		//PART 4
		
		try
		{
			app.loginUser("Alpha", "***");
			fail("Unauthorized access permitted!");
		}
		catch(Exception e)
		{
			assertNotNull(e);
		}
		
		//PART 5
		
		app.createUser("Beta", "abc");
		IUser userBeta = app.loginUser("Beta", "abc");

		app.changePassword("Beta", "abc", "xyz");
		IUser userBetaNew = app.loginUser("Beta", "xyz");
		assertEquals(userBeta, userBetaNew);
		
	}

	@Test
	public void userBetaShouldSeePublicEventsFromForeignCalendar() throws UnknownUserException, UnknownCalendarException, AccessDeniedException, ParseException, InvalidDateException, CalendarIsNotUniqueException, UsernameAlreadyExistException, UnknownEventException
	{
		AppCalendar app = initCalendarAndUserAlpha();
		
		IUser userAlpha = createMyCalendar(app);
		
		//PART 3
		
		userAlpha.createPublicEvent("My calendar", "My public event", this.stringParseToDate("23.01.2011"), this.stringParseToDate("23.08.2011"));

		Event myPublicEvent = userAlpha.getCalendar("My calendar").getEvent("My public event", this.stringParseToDate("23.01.2011"));

		assertTrue(myPublicEvent.isPublic());
		
		//PART4
		
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
		
	}

	@Test
	public void invalidCalendarTest() throws AccessDeniedException, InvalidDateException, UnknownCalendarException, ParseException, UsernameAlreadyExistException, UnknownUserException, CalendarIsNotUniqueException, UnknownEventException
	{
		AppCalendar app = initCalendarAndUserAlpha();
		
		IUser userAlpha = createMyCalendar(app);
		
		//PART 3
		
		createMyPrivateEvent(userAlpha);
		
		//PART 4
		
		try
		{
			userAlpha.getCalendar("No calendar");
		}
		catch(UnknownCalendarException e)
		{
			assertNotNull(e);
		}
		
	}

	private void createMyPrivateEvent(IUser userAlpha)
			throws AccessDeniedException, InvalidDateException,
			UnknownCalendarException, ParseException, UnknownEventException {
		userAlpha.createPrivateEvent("My calendar", "My private event", this.stringParseToDate("22.01.2011"), this.stringParseToDate("22.08.2011"));

		Event myPrivateEvent = userAlpha.getCalendar("My calendar").getEvent("My private event", this.stringParseToDate("22.01.2011"));

		assertTrue(myPrivateEvent.isPrivate());
	}

	@Given("eventShouldBePrivate")
	public void invalidEventStringButValidDateTest() throws UnknownCalendarException, ParseException, AccessDeniedException, InvalidDateException, UnknownUserException, CalendarIsNotUniqueException, UnknownEventException, UsernameAlreadyExistException
	{
		AppCalendar app = initCalendarAndUserAlpha();
		
		IUser userAlpha = createMyCalendar(app);
		
		//PART 3
		
		createMyPrivateEvent(userAlpha);
		
		//PART 4
		
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
	}

	@Given("eventShouldBePrivate")
	public void validEventStringButInvalidDateTest() throws UnknownCalendarException, ParseException, UnknownUserException, AccessDeniedException, UsernameAlreadyExistException, CalendarIsNotUniqueException, InvalidDateException, UnknownEventException
	{
		AppCalendar app = initCalendarAndUserAlpha();
		
		//PART 2
		IUser userAlpha = createMyCalendar(app);
		
		//PART 3
		
		createMyPrivateEvent(userAlpha);
		
		//Part 4
		
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
		
	}

	private IUser createMyCalendar(AppCalendar app)
			throws UnknownUserException, AccessDeniedException,
			CalendarIsNotUniqueException, UnknownCalendarException {
		IUser userAlpha=app.loginUser("Alpha", "123");
		
		userAlpha.createNewCalendar("My calendar");
		Calendar myCalendar;

		myCalendar = userAlpha.getCalendar("My calendar");

		assertEquals(myCalendar.getOwner(), userAlpha);
		return userAlpha;
	}
}
