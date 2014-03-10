package hk.ssutt.api.sql;

import hk.ssutt.api.admin.PasswordHandler;
import hk.ssutt.api.admin.User;
import hk.ssutt.utils.Utils;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class SQLHandler {
    private static Connection connection;
    private static SQLHandler sqlm;

    private SQLHandler() {
    }

    public static SQLHandler getInstance(Connection c) {
        if (sqlm == null) {
            sqlm = new SQLHandler();
        }

        setConnection(c);
        return sqlm;
    }

    private static void setConnection(Connection c) {
        connection = c;
    }

    public void createDepartments(List<String[]> faculties) {
        //id-link-name
        if (pushOperation(Queries.createDepartments)) {
            for (String[] faculty : faculties) {
                pushOperation(String.format(Queries.registerDepartment, faculty[0], faculty[1], faculty[2]));
            }
        } else {
            System.out.println("Creating departments went wrong. Exiting");
            System.exit(-1);
        }
    }

    public void createFaculty(String faculty) {
        pushOperation(String.format(Queries.createFaculty, faculty));
    }

    public void fillFaculty(String faculty, List<String[]> groups, String currentDirectory) {
        if (currentDirectory.endsWith("/"))
            currentDirectory = currentDirectory.substring(0, currentDirectory.length() - 1);

        Collections.sort(groups, Utils.groupArrayDirectOrderCmp);

        for (String[] s : groups) {
            String evenfile = String.format("%s/%s/%s/even%s.xml", currentDirectory, faculty, s[0], s[0]);
            String oddfile = String.format("%s/%s/%s/odd%s.xml", currentDirectory, faculty, s[0], s[0]);

            pushOperation(String.format(Queries.fillFaculty,
                    faculty, s[0], s[1], 1, evenfile, 0));

            pushOperation(String.format(Queries.fillFaculty,
                    faculty, s[0], s[1], 0, oddfile, 0));
        }
    }

    public void createManagers() {
        pushOperation(Queries.createManagers);
    }


    //XML files queries
    //return path files per group on faculty (do we need Group entity?)
    public List<String> getFilesOnFaculty(String faculty) {
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
    public List<String> getGroupID(String faculty) {
        String[] params = {"GRP"};

        return pullListOperation(String.format(Queries.groupListOnFaculty, faculty), params);
    }

    public List<String> getAllFacultiesID() {
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

    public boolean setManagedState(String faculty, String group, int state) {
        return pushOperation(String.format(Queries.setGroupManaged, faculty, state, group));
    }

    //protection queries
    public boolean dropFacultyManagedState(String faculty) {
        List<String> groups = getGroupID(faculty);
        System.out.println(groups);
        for (String grp : groups) {
            System.out.println(grp);
            if (!(setManagedState(faculty, grp, 0)))
                return false;
        }
        return true;
    }

    public boolean dropAllManagers() {
        List<String> faculties = getAllFacultiesID();
        for (String fac : faculties) {
            System.out.println(fac);
            if (!(dropFacultyManagedState(fac)))
                return false;
        }
        return true;
    }


    public List<String> getProtectedGroupsOnFaculty(String faculty) {
        String[] params = {"GRP"};
        return pullListOperation(String.format(Queries.managedGroupsOnFaculty, faculty), params);
    }

    public Map<String, List<String>> getAllProtectedGroups() {
        Map<String, List<String>> result = new HashMap<>();


        List<String> faculties = getAllFacultiesID();
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
            if (!(headOnFacultyRegistered(faculty, group))) {
                setManagedState(faculty, group, 1);
                return pushOperation(String.format(Queries.addHead, name, password, faculty, group));
            }
        }
        return false;
    }

    public boolean dropHead(String faculty, String group) {
        setManagedState(faculty, group, 0);
        return pushOperation(String.format(Queries.deleteHead, faculty, group));
    }

    public boolean dropAllHeadsOnFaculty(String faculty) {
        boolean state = false;
        for (String group : getGroupID(faculty)) {
            if (headOnFacultyRegistered(faculty, group))
                state = dropHead(faculty, group);
        }
        return state;
    }

    public boolean dropAllHeads() {
        boolean state = false;
        for (String faculty : getAllFacultiesID()) {
            state = dropAllHeadsOnFaculty(faculty);
        }
        return state;
    }

    public User getHead(String faculty, String group) {
        String[] params = {"USERNAME", "SALTEDPASS"};
        String info = pullStringOperation(String.format(Queries.getHead, faculty, group), params);

        return new User(info.split("\\s+")[0], info.split("\\s+")[1]);
    }

    public void transferHeadsOnFaculty(String faculty) {
        List<String> groups = getGroupID(faculty);

        //should be sorted! make them sorted on deployment stage
        for (String group : groups) {
            //avoid non-managed groups
            if (headOnFacultyRegistered(faculty, group)) {
                //avoid the 4th, 5th years or masters
                //or non-numerical things
                String elderGrp = elderGroup(group);
                if (elderGrp != null) {
                    if (groupOnFacultyExists(faculty, elderGrp) && (groupOnFacultyExists(faculty, group))) {
                        makeTransfer(faculty, group, elderGrp);
                    }
                }
            }
        }
    }

    private void makeTransfer(String faculty, String grp1, String grp2) {
        //TODO!
    }

    private boolean groupOnFacultyExists(String faculty, String group) {
        List<String> faculties = getAllFacultiesID();
        if (faculties.contains(faculty)) {
            List<String> groups = getGroupID(faculty);
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
            System.out.println("On query: " + query);
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
            System.out.println("On query: " + query);
        }

        return result;
    }


    private boolean pushOperation(String query) {
        try (Statement stmt = connection.createStatement();) {
            stmt.executeUpdate(query);
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e.getClass().getName() + ": " + e.getMessage());
            System.out.println("On query: " + query);
            return false;
        }
        return true;
    }

    private String elderGroup(String group) {
        String result = null;
         /*
            for fuck's sake, if there where no social science students
            and some strange guys who don't like numbers
            it was much much easier
            I just can't stand all of you
            by fau when writing this code
            */
        try {
            int elder = Integer.parseInt(group);
            elder += 100;
            result = Integer.toString(elder);
        } catch (NumberFormatException ex) {
            return null;
        }
        return result;
    }
}