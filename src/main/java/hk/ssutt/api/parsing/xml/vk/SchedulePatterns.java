package hk.ssutt.api.parsing.xml.vk;

/**
 * Created by Севак on 24.02.14.
 */
public abstract class SchedulePatterns {
	public static final String numeratorStart = "чис.";
	public static final String denominatorStart = "знам.";

	public static final String dateFormat =
			"|%s || %d - %d || %d - %d\n"
					+ "|-\n"
					+ "|%s || %d - %d || %d - %d\n"
					+ "|-\n"
					+ "|%s || %d - %d || %d - %d\n"
					+ "|-\n"
					+ "|%s || %d - %d || %d - %d\n";

	public static final String line = " || %s || %s || %s || %s || %s || %s \n";

	public static final String tableFormat =
			"|День/Время || пн || вт || ср || чт || пт || суб\n"
					+ "|-\n"
					+ "|8.20 - 9.50" + line
					+ "|-\n"
					+ "|10.00 - 11.35" + line
					+ "|-\n"
					+ "|12.05 - 13.40" + line
					+ "|-\n"
					+ "|13.50 - 15.25" + line
					+ "|-\n"
					+ "|15.35 - 17.10" + line
					+ "|-\n"
					+ "|17.20 - 18.40" + line
					+ "|-\n"
					+ "|18.45 - 20:10" + line
					+ "|-\n"
					+ "|20:10 - 21:30" + line;

	public static final String scheduleFormat =
			"ЧИСЛИТЕЛЬ\n"
					+ "{|\n"
					+ "|-\n"
					+ "%s" // дата
					+ "|}\n"
					+ "{|\n"
					+ "|-\n"
					+ "%s\n" // числитель
					+ "|}\n"
					+ "ЗНАМЕНАТЕЛЬ\n"
					+ "{|\n"
					+ "|-\n"
					+ "%s\n" // дата
					+ "|}\n"
					+ "{|\n"
					+ "|-\n"
					+ "%s\n" // знаменатель
					+ "|}\n";
}
