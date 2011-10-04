package JUnitTests;

/**
 * Calendar framework
 */

import interfaces.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import org.junit.*;
import ch.unibe.jexample.*;
import static org.junit.Assert.*;

import org.junit.runner.RunWith;

import models.*;
import models.AppExceptions.*;

/**
 * @author Lukas Keller
 * @author Renato Corti
 *
 */
//TODO: mod. for play!
public class CalendarSpecificTests extends TestTemplate
{
	private AppCalendar initCalendarAndUserAlpha()throws UsernameAlreadyExistException, UnknownUserException,AccessDeniedException {
		AppCalendar app = new AppCalendar();
		
		app.createUser("Alpha", "123");
		IUser userAlpha = app.loginUser("Alpha", "123");

		assertEquals(userAlpha.getName(), "Alpha");
		return app;
	}
	
	

	@Test
	public void userAlphaShouldHaveNoCalendars() throws UsernameAlreadyExistException, UnknownUserException, AccessDeniedException
	{
		//PART 1:
		IUser userAlpha = loginUserAlpha();
		
		assertTrue(userAlpha.hasNoCalendar());
		
	}



	private IUser loginUserAlpha() throws UsernameAlreadyExistException,UnknownUserException, AccessDeniedException {
		AppCalendar app=initCalendarAndUserAlpha();
		IUser userAlpha=app.loginUser("Alpha", "123");
		return userAlpha;
	}

	@Test
	public void alphaCalendarNameShouldBeCalendarAlpha() throws UnknownCalendarException, CalendarIsNotUniqueException, UsernameAlreadyExistException, UnknownUserException, AccessDeniedException
	{
		IUser userAlpha = loginUserAlpha();
		
		userAlpha.createNewCalendar("CalendarAlpha");
		Calendar calendarAlpha= userAlpha.getCalendar("CalendarAlpha");
		assertEquals(calendarAlpha.getName(), "CalendarAlpha");
		
	}

	@Test
	public void alphaCalendarOwnerShouldBeUserAlpha() throws UsernameAlreadyExistException, UnknownUserException, AccessDeniedException, CalendarIsNotUniqueException, UnknownCalendarException
	{
		//PART 1:
		IUser userAlpha = loginUserAlpha();
		
		userAlpha.createNewCalendar("CalendarAlpha");
		Calendar calendarAlpha= userAlpha.getCalendar("CalendarAlpha");
		assertEquals(calendarAlpha.getName(), "CalendarAlpha");
		//PART 2:
		assertEquals(calendarAlpha.getOwner(), userAlpha);
	}

	@Test
	public void alphaCalendarShouldBeEmpty() throws AccessDeniedException, UnknownCalendarException, ParseException, UsernameAlreadyExistException, UnknownUserException, CalendarIsNotUniqueException
	{
		//PART 1:
		IUser userAlpha = loginUserAlpha();
		
		userAlpha.createNewCalendar("CalendarAlpha");
		Calendar calendarAlpha= userAlpha.getCalendar("CalendarAlpha");
		assertEquals(calendarAlpha.getName(), "CalendarAlpha");
		//PART 2:
		assertEquals(calendarAlpha.getOwner(), userAlpha);
		
		//Phase 3
		
		ArrayList<IEvent> allEvents = userAlpha.getMyCalendarAllEventsAtDate("CalendarAlpha", this.stringParseToDate("1.1.1970"));
		assertTrue(allEvents.isEmpty());
		
	}

	@Test
	public void userAlphaShouldHaveOneCalendar() throws UsernameAlreadyExistException, UnknownUserException, AccessDeniedException, CalendarIsNotUniqueException, UnknownCalendarException
	{
		//PART 1
		
		IUser userAlpha = loginUserAlpha();
		
		userAlpha.createNewCalendar("CalendarAlpha");
		Calendar calendarAlpha= userAlpha.getCalendar("CalendarAlpha");
		assertEquals(calendarAlpha.getName(), "CalendarAlpha");
		
		//Part 2:
		
		assertFalse(userAlpha.hasNoCalendar());
	}

