package persistencia;

import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JPAUtil {

    private static EntityManagerFactory emf;

    public static EntityManagerFactory getEMF() {
        if (emf == null) {

            Map<String, String> props = new HashMap<>();

            String host = System.getenv("MYSQLHOST");

            if (host != null) {
                //Railway
                String port = System.getenv("MYSQLPORT");
                String db = System.getenv("MYSQLDATABASE");
                String user = System.getenv("MYSQLUSER");
                String password = System.getenv("MYSQLPASSWORD");

                String url = "jdbc:mysql://" + host + ":" + port + "/" + db +
                        "?useSSL=false&serverTimezone=UTC&useUnicode=true&characterEncoding=UTF-8";

                props.put("javax.persistence.jdbc.url", url);
                props.put("javax.persistence.jdbc.user", user);
                props.put("javax.persistence.jdbc.password", password);

            } else {
                //Local
                props.put("javax.persistence.jdbc.url",
                        "jdbc:mysql://localhost:3306/dbtecnoaprende?serverTimezone=America/Mexico_City&useUnicode=true&characterEncoding=UTF-8");
                props.put("javax.persistence.jdbc.user", "Admin1");
                props.put("javax.persistence.jdbc.password", "mdcgr050504");
            }

            emf = Persistence.createEntityManagerFactory("JavaWebPU", props);
        }

        return emf;
    }
}