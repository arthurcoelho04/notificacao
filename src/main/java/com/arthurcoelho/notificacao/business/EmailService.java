package com.arthurcoelho.notificacao.business;

import com.arthurcoelho.notificacao.business.dto.TarefasDTO;
import com.arthurcoelho.notificacao.infrastructure.EmailException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor

public class EmailService {

    private final JavaMailSender javamailSender;
    private final TemplateEngine templateEngine;

    @Value("${envio.email.remetente}")
    public String remetente;

    @Value("${envio.email.nomeRemetente")
    public String nomeRemetente;

    public void enviaEmail(TarefasDTO dto){

        try {
            MimeMessage mimeMessage = javamailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true,
                                                                StandardCharsets.UTF_8.name());
            helper.setFrom(new InternetAddress(remetente, nomeRemetente));
            //helper.setTo(InternetAddress.parse(dto.getEmailUsuario()));
            helper.setTo("arthurteste2605@gmail.com");
            helper.setSubject("notificação de tarefa");

            Context context = new Context();
            context.setVariable("nomeTarefa", dto.getNomeTarefa());
            context.setVariable("dataEvento", dto.getDataEvento());
            context.setVariable("descricao", dto.getDescricaoTarefa());
            String template = templateEngine.process("notificacao", context);
            helper.setText(template, true);

            System.out.println("EMAIL DESTINO: " + dto.getEmailUsuario());
            System.out.println("ENVIANDO EMAIL...");

            javamailSender.send(mimeMessage);

            System.out.println("EMAIL ENVIADO COM SUCESSO");

        }catch (MessagingException | UnsupportedEncodingException e){
            throw new EmailException("Erro ao enviar e-mail", e.getCause());
        }
    }
}
