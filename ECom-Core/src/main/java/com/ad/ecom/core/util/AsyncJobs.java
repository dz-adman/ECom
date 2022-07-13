package com.ad.ecom.core.util;

import com.ad.ecom.registration.persistance.VerificationToken;
import com.ad.ecom.registration.repository.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Component
public class AsyncJobs {

    @Autowired
    private VerificationTokenRepository tokenRepo;

    @Scheduled(cron = "0 0 0 * * *")
    private synchronized void flushExpiredTokens() {
        Date dt = new Date(Calendar.getInstance().getTime().getTime());
        Optional<List<VerificationToken>> tokens = tokenRepo.findByExpiresOnLessThanEqual(dt);
        if(tokens.isPresent()) {
            tokens.get().stream()
                  .forEach(t -> tokenRepo.delete(t));
        }
    }
}
