use test_db;
INSERT INTO store (latitude, longitude, user_register_permission, address, store_img_url, store_name, type,
                   store_description)
VALUES (36.10680576, 128.418318, 0, '경북 구미시 인동가산로 20',
        'https://firebasestorage.googleapis.com/v0/b/kyung0216-10d14.appspot.com/o/%ED%88%AC%EC%8D%B8.png?alt=media&token=7a8d40e4-c4bd-48ec-a05c-8930e54ff522',
        '투썸플레이스 인동점', '카페', '티라미수가 맛있는 브랜드 투썸플레이스'),
       (36.10556191, 128.4207523, 0, '경북 구미시 여헌로7길 46',
        'https://firebasestorage.googleapis.com/v0/b/kyung0216-10d14.appspot.com/o/%ED%95%9C%EB%8F%88%EC%B0%B8%EC%86%A3%EA%BC%AC%EA%B8%B0.jpg?alt=media&token=2c43cf8a-8c91-4c2a-9d2d-2d5520ff4db3',
        '한돈참숯꼬기 인동점', '식당', '저렴한 한돈구이집 한돈참숯꼬기 인동점입니다.'),
       (36.10747075, 128.4193179, 0, '경북 구미시 인동중앙로1길 16',
        'https://firebasestorage.googleapis.com/v0/b/kyung0216-10d14.appspot.com/o/%EC%A0%9C%EC%9D%BC%EC%88%9C%EB%8C%80.jpg?alt=media&token=2dbf36e9-b3d0-4a29-84d1-96052416c2de',
        '전국제일순대 인동직영점', '식당', '든든한 가성비의 순대국밥집'),
       (36.10266058, 128.4212725, 0, '경북 구미시 진평2길 27',
        'https://firebasestorage.googleapis.com/v0/b/kyung0216-10d14.appspot.com/o/%EC%B0%A8%EC%9D%B4%EC%95%A4%EC%9B%8D.jpg?alt=media&token=b3931fd0-59f6-4e7b-a733-7e2b4538c2ea',
        '차이앤웍', '식당', '싸고 양 많은 중국집 차이앤웍'),
       (36.10960074, 128.4158019, 0, '경북 구미시 3공단3로 302',
        'https://firebasestorage.googleapis.com/v0/b/kyung0216-10d14.appspot.com/o/%EC%9D%B4%EB%94%94%EC%95%BC.png?alt=media&token=12734fe3-8cd9-4b9a-91b6-16a261ba936c',
        '삼성전자2공장 이디야', '카페', '삼성전자2공장 내부의 이디야'),
       (36.10262024, 128.4235472, 0, '경북 구미시 인동북길 43',
        'https://firebasestorage.googleapis.com/v0/b/kyung0216-10d14.appspot.com/o/%EC%97%90%EC%9D%B4%EB%B0%94%EC%9A%B0%ED%8A%B8.png?alt=media&token=fd2ad24e-d60f-4ab8-af19-f3e8743f8e5b',
        '에이바우트커피 인동점', '카페', '제주도의 커피 브랜드 에이바우트커피 인동점'),
       (36.10075104, 128.4249629, 0, '경북 구미시 인동북길 67',
        'https://firebasestorage.googleapis.com/v0/b/kyung0216-10d14.appspot.com/o/%EB%B3%B5%ED%84%B0%EC%A7%84%EC%A7%91.jpg?alt=media&token=029f264d-5b88-40fd-ae89-c6260f7fc2fc',
        '복터진집 본점', '식당', '복어조리명장의 백년가게 복터진집 본점'),
       (36.10817914, 128.4182366, 0, '경북 구미시 인동가산로 5',
        'https://firebasestorage.googleapis.com/v0/b/kyung0216-10d14.appspot.com/o/%ED%95%B5%EB%B0%A5.png?alt=media&token=18c648a7-a891-466a-a7ab-058674723d8b',
        '핵밥 구미인동점', '식당', '간단하게 먹기 좋은 핵밥 구미인동점'),
       (36.10712319, 128.4180014, 0, '경북 구미시 인동가산로 14',
        'https://firebasestorage.googleapis.com/v0/b/kyung0216-10d14.appspot.com/o/%EB%AF%B8%EC%9A%A9%EC%8B%A4.png?alt=media&token=4aa29380-ed58-4923-9a54-fd6a9a36a4fc',
        '글로우업헤어', '미용실', '스타일링 받기 좋은 글로우업헤어'),
       (36.10730119, 128.4195667, 0, '경북 구미시 인동중앙로1길 12',
        'https://firebasestorage.googleapis.com/v0/b/kyung0216-10d14.appspot.com/o/%EA%B5%90%EB%B0%98.png?alt=media&token=7f86b229-ec45-43e6-803c-dc681658d01d',
        '교반인동점', '식당', '맛 좋은 한식집 교반인동점');


