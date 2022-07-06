package gg.bayes.challenge.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gg.bayes.challenge.entity.Hero;
import gg.bayes.challenge.entity.HeroDamageEntity;
import gg.bayes.challenge.entity.HeroItem;
import gg.bayes.challenge.entity.HeroKill;
import gg.bayes.challenge.entity.HeroSpell;
import gg.bayes.challenge.entity.Item;
import gg.bayes.challenge.entity.Match;
import gg.bayes.challenge.repository.HeroDamageEntityRepository;
import gg.bayes.challenge.repository.HeroRepository;
import gg.bayes.challenge.repository.ItemRepository;
import gg.bayes.challenge.repository.MatchRepository;
import gg.bayes.challenge.rest.dto.MatchDTO;
import gg.bayes.challenge.rest.model.HeroDamage;
import gg.bayes.challenge.rest.model.HeroItems;
import gg.bayes.challenge.rest.model.HeroKills;
import gg.bayes.challenge.rest.model.HeroSpells;
import gg.bayes.challenge.service.MatchService;
import gg.bayes.challenge.utils.ParseUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementation of MatchService.
 *
 */
@Slf4j
@Service
public class MatchServiceImpl implements MatchService {

	private static Logger logger = LoggerFactory.getLogger(MatchServiceImpl.class);

	@Autowired
	MatchRepository matchRepository;

	@Autowired
	HeroRepository heroRepository;

	@Autowired
	ItemRepository itemRepository;

	@Autowired
	HeroDamageEntityRepository heroDamageEntityRepository;

	@Autowired
	public MatchServiceImpl() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Long ingestMatch(String payload) {
		logger.info("ingestMatch payload" + payload);

		// converting the payload to list of strings by delimeter
		List<String> events = Arrays.asList(payload.split("\n"));
		// saving Match entity
		Match match = new Match();
		match.setStartDateTime(LocalDateTime.now());
		matchRepository.save(match);
		Long matchId = match.getId();

		// processing events list
		for (String eventString : events) {

			// processing hero items
			if (eventString.contains("buys")) {
				MatchDTO dto = ParseUtils.getHeroItemsInput(eventString);
				dto.setMatchId(matchId);
				saveHeroItems(dto);

			}
			// processing hero kills
			else if (eventString.contains("killed")) {
				MatchDTO dto = ParseUtils.getHeroKillsInput(eventString);
				if (dto.isBothAreNotHeroes())
					continue;
				dto.setMatchId(matchId);
				saveHeroKills(dto);

			}
			// processing hero spells
			else if (eventString.contains("casts")) {
				MatchDTO dto = ParseUtils.getHeroSpellsInput(eventString);
				dto.setMatchId(matchId);
				saveHeroSpells(dto);

			}
			// processing hero damage
			else if (eventString.contains("hits")) {
				MatchDTO dto = ParseUtils.getHeroDamageInput(eventString);
				dto.setMatchId(matchId);
				saveHeroDamage(dto);
			}

		}
		return matchId;
	}

	/**
	 * Method to save HeroItem entities.
	 * 
	 * @param matchDto the MatchDTO object
	 */
	private void saveHeroItems(MatchDTO matchDto) {
		logger.info("saveHeroItems with dto:" + matchDto);
		Match match = matchRepository.findById(matchDto.getMatchId()).get();
		Hero hero = saveHero(matchDto);
		Item item = saveItem(matchDto);
		HeroItem heroItem = new HeroItem();
		heroItem.setHero(hero);
		heroItem.setItem(item);
		heroItem.setCreatedTime(matchDto.getEventTime());
		heroItem.setMatch(match);
		Set<HeroItem> heroItems = new HashSet<>();
		heroItems.add(heroItem);
		match.setHeroItems(heroItems);
		matchRepository.save(match);
	}

	/**
	 * Save Item entity.
	 * 
	 * @param matchDto the MatchDTO object
	 * @return Item entity
	 */
	private Item saveItem(MatchDTO matchDto) {
		logger.info("saveItem with dto:" + matchDto);
		Optional<Item> result = itemRepository.findById(matchDto.getItemName());
		Item item;
		if (result.isPresent()) {
			item = result.get();
		} else {
			item = new Item();
			item.setName(matchDto.getItemName());
		}
		return item;
	}

	/**
	 * Save Hero entity.
	 * 
	 * @param matchDto the MatchDTO object
	 * @return Hero entity
	 */
	private Hero saveHero(MatchDTO matchDto) {
		logger.info("saveHero with dto:" + matchDto);
		Hero hero;
		Optional<Hero> result = heroRepository.findById(matchDto.getHeroName());
		if (result.isPresent()) {
			hero = result.get();
		} else {
			hero = new Hero();
			hero.setName(matchDto.getHeroName());
		}
		return hero;
	}

	/**
	 * Method to save HeroKill entities.
	 * 
	 * @param matchDto the MatchDTO object
	 */
	private void saveHeroKills(MatchDTO matchDto) {
		logger.info("saveHeroKills with dto:" + matchDto);
		Match match = matchRepository.findById(matchDto.getMatchId()).get();
		Hero hero = saveHero(matchDto);
		Hero target = saveTarget(matchDto);
		HeroKill heroKill = new HeroKill();
		heroKill.setHero(hero);
		if (matchDto.getHeroName().equals(matchDto.getTargetName())) {
			heroKill.setKilledBy(hero);
		} else {
			heroKill.setKilledBy(target);
		}
		heroKill.setKilledBy(target);
		heroKill.setCreatedtime(matchDto.getEventTime());
		heroKill.setMatch(match);
		List<HeroKill> heroKills = new ArrayList<>();
		heroKills.add(heroKill);
		match.setHeroKills(heroKills);
		matchRepository.save(match);
	}

