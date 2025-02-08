package com.d111.PrePay.service;

import com.d111.PrePay.RequestStatus;
import com.d111.PrePay.dto.request.*;
import com.d111.PrePay.dto.respond.GetUserOfTeamRes;
import com.d111.PrePay.dto.respond.StoresRes;
import com.d111.PrePay.dto.respond.TeamDetailRes;
import com.d111.PrePay.dto.respond.TeamRes;
import com.d111.PrePay.dto.request.TeamCreateStoreReq;
import com.d111.PrePay.dto.request.TeamCreateReq;
import com.d111.PrePay.dto.respond.*;
import com.d111.PrePay.model.Team;
import com.d111.PrePay.model.TeamStore;
import com.d111.PrePay.model.User;
import com.d111.PrePay.model.UserTeam;
import com.d111.PrePay.repository.TeamRepository;
import com.d111.PrePay.repository.TeamStoreRepository;
import com.d111.PrePay.repository.UserRepository;
import com.d111.PrePay.repository.UserTeamRepository;
import com.d111.PrePay.model.*;
import com.d111.PrePay.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j
public class TeamService {

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final UserTeamRepository userTeamRepository;
    private final TeamStoreRepository teamStoreRepository;
    private final StoreRepository storeRepository;
    private final ChargeRequestRepository chargeRequestRepository;
    private final PartyRequestRepository partyRequestRepository;
    private final ImageService imageService;