INSERT INTO team (team_name,
                  public_team,
                  daily_price_limit,
                  count_limit,
                  team_message,
                  code_gen_date,
                  color,
                  team_img_url,
                  test_db.team.user_id,
                  gen_date)
VALUES ('기훈솔루션', 0, 8000, 0, '오늘 걷지 않으면 내일은 뛰어야한다!', 0, '#21c8f1', null, 1, 1739700507000),
       ('아이유애나', 1, 6000, 0, '아이유가 좋은 이유~', 0, '#9f75b9',
        'https://firebasestorage.googleapis.com/v0/b/kyung0216-10d14.appspot.com/o/%EC%95%84%EC%9D%B4%EC%9C%A0.webp?alt=media&token=527551e5-ce4e-45c5-9164-d6389a8297b3',
        8, 1739700507000),
       ('알고리즘 스터디', 0, 7000, 0, '일일 1골드 B형 가보자!', 0, '#e89800', NULL, 1, 1739700507000),
       ('구미 취준생 모여라', 1, 10000, 0, '힘들겠지만 오늘도 파이팅….', 0, '#777777',
        'https://firebasestorage.googleapis.com/v0/b/kyung0216-10d14.appspot.com/o/%EC%9D%B4%EB%94%94%EC%95%BC%20%ED%8C%80.png?alt=media&token=8d998bf7-535d-4670-bbff-a953f673a9e7',
        10, 1739700507000),
       ('삼성전자 개발팀', 0, 10000, 0, '삼성전자 개발팀을 위한 선결제 그룹입니다.', 0, '#0c4da2', NULL, 8, 1739700507000),
       ('핵밥 먹자용', 0, 10000, 0, '싸피 12기 파이팅', 0, '#21c8f1',
        'https://firebasestorage.googleapis.com/v0/b/kyung0216-10d14.appspot.com/o/%ED%95%B5%EB%B0%A5.png?alt=media&token=dc85a7b3-1d90-418e-a469-',
        8, 1739700507000),
       ('살롱드미뇽', 0, 20000, 0, '머리를 자릅시다.', 0, '#21c8f1',
        'https://firebasestorage.googleapis.com/v0/b/kyung0216-10d14.appspot.com/o/%EC%82%B4%EB%A1%B1%EB%93%9C%EB%AF%B8%EB%87%BD.jpg?alt=media&token=2e516725-f6d2-42cb-84bc-e1e36200425d',
        5, 1739700507000),
       ('독서동아리', 0, 6000, 0, '책 읽고 밥 먹어요', 0, '#21c8f1', NULL, 3, 1739700507000),
       ('코딩하는사람들', 1, 10000, 0, '모각코 ㄱㄱㄱ 같이 코딩해요', 0, '#21c8f1',
        'https://firebasestorage.googleapis.com/v0/b/kyung0216-10d14.appspot.com/o/%EC%BD%94%EB%94%A9%20%EC%82%AC%EC%A7%84.jpg?alt=media&token=a3b1eddb-aa64-47db-b479-2176b911234a',
        2, 1739700507000),
       ('투썸 같이먹어요', 1, 5000, 0, '커피 마시고 좋은 하루 보내세요', 0, '#21c8f1',
        'https://firebasestorage.googleapis.com/v0/b/kyung0216-10d14.appspot.com/o/%ED%88%AC%EC%8D%B8.png?alt=media&token=7a8d40e4-c4bd-48ec-a05c-',
        11, 1739700507000),
       ('치킨 먹자용', 1, 25000, 0, '치맥 한번 하시죠', 0, '#21c8f1',
        'https://firebasestorage.googleapis.com/v0/b/kyung0216-10d14.appspot.com/o/%EC%8B%A0%EC%A7%B1%EA%B5%AC.png?alt=media&token=b3d50446-00fe-455e-',
        4, 1739700507000),
       ('구미친구들', 0, 8500, 0, '구미 밥친구 모임', 0, '#21c8f1',
        'https://firebasestorage.googleapis.com/v0/b/kyung0216-10d14.appspot.com/o/%EC%8B%A0%EC%A7%B1%EA%B5%AC.png?alt=media&token=b3d50446-00fe-455e-',
        9, 1739700507000);

