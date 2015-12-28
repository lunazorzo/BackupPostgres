package PostgresBackup;

import EnvioEmail.EnviaEmail;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.swing.JOptionPane;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.MultiPartEmail;

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

        compacata();
    }

    private static void VerificaArquivo() {
        Date dtAtual = new Date();
        File arquivo = new File("C:\\Backup_Postgres\\Backup_BancoDados_Dia_" + dfLog.format(dtAtual) + ".backup");
        if (arquivo.exists()) {
            JOptionPane.showMessageDialog(null, "Backup realizado com sucesso!");
            EnviaEmail();
            VerificaArquivo();
        } else {
            JOptionPane.showMessageDialog(null, "Backup Não Realizado!");
            //System.out.println("Backup Não Realizadorealizado com sucesso!");
            EnviaEmailErro();
        }
        EnviaEmail();
    }

    //http://www.botecodigital.info/java/compactando-e-descompactando-arquivos-em-java/
    public static void compacata() {
        Date dtAtual = new Date();
        try {
            //Passa o local do arquivo desejado a ser compactado
            FileInputStream fis = new FileInputStream("C:\\Backup_Postgres\\Backup_BancoDados_Dia_" + dfLog.format(dtAtual) + ".backup");
            //passa o local que o arquivo compactado será salvo, juntamente com o nome
            FileOutputStream fos = new FileOutputStream("C:\\Backup_Postgres\\Backup_BancoDados_Dia_" + dfLog.format(dtAtual) + ".zip");
            ZipOutputStream zipOut = new ZipOutputStream(fos);
            //Pega o arquivo que será compactado e compacta
            zipOut.putNextEntry(new ZipEntry("C:\\Backup_Postgres\\Backup_BancoDados_Dia_" + dfLog.format(dtAtual) + ".backup"));

            int content;
            while ((content = fis.read()) != -1) {
                zipOut.write(content);
            }

            zipOut.closeEntry();
            zipOut.close();
            JOptionPane.showMessageDialog(null, "Backup compactado com sucesso!");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Erro ao compactar arquivo!" + "\n" + e.getMessage());
        }
    }

    private static void EnviaEmail() {
        File f = new File("C:\\Backup_Postgres\\testando.txt");

        EmailAttachment attachment = new EmailAttachment();
        attachment.setPath(f.getPath()); // Obtem o caminho do arquivo  
        attachment.setDisposition(EmailAttachment.ATTACHMENT);
        attachment.setDescription("Anexo");
        attachment.setName(f.getName()); // Obtem o nome do arquivo  

        try {
            //Envia o E-mail
            MultiPartEmail email = new MultiPartEmail();
            //Utilize o hostname do seu provedor de email
            System.out.println("alterando hostname...");
            email.setHostName("smtp.batata.com");
            //Quando a porta utilizada não é a padrão (gmail = 465)
            email.setSmtpPort(587);
            //Adicione os destinatários
            email.addTo("allan@batata.com", "Jose");
            //Configure o seu email do qual enviará
            email.setFrom("allan@batata.com", "Seu nome");
            //Adicione um assunto
            email.setSubject("Test message");
            //Adicione a mensagem do email
            email.setMsg("This is a simple test of commons-email");
            //email.attach(attachment);
            //Para autenticar no servidor é necessário chamar os dois métodos abaixo
            System.out.println("autenticando...");
            email.setSSL(true);
            email.setAuthentication("allan@nobresistemas.com", "xtz7qr87");
            System.out.println("enviando...");
            email.send();
            System.out.println("Email enviado!");
        } catch (Exception e) {
            System.out.println("Erro ao enviar email!");
        }
    }

    private static void EnviaEmailErro() {
        File f = new File("C:\\Backup_Postgres\\testando.txt");

        EmailAttachment attachment = new EmailAttachment();
        attachment.setPath(f.getPath()); // Obtem o caminho do arquivo  
        attachment.setDisposition(EmailAttachment.ATTACHMENT);
        attachment.setDescription("Anexo");
        attachment.setName(f.getName()); // Obtem o nome do arquivo  

        try {
            //Envia o E-mail
            MultiPartEmail email = new MultiPartEmail();
            //Utilize o hostname do seu provedor de email
            System.out.println("alterando hostname...");
            email.setHostName("smtp.batata.com");
            //Quando a porta utilizada não é a padrão (gmail = 465)
            email.setSmtpPort(587);
            //Adicione os destinatários
            email.addTo("allan@batata.com", "Jose");
            //Configure o seu email do qual enviará
            email.setFrom("allan@batata.com", "Seu nome");
            //Adicione um assunto
            email.setSubject("Test message BKP Banco");
            //Adicione a mensagem do email
            email.setMsg("Erro ao gerar o backup, Verifique");
            //email.attach(attachment);
            //Para autenticar no servidor é necessário chamar os dois métodos abaixo
            System.out.println("autenticando...");
            email.setSSL(true);
            email.setAuthentication("allan@nobresistemas.com", "xtz7qr87");
            System.out.println("enviando...");
            email.send();
            System.out.println("Email enviado!");
        } catch (Exception e) {
            System.out.println("Erro ao enviar email!");
        }
    }

}
