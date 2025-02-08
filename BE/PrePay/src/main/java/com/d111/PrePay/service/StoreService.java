package com.d111.PrePay.service;

import com.d111.PrePay.dto.request.CreateStoreReq;
import com.d111.PrePay.model.Store;
import com.d111.PrePay.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StoreService {
    private final StoreRepository storeRepository;

    public void makeStore(CreateStoreReq createStoreReq){
        Store store= new Store(createStoreReq);
        storeRepository.save(store);

    }
}
