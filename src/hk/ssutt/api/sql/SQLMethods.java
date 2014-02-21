package hk.ssutt.api.sql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fau on 18/02/14.
 */
public class SQLMethods {
    private Connection connection = null;

    public SQLMethods(Connection connection) {
        this.connection = connection;
    }

    //XML files queries
    //return path files per group on faculty (do we need Group entity?)
    public List<String> getAllGroupsOnFaculty(String faculty) {
        String[] params = {"PATH"};

        return processListOperation(String.format(Queries.getAllGroupsOnFacultyQuery(), faculty), params);
    }

    public String getEvenTT(String faculty, String group) {
        String[] params = {"PATH"};

        return processStringOperation(String.format(Queries.getEvenTTQuery(), faculty, group), params);
    }

    public String getOddTT(String faculty, String group) {
        String[] params = {"PATH"};

        return processStringOperation(String.format(Queries.getOddTTQuery(), faculty, group), params);
    }

    //just ids
    public List<String> getGroupListOnFaculty(String faculty) {
        String[] params = {"GRP"};

        return processListOperation(String.format(Queries.getGroupListOnFacultyQuery(), faculty), params);
    }

    public List<String> getAllFacultiesIDs() {
        String[] params = {"ID"};

        return processListOperation(Queries.getAllFacultiesIDs(),params);
    }

    public String getFacultyNameFromID(String facultyID) {
        String[] params = {"NAME"};
        return processStringOperation(String.format(Queries.getFacultyNameFromID(),facultyID),params);
    }



    //protection queries
    public String getProtectionByGroupIdentity(String faculty, String group) {
       return null;
    }

    public List<String> getProtectedGroupsOnFaculty(String faculty) {
        return null;
    }

    public List<String> getAllProtectedGroups() {
        return null;
    }

    //web addresses queries
    public String getFacultyWebAddress(String faculty) {
        return null;
    }

    public String getGroupWebAddress(String faculty, String group) {
        return null;
    }



    private String processStringOperation(String query, String[] params) {
        String result = "";
        Statement stmt = null;

        try {

            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {

                for (String s: params) {
                result += rs.getString(s)+" ";
                }
            }
            stmt.close();

       } catch (SQLException e) {
            System.out.println(e.getClass().getName() + ": " + e.getMessage());
        }


        return result;
    }

    private List<String> processListOperation(String query, String[] params) {
        List<String> result = new ArrayList<String>();
        Statement stmt = null;

        try {

            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                String str = "";

                for (String s: params) {
                    str += rs.getString(s);
                    if (params.length>1) str += " ";
                }

                result.add(str);
            }
            stmt.close();
        } catch (SQLException e) {
           System.out.println(e.getClass().getName() + ": " + e.getMessage());
        }

        return result;
    }
}