    // 팀 이미지 업로드
    @Transactional
    public UploadImageRes uploadImage(TeamIdReq req, MultipartFile image) {
        Team team = teamRepository.findById(req.getTeamId()).orElseThrow();
        if (image != null && !image.isEmpty()) {
            String imgUrl;
            try {
                imgUrl = imageService.uploadImage(image, req.getTeamId());
                imageService.uploadImage(image, team.getId());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            team.setTeamImgUrl(imgUrl);
        }

        UploadImageRes uploadImageRes = UploadImageRes.builder()
                .teamId(team.getId())
                .teamName(team.getTeamName())
                .imgUrl(team.getTeamImgUrl())
                .build();

        return uploadImageRes;
    }


    // 팀 사용자 추방
    public void banUser(BanUserReq req) {

        UserTeam findUserTeam = userTeamRepository.findByTeam_IdAndUser_Email(req.getTeamId(), req.getBanUserEmail())
                .orElseThrow();
        userTeamRepository.delete(findUserTeam);
    }


    // 팀 나가기
    public void exitTeam(Long userId, TeamIdReq req) {
        UserTeam findUserTeam = userTeamRepository.findByTeamIdAndUserId(req.getTeamId(), userId)
                .orElseThrow();
        userTeamRepository.delete(findUserTeam);
    }


    // 팀 회식 권한 요청 처리
    @Transactional
    public PartyConfirmRes confirmPrivilege(PartyConfirmReq req) {
        PartyRequest findPartyRequest = partyRequestRepository.findById(req.getPartyRequestId())
                .orElseThrow();
        if (req.isAccept()) {
            findPartyRequest.setRequestStatus(RequestStatus.Approved);
        } else {
            findPartyRequest.setRequestStatus(RequestStatus.Refused);
        }

        PartyConfirmRes partyConfirmRes = PartyConfirmRes.builder()
                .partyRequestId(findPartyRequest.getId())
                .requestStatus(findPartyRequest.getRequestStatus())
                .build();
        return partyConfirmRes;
    }


    // 팀 회식 권한 요청
    public PartyRequestRes privilegeRequest(Long userId, TeamIdReq req) {
        UserTeam findUserTeam = userTeamRepository.findByTeamIdAndUserId(req.getTeamId(), userId)
                .orElseThrow();
        PartyRequest savePartyRequest = partyRequestRepository.save(PartyRequest.builder()
                .requestDate(System.currentTimeMillis())
                .requestStatus(RequestStatus.Waiting)
                .statusChangedDate(0)
                .userTeam(findUserTeam)
                .build());
        PartyRequestRes partyRequestRes = PartyRequestRes.builder()
                .partyRequestId(savePartyRequest.getId())
                .requestDate(savePartyRequest.getRequestDate())
                .requestStatus(savePartyRequest.getRequestStatus())
                .statusChangedDate(savePartyRequest.getStatusChangedDate())
                .build();
        return partyRequestRes;
    }


    // 팀 가맹점 잔액 충전 요청
    public ChargeRes chargeRequest(ChargeReq req) {
        TeamStore findTeamStore = teamStoreRepository.findByTeamIdAndStoreId(req.getTeamId(), req.getStoreId())
                .orElseThrow();
        ChargeRequest saveChargeRequest = chargeRequestRepository.save(ChargeRequest.builder()
                .requestStatus(RequestStatus.Waiting)
                .requestPrice(req.getRequestPrice())
                .requestDate(System.currentTimeMillis())
                .teamStore(findTeamStore)
                .build());
        ChargeRes chargeRes = ChargeRes.builder()
                .chargeRequestId(saveChargeRequest.getId())
                .RequestStatus(saveChargeRequest.getRequestStatus())
                .requestPrice(saveChargeRequest.getRequestPrice())
                .requestDate(saveChargeRequest.getRequestDate())
                .build();

        return chargeRes;
    }


    // 팀 비밀번호를 이용한 팀 가입
    // 확인
    @Transactional
    public GetUserOfTeamRes signInTeam(Long userId, SignInTeamReq req) {
        Team findTeam = teamRepository.findByTeamPassword(req.getTeamPassword())
                .orElseThrow(() -> new RuntimeException("일치하는 팀이 없습니다."));

        User findUser = userRepository.findById(userId).orElseThrow();

        if (userTeamRepository.existsByUserAndTeam(findUser, findTeam)) {
            log.error("이미 가입된 팀입니다.");
            throw new RuntimeException();
        }
        UserTeam userTeam = UserTeam.builder()
                .team(findTeam)
                .user(findUser)
                .privilege(false)
                .usageCount(0)
                .usedAmount(0)
                .position(false)
                .build();
        userTeamRepository.save(userTeam);
        GetUserOfTeamRes getUserOfTeamRes = new GetUserOfTeamRes(userTeam);
        return getUserOfTeamRes;
    }


    // 팀 회식 권한 부여
    @Transactional
    public GrantPrivilegeRes grantPrivilege(GrantPrivilegeReq req) {
        UserTeam findUserTeam = userTeamRepository.findByTeamIdAndUserId(req.getTeamId(), req.getChangeUserId())
                .orElseThrow();
        findUserTeam.setPrivilege(req.isPrivilege());

        GrantPrivilegeRes grantPrivilegeRes = GrantPrivilegeRes.builder()
                .changeUserId(req.getChangeUserId())
                .teamId(req.getTeamId())
                .privilege(findUserTeam.isPrivilege())
                .build();

        return grantPrivilegeRes;
    }


    // 팀 운영자 권한 부여
    // 확인
    @Transactional
    public GrantAdminPositionRes grantAdminPosition(GrantAdminPositionReq req) {
        UserTeam findUserTeam = userTeamRepository.findByTeamIdAndUserId(req.getTeamId(), req.getChangeUserId())
                .orElseThrow();
        findUserTeam.setPosition(req.isPosition());
        GrantAdminPositionRes grantAdminPositionRes = GrantAdminPositionRes.builder()
                .changeUserId(req.getChangeUserId())
                .teamId(req.getTeamId())
                .position(findUserTeam.isPosition())
                .build();

        return grantAdminPositionRes;
    }


    // 팀 한도 변경
    // 확인
    @Transactional
    public TeamDetailRes changeDailyPriceLimit(ChangeDailyPriceLimitReq req, Long userId) {
        Team findTeam = teamRepository.findById(req.getTeamId()).orElseThrow();
        findTeam.setDailyPriceLimit(req.getDailyPriceLimit());
        UserTeam userTeam = userTeamRepository.findByTeamIdAndUserId(findTeam.getId(), userId)
                .orElseThrow();

        TeamDetailRes teamDetailRes = new TeamDetailRes(findTeam, userTeam);
        return teamDetailRes;
    }


    // 팀 초대 코드 생성
    // 확인
    @Transactional
    public TeamDetailRes generateInviteCode(Long userId, TeamIdReq req) {
        Team team = teamRepository.findById(req.getTeamId()).orElseThrow();
        String password = generateRandomPassword();
        team.setTeamPassword(password);
        TeamDetailRes teamDetailRes = TeamDetailRes.builder()
                .teamId(team.getId())
                .teamName(team.getTeamName())
                .countLimit(team.getCountLimit())
                .teamPassword(team.getTeamPassword())
                .dailyPriceLimit(team.getDailyPriceLimit())
                .publicTeam(team.isPublicTeam())
                .teamMessage(team.getTeamMessage())

                .build();
        return teamDetailRes;
    }


    // 팀 가맹점 추가
    // 확인
    @Transactional
    public TeamCreateStoreRes createStore(TeamCreateStoreReq req) {
        Team findTeam = teamRepository.findById(req.getTeamId()).orElseThrow();
        Store findStore = storeRepository.findById(req.getStoreId()).orElseThrow();

        TeamStore teamStore = new TeamStore(findTeam, findStore, req.getBalance());
        TeamStore savedTeamStore = teamStoreRepository.save(teamStore);

        TeamCreateStoreRes teamCreateStoreRes = TeamCreateStoreRes.builder()
                .teamStoreId(savedTeamStore.getId())
                .teamId(findTeam.getId())
                .storeId(findStore.getId())
                .teamStoreBalance(savedTeamStore.getTeamStoreBalance())
                .build();

        return teamCreateStoreRes;
    }


    // 팀 유저 조회
    // 확인
    public List<GetUserOfTeamRes> getUsersOfTeam(Long teamId, Long userId) {
        Team findTeam = teamRepository.findById(teamId).orElseThrow();
        List<UserTeam> findUserTeams = findTeam.getUserTeams();
        List<GetUserOfTeamRes> result = new ArrayList<>();
        for (UserTeam findUserTeam : findUserTeams) {
            GetUserOfTeamRes getUserOfTeamRes = new GetUserOfTeamRes(findUserTeam);
            result.add(getUserOfTeamRes);
        }

        return result;

    }


    // 팀 상세 조회
    // 확인
    public TeamDetailRes getTeamDetails(Long teamId, Long userId) {
        UserTeam findUserTeam = userTeamRepository.findByTeamIdAndUserId(teamId, userId)
                .orElseThrow(() -> new RuntimeException("유저팀을 찾을 수 없습니다."));

        Team findTeam = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("팀을 찾을 수 없습니다"));
        TeamDetailRes res = TeamDetailRes.builder()
                .teamId(teamId)
                .teamName(findTeam.getTeamName())
                .dailyPriceLimit(findTeam.getDailyPriceLimit())
                .publicTeam(findTeam.isPublicTeam())
                .countLimit(findTeam.getCountLimit())
                .teamMessage(findTeam.getTeamMessage())
                .position(findUserTeam.isPosition())
                .teamPassword(findTeam.getTeamPassword())
                .build();

        return res;
    }


