package jExampleTest;
/**
 * Calendar framework
 */

import interfaces.*;

import java.text.ParseException;
import java.util.ArrayList;
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

@RunWith(JExample.class)
public class EventsTest extends TestTemplate
{
	private IUser userAlpha;

	@Test
	public AppCalendar eventTest() throws UsernameAlreadyExistException, UnknownUserException, AccessDeniedException
	{
		AppCalendar app = new AppCalendar();
		app.createUser("Alpha", "123");
		this.userAlpha= app.loginUser("Alpha", "123");
		return app;
	}

	@Given("eventTest")
	public AppCalendar newNameOfEventShouldBeAreWeDead(AppCalendar app) throws AccessDeniedException, InvalidDateException, UnknownCalendarException, UnknownEventException, ParseException, CalendarIsNotUniqueException
	{
		this.userAlpha.createNewCalendar("MyCalendar");
		this.userAlpha.createPrivateEvent("MyCalendar", "What happen after year 1999?", this.stringParseToDate("01.01.2000"), this.stringParseToDate("2.01.2000"));

		Calendar myCalendar = this.userAlpha.getCalendar("MyCalendar");

		this.userAlpha.editEventName("MyCalendar", "What happen after year 1999?", this.stringParseToDate("01.01.2000"), "Are we dead?");

		Event milleniumEvent = myCalendar.getEvent("Are we dead?", this.stringParseToDate("01.01.2000"));

		assertEquals(milleniumEvent.getEventName(), "Are we dead?");

		return app;
	}

	@Given("eventTest")
	public AppCalendar newStartDateOfEventShouldBe01_08_2011(AppCalendar app) throws AccessDeniedException, InvalidDateException, UnknownCalendarException, UnknownEventException, ParseException, CalendarIsNotUniqueException
	{
		this.userAlpha.createNewCalendar("MyCalendar");
		this.userAlpha.createPrivateEvent("MyCalendar", "NationalDay", this.stringParseToDate("01.07.2011"), this.stringParseToDate("01.08.2011"));

		Calendar myCalendar = this.userAlpha.getCalendar("MyCalendar");

		this.userAlpha.editEventStartDate("MyCalendar", "NationalDay", this.stringParseToDate("01.07.2011"), this.stringParseToDate("01.08.2011"));

		Event nationalDayEvent = myCalendar.getEvent("NationalDay", this.stringParseToDate("01.08.2011"));

		assertEquals(nationalDayEvent.getStartDate(), this.stringParseToDate("01.08.2011"));

		return app;
	}

	@Given("eventTest")
	public AppCalendar newEndDateOfEventShouldBe01_08_2011(AppCalendar app) throws AccessDeniedException, InvalidDateException, UnknownCalendarException, UnknownEventException, ParseException, CalendarIsNotUniqueException
	{
		this.userAlpha.createNewCalendar("MyCalendar");
		this.userAlpha.createPrivateEvent("MyCalendar", "Holiday", this.stringParseToDate("01.07.2011"), this.stringParseToDate("01.08.2011"));

		Calendar myCalendar = this.userAlpha.getCalendar("MyCalendar");

		this.userAlpha.editEventEndDate("MyCalendar", "Holiday", this.stringParseToDate("01.07.2011"), this.stringParseToDate("01.09.2011"));

		Event holidayEvent = myCalendar.getEvent("Holiday", this.stringParseToDate("01.07.2011"));

		assertEquals(holidayEvent.getEndDate(), this.stringParseToDate("01.09.2011"));

		return app;
	}

	@Given("eventTest")
	public AppCalendar newVisibilityShouldBePublic(AppCalendar app) throws AccessDeniedException, InvalidDateException, UnknownCalendarException, UnknownEventException, ParseException, CalendarIsNotUniqueException
	{
		this.userAlpha.createNewCalendar("MyCalendar");
		this.userAlpha.createPrivateEvent("MyCalendar", "Public Event", this.stringParseToDate("01.07.2011"), this.stringParseToDate("01.08.2011"));

		Calendar myCalendar = this.userAlpha.getCalendar("MyCalendar");

		this.userAlpha.editEventStateToPublic("MyCalendar", "Public Event", this.stringParseToDate("01.07.2011"));

		Event publicEvent = myCalendar.getEvent("Public Event", this.stringParseToDate("01.07.2011"));

		assertTrue(publicEvent.isPublic());

		return app;
	}

	@Given("eventTest")
	public AppCalendar newVisibilityShouldBePrivate(AppCalendar app) throws AccessDeniedException, InvalidDateException, UnknownCalendarException, UnknownEventException, ParseException, CalendarIsNotUniqueException
	{
		this.userAlpha.createNewCalendar("MyCalendar");
		this.userAlpha.createPublicEvent("MyCalendar", "Secret mission", this.stringParseToDate("01.07.2011"), this.stringParseToDate("01.08.2011"));

		Calendar myCalendar = this.userAlpha.getCalendar("MyCalendar");

		this.userAlpha.editEventStateToPrivate("MyCalendar", "Secret mission", this.stringParseToDate("01.07.2011"));

		Event missionEvent = myCalendar.getEvent("Secret mission", this.stringParseToDate("01.07.2011"));

		assertTrue(missionEvent.isPrivate());

		return app;
	}

	@Given("eventTest")
	public AppCalendar userShouldDeleteEvent(AppCalendar app) throws AccessDeniedException, InvalidDateException, UnknownCalendarException, UnknownEventException, ParseException, CalendarIsNotUniqueException
	{
		this.userAlpha.createNewCalendar("MyCalendar");
		this.userAlpha.createPublicEvent("MyCalendar", "Void entry", this.stringParseToDate("01.07.2011"), this.stringParseToDate("01.08.2011"));

		this.userAlpha.deleteEvent("MyCalendar", "Void entry", this.stringParseToDate("01.07.2011"));

		assertEquals(new ArrayList<IEvent>(), this.userAlpha.getMyCalendarAllEventsAtDate("MyCalendar", this.stringParseToDate("01.07.2011")));

		return app;
	}
}
