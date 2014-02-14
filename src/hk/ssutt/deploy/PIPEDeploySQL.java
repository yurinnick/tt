package hk.ssutt.deploy;


public class PIPEDeploySQL {
    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        new DeployDB(args[0]);
        long end = System.currentTimeMillis();
        System.out.println("Database deployed in: " + (end - start) / 1_000 + " sec");
    }
}

