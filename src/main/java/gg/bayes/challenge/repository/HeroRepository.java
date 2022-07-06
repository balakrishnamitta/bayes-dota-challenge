package gg.bayes.challenge.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import gg.bayes.challenge.entity.Hero;

/**
 * Hero repository.
 *
 */
@Repository
public interface HeroRepository extends CrudRepository<Hero, String> {

}