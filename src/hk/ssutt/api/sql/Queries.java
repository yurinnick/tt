package hk.ssutt.api.sql;

public abstract class Queries {
    /**Church of Good Design doesn't allow me too many various variable strings
    * in the SQLMethods class, so here most of them
    */
    private static String allGroupsOnFacultyQuery = "SELECT PATH FROM %s;";
    private static String evenTTQuery = "SELECT PATH FROM %s WHERE GRP=%s AND EVEN=1;";
    private static String oddTTQuery = "SELECT PATH FROM %s WHERE GRP=%s AND EVEN=0;";

    //as groups double (even and odd xml files) select only one. API.FS handles
    //existence of both files
    private static String groupListOnFacultyQuery = "SELECT GRP FROM %s WHERE EVEN=1;";

    private static String allFacultiesIDs = "SELECT ID FROM ILN;";

    // " are required for CHAR(6)
    private static String facultyNameFromID = "SELECT NAME FROM ILN WHERE ID=\"%s\";";

    public static String getAllGroupsOnFacultyQuery() {
        return allGroupsOnFacultyQuery;
    }

    public static String getOddTTQuery() {
        return oddTTQuery;
    }

    public static String getEvenTTQuery() {
        return evenTTQuery;
    }

    public static String getGroupListOnFacultyQuery() {
        return groupListOnFacultyQuery;
    }

    public static String getAllFacultiesIDs() {
        return allFacultiesIDs;
    }

    public static String getFacultyNameFromID() {
        return facultyNameFromID;
    }
}
