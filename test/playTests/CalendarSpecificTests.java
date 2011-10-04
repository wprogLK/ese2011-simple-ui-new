package playTests;
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
//TODO: FIX IT FOR PLAY TEST!
@RunWith(JExample.class)
public class CalendarSpecificTests extends TestTemplate
{
	private IUser userAlpha;
	private Calendar calendarAlpha;

	@Test
	public AppCalendar oneUserTest() throws UsernameAlreadyExistException, UnknownUserException, AccessDeniedException
	{
		AppCalendar AppCalendar = new AppCalendar();
		AppCalendar.createUser("Alpha", "123");
		this.userAlpha= AppCalendar.loginUser("Alpha", "123");
		return AppCalendar;
	}

	@Given("oneUserTest")
	public AppCalendar userAlphaNameShouldBeUserAlpha(AppCalendar AppCalendar)
	{
		assertEquals(this.userAlpha.getName(), "Alpha");
		return AppCalendar;
	}

	@Given("userAlphaNameShouldBeUserAlpha")
	public AppCalendar userAlphaShouldHaveNoCalendars(AppCalendar AppCalendar)
	{
		assertTrue(this.userAlpha.hasNoCalendar());
		return AppCalendar;
	}

	@Given("userAlphaShouldHaveNoCalendars")
	public AppCalendar alphaCalendarNameShouldBeCalendarAlpha(AppCalendar AppCalendar) throws UnknownCalendarException, CalendarIsNotUniqueException
	{
		this.userAlpha.createNewCalendar("CalendarAlpha");
		this.calendarAlpha = this.userAlpha.getCalendar("CalendarAlpha");
		assertEquals(calendarAlpha.getName(), "CalendarAlpha");
		return AppCalendar;
	}

	@Given("alphaCalendarNameShouldBeCalendarAlpha")
	public AppCalendar alphaCalendarOwnerShouldBeUserAlpha(AppCalendar AppCalendar)
	{
		assertEquals(calendarAlpha.getOwner(), this.userAlpha);
		return AppCalendar;
	}

	@Given("alphaCalendarOwnerShouldBeUserAlpha")
	public AppCalendar alphaCalendarShouldBeEmpty(AppCalendar AppCalendar) throws AccessDeniedException, UnknownCalendarException, ParseException
	{
		ArrayList<IEvent> allEvents = this.userAlpha.getMyCalendarAllEventsAtDate("CalendarAlpha", this.stringParseToDate("1.1.1970"));
		assertTrue(allEvents.isEmpty());
		return AppCalendar;
	}

	@Given("alphaCalendarNameShouldBeCalendarAlpha")
	public AppCalendar userAlphaShouldHaveOneCalendar(AppCalendar AppCalendar)
	{
		assertFalse(this.userAlpha.hasNoCalendar());
		return AppCalendar;
	}

	@Given("alphaCalendarShouldBeEmpty")
	public AppCalendar eventNameShouldBeParty(AppCalendar AppCalendar) throws AccessDeniedException, UnknownEventException, InvalidDateException, UnknownCalendarException, ParseException
	{

		this.userAlpha.createPrivateEvent("CalendarAlpha", "Party", this.stringParseToDate("22.09.2011"),  this.stringParseToDate("29.09.2011"));

		assertFalse(calendarAlpha.getAllEventsAtDate(this.stringParseToDate("1.1.1970")).isEmpty());
		Event eventParty = calendarAlpha.getEvent("Party", this.stringParseToDate("22.09.2011"));
		assertEquals(eventParty.getEventName(), "Party");
		return AppCalendar;
	}

	@Given("eventNameShouldBeParty")
	public AppCalendar eventStartDateShouldBe22_09_2011(AppCalendar AppCalendar) throws UnknownEventException, ParseException
	{
		Event eventParty = calendarAlpha.getEvent("Party", this.stringParseToDate("22.09.2011"));
		assertEquals(eventParty.getStartDate(), this.stringParseToDate("22.09.2011"));
		return AppCalendar;
	}

