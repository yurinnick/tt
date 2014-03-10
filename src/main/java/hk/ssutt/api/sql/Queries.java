package hk.ssutt.api.sql;

public abstract class Queries {
    /**
     * Church of Good Design doesn't allow me too many various variable strings
     * in the SQLHandler class, so here most of them
     */
    public static final String createDepartments = "CREATE TABLE departments (ID CHAR(6) NOT NULL, LINK TEXT NOT NULL, " +
            "NAME TEXT NOT NULL);";

    public static final String registerDepartment = "INSERT INTO departments (ID, LINK, NAME) VALUES ('%s','%s','%s');";

    public static final String createFaculty = "CREATE TABLE %s (GRP TEXT, ESC TEXT, EVEN INT, PATH TEXT, MANAGED INT);";

    public static final String fillFaculty = "INSERT INTO %s (GRP, ESC, EVEN, PATH, MANAGED) " +
            "VALUES ('%s', '%s', %d, '%s', %d);";

    public static final String createManagers = "CREATE TABLE managers(USER TEXT, PASS TEXT, FACULTY TEXT, GRP TEXT);";

    public static final String allGroupsOnFaculty = "SELECT PATH FROM %s;";
    public static final String evenTT = "SELECT PATH FROM %s WHERE GRP=%s AND EVEN=1;";
    public static final String oddTT = "SELECT PATH FROM %s WHERE GRP=%s AND EVEN=0;";

    //as groups double (even and odd xml files) select only one. API.FS handles
    //existence of both files
    public static final String groupListOnFaculty = "SELECT GRP FROM %s WHERE EVEN=1;";

    public static final String allFacultiesIDs = "SELECT ID FROM departments;";

    // " are required for CHAR(6)
    public static final String facultyNameFromID = "SELECT NAME FROM departments WHERE ID=\"%s\";";
    //once more - need only one!
    public static final String managedGroupsOnFaculty = "SELECT GRP FROM %s WHERE EVEN=1 AND MANAGED=1;";

    public static final String facultyWebAddress = "SELECT LINK FROM departments WHERE ID=\"%s\";";

    public static final String groupWebAddress = "SELECT ESC FROM %s WHERE GRP=%s AND EVEN=1;";

    public static final String setGroupManaged = "UPDATE %s SET MANAGED=%d WHERE GRP=\"%s\"";

    //admin operations
    public static final String addHead = "INSERT INTO HEADS(USER,PASS,FACULTY,GRP) VALUES('%s','%s','%s','%s');";

    public static final String headOfGroupExists = "SELECT * FROM HEADS WHERE FACULTY='%s' AND GRP='%s';";

    public static final String deleteHead = "DELETE FROM HEADS WHERE FACULTY='%s' AND GRP='%s';";

    public static final String getHead = "SELECT USER, PASS FROM HEADS WHERE FACULTY='%s' AND GRP='%s'";

    public static final String transferHead = "UPDATE HEADS SET USER='%s', PASS='%s' WHERE FACULTY='%s' and GRP='%s'";

}
