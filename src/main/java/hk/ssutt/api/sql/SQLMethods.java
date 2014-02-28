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
	public List<String> getAllGroupsFilesOnFaculty(String faculty) {
		String[] params = {"PATH"};

		return pullListOperation(String.format(Queries.allGroupsOnFaculty, faculty), params);
	}

	public String getEvenTT(String faculty, String group) {
		String[] params = {"PATH"};

		return pullStringOperation(String.format(Queries.evenTT, faculty, group), params);
	}

	public String getOddTT(String faculty, String group) {
		String[] params = {"PATH"};

		return pullStringOperation(String.format(Queries.oddTT, faculty, group), params);
	}

	//just ids
	public List<String> getGroupIDListOnFaculty(String faculty) {
		String[] params = {"GRP"};

		return pullListOperation(String.format(Queries.groupListOnFaculty, faculty), params);
	}

	public List<String> getAllFacultiesIDs() {
		String[] params = {"ID"};

		return pullListOperation(Queries.allFacultiesIDs, params);
	}

	public String getFacultyNameFromID(String facultyID) {
		String[] params = {"NAME"};
		return pullStringOperation(String.format(Queries.facultyNameFromID, facultyID), params);
	}

	//web addresses queries
	public String getFacultyWebAddress(String faculty) {
		String[] params = {"LINK"};
		return pullStringOperation(String.format(Queries.facultyWebAddress, faculty), params).trim();
	}

	public String getGroupWebAddress(String faculty, String group) {
		String[] params = {"LINK"};
		StringBuilder sb = new StringBuilder();

		sb.append(pullStringOperation(String.format(Queries.facultyWebAddress, faculty), params).trim());
		sb.append("/do/");

		params = new String[]{"ESC"};
		sb.append(pullStringOperation(String.format(Queries.groupWebAddress, faculty, group), params));

		return sb.toString();
	}

    public boolean setProtectedState(String faculty, String group, int state) {
        return pushOperation(String.format(Queries.setGroupProtected, faculty, state,group));
    }

    //protection queries
    public boolean dropFacultyProtectedState(String faculty) {
        List<String> groups = getGroupIDListOnFaculty(faculty);
        System.out.println(groups);
        for (String grp: groups){
            System.out.println(grp);
            if (!(setProtectedState(faculty,grp,0)))
                return false;
        }
        return true;
    }

    public boolean dropAllFacultiesProtectedState() {
        List<String> faculties = getAllFacultiesIDs();
        for (String fac: faculties) {
            System.out.println(fac);
            if (!(dropFacultyProtectedState(fac)))
                return false;
        }
        return true;
    }


    public List<String> getProtectedGroupsOnFaculty(String faculty) {
        String[] params = {"GRP"};
        return pullListOperation(String.format(Queries.protectedGroupsOnFaculty, faculty), params);
    }

    public Map<String,List<String>> getAllProtectedGroups() {
        Map <String,List<String>> result = new HashMap<>();


        List<String> faculties = getAllFacultiesIDs();
        for (String fac: faculties) {
            result.put(fac,getProtectedGroupsOnFaculty(fac));
        }
        return result;
    }


	private String pullStringOperation(String query, String[] params) {
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

		return result.toString().trim();
	}

	private List<String> pullListOperation(String query, String[] params) {
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

				result.add(str.toString().trim());
			}
		} catch (SQLException e) {
			System.out.println(e.getClass().getName() + ": " + e.getMessage());
		}

		return result;
	}


    private boolean pushOperation(String query) {
        try (Statement stmt = connection.createStatement();) {
            stmt.executeUpdate(query);
            stmt.close();
        } catch(SQLException e) {
            System.out.println(e.getClass().getName() + ": "+ e.getMessage());
            return false;
        }
        return true;
    }
}