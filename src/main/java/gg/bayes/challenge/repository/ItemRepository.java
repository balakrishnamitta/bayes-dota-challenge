package gg.bayes.challenge.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import gg.bayes.challenge.entity.Item;

/**
 * Item repository.
 *
 */
@Repository
public interface ItemRepository extends CrudRepository<Item, String> {

}