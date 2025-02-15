package com.d111.PrePay.service;

import com.d111.PrePay.dto.request.CreateStoreReq;
import com.d111.PrePay.dto.request.StoresReq;
import com.d111.PrePay.dto.respond.AllStoreRes;
import com.d111.PrePay.dto.respond.StoresRes;
import com.d111.PrePay.model.Store;
import com.d111.PrePay.model.TeamStore;
import com.d111.PrePay.model.UserTeam;
import com.d111.PrePay.repository.StoreRepository;
import com.d111.PrePay.repository.UserTeamRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class StoreService {
    private final StoreRepository storeRepository;
    private final UserTeamRepository userTeamRepository;

    // 모든 가게 조회
    public List<AllStoreRes> getAllStores(){
        List<Store> stores = storeRepository.findAll();
        List<AllStoreRes> result = new ArrayList<>();

        for (Store store : stores) {
            AllStoreRes res = new AllStoreRes(store);
            result.add(res);
        }

        return result;
    }


    public void makeStore(CreateStoreReq createStoreReq) {
        Store store = new Store(createStoreReq);
        storeRepository.save(store);

    }

    public List<StoresRes> getNewNearStores(StoresReq coordinatesReq, String email) {
        List<Store> stores = storeRepository.findAll();
        UserTeam userTeam = userTeamRepository.findByTeamIdAndUser_Email(coordinatesReq.getTeamId(), email).orElseThrow();
        List<TeamStore> teamStores = userTeam.getTeam().getTeamStores();
        List<StoresRes> result = new ArrayList<>();
        for (TeamStore teamStore : teamStores) {
            StoresRes storesRes = new StoresRes(teamStore);
            storesRes.setLatitude(teamStore.getStore().getLatitude());
            storesRes.setLongitude(teamStore.getStore().getLongitude());
            storesRes.setMyteam(true);
            result.add(storesRes);
        }
        for (Store store : stores) {
            if (calDistance(store.getLongitude(), store.getLatitude(), coordinatesReq.getLongitude(), coordinatesReq.getLatitude())<2F) {
                boolean check = false;
                for (TeamStore teamStore : teamStores) {
                    if (teamStore.getStore() == store) {
                        check=true;
                    }
                }
                if(check)continue;
                StoresRes storesRes = new StoresRes(store);
                storesRes.setMyteam(false);
                storesRes.setLongitude(store.getLongitude());
                storesRes.setLatitude(store.getLatitude());
                result.add(storesRes);
            }
        }

        return result;
    }

    // 경도 위도로 2km 이내이면 true
    public float calDistance(float storeLongitude, float storeLatitude, float userLongitude, float userLatitude) {
        final int EARTH_RADIUS_KM = 6371; // 지구 반지름 (km)

        // 라디안으로 변환
        double lat1 = Math.toRadians(storeLatitude);
        double lon1 = Math.toRadians(storeLongitude);
        double lat2 = Math.toRadians(userLatitude);
        double lon2 = Math.toRadians(userLongitude);

        // 위도/경도 차이 계산
        double dLat = lat2 - lat1;
        double dLon = lon2 - lon1;

        // Haversine 공식 적용
        double a = Math.pow(Math.sin(dLat / 2), 2)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.pow(Math.sin(dLon / 2), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double distanceKm = EARTH_RADIUS_KM * c; // 최종 거리 (km)
        return (float) distanceKm; // 2km 이내인지 확인
    }
}