-- userService.userSignUp(new UserSignUpReq("1@gmail.com", "123", "김기훈"));
-- userService.userSignUp(new UserSignUpReq("2@gmail.com", "123", "조성윤"));
-- userService.userSignUp(new UserSignUpReq("3@gmail.com", "123", "차현우"));
-- userService.userSignUp(new UserSignUpReq("4@gmail.com", "123", "서현석"));
-- userService.userSignUp(new UserSignUpReq("5@gmail.com", "123", "경이현"));
-- userService.userSignUp(new UserSignUpReq("6@gmail.com", "123", "김성수"));
-- userService.userSignUp(new UserSignUpReq("7@gmail.com", "123", "이재용"));
-- userService.userSignUp(new UserSignUpReq("8@gmail.com", "123", "아이유"));
-- userService.userSignUp(new UserSignUpReq("9@gmail.com", "123", "반짝이는바지"));
-- userService.userSignUp(new UserSignUpReq("10@gmail.com", "123", "싸피최고컨설턴트"));
-- userService.userSignUp(new UserSignUpReq("11@gmail.com", "123", "구미꿀주먹"));
-- userService.userSignUp(new UserSignUpReq("12@gmail.com", "123", "배고픈취준생"));


INSERT INTO user_team (is_like, position, privilege, usage_count, used_amount, team_id, user_id)
VALUES (false, true, true, 0, 0, 1, 1); -- 기훈 솔루션 생성자 김기훈 추가
INSERT INTO user_team (is_like, position, privilege, usage_count, used_amount, team_id, user_id)
VALUES (false, false, false, 0, 0, 1, 2); -- 기훈 솔루션 조성윤 추가
INSERT INTO user_team (is_like, position, privilege, usage_count, used_amount, team_id, user_id)
VALUES (false, false, false, 0, 0, 1, 3); -- 기훈 솔루션 차현우 추가
INSERT INTO user_team (is_like, position, privilege, usage_count, used_amount, team_id, user_id)
VALUES (false, false, false, 0, 0, 1, 4); -- 기훈 솔루션 서현석 추가
INSERT INTO user_team (is_like, position, privilege, usage_count, used_amount, team_id, user_id)
VALUES (false, false, false, 0, 0, 1, 5); -- 기훈 솔루션 경이현 추가
INSERT INTO user_team (is_like, position, privilege, usage_count, used_amount, team_id, user_id)
VALUES (false, false, false, 0, 0, 1, 6); -- 기훈 솔루션 김성수 추가

INSERT INTO user_team (is_like, position, privilege, usage_count, used_amount, team_id, user_id)
VALUES (false, true, true, 0, 0, 2, 8); -- 아이유애나 생성자 아이유 추가
INSERT INTO user_team (is_like, position, privilege, usage_count, used_amount, team_id, user_id)
VALUES (false, false, false, 0, 0, 2, 9); -- 아이유애나 반짝이는바지 추가
INSERT INTO user_team (is_like, position, privilege, usage_count, used_amount, team_id, user_id)
VALUES (false, false, false, 0, 0, 2, 11); -- 아이유애나 구미꿀주먹 추가
INSERT INTO user_team (is_like, position, privilege, usage_count, used_amount, team_id, user_id)
VALUES (false, false, false, 0, 0, 2, 12); -- 아이유애나 배고픈취준생 추가
INSERT INTO user_team (is_like, position, privilege, usage_count, used_amount, team_id, user_id)
VALUES (false, false, false, 0, 0, 2, 5); -- 아이유애나 경이현 추가
INSERT INTO user_team (is_like, position, privilege, usage_count, used_amount, team_id, user_id)
VALUES (false, false, false, 0, 0, 2, 6); -- 아이유애나 김성수 추가