	@Test
	public void eventNameShouldBeParty() throws AccessDeniedException, UnknownEventException, InvalidDateException, UnknownCalendarException, ParseException, UsernameAlreadyExistException, UnknownUserException, CalendarIsNotUniqueException
	{
		//PART 1:
		IUser userAlpha = loginUserAlpha();
		
		userAlpha.createNewCalendar("CalendarAlpha");
		Calendar calendarAlpha= userAlpha.getCalendar("CalendarAlpha");
		assertEquals(calendarAlpha.getName(), "CalendarAlpha");
		//PART 2:
		assertEquals(calendarAlpha.getOwner(), userAlpha);
		
		//Phase 3
		
		ArrayList<IEvent> allEvents = userAlpha.getMyCalendarAllEventsAtDate("CalendarAlpha", this.stringParseToDate("1.1.1970"));
		assertTrue(allEvents.isEmpty());
		
		//PART 4
		userAlpha.createPrivateEvent("CalendarAlpha", "Party", this.stringParseToDate("22.09.2011"),  this.stringParseToDate("29.09.2011"));

		assertFalse(calendarAlpha.getAllEventsAtDate(this.stringParseToDate("1.1.1970")).isEmpty());
		Event eventParty = calendarAlpha.getEvent("Party", this.stringParseToDate("22.09.2011"));
		assertEquals(eventParty.getEventName(), "Party");
	}

	@Test
	public void eventStartDateShouldBe22_09_2011() throws UnknownEventException, ParseException, AccessDeniedException, InvalidDateException, UnknownCalendarException, UsernameAlreadyExistException, UnknownUserException, CalendarIsNotUniqueException
	{
		//PART 1:
		IUser userAlpha = loginUserAlpha();
		
		userAlpha.createNewCalendar("CalendarAlpha");
		Calendar calendarAlpha= userAlpha.getCalendar("CalendarAlpha");
		assertEquals(calendarAlpha.getName(), "CalendarAlpha");
		//PART 2:
		assertEquals(calendarAlpha.getOwner(), userAlpha);
		
		//Phase 3
		
		ArrayList<IEvent> allEvents = userAlpha.getMyCalendarAllEventsAtDate("CalendarAlpha", this.stringParseToDate("1.1.1970"));
		assertTrue(allEvents.isEmpty());
		
		//PART 4
		userAlpha.createPrivateEvent("CalendarAlpha", "Party", this.stringParseToDate("22.09.2011"),  this.stringParseToDate("29.09.2011"));

		assertFalse(calendarAlpha.getAllEventsAtDate(this.stringParseToDate("1.1.1970")).isEmpty());
		Event eventParty = calendarAlpha.getEvent("Party", this.stringParseToDate("22.09.2011"));
		assertEquals(eventParty.getEventName(), "Party");
		
		//PART 5
		
		//Event eventParty = calendarAlpha.getEvent("Party", this.stringParseToDate("22.09.2011"));	//TODO: may be delete
		assertEquals(eventParty.getStartDate(), this.stringParseToDate("22.09.2011"));
	}

	@Test
	public void eventEndDateShouldBe29_09_2011() throws UnknownEventException, ParseException, UsernameAlreadyExistException, UnknownUserException, AccessDeniedException, CalendarIsNotUniqueException, UnknownCalendarException, InvalidDateException
	{	
		//PART 1:
		IUser userAlpha = loginUserAlpha();
		
		userAlpha.createNewCalendar("CalendarAlpha");
		Calendar calendarAlpha= userAlpha.getCalendar("CalendarAlpha");
		assertEquals(calendarAlpha.getName(), "CalendarAlpha");
		//PART 2:
		assertEquals(calendarAlpha.getOwner(), userAlpha);
		
		//Phase 3
		
		ArrayList<IEvent> allEvents = userAlpha.getMyCalendarAllEventsAtDate("CalendarAlpha", this.stringParseToDate("1.1.1970"));
		assertTrue(allEvents.isEmpty());
		
		//PART 4
		userAlpha.createPrivateEvent("CalendarAlpha", "Party", this.stringParseToDate("22.09.2011"),  this.stringParseToDate("29.09.2011"));

		assertFalse(calendarAlpha.getAllEventsAtDate(this.stringParseToDate("1.1.1970")).isEmpty());
		Event eventParty = calendarAlpha.getEvent("Party", this.stringParseToDate("22.09.2011"));
		assertEquals(eventParty.getEventName(), "Party");
		
		//PART 5
		
		//Event eventParty = calendarAlpha.getEvent("Party", this.stringParseToDate("22.09.2011"));	//TODO: maybe delete
		assertEquals(eventParty.getStartDate(), this.stringParseToDate("22.09.2011"));
		
		//PART 6:
		
		//Event eventParty = calendarAlpha.getEvent("Party", this.stringParseToDate("22.09.2011"));	//TODO: maybe delete
		assertEquals(eventParty.getEndDate(), this.stringParseToDate("29.09.2011"));
	}