    // 팀 생성
    // 확인
    public TeamCreateRes createTeam(TeamCreateReq request, Long userId, MultipartFile image) {
        String teamPassword;
        if (!request.isPublicTeam()) {
            teamPassword = generateRandomPassword();
        } else {
            teamPassword = null;
        }

        Team team = Team.builder()
                .teamName(request.getTeamName())
                .publicTeam(request.isPublicTeam())
                .teamPassword(teamPassword)
                .dailyPriceLimit(request.getDailyPriceLimit())
                .countLimit(request.getCountLimit())
                .teamMessage(request.getTeamMessage())
                .teamInitializer(userRepository.findById(userId).orElseThrow(() ->new NoSuchElementException("유저를 찾을 수 없습니다.")))
                .build();


        Team savedTeam = teamRepository.save(team);

        if (image != null && !image.isEmpty()) {
            String imgUrl =null;
            try{
               imgUrl = imageService.uploadImage(image, team.getId());
            }catch (IOException e) {
                throw new RuntimeException(e);
            }

            team.setTeamImgUrl(imgUrl);
            teamRepository.save(team);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("유저를 찾을 수 없습니다."));

        UserTeam userTeam = UserTeam.builder()
                .team(savedTeam)
                .user(user)
                .position(true)
                .privilege(true)
                .usedAmount(0)
                .usageCount(0)
                .build();

        userTeamRepository.save(userTeam);
        TeamCreateRes teamCreateRes = TeamCreateRes.builder()
                .teamId(team.getId())
                .build();
        return teamCreateRes;
    }

