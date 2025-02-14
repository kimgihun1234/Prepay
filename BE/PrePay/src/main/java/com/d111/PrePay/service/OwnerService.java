package com.d111.PrePay.service;

import com.d111.PrePay.dto.respond.GetPrepaidTeamsRes;
import com.d111.PrePay.dto.respond.PublicTeamDetailRes;
import com.d111.PrePay.dto.respond.TeamDetailRes;
import com.d111.PrePay.model.TeamStore;
import com.d111.PrePay.repository.TeamStoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OwnerService {

    private final TeamStoreRepository teamStoreRepository;
    // 사장님 가게에 선결제한 팀들 보기
    public List<GetPrepaidTeamsRes> getPrepaidTeams(Long storeId){
        List<TeamStore> teamStores = teamStoreRepository.findByStoreId(storeId);
        List<GetPrepaidTeamsRes> resultList = new ArrayList<>();

        for (TeamStore teamStore : teamStores) {
            GetPrepaidTeamsRes res = new GetPrepaidTeamsRes(teamStore,teamStore.getTeam());
            resultList.add(res);
        }

        return resultList;
    }

    // 가게 주문 내역 보기

}