	@Test
	public void startDateShouldNotBeAfterEndDate() throws AccessDeniedException, UnknownCalendarException, ParseException, UsernameAlreadyExistException, UnknownUserException, CalendarIsNotUniqueException
	{
		
		//PART 1:
		IUser userAlpha = loginUserAlpha();
		
		userAlpha.createNewCalendar("CalendarAlpha");
		Calendar calendarAlpha= userAlpha.getCalendar("CalendarAlpha");
		assertEquals(calendarAlpha.getName(), "CalendarAlpha");
		//PART 2:
		assertEquals(calendarAlpha.getOwner(), userAlpha);
		
		//Phase 3
		
		ArrayList<IEvent> allEvents = userAlpha.getMyCalendarAllEventsAtDate("CalendarAlpha", this.stringParseToDate("1.1.1970"));
		assertTrue(allEvents.isEmpty());
		
		//PART 4
		
		try
		{
			userAlpha.createPrivateEvent("CalendarAlpha", "BackToTheFutureEvent", this.stringParseToDate("23.09.2011"), this.stringParseToDate("22.09.2011"));
			fail("InvalidDateException expected!");
		}
		catch(InvalidDateException e)
		{
			assertNotNull(e);
		}
		assertTrue(calendarAlpha.getAllEventsAtDate(this.stringParseToDate("1.1.1970")).isEmpty());
	}

	@Test
	public void deleteEventsTest() throws AccessDeniedException, UnknownEventException, InvalidDateException, UnknownCalendarException, ParseException, UsernameAlreadyExistException, UnknownUserException, CalendarIsNotUniqueException
	{
		//PART 1
		
		IUser userAlpha = loginUserAlpha();
		
		userAlpha.createNewCalendar("CalendarAlpha");
		Calendar calendarAlpha= userAlpha.getCalendar("CalendarAlpha");
		assertEquals(calendarAlpha.getName(), "CalendarAlpha");
		
		//Part 2:
		
		assertFalse(userAlpha.hasNoCalendar());
		
		//PART 3
		
		userAlpha.createPrivateEvent("CalendarAlpha", "My Event", this.stringParseToDate("23.09.2011"), this.stringParseToDate("23.09.2011"));
		userAlpha.createPublicEvent("CalendarAlpha", "Our Event", this.stringParseToDate("23.09.2011"), this.stringParseToDate("23.09.2011"));

		calendarAlpha.deleteEvent("My Event", this.stringParseToDate("23.09.2011"));
		calendarAlpha.deleteEvent("Our Event", this.stringParseToDate("23.09.2011"));

		// alphaCalendarShouldBeEmpty
		assertTrue(calendarAlpha.getAllEventsAtDate(this.stringParseToDate("1.1.1970")).isEmpty());
	}

	@Test
	public void caledarNameShouldBeUnique() throws UsernameAlreadyExistException, UnknownUserException, AccessDeniedException, CalendarIsNotUniqueException, UnknownCalendarException
	{	
		//PART 1:
		IUser userAlpha = loginUserAlpha();
		
		userAlpha.createNewCalendar("CalendarAlpha");
		Calendar calendarAlpha= userAlpha.getCalendar("CalendarAlpha");
		assertEquals(calendarAlpha.getName(), "CalendarAlpha");
		//PART 2:
		assertEquals(calendarAlpha.getOwner(), userAlpha);
		
		//PART 3:
		
		try
		{
			userAlpha.createNewCalendar("CalendarAlpha");
			fail("CalendarIsNotUniqueException expected!");
		}
		catch (CalendarIsNotUniqueException e)
		{
			assertNotNull(e);
		}
	}

