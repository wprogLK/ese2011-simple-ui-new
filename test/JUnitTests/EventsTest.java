package JUnitTests;

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

public class EventsTest extends TestTemplate
{

	@Test
	public void newNameOfEventShouldBeAreWeDead() throws AccessDeniedException, InvalidDateException, UnknownCalendarException, UnknownEventException, ParseException, CalendarIsNotUniqueException, UsernameAlreadyExistException, UnknownUserException
	{
		AppCalendar app=this.init();
		
		IUser userAlpha= app.loginUser("Alpha", "123");		
		
		userAlpha.createNewCalendar("MyCalendar");
		userAlpha.createPrivateEvent("MyCalendar", "What happen after year 1999?", this.stringParseToDate("01.01.2000"), this.stringParseToDate("2.01.2000"));

		Calendar myCalendar = userAlpha.getCalendar("MyCalendar");

		userAlpha.editEventName("MyCalendar", "What happen after year 1999?", this.stringParseToDate("01.01.2000"), "Are we dead?");

		Event milleniumEvent = myCalendar.getEvent("Are we dead?", this.stringParseToDate("01.01.2000"));

		assertEquals(milleniumEvent.getEventName(), "Are we dead?");

	}

	@Test
	public void newStartDateOfEventShouldBe01_08_2011() throws AccessDeniedException, InvalidDateException, UnknownCalendarException, UnknownEventException, ParseException, CalendarIsNotUniqueException, UsernameAlreadyExistException, UnknownUserException
	{
		AppCalendar app=this.init();
		
		IUser userAlpha= app.loginUser("Alpha", "123");		
		
		userAlpha.createNewCalendar("MyCalendar");
		userAlpha.createPrivateEvent("MyCalendar", "NationalDay", this.stringParseToDate("01.07.2011"), this.stringParseToDate("01.08.2011"));

		Calendar myCalendar = userAlpha.getCalendar("MyCalendar");

		userAlpha.editEventStartDate("MyCalendar", "NationalDay", this.stringParseToDate("01.07.2011"), this.stringParseToDate("01.08.2011"));

		Event nationalDayEvent = myCalendar.getEvent("NationalDay", this.stringParseToDate("01.08.2011"));

		assertEquals(nationalDayEvent.getStartDate(), this.stringParseToDate("01.08.2011"));

	}

	@Test
	public void newEndDateOfEventShouldBe01_08_2011() throws AccessDeniedException, InvalidDateException, UnknownCalendarException, UnknownEventException, ParseException, CalendarIsNotUniqueException, UsernameAlreadyExistException, UnknownUserException
	{
		AppCalendar app=this.init();
		
		IUser userAlpha= app.loginUser("Alpha", "123");		
		
		userAlpha.createNewCalendar("MyCalendar");
		userAlpha.createPrivateEvent("MyCalendar", "Holiday", this.stringParseToDate("01.07.2011"), this.stringParseToDate("01.08.2011"));

		Calendar myCalendar = userAlpha.getCalendar("MyCalendar");

		userAlpha.editEventEndDate("MyCalendar", "Holiday", this.stringParseToDate("01.07.2011"), this.stringParseToDate("01.09.2011"));

		Event holidayEvent = myCalendar.getEvent("Holiday", this.stringParseToDate("01.07.2011"));

		assertEquals(holidayEvent.getEndDate(), this.stringParseToDate("01.09.2011"));

	}

	@Test
	public void newVisibilityShouldBePublic() throws AccessDeniedException, InvalidDateException, UnknownCalendarException, UnknownEventException, ParseException, CalendarIsNotUniqueException, UsernameAlreadyExistException, UnknownUserException
	{
		AppCalendar app=this.init();
		
		IUser userAlpha= app.loginUser("Alpha", "123");		
		
		userAlpha.createNewCalendar("MyCalendar");
		userAlpha.createPrivateEvent("MyCalendar", "Public Event", this.stringParseToDate("01.07.2011"), this.stringParseToDate("01.08.2011"));

		Calendar myCalendar = userAlpha.getCalendar("MyCalendar");

		userAlpha.editEventStateToPublic("MyCalendar", "Public Event", this.stringParseToDate("01.07.2011"));

		Event publicEvent = myCalendar.getEvent("Public Event", this.stringParseToDate("01.07.2011"));

		assertTrue(publicEvent.isPublic());
	}

	@Test
	public void newVisibilityShouldBePrivate() throws AccessDeniedException, InvalidDateException, UnknownCalendarException, UnknownEventException, ParseException, CalendarIsNotUniqueException, UsernameAlreadyExistException, UnknownUserException
	{
		AppCalendar app=this.init();
		
		IUser userAlpha= app.loginUser("Alpha", "123");		
		
		userAlpha.createNewCalendar("MyCalendar");
		userAlpha.createPublicEvent("MyCalendar", "Secret mission", this.stringParseToDate("01.07.2011"), this.stringParseToDate("01.08.2011"));

		Calendar myCalendar = userAlpha.getCalendar("MyCalendar");

		userAlpha.editEventStateToPrivate("MyCalendar", "Secret mission", this.stringParseToDate("01.07.2011"));

		Event missionEvent = myCalendar.getEvent("Secret mission", this.stringParseToDate("01.07.2011"));

		assertTrue(missionEvent.isPrivate());
	}

	@Test
	public void userShouldDeleteEvent() throws AccessDeniedException, InvalidDateException, UnknownCalendarException, UnknownEventException, ParseException, CalendarIsNotUniqueException, UsernameAlreadyExistException, UnknownUserException
	{
		AppCalendar app=this.init();
		
		IUser userAlpha= app.loginUser("Alpha", "123");		
		
		userAlpha.createNewCalendar("MyCalendar");
		userAlpha.createPublicEvent("MyCalendar", "Void entry", this.stringParseToDate("01.07.2011"), this.stringParseToDate("01.08.2011"));

		userAlpha.deleteEvent("MyCalendar", "Void entry", this.stringParseToDate("01.07.2011"));

		assertEquals(new ArrayList<IEvent>(), userAlpha.getMyCalendarAllEventsAtDate("MyCalendar", this.stringParseToDate("01.07.2011")));
	}
}
