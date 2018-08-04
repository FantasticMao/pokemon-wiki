package cn.fantasticmao.pokemon.spider;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Config
 *
 * @author maodh
 * @since 2018/7/29
 */
public interface Config {

    enum Site {
        BASE_URL("https://wiki.52poke.com"),
        POKEMON_LIST("https://wiki.52poke.com/zh-hans/宝可梦列表（按全国图鉴编号）"),
        POKEMON_ABILITY_LIST("https://wiki.52poke.com/zh-hans/特性列表（按全国图鉴编号）"),
        POKEMON_NATURE_LIST("https://wiki.52poke.com/zh-hans/性格"),
        POKEMON_MOVE_LIST("https://wiki.52poke.com/zh-hans/招式列表"),
        POKEMON_BASE_STAT_LIST("https://wiki.52poke.com/zh-hans/种族值列表（第七世代）"),
        ITEM_LIST("https://wiki.52poke.com/zh-hans/道具列表（主系列）");

        public String url;

        Site(String url) {
            this.url = url;
        }


        @Override
        public String toString() {
            return this.url;
        }
    }

    enum ConnectionHolder {
        INSTANCE;

        public final Connection connection;

        ConnectionHolder() {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                this.connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pokemon_wiki", "pokemon", "I_Love_Pokemon");
                this.connection.setAutoCommit(false);
            } catch (SQLException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
