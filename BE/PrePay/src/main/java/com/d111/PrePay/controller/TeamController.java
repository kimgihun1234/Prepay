package com.d111.PrePay.controller;

import com.d111.PrePay.dto.request.TeamCreateReq;
import com.d111.PrePay.dto.request.TeamCreateStoreReq;
import com.d111.PrePay.dto.respond.*;

import com.d111.PrePay.dto.request.*;
import com.d111.PrePay.dto.respond.GetUserOfTeamRes;
import com.d111.PrePay.dto.respond.StoresRes;
import com.d111.PrePay.dto.respond.TeamDetailRes;
import com.d111.PrePay.dto.respond.TeamRes;
import com.d111.PrePay.model.Team;
import com.d111.PrePay.model.TeamStore;
import com.d111.PrePay.model.UserTeam;
import com.d111.PrePay.service.ImageService;
import com.d111.PrePay.service.TeamService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/team")
@RequiredArgsConstructor
@Slf4j
public class TeamController {
    private final TeamService teamService;
    private final ImageService imageService;

    // 팀 이미지 수정
    @PostMapping("/image")
    @Operation(summary = "팀 이미지 수정", description = "<b>Header access : accessToken" +
            "<br>long : teamId" +
            "<br>requestpart : image")
    public ResponseEntity<UploadImageRes> uploadImage(@RequestHeader("userId") Long userId,
                                                      @RequestPart("request") TeamIdReq req,
                                                      @RequestPart(value = "image", required = false) MultipartFile image) throws IOException {

        return ResponseEntity.ok(teamService.uploadImage(req, image));

    }


    @PostMapping("/ban")
    @Operation(summary = "팀에서 유저 강퇴", description = "<b>Header access : accessToken" +
            "<br>long : banUserId" +
            "<br>long : teamId")
    public ResponseEntity<Map<String,String>> banUser(@RequestHeader("userId") Long userId,
                                        @RequestBody BanUserReq req) {
        teamService.banUser(req);
        String message = String.format("%d번팀에서 %d번 유저를 강퇴하였습니다.", req.getTeamId(), userId);
        Map<String, String> response = new HashMap<>();
        response.put("message", message);
        return ResponseEntity.ok(response);
    }


    @PostMapping("/exit")
    @Operation(summary = "팀에서 나가기", description = "<b>Header" +
            "<br>long teamId")
    public ResponseEntity<Map<String, String>> exitTeam(@RequestHeader("userId") Long userId,
                                                        @RequestBody TeamIdReq req) {
        teamService.exitTeam(userId, req);
        String message = String.format("%d번팀에서 %d번 유저가 퇴장하였습니다.", req.getTeamId(), userId);
        Map<String, String> response = new HashMap<>();
        response.put("message", message);
        return ResponseEntity.ok(response);
    }


    @PostMapping("confirm-privilege")
    @Operation(summary = "회식 권한 수락",description = "<b>헤더" +
            "<br>long : partyRequestId" +
            "<br> boolean : accept ->true면 허가")
    public ResponseEntity<PartyConfirmRes> confirmPrivilege(@RequestHeader("userId") Long userId,
                                                            @RequestBody PartyConfirmReq req) {
        return ResponseEntity.ok(teamService.confirmPrivilege(req));

    }


    @PostMapping("/request-privilege")
    @Operation(summary = "회식 권한 요청", description = "<b>헤더" +
            "<br>long teamId")
    public ResponseEntity<PartyRequestRes> privilegeRequest(@RequestHeader("userId") Long userId,
                                                            @RequestBody TeamIdReq req) {
        return ResponseEntity.ok(teamService.privilegeRequest(userId, req));

    }


    @PostMapping("/charge")
    @Operation(summary = "충전요청", description = "<b>헤더" +
            "<br>long : chargeRequestId" +
            "<br>String : requestStatus -> Waiting, Refused, Approved" +
            "<br>int requestPrice" +
            "<br> long requestDate")
    public ResponseEntity<ChargeRes> chargeRequest(@RequestHeader("userId") Long userId,
                                                   @RequestBody ChargeReq req) {
        return ResponseEntity.ok(teamService.chargeRequest(req));

    }


