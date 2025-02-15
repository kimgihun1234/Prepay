package com.d111.PrePay.repository;

import com.d111.PrePay.model.QTeam;
import com.d111.PrePay.model.QUser;
import com.d111.PrePay.model.Team;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TeamRepoCustomImpl implements TeamRepoCustom {

    private final JPAQueryFactory queryFactory;

    public TeamRepoCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Team> findTeamsbyKeywordNoN(String keyword) {
        QTeam team = QTeam.team;
        QUser user = QUser.user;
        return queryFactory
                .selectFrom(team)
                .join(team.teamInitializer, user).fetchJoin()
                .where(team.teamName.containsIgnoreCase(keyword))
                .fetch();
    }
}