INSERT INTO user_team (is_like, position, privilege, usage_count, used_amount, team_id, user_id)
VALUES (false, true, true, 0, 0, 3, 1); -- 알고리즘 스터디 생성자 김기훈 추가
INSERT INTO user_team (is_like, position, privilege, usage_count, used_amount, team_id, user_id)
VALUES (false, false, false, 0, 0, 3, 2); -- 알고리즘 스터디  조성윤 추가
INSERT INTO user_team (is_like, position, privilege, usage_count, used_amount, team_id, user_id)
VALUES (false, false, false, 0, 0, 3, 3); -- 알고리즘 스터디  차현우 추가
INSERT INTO user_team (is_like, position, privilege, usage_count, used_amount, team_id, user_id)
VALUES (false, false, false, 0, 0, 3, 4); -- 알고리즘 스터디  서현석 추가
INSERT INTO user_team (is_like, position, privilege, usage_count, used_amount, team_id, user_id)
VALUES (false, false, false, 0, 0, 3, 5); -- 알고리즘 스터디  경이현 추가
INSERT INTO user_team (is_like, position, privilege, usage_count, used_amount, team_id, user_id)
VALUES (false, false, false, 0, 0, 3, 6); -- 알고리즘 스터디  김성수 추가

INSERT INTO user_team (is_like, position, privilege, usage_count, used_amount, team_id, user_id)
VALUES (false, true, true, 0, 0, 4, 10); -- 구미 취준생 모여라
INSERT INTO user_team (is_like, position, privilege, usage_count, used_amount, team_id, user_id)
VALUES (false, false, false, 0, 0, 4, 2); --
INSERT INTO user_team (is_like, position, privilege, usage_count, used_amount, team_id, user_id)
VALUES (false, false, false, 0, 0, 4, 3); --
INSERT INTO user_team (is_like, position, privilege, usage_count, used_amount, team_id, user_id)
VALUES (false, false, false, 0, 0, 4, 4); --
INSERT INTO user_team (is_like, position, privilege, usage_count, used_amount, team_id, user_id)
VALUES (false, false, false, 0, 0, 4, 5); --
INSERT INTO user_team (is_like, position, privilege, usage_count, used_amount, team_id, user_id)
VALUES (false, false, false, 0, 0, 4, 6); --
INSERT INTO user_team (is_like, position, privilege, usage_count, used_amount, team_id, user_id)
VALUES (false, false, false, 0, 0, 4, 1);

INSERT INTO user_team (is_like, position, privilege, usage_count, used_amount, team_id, user_id)
VALUES (false, true, true, 0, 0, 5, 7); -- 삼성전자 개발팀
INSERT INTO user_team (is_like, position, privilege, usage_count, used_amount, team_id, user_id)
VALUES (false, false, false, 0, 0, 5, 2); --
INSERT INTO user_team (is_like, position, privilege, usage_count, used_amount, team_id, user_id)
VALUES (false, false, false, 0, 0, 5, 3); --
INSERT INTO user_team (is_like, position, privilege, usage_count, used_amount, team_id, user_id)
VALUES (false, false, false, 0, 0, 5, 4); --
INSERT INTO user_team (is_like, position, privilege, usage_count, used_amount, team_id, user_id)
VALUES (false, false, false, 0, 0, 5, 5); --
INSERT INTO user_team (is_like, position, privilege, usage_count, used_amount, team_id, user_id)
VALUES (false, false, false, 0, 0, 5, 6); --
INSERT INTO user_team (is_like, position, privilege, usage_count, used_amount, team_id, user_id)
VALUES (false, false, false, 0, 0, 5, 1);

-- 팀에 사람 추가 완료

