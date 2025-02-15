package com.d111.PrePay.repository;

import com.d111.PrePay.model.UserTeam;

public interface UserTeamCustom {
    UserTeam findUserTeamByTeamIdAndUserIdWithTeam(long teamId, long userId);
}
