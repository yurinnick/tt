package hk.ssutt.api.sql;

public abstract class Queries {
	/**
	 * Church of Good Design doesn't allow me too many various variable strings
	 * in the SQLMethods class, so here most of them
	 */
	public static final String allGroupsOnFaculty = "SELECT PATH FROM %s;";
	public static final String evenTT = "SELECT PATH FROM %s WHERE GRP=%s AND EVEN=1;";
	public static final String oddTT = "SELECT PATH FROM %s WHERE GRP=%s AND EVEN=0;";

	//as groups double (even and odd xml files) select only one. API.FS handles
	//existence of both files
	public static final String groupListOnFaculty = "SELECT GRP FROM %s WHERE EVEN=1;";

	public static final String allFacultiesIDs = "SELECT ID FROM ILN;";

	// " are required for CHAR(6)
	public static final String facultyNameFromID = "SELECT NAME FROM ILN WHERE ID=\"%s\";";
	//once more - need only one!
	public static final String protectedGroupsOnFaculty = "SELECT GRP FROM %s WHERE EVEN=1 AND PROTECTED=1;";

	public static final String facultyWebAddress = "SELECT LINK FROM ILN WHERE ID=\"%s\";";

	public static final String groupWebAddress = "SELECT ESC FROM %s WHERE GRP=%s AND EVEN=1;";

    public static final String setGroupProtected = "UPDATE %s SET PROTECTED=%d WHERE GRP=\"%s\"";
}