	@Given("eventStartDateShouldBe22_09_2011")
	public AppCalendar eventEndDateShouldBe29_09_2011(AppCalendar AppCalendar) throws UnknownEventException, ParseException
	{
		Event eventParty = calendarAlpha.getEvent("Party", this.stringParseToDate("22.09.2011"));
		assertEquals(eventParty.getEndDate(), this.stringParseToDate("29.09.2011"));
		return AppCalendar;
	}

	@Given("alphaCalendarShouldBeEmpty")
	public AppCalendar startDateShouldNotBeAfterEndDate(AppCalendar AppCalendar) throws AccessDeniedException, UnknownCalendarException, ParseException
	{
		try
		{
			this.userAlpha.createPrivateEvent("CalendarAlpha", "BackToTheFutureEvent", this.stringParseToDate("23.09.2011"), this.stringParseToDate("22.09.2011"));
			fail("InvalidDateException expected!");
		}
		catch(InvalidDateException e)
		{
			assertNotNull(e);
		}
		assertTrue(calendarAlpha.getAllEventsAtDate(this.stringParseToDate("1.1.1970")).isEmpty());
		return AppCalendar;
	}

	@Given("userAlphaShouldHaveOneCalendar")
	public AppCalendar deleteEventsTest(AppCalendar AppCalendar) throws AccessDeniedException, UnknownEventException, InvalidDateException, UnknownCalendarException, ParseException
	{
		this.userAlpha.createPrivateEvent("CalendarAlpha", "My Event", this.stringParseToDate("23.09.2011"), this.stringParseToDate("23.09.2011"));
		this.userAlpha.createPublicEvent("CalendarAlpha", "Our Event", this.stringParseToDate("23.09.2011"), this.stringParseToDate("23.09.2011"));

		calendarAlpha.deleteEvent("My Event", this.stringParseToDate("23.09.2011"));
		calendarAlpha.deleteEvent("Our Event", this.stringParseToDate("23.09.2011"));

		// alphaCalendarShouldBeEmpty
		assertTrue(calendarAlpha.getAllEventsAtDate(this.stringParseToDate("1.1.1970")).isEmpty());

		return AppCalendar;
	}

	@Given("alphaCalendarOwnerShouldBeUserAlpha")
	public AppCalendar caledarNameShouldBeUnique(AppCalendar AppCalendar)
	{
		try
		{
			this.userAlpha.createNewCalendar("CalendarAlpha");
			fail("CalendarIsNotUniqueException expected!");
		}
		catch (CalendarIsNotUniqueException e)
		{
			assertNotNull(e);
		}
		return AppCalendar;
	}

	@Given("userAlphaShouldHaveNoCalendars")
	public AppCalendar shouldGiveCalendarListOfUserAlphaTest(AppCalendar AppCalendar) throws AccessDeniedException, InvalidDateException, UnknownUserException, UnknownCalendarException, CalendarIsNotUniqueException, ParseException
	{
		this.userAlpha.createNewCalendar("New calendar");
		this.userAlpha.createPrivateEvent("New calendar", "Private Event", this.stringParseToDate("21.9.2011"), this.stringParseToDate("21.9.2011"));
		this.userAlpha.createPublicEvent("New calendar", "Public Event", this.stringParseToDate("21.9.2011"), this.stringParseToDate("21.9.2011"));

		this.userAlpha.createNewCalendar("Second calendar");
		this.userAlpha.createPrivateEvent("Second calendar", "Private Event", this.stringParseToDate("22.9.2011"), this.stringParseToDate("22.9.2011"));
		this.userAlpha.createPublicEvent("Second calendar", "Public Event", this.stringParseToDate("22.9.2011"), this.stringParseToDate("22.9.2011"));

		this.userAlpha.createNewCalendar("Other calendar");
		this.userAlpha.createPrivateEvent("Other calendar", "Private Event", this.stringParseToDate("23.9.2011"), this.stringParseToDate("23.9.2011"));
		this.userAlpha.createPublicEvent("Other calendar", "Public Event", this.stringParseToDate("23.9.2011"), this.stringParseToDate("23.9.2011"));

		ArrayList<String> alphasCalendarListViaUser = userAlpha.getAllMyCalendarNames();
		ArrayList<String> alphasCalendarListViaAppCalendar = AppCalendar.getAllCalendarsNamesFromUser("Alpha");
		assertEquals(alphasCalendarListViaUser, alphasCalendarListViaAppCalendar);

		assertEquals(3, alphasCalendarListViaUser.size());
		assertEquals(3, alphasCalendarListViaAppCalendar.size());
		assertEquals("New calendar", alphasCalendarListViaUser.get(0));
		assertEquals("New calendar", alphasCalendarListViaAppCalendar.get(0));
		assertEquals("Second calendar", alphasCalendarListViaUser.get(1));
		assertEquals("Second calendar", alphasCalendarListViaAppCalendar.get(1));
		assertEquals("Other calendar", alphasCalendarListViaUser.get(2));
		assertEquals("Other calendar", alphasCalendarListViaAppCalendar.get(2));

		return AppCalendar;
	}

