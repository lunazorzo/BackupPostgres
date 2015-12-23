package EnvioEmail;

import java.io.File;
import javax.mail.MessagingException;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;

public class EnviaEmail {

/*
    http://jamacedo.com/2010/02/envio-de-email-de-forma-facil-com-java/
    http://commons.apache.org/proper/commons-email/userguide.html
 */
    public static void main(String[] args) throws EmailException, MessagingException {
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
            email.setHostName("smtp.nobresistemas.com");
            //Quando a porta utilizada não é a padrão (gmail = 465)
            email.setSmtpPort(587);
            //Adicione os destinatários
            email.addTo("allan@nobresistemas.com", "Jose");
            //Configure o seu email do qual enviará
            email.setFrom("allan@nobresistemas.com", "Seu nome");
            //Adicione um assunto
            email.setSubject("Test message");
            //Adicione a mensagem do email
            email.setMsg("This is a simple test of commons-email");
            email.attach(attachment);
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
