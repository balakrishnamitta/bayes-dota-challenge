package gg.bayes.challenge.rest.controller;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gg.bayes.challenge.rest.model.HeroDamage;
import gg.bayes.challenge.rest.model.HeroItems;
import gg.bayes.challenge.rest.model.HeroKills;
import gg.bayes.challenge.rest.model.HeroSpells;
import gg.bayes.challenge.service.MatchService;
import lombok.extern.slf4j.Slf4j;

/**
 * Match rest controller.
 *
 */
@Slf4j
@RestController
@RequestMapping("/api/match")
public class MatchController {

	private final MatchService matchService;

	@Autowired
	public MatchController(MatchService matchService) {
		this.matchService = matchService;
	}

	/**
	 * REST Endpoint to ingest match data.
	 * 
	 * @param payload the payload
	 * @return response with match id
	 */
	@PostMapping(consumes = "text/plain")
	public ResponseEntity<Long> ingestMatch(@RequestBody @NotNull @NotBlank String payload) {
		final Long matchId = matchService.ingestMatch(payload);
		return ResponseEntity.ok(matchId);
	}

	/**
	 * REST Endpoint to get match details.
	 * 
	 * @param matchId the match id
	 * @return response with list of HeroKills
	 */
	@GetMapping("{matchId}")
	public ResponseEntity<List<HeroKills>> getMatch(@PathVariable("matchId") Long matchId) {
		return ResponseEntity.ok(matchService.getMatch(matchId));
	}

	/**
	 * REST Endpoint to get hero items.
	 * 
	 * @param matchId  the match id
	 * @param heroName the hero name
	 * @return response with list of HeroItems
	 */
	@GetMapping("{matchId}/{heroName}/items")
	public ResponseEntity<List<HeroItems>> getItems(@PathVariable("matchId") Long matchId,
			@PathVariable("heroName") String heroName) {
		return ResponseEntity.ok(matchService.getItems(matchId, heroName));
	}

	/**
	 * REST Endpoint to get hero spells.
	 * 
	 * @param matchId  the match id
	 * @param heroName the hero name
	 * @return response with list of HeroSpells
	 */
	@GetMapping("{matchId}/{heroName}/spells")
	public ResponseEntity<List<HeroSpells>> getSpells(@PathVariable("matchId") Long matchId,
			@PathVariable("heroName") String heroName) {
		return ResponseEntity.ok(matchService.getSpells(matchId, heroName));
	}

	/**
	 * REST Endpoint to get hero damage details.
	 * 
	 * @param matchId  the match id
	 * @param heroName
	 * @return response with list of HeroDamage
	 */
	@GetMapping("{matchId}/{heroName}/damage")
	public ResponseEntity<List<HeroDamage>> getDamage(@PathVariable("matchId") Long matchId,
			@PathVariable("heroName") String heroName) {
		return ResponseEntity.ok(matchService.getDamage(matchId, heroName));
	}
}
