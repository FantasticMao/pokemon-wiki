package cn.fantasticmao.pokemon.spider.task2;

import cn.fantasticmao.mundo.core.support.Constant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.apache.commons.collections4.CollectionUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * PokemonDetailSpider
 *
 * @author maodh
 * @since 2018/8/28
 */
class PokemonDetailSpider extends AbstractTask2Spider<PokemonDetailSpider.Data> {
    private final int index;
    private final String nameZh;

    PokemonDetailSpider(int index, String nameZh) {
        super("https://wiki.52poke.com/zh-hans/" + nameZh, "");
        this.index = index;
        this.nameZh = nameZh;
    }

    @Override
    protected PokemonDetailSpider.Data parseData(Document document) throws Exception {
        return _parseData(document);
    }

    @Getter
    @ToString
    @AllArgsConstructor
    static class Data implements AbstractTask2Spider.Data {
        private final int index;
        private final String nameZh;
        private final String imgUrl; // 图片链接
        private final String type; // 属性
        private final String category; // 分类
        private final String ability; // 特性
        private final String height; // 身高
        private final String weight; // 体重
        private final String bodyStyle; // 体形
        private final String catchRate; // 捕获率
        private final String genderRatio; // 性别比例
        private final String eggGroup1; // 第一生蛋分组
        private final String eggGroup2; // 第二生蛋分组
        private final String hatchTime; // 孵化时间
        private final String effortValue; // 基础点数
        private final BaseStat BaseStat;
        private final List<LearnSetByLevelingUp> learnSetByLevelingUpList; // 可学会的招式
        private final List<LearnSetByTechnicalMachine> learnSetByTechnicalMachineList; // 能使用的招式学习器
        private final List<LearnSetByBreeding> learnSetByBreedingList; // 蛋招式

        @Getter
        @ToString
        @AllArgsConstructor
        static class BaseStat {
            private final int hp;
            private final int attack;
            private final int defense;
            private final int spAttack;
            private final int spDefense;
            private final int speed;
            private final int total;
            private final float average;
        }

        @Getter
        @ToString
        @AllArgsConstructor
        static class LearnSetByLevelingUp {
            private final String level1; // 太阳/月亮
            private final String level2; // 究极之日/究极之月
            private final String move;
            private final String type;
            private final String category;
            private final String power;
            private final String accuracy;
            private final String pp;
        }

        @Getter
        @ToString
        @AllArgsConstructor
        static class LearnSetByTechnicalMachine {
            private final String imgUrl;
            private final String technicalMachine;
            private final String move;
            private final String type;
            private final String category;
            private final String power;
            private final String accuracy;
            private final String pp;
        }

        @Getter
        @ToString
        @AllArgsConstructor
        static class LearnSetByBreeding {
            private final String parent;
            private final String move;
            private final String type;
            private final String category;
            private final String power;
            private final String accuracy;
            private final String pp;
        }
    }