	@Test
	public void shouldGiveCalendarListOfUserAlphaTest() throws AccessDeniedException, InvalidDateException, UnknownUserException, UnknownCalendarException, CalendarIsNotUniqueException, ParseException, UsernameAlreadyExistException
	{
		//PART 1:
		AppCalendar app=initCalendarAndUserAlpha();
		IUser userAlpha=app.loginUser("Alpha", "123");

		assertTrue(userAlpha.hasNoCalendar());
		
		//PART 2:
		
		userAlpha.createNewCalendar("New calendar");
		userAlpha.createPrivateEvent("New calendar", "Private Event", this.stringParseToDate("21.9.2011"), this.stringParseToDate("21.9.2011"));
		userAlpha.createPublicEvent("New calendar", "Public Event", this.stringParseToDate("21.9.2011"), this.stringParseToDate("21.9.2011"));

		userAlpha.createNewCalendar("Second calendar");
		userAlpha.createPrivateEvent("Second calendar", "Private Event", this.stringParseToDate("22.9.2011"), this.stringParseToDate("22.9.2011"));
		userAlpha.createPublicEvent("Second calendar", "Public Event", this.stringParseToDate("22.9.2011"), this.stringParseToDate("22.9.2011"));

		userAlpha.createNewCalendar("Other calendar");
		userAlpha.createPrivateEvent("Other calendar", "Private Event", this.stringParseToDate("23.9.2011"), this.stringParseToDate("23.9.2011"));
		userAlpha.createPublicEvent("Other calendar", "Public Event", this.stringParseToDate("23.9.2011"), this.stringParseToDate("23.9.2011"));

		ArrayList<String> alphasCalendarListViaUser = userAlpha.getAllMyCalendarNames();
		ArrayList<String> alphasCalendarListViaAppCalendar = app.getAllCalendarsNamesFromUser("Alpha");
		assertEquals(alphasCalendarListViaUser, alphasCalendarListViaAppCalendar);

		assertEquals(3, alphasCalendarListViaUser.size());
		assertEquals(3, alphasCalendarListViaAppCalendar.size());
		assertEquals("New calendar", alphasCalendarListViaUser.get(0));
		assertEquals("New calendar", alphasCalendarListViaAppCalendar.get(0));
		assertEquals("Second calendar", alphasCalendarListViaUser.get(1));
		assertEquals("Second calendar", alphasCalendarListViaAppCalendar.get(1));
		assertEquals("Other calendar", alphasCalendarListViaUser.get(2));
		assertEquals("Other calendar", alphasCalendarListViaAppCalendar.get(2));
	}

	@Test
	public void shouldRetrieveArrayListEventsContainingInputDateFromCalendar() throws UnknownCalendarException, UnknownUserException, AccessDeniedException, InvalidDateException, ParseException, UsernameAlreadyExistException, CalendarIsNotUniqueException
	{
		
		//PART 1:
		AppCalendar app=initCalendarAndUserAlpha();
		IUser userAlpha=app.loginUser("Alpha", "123");

		userAlpha.createNewCalendar("CalendarAlpha");
		Calendar calendarAlpha= userAlpha.getCalendar("CalendarAlpha");
		assertEquals(calendarAlpha.getName(), "CalendarAlpha");
		//PART 2:
		assertEquals(calendarAlpha.getOwner(), userAlpha);
		
		//PART 3:
		
		// Create calendar entries
		userAlpha.createPrivateEvent("CalendarAlpha", "My private one-day event", this.stringParseToDate("23.09.2011"), this.stringParseToDate("23.09.2011"));
		userAlpha.createPublicEvent("CalendarAlpha", "My public one-day event", this.stringParseToDate("23.09.2011"), this.stringParseToDate("23.09.2011"));
		userAlpha.createPrivateEvent("CalendarAlpha", "My private long event", this.stringParseToDate("22.09.2011"), this.stringParseToDate("24.09.2011"));
		userAlpha.createPublicEvent("CalendarAlpha", "My public night event", this.stringParseToDate("22.09.2011"), this.stringParseToDate("24.09.2011"));

		// Retrieve via user object
			// Retrieve all 4 events with ArrayList
				ArrayList<IEvent> userAllEventsDate = userAlpha.getMyCalendarAllEventsAtDate("CalendarAlpha", this.stringParseToDate("23.09.2011"));
				assertEquals(4, userAllEventsDate.size());

			// Retrieve 2 public events with ArrayList
				ArrayList<IEvent> userPublicEventsDate = userAlpha.getMyCalendarPublicEventsAtDate("CalendarAlpha", this.stringParseToDate("23.09.2011"));
				assertEquals(2, userPublicEventsDate.size());
				assertEquals("My public one-day event", userPublicEventsDate.get(0).getEventName());
				assertEquals("My public night event", userPublicEventsDate.get(1).getEventName());

		// Retrieve via AppCalendar
			// Retrieve 2 public events with ArrayList
				ArrayList<IEvent> AppCalendarPublicEventsDate = app.getUsersCalendarPublicEventsAtDate("Alpha", "CalendarAlpha", this.stringParseToDate("23.09.2011"));
				assertEquals(2, AppCalendarPublicEventsDate.size());
				assertEquals("My public one-day event", AppCalendarPublicEventsDate.get(0).getEventName());
				assertEquals("My public night event", AppCalendarPublicEventsDate.get(1).getEventName());
	}

