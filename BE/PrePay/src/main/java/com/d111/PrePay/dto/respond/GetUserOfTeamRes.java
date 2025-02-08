package com.d111.PrePay.dto.respond;


import com.d111.PrePay.model.Team;
import com.d111.PrePay.model.User;
import com.d111.PrePay.model.UserTeam;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class GetUserOfTeamRes {
    private Long teamId;
    private String email;
    private boolean position;
    private boolean privilege;

    public GetUserOfTeamRes(UserTeam userTeam) {
        User user = userTeam.getUser();
        Team team = userTeam.getTeam();
        this.email = user.getEmail();
        this.position = userTeam.isPosition();
        this.privilege = userTeam.isPrivilege();
        this.teamId = team.getId();
    }
}
