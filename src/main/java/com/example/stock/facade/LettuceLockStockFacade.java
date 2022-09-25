package com.example.stock.facade;

import com.example.stock.repository.RedisLockRepository;
import com.example.stock.service.StockService;
import org.springframework.stereotype.Component;

@Component
public class LettuceLockStockFacade {

    private RedisLockRepository redisRepository;

    private StockService stockService;

    public LettuceLockStockFacade(RedisLockRepository repository, StockService stockService) {
        this.redisRepository = repository;
        this.stockService = stockService;
    }

    public void decrease(Long key, Long quantity) throws InterruptedException {
        while (!redisRepository.lock(key)) {
            Thread.sleep(1000);
        }

        try {
            stockService.decrease(key, quantity);
        } finally {
            redisRepository.unlock(key);
        }
    }
}
