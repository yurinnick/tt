package hk.ssutt.api.sql;

public abstract class Queries {
	/**
	 * Church of Good Design doesn't allow me too many various variable strings
	 * in the SQLMethods class, so here most of them
	 */
	private static final String allGroupsOnFacultyQuery = "SELECT PATH FROM %s;";
	private static final String evenTTQuery = "SELECT PATH FROM %s WHERE GRP=%s AND EVEN=1;";
	private static final String oddTTQuery = "SELECT PATH FROM %s WHERE GRP=%s AND EVEN=0;";

	//as groups double (even and odd xml files) select only one. API.FS handles
	//existence of both files
	private static final String groupListOnFacultyQuery = "SELECT GRP FROM %s WHERE EVEN=1;";

	private static final String allFacultiesIDsQuery = "SELECT ID FROM ILN;";

	// " are required for CHAR(6)
	private static final String facultyNameFromIDQuery = "SELECT NAME FROM ILN WHERE ID=\"%s\";";
	//once more - need only one!
	private static final String protectionByGroupIDQuery = "SELECT PROTECTED FROM %s WHERE GRP=%s AND EVEN=1;";

	private static final String protectedGroupsOnFacultyQuery = "SELECT GRP FROM %s WHERE EVEN=1 AND PROTECTED=1;";

	private static final String facultyWebAddressQuery = "SELECT LINK FROM ILN WHERE ID=\"%s\";";

	private static final String groupWebAddressQuery = "SELECT ESC FROM %s WHERE GRP=%s AND EVEN=1;";

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

	public static String getAllFacultiesIDsQuery() {
		return allFacultiesIDsQuery;
	}

	public static String getFacultyNameFromIDQuery() {
		return facultyNameFromIDQuery;
	}

	public static String getProtectionByGroupIDQuery() {
		return protectionByGroupIDQuery;
	}

	public static String getProtectedGroupsOnFacultyQuery() {
		return protectedGroupsOnFacultyQuery;
	}

	public static String getFacultyWebAddressQuery() {
		return facultyWebAddressQuery;
	}

	public static String getGroupWebAddressQuery() {
		return groupWebAddressQuery;
	}
}