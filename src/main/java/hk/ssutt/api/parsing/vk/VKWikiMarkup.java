package hk.ssutt.api.parsing.vk;

import javax.naming.OperationNotSupportedException;

import static hk.ssutt.api.parsing.vk.SchedulePatterns.*;

/**
 * Created by Севак on 22.02.14.
 */

public class VKWikiMarkup {
	private static final int ROW = 8;
	private static final int COLUMN = 6;

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
		return String.format(SchedulePatterns.tableFormat, getTableStrings(table));
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