INSERT INTO team_store (team_store_balance, store_store_id, team_team_id)
VALUES (200000, 1, 5); -- 구미 취준생 / 이디야
INSERT INTO team_store (team_store_balance, store_store_id, team_team_id)
VALUES (2147483647, 5, 4); -- 구미 취준생 / 이디야
INSERT INTO team_store (team_store_balance, store_store_id, team_team_id)
VALUES (10000000, 6, 2); -- 아이유 / 에이바우트
INSERT INTO team_store (team_store_balance, store_store_id, team_team_id)
VALUES (500000, 2, 1);-- 기훈 솔루션 / 한돈
INSERT INTO team_store (team_store_balance, store_store_id, team_team_id)
VALUES (500000, 3, 1); -- 기훈 솔루션 / 제일순대
INSERT INTO team_store (team_store_balance, store_store_id, team_team_id)
VALUES (500000, 4, 1); -- 기훈 솔루션/  차이앤웍
INSERT INTO team_store (team_store_balance, store_store_id, team_team_id)
VALUES (500000, 7, 1); -- 기훈 솔루션 / 복터진집
INSERT INTO team_store (team_store_balance, store_store_id, team_team_id)
VALUES (500000, 10, 1); -- 기훈 솔루션 / 교반
INSERT INTO team_store (team_store_balance, store_store_id, team_team_id)
VALUES (50000, 1, 3); -- 알고리즘 스터디 / 투썸
INSERT INTO team_store (team_store_balance, store_store_id, team_team_id)
VALUES (50000, 6, 3); -- 알고리즘 스터디 / 에이바우트
INSERT INTO team_store (team_store_balance, store_store_id, team_team_id)
VALUES (50000, 3, 3); -- 알고리즘 스터디 / 에이바우트

INSERT INTO team_store (team_store_balance, store_store_id, team_team_id)
VALUES (50000, 6, 9); -- 코딩하는 사람들 / 에이바우트

INSERT INTO team_store (team_store_balance, store_store_id, team_team_id)
VALUES (100000, 1, 10); -- 투썸 같이먹어요 / 투썸

INSERT INTO team_store (team_store_balance, store_store_id, team_team_id)
VALUES (50000, 4, 11);
-- 치킨 먹자용 / 차이앤웍


-- 팀에 상점 추가 완료

-- order_history 테이블에 데이터 삽입
INSERT INTO order_history (company_dinner,
                           refund_requested,
                           total_price,
                           with_draw,
                           order_date,
                           store_store_id,
                           team_team_id,
                           user_user_id)
VALUES (0, -- company_dinner (0 = false, 1 = true)
        0, -- refund_requested (0 = false, 1 = true)
        30000, -- total_price
        0, -- with_draw (0 = false, 1 = true)
        1739507338, -- order_date (Unix timestamp in milliseconds)
        2, -- store_store_id (assuming store with ID 1 exists)
        1, -- team_team_id (assuming team with ID 2 exists)
        3 -- user_user_id (assuming user with ID 3 exists)
       );
-- 방금 삽입된 order_history의 ID를 가져옵니다
SET
    @last_order_history_id = LAST_INSERT_ID();
-- detail_history 테이블에 데이터 삽입
INSERT INTO detail_history (detail_price,
                            quantity,
                            order_history_order_history_id,
                            product)
VALUES (15000, -- detail_price
        2, -- quantity
        @last_order_history_id, -- order_history_order_history_id
        '삼겹살' -- product
       );


-- order_history 테이블에 데이터 삽입
INSERT INTO order_history (company_dinner,
                           refund_requested,
                           total_price,
                           with_draw,
                           order_date,
                           store_store_id,
                           team_team_id,
                           user_user_id)
VALUES (1, -- company_dinner (0 = false, 1 = true)
        0, -- refund_requested (0 = false, 1 = true)
        150000, -- total_price
        0, -- with_draw (0 = false, 1 = true)
        1739507338, -- order_date (Unix timestamp in milliseconds)
        2, -- store_store_id (assuming store with ID 1 exists)
        1, -- team_team_id (assuming team with ID 2 exists)
        1 -- user_user_id (assuming user with ID 3 exists)
       );
-- 방금 삽입된 order_history의 ID를 가져옵니다
SET
    @last_order_history_id = LAST_INSERT_ID();
-- detail_history 테이블에 데이터 삽입
INSERT INTO detail_history (detail_price,
                            quantity,
                            order_history_order_history_id,
                            product)
VALUES (15000, -- detail_price
        10, -- quantity
        @last_order_history_id, -- order_history_order_history_id
        '삼겹살' -- product
       );


