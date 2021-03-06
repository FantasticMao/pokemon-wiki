package cn.fantasticmao.pokemon.wiki.service.impl;

import cn.fantasticmao.pokemon.wiki.bean.MoveBean;
import cn.fantasticmao.pokemon.wiki.domain.Move;
import cn.fantasticmao.pokemon.wiki.domain.MoveDetail;
import cn.fantasticmao.pokemon.wiki.repoistory.MoveDetailRepository;
import cn.fantasticmao.pokemon.wiki.repoistory.MoveRepository;
import cn.fantasticmao.pokemon.wiki.service.MoveService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * MoveServiceImpl
 *
 * @author maodh
 * @since 2018/8/6
 */
@Service
public class MoveServiceImpl implements MoveService {
    @Resource
    private MoveRepository moveRepository;
    @Resource
    private MoveDetailRepository moveDetailRepository;

    @Override
    public List<MoveBean> listByNameZh(String nameZh) {
        if (StringUtils.isEmpty(nameZh)) {
            return Collections.emptyList();
        }

        List<Move> moveList = moveRepository.findByNameZh(nameZh);
        if (CollectionUtils.isEmpty(moveList)) {
            return Collections.emptyList();
        }

        List<Integer> moveIdList = moveList.stream().map(Move::getId).collect(Collectors.toList());
        Map<Integer, MoveDetail> moveDetailMap = moveDetailRepository.findByIdIn(moveIdList).stream()
            .collect(Collectors.toMap(MoveDetail::getId, Function.identity()));

        return moveList.stream()
            .map(move -> {
                MoveDetail moveDetail = moveDetailMap.get(move.getId());
                return MoveBean.ofDomain(move, moveDetail);
            })
            .sorted()
            .collect(Collectors.toList());
    }
}
