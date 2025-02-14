package com.d111.PrePay.controller;

import com.d111.PrePay.dto.request.TeamCreateReq;
import com.d111.PrePay.dto.request.TeamCreateStoreReq;
import com.d111.PrePay.dto.respond.*;

import com.d111.PrePay.dto.request.*;
import com.d111.PrePay.dto.respond.GetUserOfTeamRes;
import com.d111.PrePay.dto.respond.StoresRes;
import com.d111.PrePay.dto.respond.TeamDetailRes;
import com.d111.PrePay.dto.respond.TeamRes;
import com.d111.PrePay.security.dto.CustomUserDetails;
import com.d111.PrePay.service.ImageService;
import com.d111.PrePay.service.TeamService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    // 좋아요 한 퍼블릭 팀 보기
    @GetMapping("/public/liked")
    @Operation(summary = "좋아요 한 퍼블릭 팀")
    public ResponseEntity<List<PublicTeamDetailRes>> showPublicLiked(@RequestHeader String access,@AuthenticationPrincipal CustomUserDetails userDetails){
        Long userId = userDetails.getUserId();
        return ResponseEntity.ok(teamService.showPublicLiked(userId));
    }


    // 퍼블릭 팀 좋아요
    @PostMapping("/like")
    @Operation(summary = "좋아요")
    public ResponseEntity<StandardRes> like(@RequestHeader String access,
                                            @AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody LikeReq req) {
        String email = userDetails.getUsername();
        return ResponseEntity.ok(teamService.like(email, req));
    }

    // 팀 이미지 수정
    @PostMapping(value = "/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "팀 이미지 수정")
    public ResponseEntity<UploadImageRes> uploadImage(@RequestHeader String access,
                                                      @AuthenticationPrincipal CustomUserDetails userDetails,
                                                      @RequestPart("request") TeamIdReq req,
                                                      @RequestPart(value = "image", required = false) MultipartFile image) {
        Long userId = userDetails.getUserId();
        return ResponseEntity.ok(teamService.uploadImage(req, image));

    }


    @PostMapping("/ban")
    @Operation(summary = "팀에서 유저 강퇴")
    public ResponseEntity<Map<String, String>> banUser(@RequestHeader String access,
                                                       @AuthenticationPrincipal CustomUserDetails userDetails,
                                                       @RequestBody BanUserReq req) {
        teamService.banUser(req);
//        Long userId = accessToken.getUserId();
        Long userId = userDetails.getUserId();
        String message = String.format("%d번팀에서" + req.getBanUserEmail() + " 유저를 강퇴하였습니다.", req.getTeamId());
        Map<String, String> response = new HashMap<>();
        response.put("message", message);
        return ResponseEntity.ok(response);
    }


    @PostMapping("/exit")
    @Operation(summary = "팀에서 나가기", description = "<b>Header" +
            "<br>long teamId")
    public ResponseEntity<Map<String, String>> exitTeam(@RequestHeader String access,
                                                        @AuthenticationPrincipal CustomUserDetails userDetails,
                                                        @RequestBody TeamIdReq req) {
//        Long userId = accessToken.getUserId();
        Long userId = userDetails.getUserId();
        teamService.exitTeam(userId, req);
        String message = String.format("%d번팀에서 %d번 유저가 퇴장하였습니다.", req.getTeamId(), userId);
        Map<String, String> response = new HashMap<>();
        response.put("message", message);
        return ResponseEntity.ok(response);
    }


    @PostMapping("confirm-privilege")
    @Operation(summary = "회식 권한 요청 수락")
    public ResponseEntity<PartyConfirmRes> confirmPrivilege(@RequestHeader String access,
                                                            @AuthenticationPrincipal CustomUserDetails userDetails,
                                                            @RequestBody PartyConfirmReq req) {
        return ResponseEntity.ok(teamService.confirmPrivilege(req));

    }


    @PostMapping("/request-privilege")
    @Operation(summary = "회식 권한 요청", description = "<b>헤더" +
            "<br>long teamId")
    public ResponseEntity<PartyRequestRes> privilegeRequest(@RequestHeader String access,
                                                            @AuthenticationPrincipal CustomUserDetails userDetails,
                                                            @RequestBody TeamIdReq req) {
//        Long userId = accessToken.getUserId();
        Long userId = userDetails.getUserId();
        return ResponseEntity.ok(teamService.privilegeRequest(userId, req));

    }


    @PostMapping("/charge")
    @Operation(summary = "충전요청", description = "<b>헤더" +
            "<br>long : chargeRequestId" +
            "<br>String : requestStatus -> Waiting, Refused, Approved" +
            "<br>int requestPrice" +
            "<br> long requestDate")
    public ResponseEntity<ChargeRes> chargeRequest(@RequestHeader String access,
                                                   @AuthenticationPrincipal CustomUserDetails userDetails,
                                                   @RequestBody ChargeReq req) {
//        Long userId = accessToken.getUserId();
        return ResponseEntity.ok(teamService.chargeRequest(req));

    }


    @PostMapping("/signin")
    @Operation(summary = "팀 가입")
    public ResponseEntity<GetUserOfTeamRes> signinTeam(@RequestHeader String access,
                                                       @AuthenticationPrincipal CustomUserDetails userDetails,
                                                       @RequestBody SignInTeamReq req) {
//        Long userId = accessToken.getUserId();
        Long userId = userDetails.getUserId();
        String email = userDetails.getUsername();
        log.info("유저 PK : {}", userId);
        log.info("액세스 토큰 : {}", access);
        log.info("유저 이메일 : {}", email);
        return ResponseEntity.ok(teamService.signInTeam(userId, req));

    }


    @PostMapping("/privilege")
    @Operation(summary = "회식 권한 부여")
    public ResponseEntity<GrantPrivilegeRes> grantPrivilege(@RequestHeader String access,
                                                            @AuthenticationPrincipal CustomUserDetails userDetails,
                                                            @RequestBody GrantPrivilegeReq req) {
        return ResponseEntity.ok(teamService.grantPrivilege(req));
    }


    @PostMapping("/position")
    @Operation(summary = "운영자 권한 부여")
    public ResponseEntity<GrantAdminPositionRes> grantAdminPosition(@RequestHeader String access,
                                                                    @AuthenticationPrincipal CustomUserDetails userDetails,
                                                                    @RequestBody GrantAdminPositionReq req) {
        return ResponseEntity.ok(teamService.grantAdminPosition(req));

    }


    @PostMapping("/limit")
    @Operation(summary = "일일 결제 한도 변경")
    public ResponseEntity<TeamDetailRes> changeDailyPriceLimit(@RequestHeader String access,
                                                               @AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody ChangeDailyPriceLimitReq req) {
//        Long userId = accessToken.getUserId();
        Long userId = userDetails.getUserId();
        return ResponseEntity.ok(teamService.changeDailyPriceLimit(req, userId));
    }


    @PostMapping("/code")
    @Operation(summary = "팀 초대 코드 생성")
    public ResponseEntity<InviteCodeRes> generateInviteCode(/*@RequestHeader Long userId,*/ @RequestBody TeamIdReq req,
                                                                                            @AuthenticationPrincipal CustomUserDetails userDetails,
                                                                                            @RequestHeader String access) {
//        Long userId = accessToken.getUserId();
        Long userId = userDetails.getUserId();
        return ResponseEntity.ok(teamService.generateInviteCode(userId, req));

    }

    @GetMapping("/code")
    @Operation(summary = "팀 초대코드 조회")
    public ResponseEntity<InviteCodeRes> getInviteCode(@RequestHeader String access,
                                                       @AuthenticationPrincipal CustomUserDetails userDetails,
                                                       @RequestParam long teamId) {
        String email = userDetails.getUsername();
        return ResponseEntity.ok(teamService.getTeamInviteCode(email, teamId));
    }

    @PostMapping(value = "/store", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "팀 가맹점 추가")
    public ResponseEntity<TeamCreateStoreRes> createStore(
            @RequestPart("request") TeamCreateStoreReq req,
            @RequestPart(value = "image", required = false) MultipartFile image,
            @RequestHeader String access,
            @AuthenticationPrincipal CustomUserDetails userDetails) throws IOException {
        return ResponseEntity.ok(teamService.createStore(req, image));
    }


    @GetMapping("/{teamId}/user")
    @Operation(summary = "팀 유저 조회")
    public List<GetUserOfTeamRes> getUserOfTeam(@PathVariable Long teamId,
                                                @RequestHeader String access,
                                                @AuthenticationPrincipal CustomUserDetails userDetails) {
//        Long userId = accessToken.getUserId();
        Long userId = userDetails.getUserId();
        return teamService.getUsersOfTeam(teamId, userId);
    }


    @GetMapping("/{teamId}")
    @Operation(summary = "팀 상세 정보 조회")
    public ResponseEntity<TeamDetailRes> getTeamDetails(@PathVariable Long teamId,
                                                        @RequestHeader String access,
                                                        @AuthenticationPrincipal CustomUserDetails userDetails) {
//        Long userId = accessToken.getUserId();
        Long userId = userDetails.getUserId();
        return ResponseEntity.ok(teamService.getTeamDetails(teamId, userId));
    }


    @PostMapping(value = "/signup", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "팀 생성")
    public ResponseEntity<TeamCreateRes> createTeam(@RequestPart("request") TeamCreateReq request,
                                                    @RequestPart(value = "image", required = false) MultipartFile image,
                                                    @RequestHeader String access,
                                                    @AuthenticationPrincipal CustomUserDetails userDetails) throws IOException {
        Long userId = userDetails.getUserId();
        return ResponseEntity.ok(teamService.createTeam(request, userId, image));
    }

    @GetMapping("/myTeams")
    @Operation(summary = "<b>나의 팀 리스트")
    public ResponseEntity<List<TeamRes>> getMyTeams(@RequestHeader String access,
                                                    @AuthenticationPrincipal CustomUserDetails userDetails)
//    @AuthenticationPrincipal CustomUserDetails userDetails)

    {
        Long userId = userDetails.getUserId();
        return ResponseEntity.ok(teamService.getMyTeams(userId));
    }

    @GetMapping("/{teamId}/stores")
    @Operation(summary = "팀 가맹점 조회")
    public ResponseEntity<List<StoresRes>> getMyTeamStores(@RequestHeader String access,@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long teamId) {
//        Long userId = userDetails.getUserId();

        Long userId = userDetails.getUserId();
        return ResponseEntity.ok(teamService.getMyTeamStores(teamId, userId));
    }


    @GetMapping("/public-teams")
    @Operation(summary = "<b>퍼블릭 팀 리스트 조회")
    public ResponseEntity<List<PublicTeamsRes>> getPublicTeams(@RequestHeader String access,
                                                               @AuthenticationPrincipal CustomUserDetails userDetails) {
        String email = userDetails.getUsername();
        return ResponseEntity.ok(teamService.getPublicTeams(email));
    }

    @GetMapping("/public-teams/{keyword}")
    @Operation(summary = "<b>퍼블릭 팀 검색")
    public ResponseEntity<List<PublicTeamsRes>> getPublicTeamsByKeyword(@RequestHeader String access,
            @PathVariable String keyword) {
        return ResponseEntity.ok(teamService.getPublicTeamsByKeyword(keyword));
    }

    @GetMapping("/public-team/{teamId}")
    @Operation(summary = "퍼블릭 팀 디테일")
    public ResponseEntity<PublicTeamDetailRes> getPublicTeamDetail(@RequestHeader String access,
            @AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable long teamId) {
        String email = userDetails.getUsername();
        return ResponseEntity.ok(teamService.getPublicTeamDetail(email, teamId));
    }

    @GetMapping("/public-team/2km")
    @Operation(summary = "2km 이내의 퍼블릭 팀 조회")
    public ResponseEntity<List<PublicTeams2kmRes>> get2kmPublicTeams(@RequestHeader String access,
            @AuthenticationPrincipal CustomUserDetails userDetails, @RequestParam float latitude, @RequestParam float longitude) {
        String email = userDetails.getUsername();
        return ResponseEntity.ok(teamService.get2kmPublicTeams(email, latitude, longitude));
    }
}
