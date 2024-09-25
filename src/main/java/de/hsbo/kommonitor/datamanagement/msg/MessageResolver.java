package de.hsbo.kommonitor.datamanagement.msg;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class MessageResolver {

    @Autowired
    private MessageSource messageSource;

    @Value("${kommonitor.locale:de}")
    private String locale;

    public String getMessage(String messageKey) {
        return messageSource.getMessage(messageKey, null, new Locale(locale));
    }
}
