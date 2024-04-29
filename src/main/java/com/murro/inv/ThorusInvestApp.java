package com.murro.inv;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.murro.inv.config.RepoConfig;
import com.murro.inv.model.Subscriber;
import com.murro.inv.repo.WIrer;
import jakarta.xml.bind.JAXBException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.net.SocketException;

@Configuration
@Import(RepoConfig.class)
@ComponentScan(basePackages = "com.murro.inv")
public class ThorusInvestApp {

    public static final String URI = "";

    public static void main(String[] args) throws TelegramApiException, SocketException, JAXBException {

        ApplicationContext context =
                new AnnotationConfigApplicationContext
                        (ThorusInvestApp.class);

        WIrer repo = context.getBean(WIrer.class);

        Subscriber sub = new Subscriber();
        sub.setId(1L);
        sub.setTopic("kline.up");

        repo.save(sub);
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

}