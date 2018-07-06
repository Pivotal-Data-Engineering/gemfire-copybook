import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Handles parsing of locator information
 *
 * @author rmay
 *
 */
public class LocatorHelper {
	private static final Logger _log = LogManager.getLogger(LocatorHelper.class);

	private static Pattern LOCATOR_PATTERN = Pattern.compile("(\\S+)\\[(\\d{1,5})\\]");

	private String host;
	private int port;

	public LocatorHelper(String host, int port) {
		this.host = host;
		this.port = port;
	}

	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}

	public static List<LocatorHelper> parseLocatorList(String in) {
		String[] locatorStrings = in.split(",");
		ArrayList<LocatorHelper> result = new ArrayList<LocatorHelper>(locatorStrings.length);

		for (String l : locatorStrings) {
			result.add(parseLocatorString(l));
		}

		return result;
	}

	public static LocatorHelper parseLocatorString(String in) {
		String host;
		int port;

		Matcher m = LOCATOR_PATTERN.matcher(in);
		if (!m.matches()) {
			_log.error("'{}' does not have the correct format for a locator.", in);
			throw new RuntimeException(
					String.format("'%s' does not have the correct format for a locator String.", in));
		} else {
			host = m.group(1);
			try {
				port = Integer.parseInt(m.group(2));
			} catch (NumberFormatException x) {
				_log.error(
						"'{}' does not have the correct format for a locator String. The portion in square brackets could not be parsed as an integer.",
						in);
				throw new RuntimeException(String.format(
						"'%s' does not have the correct format for a locator String. The portion in square brackets could not be parsed as an integer.",
						in));
			}
		}

		return new LocatorHelper(host, port);
	}
}