	@Test
	public void shouldRetrieveIteratorEventsContainingInputDateFromCalendar() throws UnknownCalendarException, UnknownUserException, AccessDeniedException, InvalidDateException, ParseException, UsernameAlreadyExistException, CalendarIsNotUniqueException
	{
		//PART 1:
		AppCalendar app=initCalendarAndUserAlpha();
		IUser userAlpha=app.loginUser("Alpha", "123");

		userAlpha.createNewCalendar("CalendarAlpha");
		Calendar calendarAlpha= userAlpha.getCalendar("CalendarAlpha");
		assertEquals(calendarAlpha.getName(), "CalendarAlpha");
		//PART 2:
		assertEquals(calendarAlpha.getOwner(), userAlpha);
		
		//PART 3:
		
		// Create calendar entries
		userAlpha.createPrivateEvent("CalendarAlpha", "My private one-day event", this.stringParseToDate("23.09.2011"), this.stringParseToDate("23.09.2011"));
		userAlpha.createPublicEvent("CalendarAlpha", "My public one-day event", this.stringParseToDate("23.09.2011"), this.stringParseToDate("23.09.2011"));
		userAlpha.createPrivateEvent("CalendarAlpha", "My private past event", this.stringParseToDate("22.09.2011"), this.stringParseToDate("24.09.2011"));
		userAlpha.createPublicEvent("CalendarAlpha", "My public past event", this.stringParseToDate("22.09.2011"), this.stringParseToDate("24.09.2011"));

		// Retrieve via user object
			// Retrieve 2 events with iterator
				Iterator<IEvent> userAllEventsStarting = userAlpha.getMyCalendarAllEventsStartingFrom("CalendarAlpha", this.stringParseToDate("23.09.2011"));
				int i = 0;
				while (userAllEventsStarting.hasNext())
				{
					userAllEventsStarting.next();
					i++;
				}
				assertEquals(2, i);

			// Retrieve 1 public event with iterator
				Iterator<IEvent> userPublicEventsStarting = userAlpha.getMyCalendarPublicEventsStartingFrom("CalendarAlpha", this.stringParseToDate("23.09.2011"));
				i = 0;
				while (userPublicEventsStarting.hasNext())
				{
					userPublicEventsStarting.next();
					i++;
				}
				assertEquals(1, i);

		// Retrieve via AppCalendar
			// Retrieve 1 public event with iterator
				Iterator<IEvent> AppCalendarPublicEventsStarting = app.getUsersCalendarPublicEvents("Alpha", "CalendarAlpha", this.stringParseToDate("23.09.2011"));
				i = 0;
				while (AppCalendarPublicEventsStarting.hasNext())
				{
					AppCalendarPublicEventsStarting.next();
					i++;
				}
				assertEquals(1, i);
	}

