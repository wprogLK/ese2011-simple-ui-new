package jExampleTest;
/**
 * Calendar framework
 */

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import play.test.UnitTest;

/**
 * @author Lukas Keller
 * @author Renato Corti
 *
 */

public class TestTemplate extends UnitTest
{
	protected Date stringParseToDate(String strDate) throws ParseException
	{
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

		return sdf.parse(strDate);
	}
}
