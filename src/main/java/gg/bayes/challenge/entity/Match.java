package gg.bayes.challenge.entity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.Setter;

/**
 * Match entity. 
 *
 */
@Entity
@Getter
@Setter
public class Match {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private LocalDateTime startDateTime;

	@OneToMany(mappedBy = "match", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	Set<HeroItem> heroItems;

	@OneToMany(mappedBy = "match", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	List<HeroKill> heroKills;

	@OneToMany(mappedBy = "match", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	List<HeroSpell> heroSpells;

	@OneToMany(mappedBy = "match", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	List<HeroDamageEntity> heroDamages;

}