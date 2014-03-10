package hk.ssutt.deploy;


import hk.ssutt.api.fs.FSHandler;
import hk.ssutt.api.parsing.html.HTMLParser;
import hk.ssutt.api.sql.SQLHandler;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class DeployDB {
	private static final String scheduleURL = "http://www.sgu.ru/schedule";
	private static final Path exclFile = Paths.get(".excludeFaculty").toAbsolutePath();
	private static String dbPath = "";
	private static Connection c = null;

	public DeployDB(String dbPath) {
        this.dbPath = dbPath;

		SQLHandler sqlh = SQLHandler.getInstance(getConnection());
        HTMLParser htmlh = HTMLParser.getInstance();
        FSHandler fsh = FSHandler.getInstance();

        String currentDirectory = fsh.getTTDir();

		List<String[]> faculties = htmlh.getFacultiesFromSSU();
		sqlh.createDepartments(faculties);


        for (String[] s : faculties) {
			List<String[]> groups = htmlh.getGroupsFromSSU(s[1], s[0]);
	        sqlh.createFaculty(s[0]);
            sqlh.fillFaculty(s[0], groups, currentDirectory);
    	}

        sqlh.createManagers();
	}

	//database can exist, but in this case it stays uninitialized
	public DeployDB(FSHandler fsm) {
		dbPath = fsm.getTTDirPath();
	}

	public static Connection getConnection() {
		if (c == null) {
			createConnection();
		}

		return c;
	}

	private static void createConnection() {
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
		} catch (Exception e) {
			System.out.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(1);
		}
	}
}
