package com.ad.ecom.core.util;

import com.ad.ecom.registration.persistance.VerificationToken;
import com.ad.ecom.registration.repository.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
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

    @Autowired
    private CacheManager cacheManager;

    // Clear expired tokens from VerificationToken
    @Scheduled(cron = "0 0 0 * * *")
    private synchronized void flushExpiredTokens() {
        Date dt = new Date(Calendar.getInstance().getTime().getTime());
        Optional<List<VerificationToken>> tokens = tokenRepo.findByExpiresOnLessThanEqual(dt);
        if(tokens.isPresent()) {
            tokens.get().stream()
                  .forEach(t -> tokenRepo.delete(t));
        }
    }

    // Clear all caches
    @Scheduled(cron = "0 0 0 * * *")
    private synchronized void evictAllCaches() {
        cacheManager.getCacheNames().stream().forEach(cacheName -> cacheManager.getCache(cacheName).clear());
    }
}
