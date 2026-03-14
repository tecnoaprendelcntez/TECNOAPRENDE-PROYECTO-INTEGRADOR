package Logica;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Utilidad para hashear y verificar contraseñas con BCrypt.
 * 
 * DEPENDENCIA: Agregar en pom.xml:
 * <dependency>
 *     <groupId>org.mindrot</groupId>
 *     <artifactId>jbcrypt</artifactId>
 *     <version>0.4</version>
 * </dependency>
 */
public class PasswordUtil {

    private static final int SALT_ROUNDS = 12;

    /**
     * Genera un hash seguro de la contraseña.
     * Llamar al REGISTRAR un usuario.
     */
    public static String hashear(String contrasenaPlana) {
        return BCrypt.hashpw(contrasenaPlana, BCrypt.gensalt(SALT_ROUNDS));
    }

    /**
     * Verifica si una contraseña en texto plano coincide con su hash.
     * Llamar al INICIAR SESIÓN.
     */
    public static boolean verificar(String contrasenaPlana, String hashAlmacenado) {
        if (contrasenaPlana == null || hashAlmacenado == null) {
            return false;
        }
        try {
            return BCrypt.checkpw(contrasenaPlana, hashAlmacenado);
        } catch (Exception e) {
            return false;
        }
    }
}
