package hk.ssutt.deploy;


import java.io.File;

public class PIPEDeploySQL {
    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        new DeployDB(new File(".").getAbsolutePath());
        long end = System.currentTimeMillis();
        System.out.println("Database deployed in: " + (end - start) / 1_000 + " sec");
    }
}

