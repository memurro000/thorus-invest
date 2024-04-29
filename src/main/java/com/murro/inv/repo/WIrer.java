package com.murro.inv.repo;

import com.murro.inv.model.Subscriber;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class WIrer {
    SubscriberRepo repo;

    public Subscriber save(Subscriber subscriber){
        return repo.save(subscriber);
    }
}