	@Test
	public void shouldRetrieveArrayListEventsNotEqualInputDateFromCalendar() throws UnknownCalendarException, UnknownUserException, AccessDeniedException, InvalidDateException, ParseException, UsernameAlreadyExistException, CalendarIsNotUniqueException
	{
		//PART 1:
		AppCalendar app=initCalendarAndUserAlpha();
		IUser userAlpha=app.loginUser("Alpha", "123");

		userAlpha.createNewCalendar("CalendarAlpha");
		Calendar calendarAlpha= userAlpha.getCalendar("CalendarAlpha");
		assertEquals(calendarAlpha.getName(), "CalendarAlpha");
		//PART 2:
		assertEquals(calendarAlpha.getOwner(), userAlpha);
		
		//PART 3:
		
		// Create calendar entries
		userAlpha.createPrivateEvent("CalendarAlpha", "My private one-day event", this.stringParseToDate("23.09.2011"), this.stringParseToDate("24.09.2011"));
		userAlpha.createPublicEvent("CalendarAlpha", "My public one-day event", this.stringParseToDate("23.09.2011"), this.stringParseToDate("24.09.2011"));
		userAlpha.createPrivateEvent("CalendarAlpha", "My private long event", this.stringParseToDate("22.09.2011"), this.stringParseToDate("26.09.2011"));
		userAlpha.createPublicEvent("CalendarAlpha", "My public night event", this.stringParseToDate("22.09.2011"), this.stringParseToDate("26.09.2011"));

		// Retrieve via user object
			// Retrieve all 4 events with ArrayList
				ArrayList<IEvent> userAllEventsDate = userAlpha.getMyCalendarAllEventsAtDate("CalendarAlpha", this.stringParseToDate("23.09.2011"));
				assertEquals(4, userAllEventsDate.size());

			// Retrieve 2 public events with ArrayList
				ArrayList<IEvent> userPublicEventsDate = userAlpha.getMyCalendarPublicEventsAtDate("CalendarAlpha", this.stringParseToDate("23.09.2011"));
				assertEquals(2, userPublicEventsDate.size());
				assertEquals("My public one-day event", userPublicEventsDate.get(0).getEventName());
				assertEquals("My public night event", userPublicEventsDate.get(1).getEventName());

		// Retrieve via AppCalendar
			// Retrieve 2 public events with ArrayList
				ArrayList<IEvent> AppCalendarPublicEventsDate = app.getUsersCalendarPublicEventsAtDate("Alpha", "CalendarAlpha", this.stringParseToDate("23.09.2011"));
				assertEquals(2, AppCalendarPublicEventsDate.size());
				assertEquals("My public one-day event", AppCalendarPublicEventsDate.get(0).getEventName());
				assertEquals("My public night event", AppCalendarPublicEventsDate.get(1).getEventName());

	}

	@Test
	public void shouldRetrieveIteratorEventsNotEqualInputDateFromCalendar() throws UnknownCalendarException, UnknownUserException, AccessDeniedException, InvalidDateException, ParseException, UsernameAlreadyExistException, CalendarIsNotUniqueException
	{
		//PART 1:
		AppCalendar app=initCalendarAndUserAlpha();
		IUser userAlpha=app.loginUser("Alpha", "123");

		userAlpha.createNewCalendar("CalendarAlpha");
		Calendar calendarAlpha= userAlpha.getCalendar("CalendarAlpha");
		assertEquals(calendarAlpha.getName(), "CalendarAlpha");
		//PART 2:
		assertEquals(calendarAlpha.getOwner(), userAlpha);
		
		//PART 3:
		
		// Create calendar entries
		userAlpha.createPrivateEvent("CalendarAlpha", "My private one-day event", this.stringParseToDate("24.09.2011"), this.stringParseToDate("24.09.2011"));
		userAlpha.createPublicEvent("CalendarAlpha", "My public one-day event", this.stringParseToDate("24.09.2011"), this.stringParseToDate("24.09.2011"));
		userAlpha.createPrivateEvent("CalendarAlpha", "My private past event", this.stringParseToDate("20.09.2011"), this.stringParseToDate("22.09.2011"));
		userAlpha.createPublicEvent("CalendarAlpha", "My public past event", this.stringParseToDate("20.09.2011"), this.stringParseToDate("22.09.2011"));

		// Retrieve via user object
			// Retrieve 2 events with iterator
				Iterator<IEvent> userAllEventsStarting = userAlpha.getMyCalendarAllEventsStartingFrom("CalendarAlpha", this.stringParseToDate("23.09.2011"));
				int i = 0;
				while (userAllEventsStarting.hasNext())
				{
					userAllEventsStarting.next();
					i++;
				}
				assertEquals(2, i);

			// Retrieve 1 public event with iterator
				Iterator<IEvent> userPublicEventsStarting = userAlpha.getMyCalendarPublicEventsStartingFrom("CalendarAlpha", this.stringParseToDate("23.09.2011"));
				i = 0;
				while (userPublicEventsStarting.hasNext())
				{
					userPublicEventsStarting.next();
					i++;
				}
				assertEquals(1, i);

		// Retrieve via AppCalendar
			// Retrieve 1 public event with iterator
				Iterator<IEvent> calendarPublicEventsStarting = app.getUsersCalendarPublicEvents("Alpha", "CalendarAlpha", this.stringParseToDate("23.09.2011"));
				i = 0;
				while (calendarPublicEventsStarting.hasNext())
				{
					calendarPublicEventsStarting.next();
					i++;
				}
				assertEquals(1, i);

		;
	}
}