-- order_history 테이블에 데이터 삽입
INSERT INTO order_history (company_dinner,
                           refund_requested,
                           total_price,
                           with_draw,
                           order_date,
                           store_store_id,
                           team_team_id,
                           user_user_id)
VALUES (1, -- company_dinner (0 = false, 1 = true)
        0, -- refund_requested (0 = false, 1 = true)
        45000, -- total_price
        0, -- with_draw (0 = false, 1 = true)
        1739507338, -- order_date (Unix timestamp in milliseconds)
        3, -- store_store_id (assuming store with ID 1 exists)
        1, -- team_team_id (assuming team with ID 2 exists)
        5 -- user_user_id (assuming user with ID 3 exists)
       );
-- 방금 삽입된 order_history의 ID를 가져옵니다
SET
    @last_order_history_id = LAST_INSERT_ID();
-- detail_history 테이블에 데이터 삽입
INSERT INTO detail_history (detail_price,
                            quantity,
                            order_history_order_history_id,
                            product)
VALUES (9000, -- detail_price
        5, -- quantity
        @last_order_history_id, -- order_history_order_history_id
        '제일순살국밥' -- product
       );


-- order_history 테이블에 데이터 삽입
INSERT INTO order_history (company_dinner,
                           refund_requested,
                           total_price,
                           with_draw,
                           order_date,
                           store_store_id,
                           team_team_id,
                           user_user_id)
VALUES (0, -- company_dinner (0 = false, 1 = true)
        0, -- refund_requested (0 = false, 1 = true)
        8000, -- total_price
        0, -- with_draw (0 = false, 1 = true)
        1739507338, -- order_date (Unix timestamp in milliseconds)
        3, -- store_store_id (assuming store with ID 1 exists)
        1, -- team_team_id (assuming team with ID 2 exists)
        1 -- user_user_id (assuming user with ID 3 exists)
       );
-- 방금 삽입된 order_history의 ID를 가져옵니다
SET
    @last_order_history_id = LAST_INSERT_ID();
-- detail_history 테이블에 데이터 삽입
INSERT INTO detail_history (detail_price,
                            quantity,
                            order_history_order_history_id,
                            product)
VALUES (8000, -- detail_price
        1, -- quantity
        @last_order_history_id, -- order_history_order_history_id
        '제일국밥' -- product
       );

-- order_history 테이블에 데이터 삽입
INSERT INTO order_history (company_dinner,
                           refund_requested,
                           total_price,
                           with_draw,
                           order_date,
                           store_store_id,
                           team_team_id,
                           user_user_id)
VALUES (1, -- company_dinner (0 = false, 1 = true)
        0, -- refund_requested (0 = false, 1 = true)
        54000, -- total_price
        0, -- with_draw (0 = false, 1 = true)
        1739507338, -- order_date (Unix timestamp in milliseconds)
        10, -- store_store_id (assuming store with ID 1 exists)
        1, -- team_team_id (assuming team with ID 2 exists)
        5 -- user_user_id (assuming user with ID 3 exists)
       );
-- 방금 삽입된 order_history의 ID를 가져옵니다
SET
    @last_order_history_id = LAST_INSERT_ID();
-- detail_history 테이블에 데이터 삽입
INSERT INTO detail_history (detail_price,
                            quantity,
                            order_history_order_history_id,
                            product)
VALUES (9000, -- detail_price
        3, -- quantity
        @last_order_history_id, -- order_history_order_history_id
        '불고기비빔밥' -- product
       );
INSERT INTO detail_history (detail_price,
                            quantity,
                            order_history_order_history_id,
                            product)
VALUES (9000, -- detail_price
        3, -- quantity
        @last_order_history_id, -- order_history_order_history_id
        '육회비빔밥' -- product
       );


-- order_history 테이블에 데이터 삽입
INSERT INTO order_history (company_dinner,
                           refund_requested,
                           total_price,
                           with_draw,
                           order_date,
                           store_store_id,
                           team_team_id,
                           user_user_id)
