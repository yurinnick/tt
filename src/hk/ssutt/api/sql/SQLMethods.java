package hk.ssutt.api.sql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        return processListOperation(Queries.getAllFacultiesIDsQuery(),params);
    }

    public String getFacultyNameFromID(String facultyID) {
        String[] params = {"NAME"};
        return processStringOperation(String.format(Queries.getFacultyNameFromIDQuery(), facultyID),params);
    }


    //protection queries
    public String getProtectionByGroupID(String faculty, String group) {
       String[] params = {"PROTECTED"};
        return processStringOperation(String.format(Queries.getProtectionByGroupIDQuery(), faculty, group),params);
    }

    public List<String> getProtectedGroupsOnFaculty(String faculty) {
        String[] params = {"GRP"};
        return processListOperation(String.format(Queries.getProtectedGroupsOnFacultyQuery(), faculty),params);
    }

    public Map<String,List<String>> getAllProtectedGroups() {
        Map<String,List<String>> result = new HashMap<>();
        List<String> faculties = getAllFacultiesIDs();

        String[] params = {"GRP"};

        for (String faculty: faculties) {
            result.put(faculty,getProtectedGroupsOnFaculty(faculty));
        }
        return result;
    }

    //web addresses queries
    public String getFacultyWebAddress(String faculty) {
        String[] params = {"LINK"};
        return processStringOperation(String.format(Queries.getFacultyWebAddressQuery(), faculty), params);
    }

    public String getGroupWebAddress(String faculty, String group) {
        String[] params = {"LINK"};
        StringBuilder sb = new StringBuilder();

        sb.append(processStringOperation(String.format(Queries.getFacultyWebAddressQuery(), faculty), params));
        
        sb.append("/do/");

        params = new String[]{"ESC"};
        sb.append(processStringOperation(String.format(Queries.getGroupWebAddressQuery(), faculty, group), params));

        return sb.toString();
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
