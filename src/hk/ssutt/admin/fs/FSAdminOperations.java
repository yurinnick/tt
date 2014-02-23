package hk.ssutt.admin.fs;

/**
 * Created by fau on 18/02/14.
 */
public class FSAdminOperations {
    private static FSAdminOperations fsao;

    private FSAdminOperations() {
    }

    public static FSAdminOperations getInstance() {
        if (fsao == null)
            fsao = new FSAdminOperations();
        return fsao;
    }
}
