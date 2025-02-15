package com.d111.PrePay.repository;

import com.d111.PrePay.model.QTeam;
import com.d111.PrePay.model.QUser;
import com.d111.PrePay.model.QUserTeam;
import com.d111.PrePay.model.UserTeam;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

@Repository

public class UserTeamCustomImpl implements UserTeamCustom{
    private final JPAQueryFactory queryFactory;

    public UserTeamCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public UserTeam findUserTeamByTeamIdAndUserIdWithTeam(long teamId, long userId) {
        QUserTeam userTeam = QUserTeam.userTeam;
        QUser user = QUser.user;
        QTeam team = QTeam.team;
        return queryFactory
                .selectFrom(userTeam)
                .leftJoin(userTeam.user, user)
                .leftJoin(userTeam.team, team).fetchJoin()
                .where(user.id.eq(userId),
                        userTeam.team.id.eq(teamId))
                .fetchOne();
    }
}
