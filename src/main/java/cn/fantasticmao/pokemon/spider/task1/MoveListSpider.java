package cn.fantasticmao.pokemon.spider.task1;

import cn.fantasticmao.mundo.core.support.Constant;
import cn.fantasticmao.pokemon.spider.Config;
import cn.fantasticmao.pokemon.spider.PokemonDataSource;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.ObjectUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * MoveListSpider
 *
 * @author maodh
 * @since 2018/8/4
 */
public class MoveListSpider extends AbstractTask1Spider<MoveListSpider.Data> {

    public MoveListSpider(CountDownLatch doneSignal) {
        super(Config.Site.MOVE_LIST, doneSignal);
    }

    @Override
    public List<MoveListSpider.Data> parseData(Document document) {
        List<MoveListSpider.Data> dataList = new LinkedList<>();
        dataList.addAll(getData1(document));
        dataList.addAll(getData2(document));
        dataList.addAll(getData3(document));
        dataList.addAll(getData4(document));
        dataList.addAll(getData5(document));
        dataList.addAll(getData6(document));
        dataList.addAll(getData7(document));
        dataList.addAll(getData8(document));
        return Collections.unmodifiableList(dataList);
    }

    @Override
    public boolean saveData(List<MoveListSpider.Data> dataList) {
        final int batchSize = 100;
        final String sql = "INSERT INTO pw_move(nameZh, nameJa, nameEn, type, category, power, accuracy, pp, generation) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = PokemonDataSource.INSTANCE.getConnection();
             PreparedStatement prep = connection.prepareStatement(sql)) {
            MoveListSpider.Data tempData = null;
            for (int i = batchSize, j = 0; ; i += batchSize) {
                for (; j < i && j < dataList.size(); j++) {
                    tempData = dataList.get(j);
                    prep.setString(1, tempData.getNameZh());
                    prep.setString(2, tempData.getNameJa());
                    prep.setString(3, tempData.getNameEn());
                    prep.setString(4, tempData.getType());
                    prep.setString(5, tempData.getCategory());
                    prep.setString(6, ObjectUtils.defaultIfNull(tempData.getPower(), Constant.Strings.EMPTY));
                    prep.setString(7, ObjectUtils.defaultIfNull(tempData.getAccuracy(), Constant.Strings.EMPTY));
                    prep.setString(8, tempData.getPp());
                    prep.setInt(9, tempData.getGeneration());
                    prep.addBatch();
                }
                prep.executeBatch();
                if (j >= dataList.size()) {
                    connection.commit();
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    static class Data implements AbstractTask1Spider.Data {
        private final String nameZh;
        private final String nameJa;
        private final String nameEn;
        private final String type;
        private final String category;
        private final String power;
        private final String accuracy;
        private final String pp;
        private final int generation;
    }

    private static final Function<Element, MoveListSpider.Data.DataBuilder> PARSER = element -> {
        String nameZh = element.child(1).text();
        String nameJa = element.child(2).text();
        String nameEn = element.child(3).text();
        String type = element.child(4).text();
        String category = element.child(5).text();
        String power = "—".equals(element.child(6).text()) ? null : element.child(6).text();
        String accuracy = "—".equals(element.child(7).text()) ? null : element.child(7).text();
        String pp = element.child(8).text();
        return new Data.DataBuilder()
            .nameZh(nameZh).nameJa(nameJa).nameEn(nameEn)
            .type(type).category(category)
            .power(power).accuracy(accuracy).pp(pp);
    };

    // 第一世代
    private List<MoveListSpider.Data> getData1(Document document) {
        return document.select(".bg-关都 > tbody > tr").stream()
            .skip(1)
            .map(element -> PARSER.apply(element)
                .generation(1)
                .build()
            )
            .collect(Collectors.toList());
    }

    // 第二世代
    private List<MoveListSpider.Data> getData2(Document document) {
        return document.select(".bg-城都 > tbody > tr").stream()
            .skip(1)
            .map(element -> PARSER.apply(element)
                .generation(2)
                .build()
            )
            .collect(Collectors.toList());
    }

    // 第三世代
    private List<MoveListSpider.Data> getData3(Document document) {
        return document.select(".bg-丰缘 > tbody > tr").stream()
            .skip(1)
            .map(element -> PARSER.apply(element)
                .generation(3)
                .build()
            )
            .collect(Collectors.toList());
    }

    // 第四世代
    private List<MoveListSpider.Data> getData4(Document document) {
        return document.select(".bg-神奥 > tbody > tr").stream()
            .skip(1)
            .map(element -> PARSER.apply(element)
                .generation(4)
                .build()
            )
            .collect(Collectors.toList());
    }

    // 第五世代
    private List<MoveListSpider.Data> getData5(Document document) {
        return document.select(".bg-合众 > tbody > tr").stream()
            .skip(1)
            .map(element -> PARSER.apply(element)
                .generation(5)
                .build()
            )
            .collect(Collectors.toList());
    }

    // 第六世代
    private List<MoveListSpider.Data> getData6(Document document) {
        return document.select(".bg-卡洛斯 > tbody > tr").stream()
            .filter(element -> element.child(0).children().size() == 0)
            .skip(1)
            .map(element -> PARSER.apply(element)
                .generation(6)
                .build()
            )
            .collect(Collectors.toList());
    }

    // 第七世代
    private List<MoveListSpider.Data> getData7(Document document) {
        return document.select(".bg-阿罗拉 > tbody > tr").stream()
            .filter(element -> element.child(0).children().size() == 0)
            .skip(1)
            .map(element -> PARSER.apply(element)
                .generation(7)
                .build()
            )
            .collect(Collectors.toList());
    }

    // 第八世代
    private List<MoveListSpider.Data> getData8(Document document) {
        return document.select(".bg-伽勒尔 > tbody > tr").stream()
            .filter(element -> element.child(0).children().size() == 0)
            .skip(1)
            .map(element -> PARSER.apply(element)
                .generation(8)
                .build()
            )
            .collect(Collectors.toList());
    }
}