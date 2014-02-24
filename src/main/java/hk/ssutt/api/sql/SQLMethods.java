package hk.ssutt.api.sql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SQLMethods {
	private static Connection connection;
	private static SQLMethods sqlm;

	private SQLMethods() {
	}

	public static SQLMethods getInstance(Connection c) {
		if (sqlm == null) {
			sqlm = new SQLMethods();
		}

		setConnection(c);
		return sqlm;
	}

	private static void setConnection(Connection c) {
		connection = c;
	}

	//XML files queries
	//return path files per group on faculty (do we need Group entity?)
	public List<String> getAllGroupsOnFaculty(String faculty) {
		String[] params = {"PATH"};

		return processListOperation(String.format(Queries.allGroupsOnFaculty, faculty), params);
	}

	public String getEvenTT(String faculty, String group) {
		String[] params = {"PATH"};

		return processStringOperation(String.format(Queries.evenTT, faculty, group), params);
	}

	public String getOddTT(String faculty, String group) {
		String[] params = {"PATH"};

		return processStringOperation(String.format(Queries.oddTT, faculty, group), params);
	}

	//just ids
	public List<String> getGroupListOnFaculty(String faculty) {
		String[] params = {"GRP"};

		return processListOperation(String.format(Queries.groupListOnFaculty, faculty), params);
	}

	public List<String> getAllFacultiesIDs() {
		String[] params = {"ID"};

		return processListOperation(Queries.allFacultiesIDs, params);
	}

	public String getFacultyNameFromID(String facultyID) {
		String[] params = {"NAME"};
		return processStringOperation(String.format(Queries.facultyNameFromID, facultyID), params);
	}


	//protection queries
	public String getProtectionByGroupID(String faculty, String group) {
		String[] params = {"PROTECTED"};
		return processStringOperation(String.format(Queries.protectionByGroupID, faculty, group), params);
	}

	public List<String> getProtectedGroupsOnFaculty(String faculty) {
		String[] params = {"GRP"};
		return processListOperation(String.format(Queries.protectedGroupsOnFaculty, faculty), params);
	}

	public Map<String, List<String>> getAllProtectedGroups() {
		Map<String, List<String>> result = new HashMap<>();
		List<String> faculties = getAllFacultiesIDs();

		String[] params = {"GRP"};
		for (String faculty : faculties) {
			result.put(faculty, getProtectedGroupsOnFaculty(faculty));
		}
		return result;
	}

	//web addresses queries
	public String getFacultyWebAddress(String faculty) {
		String[] params = {"LINK"};
		return processStringOperation(String.format(Queries.facultyWebAddress, faculty), params).trim();
	}

	public String getGroupWebAddress(String faculty, String group) {
		String[] params = {"LINK"};
		StringBuilder sb = new StringBuilder();

		sb.append(processStringOperation(String.format(Queries.facultyWebAddress, faculty), params).trim());
		sb.append("/do/");

		params = new String[]{"ESC"};
		sb.append(processStringOperation(String.format(Queries.groupWebAddress, faculty, group), params));

		return sb.toString();
	}

	private String processStringOperation(String query, String[] params) {
		StringBuilder result = new StringBuilder();

		try (Statement stmt = connection.createStatement();){
			ResultSet rs = stmt.executeQuery(query);

			while (rs.next()) {
				for (String s : params) {
					result.append(rs.getString(s)).append(" ");
				}
			}

			stmt.close();
		} catch (SQLException e) {
			System.out.println(e.getClass().getName() + ": " + e.getMessage());
		}

		return result.toString();
	}

	private List<String> processListOperation(String query, String[] params) {
		List<String> result = new ArrayList<>();

		try (Statement stmt = connection.createStatement()){
			ResultSet rs = stmt.executeQuery(query);

			while (rs.next()) {
				StringBuilder str = new StringBuilder();

				for (String s : params) {
					str.append(rs.getString(s));

					if (params.length > 1) {
						str.append(" ");
					}
				}

				result.add(str.toString());
			}
		} catch (SQLException e) {
			System.out.println(e.getClass().getName() + ": " + e.getMessage());
		}

		return result;
	}
}
