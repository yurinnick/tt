package hk.ssutt.deploy;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.List;

public class DeployDB {
    private static final String scheduleURL = "http://www.sgu.ru/schedule";
    private static final Path exclFile = Paths.get(".excludeFaculty").toAbsolutePath();
    private static String dbPath = "";
    private static Connection c = null;

    public DeployDB(String dbPath) {
        this.dbPath = dbPath;

        //move to FS later!
        if (!(new File(dbPath +"/timetables.db").isFile())) {
            createConnection();

            List<String[]> faculties = getFaculties();
            createILNtable(faculties);//id(knt,mm) + link (http://..) + name(CS&IT)

            for (String[] s : faculties) {
                List<String[]> groups = getGroups(s[1], s[0]);
                createFacultyTable(s[0], groups);
            }

            createHeadsTable();
        }
        else createConnection();
    }

    public static Connection getConnection() {
        return c;
    }

    private void createConnection() {
    try {
        Class.forName("org.sqlite.JDBC");
        c = DriverManager.getConnection("jdbc:sqlite:" + dbPath + "/timetables.db");
    } catch (Exception e) {
        System.out.println(e.getClass().getName() + ": " + e.getMessage());
        System.exit(1);
    }
    }

    private List<String[]> getFaculties() {
        List<String[]> result = new ArrayList<>();
        Document doc = null;

        try {
            doc = Jsoup.connect(scheduleURL).get();
        } catch (IOException e) {
            System.out.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(2);
        }

        Elements links = doc.select("a[href]");
        //parsing exceptions
        for (Element link : links) {
            if (link.attr("href").startsWith("/schedule/")) {

                String[] test = link.attr("abs:href").split("/"); //test[4] - the last token like knt,mm,ff

                if (notInExclusion(test[4])) {
                    String[] aFaculty = new String[3];
                    aFaculty[0] = test[4];
                    aFaculty[1] = link.attr("abs:href");
                    aFaculty[2] = link.ownText();

                    result.add(aFaculty);
                }
            }
        }

        return result;
    }

    private boolean notInExclusion(String s) {
        try (InputStream in = Files.newInputStream(exclFile);
             BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {

            String line;
            while ((line = reader.readLine()) != null) {
                if (s.equals(line))
                    return false;
            }
        } catch (IOException e) {
            System.out.printf("%s: %s%n", e.getClass().getName(), e.getMessage());
            System.exit(-3);
        }

        return true;

    }

    private void createILNtable(List<String[]> faculties) { //id-link-name
        try {
            Statement stmt = c.createStatement();
            String sql = "CREATE TABLE ILN " +
                    "(ID CHAR(6)     NOT NULL," +
                    "LINK           TEXT     NOT NULL, " +
                    "NAME           TEXT    NOT NULL);";

            stmt.executeUpdate(sql);

            for (String[] elem : faculties) {
                String s = String.format("INSERT INTO ILN (ID, LINK, NAME) VALUES ('%s','%s','%s');", elem[0], elem[1], elem[2]); // удобнее string.format юзать
                stmt.executeUpdate(s);

            }
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(3);
        }

    }

    private List<String[]> getGroups(String url, String faculty) //we have full path from our previous query
    {
        List<String[]> result = new ArrayList<>();

        Document doc = null;

        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            System.out.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(4);
        }

        Elements links = doc.select("a[href]");
        for (Element link : links) {
            if (link.attr("href").startsWith("/schedule/" + faculty + "/do/")) {
                String s[] = new String[2];
                s[0] = link.ownText();
                String[] esc = link.attr("abs:href").split("/");

                s[1] = esc[esc.length - 1];
                result.add(s);

            }

        }
        return result;

    }

    private void createFacultyTable(String faculty, List<String[]> groups) {


        try {
            Statement stmt = c.createStatement();
            String sql = "CREATE TABLE " + faculty +
                    "(GRP TEXT NOT NULL, " +
                    "ESC TEXT NOT NULL, " + //non-unescaped addresses (for groups like 141(1))
                    "EVEN INT NOT NULL, " + //sqlite has no boolean
                    "PATH TEXT NOT NULL, " +
                    "PROTECTED INT NOT NULL);";

            stmt.executeUpdate(sql);
            stmt.close();
            int i = 1;
            for (String[] s : groups) {
                stmt = c.createStatement();
                String ssEven = String.format("INSERT INTO %s (GRP, ESC, EVEN, PATH, PROTECTED) VALUES ('%s', '%s', %d, '%s', %d);",
                        faculty, s[0], s[1], (((i - 1) % 2) == 0) ? 1 : 0, dbPath + '/' + faculty + '/' + "even" + s[0] + ".xml", 0);
                stmt.executeUpdate(ssEven);
                i++;
                String ssOdd = String.format("INSERT INTO %s (GRP, ESC, EVEN, PATH, PROTECTED) VALUES ('%s', '%s', %d, '%s', %d);",
                        faculty, s[0], s[1], (((i - 1) % 2) == 0) ? 1 : 0, dbPath + '/' + faculty + '/' + "odd" + s[0] + ".xml", 0);
                stmt.executeUpdate(ssOdd);
                stmt.close();
                i++;
            }

        } catch (SQLException e) {
            System.out.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(5);
        }
    }

    private void createHeadsTable() {
        try {
            Statement stmt = c.createStatement();
            String sql = "CREATE TABLE HEADS" +
                    " (ID INT NOT NULL, " +
                    "USERNAME TEXT NOT NULL, " +
                    "SALTEDPASS TEXT NOT NULL, " +
                    "FACULTY TEXT NOT NULL, " +
                    "GRP TEXT NOT NULL);";

            stmt.executeUpdate(sql);
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(6);
        }
    }
}
