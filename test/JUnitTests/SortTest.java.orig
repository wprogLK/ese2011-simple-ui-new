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

@RunWith(JExample.class)
public class SortTest extends TestTemplate
{
	private IUser userAlpha;

	@Test
	public AppCalendar sortTest() throws UsernameAlreadyExistException, UnknownUserException, AccessDeniedException
	{
		AppCalendar app = new AppCalendar();
		app.createUser("Alpha", "123");
		this.userAlpha = app.loginUser("Alpha", "123");
		return app;
	}

	@Given("sortTest")
	public AppCalendar sortCheck(AppCalendar app) throws AccessDeniedException, InvalidDateException, UnknownCalendarException, UnknownEventException, ParseException, CalendarIsNotUniqueException
	{
		this.userAlpha.createNewCalendar("MyCalendar");
		this.userAlpha.createPrivateEvent("MyCalendar", "Event 1", this.stringParseToDate("01.01.2000"), this.stringParseToDate("2.01.2000"));
		this.userAlpha.createPrivateEvent("MyCalendar", "Event 3", this.stringParseToDate("02.08.2011"), this.stringParseToDate("3.09.2011"));
		this.userAlpha.createPrivateEvent("MyCalendar", "Event 2", this.stringParseToDate("08.06.2006"), this.stringParseToDate("10.06.2006"));

		Calendar myCalendar = this.userAlpha.getCalendar("MyCalendar");

		Event e1 = myCalendar.getEvent("Event 1", this.stringParseToDate("01.01.2000"));
		Event e3 = myCalendar.getEvent("Event 3", this.stringParseToDate("02.08.2011"));
		Event e2 = myCalendar.getEvent("Event 2", this.stringParseToDate("08.06.2006"));

		ArrayList<IEvent> checkSortedEvents = new ArrayList<IEvent>();
		checkSortedEvents.add(e1);
		checkSortedEvents.add(e2);
		checkSortedEvents.add(e3);

		ArrayList<IEvent> sortedEvents = this.userAlpha.getMyCalendarAllEventsAtDate("MyCalendar", this.stringParseToDate("01.01.1990"));

		assertEquals(sortedEvents.size(),checkSortedEvents.size());

		for(int i = 0; i < sortedEvents.size(); i++)
		{
			IEvent eventCheck = checkSortedEvents.get(i);
			IEvent eventSorted = sortedEvents.get(i);

			assertEquals(eventCheck, eventSorted);
		}
		return app;
	}
}
