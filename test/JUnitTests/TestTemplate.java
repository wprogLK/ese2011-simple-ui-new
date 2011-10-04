/**
 * Calendar framework
 */
package JUnitTests;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Lukas Keller
 * @author Renato Corti
 *
 */

public class TestTemplate
{
	protected Date stringParseToDate(String strDate) throws ParseException
	{
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

		return sdf.parse(strDate);
	}
}
