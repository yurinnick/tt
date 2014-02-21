package hk.ssutt.api.sql;

import java.sql.Connection;
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
    //return path of XML files per group on faculty (do we need Group entity?)
    public List<String> getAllGroupsOnFaculty(String faculty) {
        return null;
    }

    //same here
    public List<String> getGroupListOnFaculty(String faculty, String group) {
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



    private String processStringOperation(String query) {
        return null;
    }

    private List<String> processListOperation(String query) {
        return null;
    }
}
