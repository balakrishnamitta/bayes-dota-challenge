package gg.bayes.challenge.entity;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.Setter;

/**
 * Item entity.
 *
 */
@Entity
@Getter
@Setter
public class Item {
	@Id
	private String name;

	@OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
	Set<HeroItem> heroItems;

}