    @PostMapping("/signin")
    public ResponseEntity<GetUserOfTeamRes> signinTeam(@RequestHeader("userId") Long userId,
                                                       @RequestBody SignInTeamReq req) {
        return ResponseEntity.ok(teamService.signInTeam(userId, req));

    }


    @PostMapping("/privilege")
    public ResponseEntity<GrantPrivilegeRes> grantPrivilege(@RequestHeader("userId") Long userId,
                                                            @RequestBody GrantPrivilegeReq req) {
        return ResponseEntity.ok(teamService.grantPrivilege(req));
    }


    @PostMapping("/position")
    public ResponseEntity<GrantAdminPositionRes> grantAdminPosition(@RequestHeader("userId") Long userId,
                                                                    @RequestBody GrantAdminPositionReq req) {
        return ResponseEntity.ok(teamService.grantAdminPosition(req));

    }


    @PostMapping("/limit")
    public ResponseEntity<TeamDetailRes> changeDailyPriceLimit(@RequestHeader("userId") Long userId, @RequestBody ChangeDailyPriceLimitReq req) {
        return ResponseEntity.ok(teamService.changeDailyPriceLimit(req, userId));
    }


    @PostMapping("/code")
    public ResponseEntity<TeamDetailRes> generateInviteCode(@RequestHeader("userId") Long userId, @RequestBody TeamIdReq req) {
        return ResponseEntity.ok(teamService.generateInviteCode(userId, req));
    }


    @PostMapping("/store-id")
    public ResponseEntity<TeamCreateStoreRes> createStore(@RequestHeader("userId") Long userId,
                                                          @RequestBody TeamCreateStoreReq req) {
        return ResponseEntity.ok(teamService.createStore(req));
    }


    @GetMapping("/{teamId}/user")
    public List<GetUserOfTeamRes> getUserOfTeam(@PathVariable Long teamId,
                                                @RequestHeader("userId") Long userId) {
        return teamService.getUsersOfTeam(teamId, userId);
    }


    @GetMapping("/{teamId}")
    public ResponseEntity<TeamDetailRes> getTeamDetails(@PathVariable Long teamId,
                                                        @RequestHeader("userId") Long userId) {
        return ResponseEntity.ok(teamService.getTeamDetails(teamId, userId));
    }


    @PostMapping(value = "/signup")
    public ResponseEntity<TeamCreateRes> createTeam(@RequestPart("request") TeamCreateReq request,
                                                    @RequestPart(value = "image", required = false) MultipartFile image,
                                                    @RequestHeader("userId") Long userId) throws IOException {
        return ResponseEntity.ok(teamService.createTeam(request, userId, image));
    }

    @GetMapping("/groups")
    public ResponseEntity<List<TeamRes>> getMyTeams(@RequestHeader("userId") Long userId) {
        return ResponseEntity.ok(teamService.getMyTeams(userId));
    }

    @GetMapping("/{teamId}/stores")
    public ResponseEntity<List<StoresRes>> getMyTeamStores(@RequestHeader("userId") Long userId, @PathVariable Long teamId) {
        return ResponseEntity.ok(teamService.getMyTeamStores(teamId, userId));
    }

    @GetMapping("/coordinate/{teamId}")
    public ResponseEntity<List<StoresCorRes>> getStoresCor(@RequestHeader("userId") Long userId, @PathVariable Long teamId) {
        return ResponseEntity.ok(teamService.getStoresCor(teamId, userId));
    }

    @GetMapping("/public-teams")
    public ResponseEntity<List<PublicTeamsRes>> getPublicTeams() {
        return ResponseEntity.ok(teamService.getPublicTeams());
    }

    @GetMapping("/public-teams/{keyword}")
    public ResponseEntity<List<PublicTeamsRes>> getPublicTeamsByKeyword(@PathVariable String keyword) {
        return ResponseEntity.ok(teamService.getPublicTeamsByKeyword(keyword));
    }
}
