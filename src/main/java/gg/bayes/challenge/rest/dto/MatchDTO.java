package gg.bayes.challenge.rest.dto;

import lombok.Data;
/**
 * Match DTO.
 *
 */
@Data
public class MatchDTO {

	private String heroName;
	private String itemName;
	private Long eventTime;
	private Long matchId;
	private String targetName;
	private String spell;
	private String level;
	private Long damage;
	private String weapon;
	private boolean bothAreNotHeroes;

}
