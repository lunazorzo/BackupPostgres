/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ConexaoBanco;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class Propriedade {

    private Propriedade() {
    }

    private static final Properties prop = new Properties();

    static {
        try (final InputStream fis = Propriedade.class.getResourceAsStream("/properties/conexao.propertie")) {
            prop.load(fis);
        } catch (final IOException e) {
            throw new ExceptionInInitializerError(e.getMessage());
        }
    }

    public static String getValor(final String key) {
        final String returning = prop.getProperty(key);
        return returning != null && returning.length() > 0 ? returning : key;
    }

}
