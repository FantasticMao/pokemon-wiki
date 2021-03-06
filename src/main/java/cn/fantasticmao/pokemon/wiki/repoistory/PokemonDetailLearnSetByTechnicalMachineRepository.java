package cn.fantasticmao.pokemon.wiki.repoistory;

import cn.fantasticmao.pokemon.wiki.domain.PokemonDetailLearnSetByTechnicalMachine;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * PokemonDetailLearnSetByTechnicalMachineRepository
 *
 * @author maomao
 * @since 2019-08-20
 */
public interface PokemonDetailLearnSetByTechnicalMachineRepository extends PagingAndSortingRepository<PokemonDetailLearnSetByTechnicalMachine, Integer> {

    @Query(value = "SELECT * FROM pw_pokemon_detail_learn_set_by_technical_machine WHERE `index` IN ?1", nativeQuery = true)
    List<PokemonDetailLearnSetByTechnicalMachine> findByIndexIn(List<Integer> idList);
}
