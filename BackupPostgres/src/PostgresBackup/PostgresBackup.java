package PostgresBackup;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.swing.JOptionPane;
import jdk.nashorn.internal.objects.Global;

/**
 *
 * @author Nobre Sistemas
 */
public class PostgresBackup {
//http://www.guj.com.br/t/backup-e-restore-bd-postgres-8-4-usando-java/130101/5

    private static final DateFormat dfLog = new SimpleDateFormat("dd-MM-yyyy");

    public static void realizaBackup() throws IOException, InterruptedException {
        Date dtAtual = new Date();
        System.err.println(dfLog.format(dtAtual));
        final List<String> comandos = new ArrayList<>();
        comandos.add("C:\\Program Files (x86)\\pgAdmin III\\1.20\\pg_dump.exe");    // esse é meu caminho  
        comandos.add("-i");
        comandos.add("-h");
        comandos.add("10.1.1.10");     //ou  comandos.add("192.168.0.1"); 
        comandos.add("-p");
        comandos.add("5432");
        comandos.add("-U");
        comandos.add("sistema");
        comandos.add("-F");
        comandos.add("c");
        comandos.add("-b");
        comandos.add("-v");
        comandos.add("-f");
        //Passa o caminho da pasta juntamente com o nome do arquivo que será gerado
        comandos.add("C:\\Backup_Postgres\\Backup_BancoDados_Dia_" + dfLog.format(dtAtual) + ".backup");   // eu utilizei meu C:\ e D:\ para os testes e gravei o backup com sucesso.  
        comandos.add("Demo");
        ProcessBuilder pb = new ProcessBuilder(comandos);
        pb.environment().put("PGPASSWORD", "xtz7qr87");      //Somente coloque sua senha         
        try {
            final Process process = pb.start();
            final BufferedReader r = new BufferedReader(
                    new InputStreamReader(process.getErrorStream()));
            String line = r.readLine();
            while (line != null) {
                //Mostra no output as tabelas que estão sendo feito o backup
                System.err.println(line);
                line = r.readLine();
            }
            r.close();
            process.waitFor();
            process.destroy();
            //JOptionPane.showMessageDialog(null, "backup realizado com sucesso.");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }

    }

    public static void main(String[] args) throws IOException, InterruptedException {
        realizaBackup();
        VerificaArquivo();
    }

    private static void VerificaArquivo() {
        Date dtAtual = new Date();
        System.err.println(dfLog.format(dtAtual));

        File arquivo = new File("C:\\Backup_Postgres\\Backup_BancoDados_Dia_" + dfLog.format(dtAtual)+ ".backup");
        if (arquivo.exists()) {
            JOptionPane.showMessageDialog(null, "Backup realizado com sucesso!");
            System.out.println("Backup realizado com sucesso!");
        } else {
            JOptionPane.showMessageDialog(null, "Backup Não Realizado!");
            System.out.println("Backup Não Realizadorealizado com sucesso!");
        }
    }

}
