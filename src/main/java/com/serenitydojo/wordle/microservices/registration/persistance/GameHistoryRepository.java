package com.serenitydojo.wordle.microservices.registration.persistance;

import com.serenitydojo.wordle.microservices.domain.GameHistory;
import com.serenitydojo.wordle.microservices.domain.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameHistoryRepository extends JpaRepository<GameHistory, Long> {
    List<GameHistory> findByPlayer(Player player);
}
