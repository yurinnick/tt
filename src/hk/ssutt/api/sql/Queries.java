package hk.ssutt.api.sql;

public class Queries {
    /**Church of Good Design doesn't allow me too many various variable strings
    * in the SQLMethods class, so here most of them
    */
    private String AllGroupsOnFacultyQuery = "SELECT PATH FROM %s";

    public Queries() {
    }

    public String getAllGroupsOnFacultyQuery() {
        return AllGroupsOnFacultyQuery;
    }
}
