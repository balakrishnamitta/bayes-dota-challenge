package gg.bayes.challenge.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import gg.bayes.challenge.entity.HeroDamageEntity;

/**
 * HeroDamageEntity repository.
 *
 */
@Repository
public interface HeroDamageEntityRepository extends CrudRepository<HeroDamageEntity, Long> {

	/**
	 * Get damages done by specific hero.
	 * @param matchId the match id
	 * @param heroName the hero name
	 * @return list of heros with damages
	 */
	@Query("select h.target.name,sum(h.damage),count(*) from HeroDamageEntity h where h.hero.name=:heroName and h.match.id=:matchId group by h.target")
	List<Object[]> getDamageByMatchIdAndHeroName(Long matchId, String heroName);
}
