package cn.fantasticmao.pokemon.wiki.repoistory;

import cn.fantasticmao.pokemon.SpringTest;
import cn.fantasticmao.pokemon.wiki.domain.PokemonAbility;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * PokemonAbilityRepositoryTest
 *
 * @author maodh
 * @since 2018/8/5
 */
public class PokemonAbilityRepositoryTest extends SpringTest {
    @Resource
    private PokemonAbilityRepository pokemonAbilityRepository;

    @Test
    public void findByIndexIn() {
        List<PokemonAbility> pokemonAbilityList = pokemonAbilityRepository.findByIndexIn(Arrays.asList(1, 2));
        Assertions.assertNotNull(pokemonAbilityList);
        System.out.println(pokemonAbilityList);
    }
}