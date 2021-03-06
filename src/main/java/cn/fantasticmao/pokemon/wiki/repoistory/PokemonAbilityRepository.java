package cn.fantasticmao.pokemon.wiki.repoistory;

import cn.fantasticmao.pokemon.wiki.domain.PokemonAbility;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * PokemonAbilityRepository
 *
 * @author maodh
 * @since 2018/8/5
 */
public interface PokemonAbilityRepository extends PagingAndSortingRepository<PokemonAbility, Integer> {

    @Query(value = "SELECT * FROM pw_pokemon_ability WHERE `index` IN ?1", nativeQuery = true)
    List<PokemonAbility> findByIndexIn(List<Integer> idList);
}
