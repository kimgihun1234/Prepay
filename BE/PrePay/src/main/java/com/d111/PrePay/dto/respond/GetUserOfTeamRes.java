package com.d111.PrePay.dto.respond;


import com.d111.PrePay.model.User;
import com.d111.PrePay.model.UserTeam;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class GetUserOfTeamRes {
    private String userName;
    private String nickName;
    private boolean position;
    private boolean privilege;

    public GetUserOfTeamRes(UserTeam userTeam) {
        User user = userTeam.getUser();
        this.userName = user.getUserName();
        this.nickName = user.getNickname();
        this.position = userTeam.isPosition();
        this.privilege = userTeam.isPrivilege();
    }
}
