package gg.bayes.challenge.service;

import java.util.List;

import gg.bayes.challenge.rest.model.HeroDamage;
import gg.bayes.challenge.rest.model.HeroItems;
import gg.bayes.challenge.rest.model.HeroKills;
import gg.bayes.challenge.rest.model.HeroSpells;

/**
 * Match service to load and get match details.
 *
 */
public interface MatchService {

	/**
	 * Method to load match details.
	 * 
	 * @param payload the payload
	 * @return match id
	 */
	Long ingestMatch(String payload);

	/**
	 * Get items for specific hero and match.
	 * 
	 * @param matchId  the match id
	 * @param heroName the hero name
	 * @return list of HeroItems
	 */
	List<HeroItems> getItems(Long matchId, String heroName);

	/**
	 * Get match details.
	 * 
	 * @param matchId the match id
	 * @return list of HeroKills
	 */
	List<HeroKills> getMatch(Long matchId);

	/**
	 * Get spells of specific hero.
	 * 
	 * @param matchId  the match id
	 * @param heroName the hero name
	 * @return list of HeroSpells
	 */
	List<HeroSpells> getSpells(Long matchId, String heroName);

	/**
	 * Get damage details.
	 * 
	 * @param matchId  the match id
	 * @param heroName the hero name
	 * @return list of HeroDamage
	 */
	List<HeroDamage> getDamage(Long matchId, String heroName);
}
