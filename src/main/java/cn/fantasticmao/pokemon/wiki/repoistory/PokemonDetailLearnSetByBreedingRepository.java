package cn.fantasticmao.pokemon.wiki.repoistory;

import cn.fantasticmao.pokemon.wiki.domain.PokemonDetailLearnSetByBreeding;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * PokemonDetailLearnSetByBreedingRepository
 *
 * @author maomao
 * @since 2019-08-20
 */
public interface PokemonDetailLearnSetByBreedingRepository extends PagingAndSortingRepository<PokemonDetailLearnSetByBreeding, Integer> {

    @Query(value = "SELECT * FROM pw_pokemon_detail_learn_set_by_breeding WHERE `index` IN ?1", nativeQuery = true)
    List<PokemonDetailLearnSetByBreeding> findByIndexIn(List<Integer> idList);
}
