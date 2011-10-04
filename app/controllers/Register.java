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

public class Register extends Controller
{
	
	public static IUser appUser;
	public static AppCalendar appCalendar=Bootstrap.getAppCalendar();
	
    public static void index() throws UnknownUserException
    {
    	render();
    }

    public static void createNewUser(@Required String userName, @Required String password) throws UsernameAlreadyExistException, UnknownUserException
    {
    	System.out.println("HELLO USER: s" + userName + " with PW " + password);
    	appCalendar.createUser(userName, password);

    	Application.index();
    }

  
    
    private static void getCurrentUser()
    {
    	String userName = Security.connected();
    	
    	try 
    	{
			appUser=appCalendar.getCurrentUser(userName);
		} catch (UnknownUserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}