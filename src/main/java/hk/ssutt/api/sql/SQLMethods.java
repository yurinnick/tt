package hk.ssutt.api.sql;

import hk.ssutt.api.admin.PasswordHandler;
import hk.ssutt.api.admin.User;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
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
        return pushOperation(String.format(Queries.setGroupProtected, faculty, state, group));
    }

    //protection queries
    public boolean dropFacultyProtectedState(String faculty) {
        List<String> groups = getGroupIDListOnFaculty(faculty);
        System.out.println(groups);
        for (String grp : groups) {
            System.out.println(grp);
            if (!(setProtectedState(faculty, grp, 0)))
                return false;
        }
        return true;
    }

    public boolean dropAllFacultiesProtectedState() {
        List<String> faculties = getAllFacultiesIDs();
        for (String fac : faculties) {
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

    public Map<String, List<String>> getAllProtectedGroups() {
        Map<String, List<String>> result = new HashMap<>();


        List<String> faculties = getAllFacultiesIDs();
        for (String fac : faculties) {
            result.put(fac, getProtectedGroupsOnFaculty(fac));
        }
        return result;
    }

    //HEADS operaions
    public boolean addHead(String name, String password, String faculty, String group) {
        PasswordHandler pwh = PasswordHandler.getInstance();
        if (groupOnFacultyExists(faculty, group)) {
            try {
                password = pwh.encrypt(password);
            } catch (GeneralSecurityException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            if (!(headOnFacultyRegistered(faculty, group)))
                return pushOperation(String.format(Queries.addHead, name, password, faculty, group));
        }
        return false;
    }

    public boolean dropHead(String faculty, String group) {
        return pushOperation(String.format(Queries.deleteHead, faculty, group));
    }

    public User getHead(String faculty, String group) {
        String[] params = {"USERNAME", "SALTEDPASS"};
        String info = pullStringOperation(String.format(Queries.getHead, faculty, group), params);

        return new User(info.split("\\s+")[0], info.split("\\s+")[1]);
    }

    public void transferHeadsOnFaculty(String faculty) {
        List<String> groups = getGroupIDListOnFaculty(faculty);

        for (String group: groups) {
            //avoid non-managed groups
            if (headOnFacultyRegistered(faculty, group)){
                //avoid the 4th, 5th years or masters
                //or non-numerical things
                makeTransfer(faculty, group);
            }
        }
    }

    private void makeTransfer(String faculty, String firstGroup) {
        if (groupOnFacultyExists(faculty, firstGroup)) {
            /*
            for fuck's sake, if there where no social science students
            and some strange guys who don't like numbers
            it was much much easier
            I just can't stand all of you
            by fau when writing this code
            */
            int group = Integer.parseInt(firstGroup);
            group += 100;
            String elderGroup = String.valueOf(group);
            if (groupOnFacultyExists(faculty, elderGroup)) {
                User u = getHead(faculty, firstGroup);
                pushOperation(String.format(Queries.transferHead, u.getName(), u.getPassword(), faculty, elderGroup));
                if (group/100==1)
                dropHead(faculty,firstGroup);
            }
        }
    }

    private boolean groupOnFacultyExists(String faculty, String group) {
        List<String> faculties = getAllFacultiesIDs();
        if (faculties.contains(faculty)) {
            List<String> groups = getGroupIDListOnFaculty(faculty);
            if (groups.contains(group))
                return true;
        }
        return false;
    }

    private boolean headOnFacultyRegistered(String faculty, String group) {
        return (pullStringOperation(String.format(Queries.headOfGroupExists, faculty, group), new String[]{"USERNAME"}).length() != 0);
    }

    private String pullStringOperation(String query, String[] params) {
        StringBuilder result = new StringBuilder();

        try (Statement stmt = connection.createStatement();) {
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

        try (Statement stmt = connection.createStatement()) {
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
        } catch (SQLException e) {
            System.out.println(e.getClass().getName() + ": " + e.getMessage());
            return false;
        }
        return true;
    }
}