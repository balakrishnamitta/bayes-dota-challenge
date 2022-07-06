package gg.bayes.challenge.utils;

import java.time.Duration;
import java.util.StringTokenizer;

import gg.bayes.challenge.rest.dto.MatchDTO;

/**
 * Utility class for parsing the match payload.
 *
 */
public class ParseUtils {

	/**
	 * Regex for parsing event time.
	 */
	public static final String TIME_SPLIT_STRING = "[:.]";

	/**
	 * Get time in seconds from event time string.
	 * 
	 * @param timeString the event time in string
	 * @return time in millis
	 */
	private static Long parseTimeInMillis(String timeString) {
		StringTokenizer tokenizer = new StringTokenizer(timeString, TIME_SPLIT_STRING);
		Long[] timeTokens = new Long[tokenizer.countTokens()];
		int index = 0;
		while (tokenizer.hasMoreElements()) {
			timeTokens[index] = Long.valueOf(tokenizer.nextToken());
			index++;
		}
		Duration duration = Duration.ofHours(Long.valueOf(timeTokens[0])).plusMinutes(Long.valueOf(timeTokens[1]))
				.plusSeconds(Long.valueOf(timeTokens[2])).plusMillis(Long.valueOf(timeTokens[3]));
		return duration.toMillis();
	}

	/**
	 * Get hero name from npc data string.
	 * 
	 * @param npcDataString the npcDataString
	 * @return hero name
	 */
	private static String parseNpcdataHero(String npcDataString) {
		return npcDataString.replace("npc_dota_hero_", "");
	}

	/**
	 * Get item name from npc data string.
	 * 
	 * @param npcDataString the npcDataString
	 * @return item name
	 */
	private static String parseNpcdataItem(String npcDataString) {
		return npcDataString.replace("item_", "");
	}

	/**
	 * Get level from npc data string.
	 * 
	 * @param npcDataString the npcDataString
	 * @return the level
	 */
	private static String parseLevel(String npcDataString) {
		return npcDataString.replace(")", "");
	}

	/**
	 * Get hero items input from input string.
	 * 
	 * @param eventString the input string
	 * @return the match dto
	 */
	public static MatchDTO getHeroItemsInput(String eventString) {
		MatchDTO matchDto = new MatchDTO();
		String[] eventData = eventString.split(" ");
		matchDto.setHeroName(parseNpcdataHero(eventData[1]));
		matchDto.setItemName(parseNpcdataItem(eventData[4]));
		matchDto.setEventTime(parseTimeInMillis(eventData[0]));
		return matchDto;
	}

	/**
	 * Get hero kills input from input string.
	 * 
	 * @param eventString the event string
	 * @return the match dto
	 */
	public static MatchDTO getHeroKillsInput(String eventString) {
		MatchDTO matchDto = new MatchDTO();
		String[] eventData = eventString.split(" ");
		matchDto.setBothAreNotHeroes(isSourceAndTargetBothAreHeroes(eventData[1], eventData[5]));
		matchDto.setHeroName(parseNpcdataHero(eventData[1]));
		matchDto.setTargetName(parseNpcdataHero(eventData[5]));
		matchDto.setEventTime(parseTimeInMillis(eventData[0]));
		return matchDto;
	}

	/**
	 * Get hero spells input from input string.
	 * 
	 * @param eventString the event string
	 * @return the match dto
	 */
	public static MatchDTO getHeroSpellsInput(String eventString) {
		MatchDTO matchDto = new MatchDTO();
		String[] eventData = eventString.split(" ");
		matchDto.setHeroName(parseNpcdataHero(eventData[1]));
		matchDto.setTargetName(parseNpcdataHero(eventData[8]));
		matchDto.setEventTime(parseTimeInMillis(eventData[0]));
		matchDto.setSpell(eventData[4]);
		matchDto.setLevel(parseLevel(eventData[6]));
		return matchDto;
	}

	/**
	 * Get hero damages input from event string.
	 * 
	 * @param eventString the event string
	 * @return the match dto
	 */
	public static MatchDTO getHeroDamageInput(String eventString) {
		MatchDTO matchDto = new MatchDTO();
		String[] eventData = eventString.split(" ");
		matchDto.setHeroName(parseNpcdataHero(eventData[1]));
		matchDto.setTargetName(parseNpcdataHero(eventData[3]));
		matchDto.setEventTime(parseTimeInMillis(eventData[0]));
		matchDto.setWeapon(eventData[5]);
		matchDto.setDamage(Long.valueOf(eventData[7]));

		return matchDto;
	}

	/**
	 * Is source and target both are heroes.
	 * 
	 * @param heroName   the npcdata string
	 * @param targetName the npcdata string
	 * @return boolean value
	 */
	private static boolean isSourceAndTargetBothAreHeroes(String heroName, String targetName) {
		return !heroName.contains("hero") || !targetName.contains("hero");
	}

}
