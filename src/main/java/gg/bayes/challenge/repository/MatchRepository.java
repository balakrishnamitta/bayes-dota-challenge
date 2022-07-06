package gg.bayes.challenge.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import gg.bayes.challenge.entity.Match;

/**
 * Match repository.
 *
 */
@Repository
public interface MatchRepository extends CrudRepository<Match, Long> {

}