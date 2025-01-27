package com.d111.PrePay.controller;

import com.d111.PrePay.dto.TeamRequestDTO;
import com.d111.PrePay.model.Team;
import com.d111.PrePay.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/team")
@RequiredArgsConstructor
public class TeamController {
    private final TeamService teamService;

    @PostMapping("/signup")
    public ResponseEntity<Void> createTeam(@RequestBody TeamRequestDTO request){
        Team team = teamService.createTeam(request);
        return ResponseEntity.ok().build();
    }
}
