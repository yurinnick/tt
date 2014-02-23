package hk.ssutt.api.xml.vk;

import javax.naming.OperationNotSupportedException;

/**
 * Created by Севак on 22.02.14.
 */

public class VKWikiMarkup {
	private static final int ROW = 8;
	private static final int COLUMN = 6;

	private static final String numeratorStart = "чис.";
	private static final String denominatorStart = "знам.";

	private static final String dateFormat =
			"|%s || %d - %d || %d - %d\n"
					+ "|-\n"
					+ "|%s || %d - %d || %d - %d\n"
					+ "|-\n"
					+ "|%s || %d - %d || %d - %d\n"
					+ "|-\n"
					+ "|%s || %d - %d || %d - %d\n";

	private static final String line = " || %s || %s || %s || %s || %s || %s \n";

	private static final String tableFormat =
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

	private static final String scheduleFormat =
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

	private String[][] table;
	private String[][] numerator = getEmptyTable();
	private String[][] denominator = getEmptyTable();
	private Object[] numeratorDate;
	private Object[] denominatorDate;
	private boolean isSeparated = false;

	public VKWikiMarkup(String[][] table, Object[] numeratorData, Object[] denominatorData) {
		this.table = table;
		this.numeratorDate = numeratorData;
		this.denominatorDate = denominatorData;
	}


	private static String[][] getEmptyTable() {
		String[][] table = new String[ROW][COLUMN];

		for (int i = 0; i < table.length; ++i) {
			for (int j = 0; j < table[i].length; ++j) {
				table[i][j] = "";
			}
		}

		return table;
	}

	//разбить расписание на числитель/знаменатель
	private void separateSchedule() {
		if (isSeparated) {
			return;
		}

		boolean isNumerator = false;
		boolean isDenominator = false;

		for (int i = 0; i < ROW; ++i) {
			for (int j = 0; j < COLUMN; ++j) {
				String s = table[i][j];
				isNumerator = isDenominator = false;

				if (s.contains(numeratorStart)) {
					isNumerator = true;
				}

				if (s.contains(denominatorStart)) {
					isDenominator = true;
				}

				if (!s.isEmpty()) {
					if (isNumerator) {
						if (isDenominator) {
							int denomIndex = s.indexOf(denominatorStart);
							numerator[i][j] = s.substring(numeratorStart.length(), denomIndex);
							denominator[i][j] = s.substring(denomIndex + denominatorStart.length(), s.length());
						} else {
							numerator[i][j] = s.substring(numeratorStart.length(), s.length());
						}
					} else {
						if (isDenominator) {
							denominator[i][j] = s.substring(denominatorStart.length(), s.length());
						} else {
							numerator[i][j] = s;
							denominator[i][j] = s;
						}
					}
				}
			}
		}

		isSeparated = true;
	}

	private static String[] getTableStrings(String[][] table) {
		String[] result = new String[ROW * COLUMN];
		int index = 0;

		// перегоняем двумерный массив в одномерный чтобы вызвать String.format()
		for (String[] rows : table) {
			for (String cell : rows) {
				result[index++] = cell;
			}
		}

		return result;
	}

	public static String getFormattedTable(String[][] table) {
		return String.format(VKWikiMarkup.tableFormat, getTableStrings(table));
	}

	public String getSchedule() throws OperationNotSupportedException {
		if (!isSeparated) {
			separateSchedule();
			isSeparated = true;
		}

		String numeratorString = getFormattedTable(numerator); // таблица раписания - числитель
		String denominatorString = getFormattedTable(denominator); // таблица расписания - знаменатель
		String numeratorDateString = String.format(dateFormat, numeratorDate); // дни неделеи - числитель
		String denominatorDateString = String.format(dateFormat, denominatorDate); // дни недели - знаманатель

		return String.format(scheduleFormat, numeratorDateString, numeratorString, denominatorDateString, denominatorString);
	}

}