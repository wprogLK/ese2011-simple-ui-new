package controllers;

import play.*;
import play.mvc.*;
import play.data.validation.*;

import interfaces.IAppCalendar;
import interfaces.IEvent;
import interfaces.IUser;

import java.text.ParseException;
import java.util.*;

import jobs.Bootstrap;

import models.*;
import models.AppExceptions.*;

@With(Secure.class)
public class Application extends Controller
{
	public static AppCalendar appCalendar=Bootstrap.getAppCalendar();
	public static IUser appUser;

    public static void index() throws UnknownUserException
    {
    	String userName = Security.connected();
    	getCurrentUser();
    	if(userName!=null)
    	{
    		List<String> calendarNames = appCalendar.getAllCalendarsNamesFromUser(userName);
        	render(calendarNames, userName);
    	}
    	else
    	{
    		render(null, null);
    	}
    	
    }
    
    public static void mainMenuUser() throws UnknownUserException
    {
//    	String userName = Security.connected();
//    	System.out.println("USERNAME CONNECTED: " + userName);
//    	List<String> calendarNames = appCalendar.getAllCalendarsNamesFromUser(userName);
    	ArrayList<String> allNames=appCalendar.getAllUserNames();
    	System.out.println("USERNAMES:");
    	for(String str:allNames)
    	{
    		System.out.println(str);
    	}
    	render();
    }

    public static List<String> showAllUsers()
    {
    	return appCalendar.getAllUserNames();
    }
    
    public static void listEventsFromCalendar(@Required String calendarName) throws UnknownCalendarException, AccessDeniedException, ParseException
    {
    	System.out.println("The appUser is " + appUser);
    	ArrayList<IEvent> eventsList = appUser.getMyCalendarAllEventsAtDate(calendarName, Helper.parseStringToDate("01.01.1970"));
    	render(calendarName, eventsList);
    }

    public static IAppCalendar getApp()
    {
		return appCalendar;
    }

    public static void getUsersCalendarPublicEventsOverview(@Required String userName, @Required String calendarName, @Required String startDate) throws UnknownUserException, AccessDeniedException, UnknownCalendarException, ParseException, UsernameAlreadyExistException, CalendarIsNotUniqueException, InvalidDateException
    {
        if(validation.hasErrors())
        {
            flash.error("No field must remain empty!");
            index();
        }

    	ArrayList<IEvent> publicEvents = appCalendar.getUsersCalendarPublicEventsOverview(userName, calendarName, Helper.parseStringToDate(startDate));
    	render(userName, calendarName, publicEvents);
    }

    public static void createNewCalendar(@Required String calendarName) throws CalendarIsNotUniqueException, UnknownUserException
    {
    	
        if(validation.hasErrors())
        {
            flash.error("No field must remain empty!");
            index();
        }

    	appUser.createNewCalendar(calendarName);
        index();
    }
    
    public static void createNewUser(@Required String userName, @Required String password) throws UsernameAlreadyExistException, UnknownUserException
    {
    	System.out.println("HELLO USER: " + userName + " with PW " + password);
    	appCalendar.createUser(userName, password);
    	
    	//mainMenuUser();
    	index();
    }

    public static void deleteCalendar(@Required String calendarName) throws UnknownCalendarException, UnknownUserException
    {
        if(validation.hasErrors())
        {
            flash.error("No field must remain empty!");
            index();
        }

    	appUser.deleteCalendar(calendarName);
        index();
    }

    public static void mainMenuUser(String userName, String password) throws UnknownUserException, AccessDeniedException, UsernameAlreadyExistException, CalendarIsNotUniqueException, InvalidDateException, UnknownCalendarException, ParseException
    {
    	try
    	{
    		appCalendar.loginUser(userName, password);
    		getCurrentUser();
    	}
    	catch(UnknownUserException e)
    	{
    		error(e);
    	}
    	catch(AccessDeniedException e)
    	{
    		error(e);
    	}
    	render(userName);
    }
    
    private static void getCurrentUser()
    {
    	String userName = Security.connected();
    	
    	try {
			appUser=appCalendar.getCurrentUser(userName);
		} catch (UnknownUserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}