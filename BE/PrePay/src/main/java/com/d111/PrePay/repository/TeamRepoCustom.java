package com.d111.PrePay.repository;

import com.d111.PrePay.model.Team;

import java.util.List;

public interface TeamRepoCustom {
    List<Team> findTeamsbyKeywordNoN(String keyword);
}
