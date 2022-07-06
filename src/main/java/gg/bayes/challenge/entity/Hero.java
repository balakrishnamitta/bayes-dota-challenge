package gg.bayes.challenge.entity;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.Setter;

/**
 * Hero entity.
 * 
 */
@Entity
@Getter
@Setter
public class Hero {
	@Id
	private String name;

	@OneToMany(mappedBy = "hero", cascade = CascadeType.ALL)
	Set<HeroItem> heroItems;

}