    //나의 팀 전체 조회
    //확인
    public List<TeamRes> getMyTeams(long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        List<UserTeam> userTeams = user.getUserTeams();
        List<TeamRes> resultList = new ArrayList<>();
        for (UserTeam userTeam : userTeams) {
            TeamRes teamRes = new TeamRes(userTeam);
            resultList.add(teamRes);
        }
        return resultList;
    }


    //팀의 가게 조회
    //확인
    public List<StoresRes> getMyTeamStores(long teamId, long userId) {
        Team team = teamRepository.findById(teamId).orElseThrow();
        List<TeamStore> teamStores = team.getTeamStores();
        List<StoresRes> resultList = new ArrayList<>();
        for (TeamStore teamStore : teamStores) {
            StoresRes storesRes = new StoresRes(teamStore);
            resultList.add(storesRes);
        }
        return resultList;
    }


    //팀 가맹점의 좌표 조회
    public List<StoresCorRes> getStoresCor(long teamId, long userId) {
        Team team = teamRepository.findById(teamId).orElseThrow();
        List<TeamStore> list = teamStoreRepository.findTeamStoresByTeam(team);
        List<StoresCorRes> result = new ArrayList<>();
        for (TeamStore teamStore : list) {
            Store store = teamStore.getStore();
            StoresCorRes storesCorRes = new StoresCorRes(store);
            result.add(storesCorRes);
        }
        return result;
    }

    //퍼블릭 팀 리스트 조회
    // 완료
    public List<PublicTeamsRes> getPublicTeams() {
        List<Team> teams = teamRepository.findTeamsByPublicTeam(true);
        List<PublicTeamsRes> resultList = new ArrayList<>();
        for (Team team : teams) {
            PublicTeamsRes publicTeamsRes = new PublicTeamsRes(team);
            publicTeamsRes.setTeamInitializerNickname(team.getTeamInitializer().getNickname());
            resultList.add(publicTeamsRes);
        }
        return resultList;
    }

    //완료
    public List<PublicTeamsRes> getPublicTeamsByKeyword(String keyword) {
        List<Team> teams = teamRepository.findTeamsByTeamNameContaining(keyword);
        List<PublicTeamsRes> resultList = new ArrayList<>();
        for (Team team : teams) {
            PublicTeamsRes publicTeamsRes = new PublicTeamsRes(team);
            publicTeamsRes.setTeamInitializerNickname(team.getTeamInitializer().getNickname());
            resultList.add(publicTeamsRes);
        }
        return resultList;
    }

    // 랜덤 비밀번호 생성
    public String generateRandomPassword() {
        String password = UUID.randomUUID().toString();
        return password;
    }


}