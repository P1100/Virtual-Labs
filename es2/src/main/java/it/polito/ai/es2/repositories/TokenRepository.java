package it.polito.ai.es2.repositories;

import it.polito.ai.es2.entities.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface TokenRepository extends JpaRepository<Token, String> {
  List<Token> findAllByExpiryDateBeforeOrderByExpiryDate(Timestamp expiryDate); //per selezionare quelli scaduti

  List<Token> findAllByExpiryDateBeforeOrderByExpiryDateDesc(Timestamp expiryDate); //per selezionare quelli scaduti

  List<Token> findAllByTeamId(Long teamId); //per selezionare quelli legati ad un team
}
