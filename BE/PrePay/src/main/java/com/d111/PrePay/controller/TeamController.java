package com.d111.PrePay.controller;

import com.d111.PrePay.dto.request.TeamCreateReq;
import com.d111.PrePay.dto.respond.GetUserOfTeamRes;
import com.d111.PrePay.dto.respond.TeamDetailRes;
import com.d111.PrePay.dto.respond.TeamRes;
import com.d111.PrePay.model.Team;
import com.d111.PrePay.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/team")
@RequiredArgsConstructor
public class TeamController {
    private final TeamService teamService;

    @GetMapping("/{teamId}/user")
    public List<GetUserOfTeamRes> getUserOfTeam(@PathVariable Long teamId,
                                                @RequestHeader("userId") Long userId) {
        return teamService.getUsersOfTeam(teamId, userId);
    }


    @GetMapping("/{teamId}")
    public TeamDetailRes getTeamDetails(@PathVariable Long teamId,
                                        @RequestHeader("userId") Long userId) {
        return teamService.getTeamDetails(teamId, userId);
    }


    @PostMapping("/signup")
    public ResponseEntity<Long> createTeam(@RequestBody TeamCreateReq request) {
        Team team = teamService.createTeam(request);
        return ResponseEntity.ok(team.getId());
    }

    @GetMapping("/groups")
    public ResponseEntity<List<TeamRes>> getMyTeams(@RequestHeader("userId") Long userId) {
        return ResponseEntity.ok(teamService.getMyTeams(userId));
    }
}