VALUES (0, -- company_dinner (0 = false, 1 = true)
        0, -- refund_requested (0 = false, 1 = true)
        4500, -- total_price
        0, -- with_draw (0 = false, 1 = true)
        1739507338, -- order_date (Unix timestamp in milliseconds)
        6, -- store_store_id (assuming store with ID 1 exists)
        2, -- team_team_id (assuming team with ID 2 exists)
        11 -- user_user_id (assuming user with ID 3 exists)
       );
-- 방금 삽입된 order_history의 ID를 가져옵니다
SET
    @last_order_history_id = LAST_INSERT_ID();
-- detail_history 테이블에 데이터 삽입
INSERT INTO detail_history (detail_price,
                            quantity,
                            order_history_order_history_id,
                            product)
VALUES (4500, -- detail_price
        1, -- quantity
        @last_order_history_id, -- order_history_order_history_id
        '아메리카노' -- product
       );

-- order_history 테이블에 데이터 삽입
INSERT INTO order_history (company_dinner,
                           refund_requested,
                           total_price,
                           with_draw,
                           order_date,
                           store_store_id,
                           team_team_id,
                           user_user_id)
VALUES (0, -- company_dinner (0 = false, 1 = true)
        0, -- refund_requested (0 = false, 1 = true)
        6500, -- total_price
        0, -- with_draw (0 = false, 1 = true)
        1739507338, -- order_date (Unix timestamp in milliseconds)
        6, -- store_store_id (assuming store with ID 1 exists)
        2, -- team_team_id (assuming team with ID 2 exists)
        12 -- user_user_id (assuming user with ID 3 exists)
       );
-- 방금 삽입된 order_history의 ID를 가져옵니다
SET
    @last_order_history_id = LAST_INSERT_ID();
-- detail_history 테이블에 데이터 삽입
INSERT INTO detail_history (detail_price,
                            quantity,
                            order_history_order_history_id,
                            product)
VALUES (6500, -- detail_price
        1, -- quantity
        @last_order_history_id, -- order_history_order_history_id
        '아이스모카' -- product
       );

-- order_history 테이블에 데이터 삽입
INSERT INTO order_history (company_dinner,
                           refund_requested,
                           total_price,
                           with_draw,
                           order_date,
                           store_store_id,
                           team_team_id,
                           user_user_id)
VALUES (0, -- company_dinner (0 = false, 1 = true)
        0, -- refund_requested (0 = false, 1 = true)
        5500, -- total_price
        0, -- with_draw (0 = false, 1 = true)
        1739507338, -- order_date (Unix timestamp in milliseconds)
        1, -- store_store_id (assuming store with ID 1 exists)
        5, -- team_team_id (assuming team with ID 2 exists)
        7 -- user_user_id (assuming user with ID 3 exists)
       );
-- 방금 삽입된 order_history의 ID를 가져옵니다
SET
    @last_order_history_id = LAST_INSERT_ID();
-- detail_history 테이블에 데이터 삽입
INSERT INTO detail_history (detail_price,
                            quantity,
                            order_history_order_history_id,
                            product)
VALUES (5500, -- detail_price
        1, -- quantity
        @last_order_history_id, -- order_history_order_history_id
        '복숭아아이스티' -- product
       );


-- order_history 테이블에 데이터 삽입
INSERT INTO order_history (company_dinner,
                           refund_requested,
                           total_price,
                           with_draw,
                           order_date,
                           store_store_id,
                           team_team_id,
                           user_user_id)
VALUES (0, -- company_dinner (0 = false, 1 = true)
        0, -- refund_requested (0 = false, 1 = true)
        5500, -- total_price
        0, -- with_draw (0 = false, 1 = true)
        1739507338, -- order_date (Unix timestamp in milliseconds)
        1, -- store_store_id (assuming store with ID 1 exists)
        5, -- team_team_id (assuming team with ID 2 exists)
        6 -- user_user_id (assuming user with ID 3 exists)
       );
-- 방금 삽입된 order_history의 ID를 가져옵니다
SET
    @last_order_history_id = LAST_INSERT_ID();
-- detail_history 테이블에 데이터 삽입
INSERT INTO detail_history (detail_price,
                            quantity,
                            order_history_order_history_id,
                            product)
VALUES (5500, -- detail_price
        1, -- quantity
        @last_order_history_id, -- order_history_order_history_id
        '복숭아아이스티' -- product
       );