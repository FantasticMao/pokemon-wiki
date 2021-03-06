package cn.fantasticmao.pokemon.wiki.repoistory;

import cn.fantasticmao.pokemon.SpringTest;
import cn.fantasticmao.pokemon.wiki.domain.Nature;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;
import java.util.List;

/**
 * NatureRepositoryTest
 *
 * @author maodh
 * @since 2018/8/5
 */
public class NatureRepositoryTest extends SpringTest {
    @Resource
    private NatureRepository natureRepository;

    @Test
    public void findAll() {
        List<Nature> natureList = natureRepository.findAll();
        Assertions.assertNotNull(natureList);
        System.out.println(natureList);
    }
}