    private PokemonDetailSpider.Data _parseData(Document document) {
        final Element table = document.select("#mw-content-text > .mw-parser-output > table").get(1);

        final String indexStr = String.format("%03d", index);

        final String imgUrl = table.selectFirst("img[alt^=" + indexStr + "]") != null ?
            table.selectFirst("img[alt^=" + indexStr + "]").attr("data-url").replace("//media.52poke.com", "https://s1.52poke.wiki")
            : Constant.Strings.EMPTY;

        final String type = table.selectFirst("[title=属性]").parent().nextElementSibling().select("span[class=type-box-8-inner]").stream()
            .map(element -> element.text().trim())
            .collect(Collectors.joining(Constant.Strings.COMMA));

        final String category = table.selectFirst("[title=分类]").parent().nextElementSibling().text().trim();

        List<String> abilityList = table.selectFirst("[title=特性]").parent().nextElementSibling().select("td").get(0).select("a").stream()
            .map(element -> element.text().trim())
            .collect(Collectors.toList());
        if (table.selectFirst("[title=特性]").parent().nextElementSibling().select("td").size() > 1) {
            String abilityHide = table.selectFirst("[title=特性]").parent().nextElementSibling().select("td").get(1).select("a").text().trim() + "（隐藏特性）";
            abilityList.add(abilityHide);
        }
        final String ability = String.join(Constant.Strings.COMMA, abilityList);

        final String height = table.selectFirst("[title=宝可梦列表（按身高排序）]").parent().nextElementSibling().text().trim();

        final String weight = table.selectFirst("[title=宝可梦列表（按体重排序）]").parent().nextElementSibling().text().trim();

        Element bodyStyleElement = table.selectFirst("[title=宝可梦列表（按体形分类）]").parent().nextElementSibling();
        final String bodyStyle = bodyStyleElement.select("img").size() == 0 ? "无" : bodyStyleElement.
            selectFirst("img").attr("data-url").replace("//media.52poke.com", "https://s1.52poke.wiki");

        final String catchRate = table.selectFirst("[title=捕获率]").parent().nextElementSibling().select("span[class=explain]").text().trim();

        List<String> genderRatioList = table.selectFirst("[title=宝可梦列表（按性别比例分类）]").parent().nextElementSibling().select("span").stream()
            .map(element -> element.text().trim())
            .collect(Collectors.toList());
        final String genderRatio = CollectionUtils.isEmpty(genderRatioList) ? "无性别" : String.join(Constant.Strings.COMMA, genderRatioList);

        List<String> eggGroupList = table.selectFirst("[title=宝可梦培育]").parent().nextElementSibling().select("td").get(0).select("a").stream()
            .map(element -> element.attr("title").trim().replaceAll("（.*）", ""))
            .collect(Collectors.toList());
        final String eggGroup1 = eggGroupList.size() >= 1 ? eggGroupList.get(0) : Constant.Strings.EMPTY;
        final String eggGroup2 = eggGroupList.size() >= 2 ? eggGroupList.get(1) : Constant.Strings.EMPTY;

        String hatchTime = table.selectFirst("[title=宝可梦培育]").parent().nextElementSibling().select("td").get(1).select("small").text().trim();
        hatchTime = hatchTime.substring(1, hatchTime.length() - 1);

        final String effortValue = table.selectFirst("[title=基础点数]").parent().nextElementSibling().selectFirst("tr").select("td").stream()
            .map(element -> element.text().trim())
            .collect(Collectors.joining(Constant.Strings.COMMA));

        // 兼容拳拳蛸
        Element baseStatSpan = document.selectFirst("#种族值_2");
        if (baseStatSpan == null) {
            // 正常情况
            baseStatSpan = document.selectFirst("#种族值");
        }
        if (baseStatSpan == null) {
            // 兼容小磁怪
            baseStatSpan = document.selectFirst("#種族值");
        }
        final Element baseStatTable = baseStatSpan.parent().nextElementSiblings().select("table").first();
        final int hp = Integer.parseInt(baseStatTable.selectFirst("tr[class=bgl-HP]").select("div[style*='float:right']").text());
        final int attack = Integer.parseInt(baseStatTable.selectFirst("tr[class=bgl-攻击]").select("div[style*='float:right']").text());
        final int defense = Integer.parseInt(baseStatTable.selectFirst("tr[class=bgl-防御]").select("div[style*='float:right']").text());
        final int spAttack = Integer.parseInt(baseStatTable.selectFirst("tr[class=bgl-特攻]").select("div[style*='float:right']").text());
        final int spDefense = Integer.parseInt(baseStatTable.selectFirst("tr[class=bgl-特防]").select("div[style*='float:right']").text());
        final int speed = Integer.parseInt(baseStatTable.selectFirst("tr[class=bgl-速度]").select("div[style*='float:right']").text());
        final int total = hp + attack + defense + spAttack + spDefense + speed;
        final float average = total / 6.0F;
        final Data.BaseStat baseStat = new Data.BaseStat(hp, attack, defense, spAttack, spDefense, speed, total, average);

        Element learnSetByLevelingUpSpan = document.selectFirst("#可学会的招式_2");
        if (learnSetByLevelingUpSpan == null) {
            learnSetByLevelingUpSpan = document.selectFirst("#可学会的招式");
        }
        if (learnSetByLevelingUpSpan == null) {
            // 兼容 https://wiki.52poke.com/wiki/风速狗
            learnSetByLevelingUpSpan = document.selectFirst("[id$=可学会的招式]");
        }

        final Element learnSetByLevelingUpTable = learnSetByLevelingUpSpan.parent().nextElementSibling();
        final List<Data.LearnSetByLevelingUp> learnSetByLevelingUpList = learnSetByLevelingUpTable.tagName().equals("table")
            ? learnSetByLevelingUpTable.select("> tbody > tr").stream()
            .filter(element -> element.hasClass("bgwhite"))
            .map(element -> {
                final String _level1 = element.child(0).text();
                final String _level2 = element.child(1).text();
                final String _move = element.child(2).child(0).text();
                final String _type = element.child(3).text();
                final String _category = element.child(4).text();
                final String _power = element.child(5).text();
                final String _accuracy = element.child(6).text();
                final String _pp = element.child(7).text();
                return new Data.LearnSetByLevelingUp(_level1, _level2, _move, _type, _category, _power, _accuracy, _pp);
            })
            .collect(Collectors.toList())
            : Collections.emptyList();

        Element learnSetByTechnicalMachineSpan = document.selectFirst("#能使用的招式学习器_2");
        if (learnSetByTechnicalMachineSpan == null) {
            learnSetByTechnicalMachineSpan = document.selectFirst("#能使用的招式学习器");
        }
        if (learnSetByTechnicalMachineSpan == null) {
            // 兼容 https://wiki.52poke.com/wiki/铝钢龙
            learnSetByTechnicalMachineSpan = document.selectFirst("#能使用的招式学习器和招式记录");
        }
        final Element learnSetByTechnicalMachineTable = learnSetByTechnicalMachineSpan.parent().nextElementSibling();
        final List<Data.LearnSetByTechnicalMachine> learnSetByTechnicalMachineList = learnSetByTechnicalMachineTable.tagName().equals("table")
            ? learnSetByTechnicalMachineTable.select("> tbody > tr").stream()
            .filter(element -> element.hasClass("bgwhite"))
            .map(element -> {
                final String _imgUrl = element.selectFirst("img").attr("data-url").replace("//media.52poke.com", "https://s1.52poke.wiki");
                final String _technicalMachine = element.child(1).text();
                final String _move = element.child(2).child(0).text();
                final String _type = element.child(3).text();
                final String _category = element.child(4).text();
                final String _power = element.child(5).text();
                final String _accuracy = element.child(6).text();
                final String _pp = element.child(7).text();
                return new Data.LearnSetByTechnicalMachine(_imgUrl, _technicalMachine, _move, _type, _category, _power, _accuracy, _pp);
            })
            .collect(Collectors.toList())
            : Collections.emptyList();

        Element learnSetByBreedingSpan = document.selectFirst("#蛋招式");
        final List<Data.LearnSetByBreeding> learnSetByBreedingList = learnSetByBreedingSpan != null
            ? learnSetByBreedingSpan.parent().nextElementSibling().select("> tbody > tr").stream()
            .filter(element -> element.hasClass("bgwhite"))
            .map(element -> {
                final String _parent = element.child(0).select("a").stream()
                    .map(e -> e.child(0).attr("title"))
                    .collect(Collectors.joining(Constant.Strings.COMMA));
                final String _move = element.child(1).child(0).text();
                final String _type = element.child(2).text();
                final String _category = element.child(3).text();
                final String _power = element.child(4).text();
                final String _accuracy = element.child(5).text();
                final String _pp = element.child(6).text();
                return new Data.LearnSetByBreeding(_parent, _move, _type, _category, _power, _accuracy, _pp);
            })
            .collect(Collectors.toList())
            : Collections.emptyList();

        return new PokemonDetailSpider.Data(index, nameZh, imgUrl, type, category, ability, height, weight, bodyStyle, catchRate, genderRatio, eggGroup1, eggGroup2, hatchTime, effortValue,
            baseStat, learnSetByLevelingUpList, learnSetByTechnicalMachineList, learnSetByBreedingList);
    }
}
