package hk.ssutt.deploy;

import com.javafx.tools.doclets.formats.html.SourceToHTMLConverter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

// It's work!

public class DeployDB {
    private static final String scheduleURL = "http://www.sgu.ru/schedule";
    private static final Path exclFile = Paths.get(".excludeFaculty").toAbsolutePath();
    private static String dbPath = "";
    private static Connection c = null;

    public DeployDB(String dbPath) {
        this.dbPath = dbPath;
        System.out.println("Gathering faculties from sgu.ru...");

        List<String[]> faculties = getFaculties();
        try {

            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:" + dbPath + "/timetables.db");
            System.out.println("sqlite initiliazed.");
        } catch (Exception e) {
            System.out.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(1);
        }
        System.out.println("Gathering done. Creating faculties table... ");

        createILNtable(faculties);//id(knt,mm) + link (http://..) + name(CS&IT)

        System.out.println("Created!");

        for (String[] s : faculties) {
            System.out.println("Gathering groups from "+s[0]+"...");

            List<String[]> groups = getGroups(s[1], s[0]);

            System.out.println("Gathering done. Creating faculty groups table...");

            createFacultyTable(s[0], groups);

            System.out.println("Created!");
        }
        System.out.println("Creating groups table done. Creating heads table...");

        createHeadsTable();

        System.out.println("Created!");
        System.out.println("Deployed!");

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
                    " NAME           TEXT    NOT NULL, " +
                    " LINK           TEXT     NOT NULL); ";

            stmt.executeUpdate(sql);
            stmt.close();

            for (String[] elem : faculties) {
                stmt = c.createStatement();
                String s = String.format("INSERT INTO ILN (ID, NAME, LINK) VALUES ('%s','%s','%s');", elem[0], elem[1], elem[2]); // удобнее string.format юзать
                stmt.executeUpdate(s);
                stmt.close();
            }

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
                    " (ID INT NOT NULL, " +
                    "GRP TEXT NOT NULL, " +
                    "ESC TEXT NOT NULL, " + //non-unescaped addresses (for groups like 141(1))
                    "EVEN INT NOT NULL, " + //sqlite has no boolean
                    "PATH TEXT NOT NULL, " +
                    "ISMANAGED INT NOT NULL);";

            stmt.executeUpdate(sql);
            stmt.close();
            int i = 1;
            for (String[] s : groups) {
                stmt = c.createStatement();
                String ssEven = String.format("INSERT INTO %s (ID, GRP, ESC, EVEN, PATH, ISMANAGED) VALUES (%d, '%s', '%s', %d, '%s', %d);",
                        faculty, i, s[0], s[1], (((i - 1) % 2) == 0) ? 1 : 0, dbPath + '/' + faculty + '/' + "even" + s[0] + ".xml", 0);
                stmt.executeUpdate(ssEven);
                stmt.close();
                i++;
                stmt = c.createStatement();
                String ssOdd = String.format("INSERT INTO %s (ID, GRP, ESC, EVEN, PATH, ISMANAGED) VALUES (%d, '%s', '%s', %d, '%s', %d);",
                        faculty, i, s[0], s[1], (((i - 1) % 2) == 0) ? 1 : 0, dbPath + '/' + faculty + '/' + "odd" + s[0] + ".xml", 0);
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