	@Given("alphaCalendarOwnerShouldBeUserAlpha")
	public AppCalendar shouldRetrieveArrayListEventsContainingInputDateFromCalendar(AppCalendar AppCalendar) throws UnknownCalendarException, UnknownUserException, AccessDeniedException, InvalidDateException, ParseException
	{
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
				ArrayList<IEvent> AppCalendarPublicEventsDate = AppCalendar.getUsersCalendarPublicEventsFromStartDate("Alpha", "CalendarAlpha", this.stringParseToDate("23.09.2011"));
				assertEquals(2, AppCalendarPublicEventsDate.size());
				assertEquals("My public one-day event", AppCalendarPublicEventsDate.get(0).getEventName());
				assertEquals("My public night event", AppCalendarPublicEventsDate.get(1).getEventName());

		return AppCalendar;
	}

	@Given("alphaCalendarOwnerShouldBeUserAlpha")
	public AppCalendar shouldRetrieveIteratorEventsContainingInputDateFromCalendar(AppCalendar AppCalendar) throws UnknownCalendarException, UnknownUserException, AccessDeniedException, InvalidDateException, ParseException
	{
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
				Iterator<IEvent> AppCalendarPublicEventsStarting = AppCalendar.getUsersCalendarPublicEvents("Alpha", "CalendarAlpha", this.stringParseToDate("23.09.2011"));
				i = 0;
				while (AppCalendarPublicEventsStarting.hasNext())
				{
					AppCalendarPublicEventsStarting.next();
					i++;
				}
				assertEquals(1, i);

		return AppCalendar;
	}

	@Given("alphaCalendarOwnerShouldBeUserAlpha")
	public AppCalendar shouldRetrieveArrayListEventsNotEqualInputDateFromCalendar(AppCalendar AppCalendar) throws UnknownCalendarException, UnknownUserException, AccessDeniedException, InvalidDateException, ParseException
	{
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
				ArrayList<IEvent> AppCalendarPublicEventsDate = AppCalendar.getUsersCalendarPublicEventsFromStartDate("Alpha", "CalendarAlpha", this.stringParseToDate("23.09.2011"));
				assertEquals(2, AppCalendarPublicEventsDate.size());
				assertEquals("My public one-day event", AppCalendarPublicEventsDate.get(0).getEventName());
				assertEquals("My public night event", AppCalendarPublicEventsDate.get(1).getEventName());

		return AppCalendar;
	}

	@Given("alphaCalendarOwnerShouldBeUserAlpha")
	public AppCalendar shouldRetrieveIteratorEventsNotEqualInputDateFromCalendar(AppCalendar AppCalendar) throws UnknownCalendarException, UnknownUserException, AccessDeniedException, InvalidDateException, ParseException
	{
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
				Iterator<IEvent> calendarPublicEventsStarting = AppCalendar.getUsersCalendarPublicEvents("Alpha", "CalendarAlpha", this.stringParseToDate("23.09.2011"));
				i = 0;
				while (calendarPublicEventsStarting.hasNext())
				{
					calendarPublicEventsStarting.next();
					i++;
				}
				assertEquals(1, i);

		return AppCalendar;
	}
}
