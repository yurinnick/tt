package hk.ssutt.api.sql;

import hk.ssutt.api.admin.PasswordHandler;
import hk.ssutt.api.admin.User;
import hk.ssutt.api.utils.Utils;

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

    //==========DEPLOYMENT==========
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

    //==========GROUPS==========
    public String getEvenTT(String faculty, String group) {
        String[] params = {"PATH"};

        return pullStringOperation(String.format(Queries.evenTT, faculty, group), params);
    }

    public String getOddTT(String faculty, String group) {
        String[] params = {"PATH"};

        return pullStringOperation(String.format(Queries.oddTT, faculty, group), params);
    }

    private boolean groupExists(String faculty, String group) {
        List<String> faculties = getAllFacultiesID();
        if (faculties.contains(faculty)) {
            List<String> groups = getGroupID(faculty);
            if (groups.contains(group))
                return true;
        }
        return false;
    }

    public List<String> getGroupID(String faculty) {
        String[] params = {"GRP"};

        return pullListOperation(String.format(Queries.groupListOnFaculty, faculty), params);
    }

    public List<String> getGroupsFiles(String faculty) {
        String[] params = {"PATH"};

        return pullListOperation(String.format(Queries.allGroupsOnFaculty, faculty), params);
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

    //==========FACULTIES==========
    public List<String> getAllFacultiesID() {
        String[] params = {"ID"};

        return pullListOperation(Queries.allFacultiesIDs, params);
    }

    public String getFacultyNameFromID(String facultyID) {
        String[] params = {"NAME"};
        return pullStringOperation(String.format(Queries.facultyNameFromID, facultyID), params);
    }

    public String getFacultyWebAddress(String faculty) {
        String[] params = {"LINK"};
        return pullStringOperation(String.format(Queries.facultyWebAddress, faculty), params).trim();
    }


    //==========GROUP MANAGEMENT==========(GROUP TABLES)
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

    public boolean dropAllManagedStates() {
        List<String> faculties = getAllFacultiesID();
        for (String fac : faculties) {
            System.out.println(fac);
            if (!(dropFacultyManagedState(fac)))
                return false;
        }
        return true;
    }

    public List<String> getManagedGroupsOnFaculty(String faculty) {
        String[] params = {"GRP"};
        return pullListOperation(String.format(Queries.managedGroupsOnFaculty, faculty), params);
    }

    public Map<String, List<String>> getAllManagedGroups() {
        Map<String, List<String>> result = new HashMap<>();

        List<String> faculties = getAllFacultiesID();
        for (String fac : faculties) {
            result.put(fac, getManagedGroupsOnFaculty(fac));
        }
        return result;
    }

    //==========MANAGERS==========
    public boolean addManager(String name, String password, String faculty, String group, boolean isTransfer) {
        PasswordHandler pwh = PasswordHandler.getInstance();
        if (groupExists(faculty, group)) {
            try {
                password = pwh.encrypt(password);
            } catch (GeneralSecurityException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            if (!(managerRegistered(faculty, group))) {
                setManagedState(faculty, group, 1);
                return pushOperation(String.format(
                        (isTransfer) ? Queries.transferManager : Queries.addManager, name, password, faculty, group));
            }
        }
        return false;
    }

    public User getManager(String faculty, String group) {
        String[] params = {"USER", "PASS"};
        String info = pullStringOperation(String.format(Queries.getManager, faculty, group), params);

        return new User(info.split("\\s+")[0], info.split("\\s+")[1]);
    }

    public boolean dropManager(String faculty, String group) {
        setManagedState(faculty, group, 0);
        return pushOperation(String.format(Queries.deleteManager, faculty, group));
    }

    public boolean dropAllManagersOnFaculty(String faculty) {
        boolean state = false;
        for (String group : getGroupID(faculty)) {
            if (managerRegistered(faculty, group))
                state = dropManager(faculty, group);
        }
        return state;
    }

    public boolean dropAllManagers() {
        boolean state = false;
        for (String faculty : getAllFacultiesID()) {
            state = dropAllManagersOnFaculty(faculty);
        }
        return state;
    }

    public void transferManagersOnFaculty(String faculty) {
        List<String> groups = getGroupID(faculty);
        Collections.sort(groups, Utils.stringReverseOrderCmp);
        //should be sorted! make them sorted on deployment stage
        for (String group : groups) {
            //avoid non-managed groups
            if (managerRegistered(faculty, group)) {
                //avoid the 4th, 5th years or masters
                //or non-numerical things
                String elderGrp = Utils.elderGroup(group);
                if (groupExists(faculty, elderGrp))
                    performTransfer(faculty, group, elderGrp);
                else
                    dropManager(faculty, group);
            }
        }
    }

    private void performTransfer(String faculty, String grp1, String grp2) {
        setManagedState(faculty, grp2, 1);

        User u = getManager(faculty, grp1);

        addManager(u.getName(), u.getPassword(), faculty, grp2, managerRegistered(faculty, grp2));

        dropManager(faculty, grp1);
    }

    private boolean managerRegistered(String faculty, String group) {
        return (pullStringOperation(String.format(Queries.groupManagerExists, faculty, group), new String[]{"USER"}).length() != 0);
    }

    //===========OPERATIONS=============
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

}