package hk.ssutt.api.sql;

import com.sun.source.tree.StatementTree;

import javax.swing.plaf.nimbus.State;
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
    private Queries q = new Queries();

    public SQLMethods(Connection connection) {
        this.connection = connection;
    }

    //XML files queries
    //return path files per group on faculty (do we need Group entity?)
    public List<String> getAllGroupsOnFaculty(String faculty) {
        List<String> result = new ArrayList<>();

        String[] params = {"PATH"};

        return processSelectListOperation(String.format(q.getAllGroupsOnFacultyQuery(), faculty), params);
    }

    //same here
    public List<String> getGroupListOnFaculty(String faculty, String group) {
        return null;
    }

    public List<String> getFacultyNameFromID(String faculty) {
        return null;
    }

    public String getOddTT(String faculty, String group) {
        return null;
    }

    public String getEvenTT(String faculty, String group) {
        return null;
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



    private String processSelectStringOperation(String query, String[] params) {
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

    private List<String> processSelectListOperation(String query, String[] params) {
        List<String> result = new ArrayList<String>();
        Statement stmt = null;

        try {

            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                String str = "";

                for (String s: params) {
                    str += rs.getString(s)+" ";
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
