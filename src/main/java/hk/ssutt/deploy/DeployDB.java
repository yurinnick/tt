package hk.ssutt.deploy;


import hk.ssutt.api.fs.FSHandler;
import hk.ssutt.api.parsing.html.HTMLParser;
import hk.ssutt.api.sql.SQLHandler;
import hk.ssutt.api.sql.SQLManager;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.util.List;

public class DeployDB {
	private static final String scheduleURL = "http://www.sgu.ru/schedule";
	private static final Path exclFile = Paths.get("/Users/fau/tt/.excludeFaculty").toAbsolutePath();
	private static String dbPath = "";
	private static Connection c = null;

    private static SQLManager sqlm;

	public DeployDB(String dbPath) {
        this.dbPath = dbPath;

        sqlm = SQLManager.getInstance();
        sqlm.createConnection(dbPath);
        c = sqlm.getConnection();

        SQLHandler sqlh = SQLHandler.getInstance(c);

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
        return sqlm.getConnection();
    }

    public static void closeConnection() {
        sqlm.closeConnection(c);
    }
}
