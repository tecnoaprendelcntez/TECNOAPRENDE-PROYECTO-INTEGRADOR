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

            String mysqlUrl = System.getenv("MYSQL_URL");

            if (mysqlUrl != null && !mysqlUrl.isEmpty()) {
                // Railway

                String user = System.getenv("MYSQLUSER");
                String password = System.getenv("MYSQLPASSWORD");

                // Si la URL viene como mysql:// la convertimos a jdbc:mysql://
                if (mysqlUrl.startsWith("mysql://")) {
                    mysqlUrl = mysqlUrl.replaceFirst("mysql://", "jdbc:mysql://");
                }

                // Agregar parámetros necesarios
                String separator = mysqlUrl.contains("?") ? "&" : "?";
                String url = mysqlUrl + separator
                        + "useSSL=false"
                        + "&allowPublicKeyRetrieval=true"
                        + "&serverTimezone=UTC"
                        + "&useUnicode=true"
                        + "&characterEncoding=UTF-8";

                props.put("javax.persistence.jdbc.url", url);
                props.put("javax.persistence.jdbc.user", user);
                props.put("javax.persistence.jdbc.password", password);

            } else {
                // Local (XAMPP)

                props.put("javax.persistence.jdbc.url",
                        "jdbc:mysql://localhost:3306/dbtecnoaprende"
                        + "?serverTimezone=America/Mexico_City"
                        + "&useUnicode=true"
                        + "&characterEncoding=UTF-8");

                props.put("javax.persistence.jdbc.user", "Admin1");
                props.put("javax.persistence.jdbc.password", "mdcgr050504");
            }

            emf = Persistence.createEntityManagerFactory("JavaWebPU", props);
        }

        return emf;
    }
}