	/**
	 * Save Target hero entity.
	 * 
	 * @param matchDto the MatchDTO object
	 * @return the Hero entity
	 */
	private Hero saveTarget(MatchDTO matchDto) {
		logger.info("saveTarget with dto:" + matchDto);
		Hero killedBy;
		Optional<Hero> result = heroRepository.findById(matchDto.getTargetName());
		if (result.isPresent()) {
			killedBy = result.get();
		} else {
			killedBy = new Hero();
			killedBy.setName(matchDto.getTargetName());
		}
		return killedBy;
	}

	/**
	 * Method to save HeroSpell entities.
	 * 
	 * @param matchDto the MatchDTO object
	 */
	private void saveHeroSpells(MatchDTO matchDto) {
		logger.info("saveHeroSpells with dto:" + matchDto);
		Match match = matchRepository.findById(matchDto.getMatchId()).get();
		Hero hero = saveHero(matchDto);
		Hero target = saveTarget(matchDto);
		HeroSpell heroSpell = new HeroSpell();
		heroSpell.setHero(hero);
		if (matchDto.getHeroName().equals(matchDto.getTargetName())) {
			heroSpell.setCastsOn(hero);
		} else {
			heroSpell.setCastsOn(target);
		}
		heroSpell.setSpell(matchDto.getSpell());
		heroSpell.setLevel(matchDto.getLevel());
		heroSpell.setCreatedtime(matchDto.getEventTime());
		heroSpell.setMatch(match);
		List<HeroSpell> heroSpells = new ArrayList<>();
		heroSpells.add(heroSpell);
		match.setHeroSpells(heroSpells);
		matchRepository.save(match);
	}

	/**
	 * Method to save HeroDamage entities.
	 * 
	 * @param matchDto the MatchDTO object
	 */
	private void saveHeroDamage(MatchDTO matchDto) {
		logger.info("saveHeroDamage with dto:" + matchDto);
		Match match = matchRepository.findById(matchDto.getMatchId()).get();
		Hero hero = saveHero(matchDto);
		Hero target = saveTarget(matchDto);
		HeroDamageEntity heroDamage = new HeroDamageEntity();
		heroDamage.setHero(hero);
		if (matchDto.getHeroName().equals(matchDto.getTargetName())) {
			heroDamage.setTarget(hero);
		} else {
			heroDamage.setTarget(target);
		}
		heroDamage.setDamage(matchDto.getDamage());
		heroDamage.setWeapon(matchDto.getWeapon());
		heroDamage.setCreatedtime(matchDto.getEventTime());
		heroDamage.setMatch(match);
		List<HeroDamageEntity> heroDamages = new ArrayList<>();
		heroDamages.add(heroDamage);
		match.setHeroDamages(heroDamages);
		matchRepository.save(match);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<HeroItems> getItems(Long matchId, String heroName) {
		logger.info("getItems with params:" + matchId + "," + heroName);
		List<HeroItems> list = null;

		Optional<Match> optional = matchRepository.findById(matchId);
		if (optional.isPresent()) {
			Match m = optional.get();
			list = m.getHeroItems().stream().filter(a -> a.getHero().getName().equalsIgnoreCase(heroName)).map(a -> {
				HeroItems h = new HeroItems();
				h.setItem(a.getItem().getName());
				h.setTimestamp(a.getCreatedTime());
				return h;
			}).collect(Collectors.toList());
		}

		return list;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<HeroKills> getMatch(Long matchId) {
		logger.info("getMatch with params:" + matchId);
		List<HeroKills> list = new ArrayList<>();
		Optional<Match> optional = matchRepository.findById(matchId);
		if (optional.isPresent()) {
			Match m = optional.get();
			Map<Hero, Long> map = m.getHeroKills().stream()
					.collect(Collectors.groupingBy(HeroKill::getHero, Collectors.counting()));
			for (Entry<Hero, Long> entry : map.entrySet()) {
				HeroKills h = new HeroKills();
				h.setHero(entry.getKey().getName());
				h.setKills(entry.getValue().intValue());
				list.add(h);
			}
		}

		return list;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<HeroSpells> getSpells(Long matchId, String heroName) {
		logger.info("getSpells with params:" + matchId + "," + heroName);
		List<HeroSpells> list = new LinkedList<>();

		Optional<Match> optional = matchRepository.findById(matchId);
		if (optional.isPresent()) {
			Match m = optional.get();
			Map<String, Long> map = m.getHeroSpells().stream()
					.filter(a -> a.getHero().getName().equalsIgnoreCase(heroName))
					.collect(Collectors.groupingBy(HeroSpell::getSpell, Collectors.counting()));
			for (Map.Entry<String, Long> e : map.entrySet()) {
				HeroSpells h = new HeroSpells();
				h.setSpell(e.getKey());
				h.setCasts(e.getValue().intValue());
				list.add(h);
			}
		}

		return list;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<HeroDamage> getDamage(Long matchId, String heroName) {
		logger.info("getDamage with params:" + matchId + "," + heroName);
		List<HeroDamage> heroDamageList = new LinkedList<>();
		List<Object[]> result = heroDamageEntityRepository.getDamageByMatchIdAndHeroName(matchId, heroName);
		for (Object[] row : result) {
			HeroDamage heroDamage = new HeroDamage();
			heroDamage.setTarget((String) row[0]);
			heroDamage.setTotalDamage(((Long) row[1]).intValue());
			heroDamage.setDamageInstances(((Long) row[2]).intValue());
			heroDamageList.add(heroDamage);
		}
		return heroDamageList;
	}
}
