package com.khenford.economy.database;

import cn.nukkit.Player;
import com.khenford.economy.Main;
import com.mefrreex.jooq.database.SQLiteDatabase;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;

import java.io.File;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Optional.of;

public class Database {

    private static SQLiteDatabase database;

    public Database() {
        Table<?> table = DSL.table("users");
        SQLiteDatabase database = new SQLiteDatabase(new File(Main.getInstance().getDataFolder()+"/database.db"));
        Database.database = database;

        database.getConnection().thenAcceptAsync(connection -> {
            DSL.using(connection)
                    .createTableIfNotExists(table)
                    .column(DSL.field("name"), SQLDataType.VARCHAR.length(255))
                    .column(DSL.field("money"), SQLDataType.INTEGER)
                    .execute();
        });
    }

    public void registerPlayer(String playerName, int initialMoney) {
        database.getConnection().thenAcceptAsync(connection -> {
            DSLContext create = DSL.using(connection);
            int count = create.selectCount()
                    .from(DSL.table("users"))
                    .where(DSL.field("name").eq(playerName))
                    .fetchOne(0, int.class);

            if (count == 0) {
                create.insertInto(DSL.table("users"))
                        .columns(DSL.field("name"), DSL.field("money"))
                        .values(playerName, initialMoney)
                        .execute();
            } else {
                System.out.println("Player already registered.");
            }
        });
    }


    public boolean isPlayer(String playerName) {
        return database.getConnection().thenApply(connection ->
                DSL.using(connection)
                        .selectFrom(DSL.table("users"))
                        .where(DSL.field("name").eq(playerName))
                        .fetch()
                        .isNotEmpty()
        ).join();
    }

    public int getMoney(String playerName) {
        return database.getConnection().thenApply(connection ->
                DSL.using(connection)
                        .select(DSL.field("money"))
                        .from(DSL.table("users"))
                        .where(DSL.field("name").eq(playerName))
                        .fetchOneInto(Integer.class)
        ).join();
    }

    public void addMoney(String playerName, int money) {
        database.getConnection().thenAcceptAsync(connection ->
                DSL.using(connection)
                        .update(DSL.table("users"))
                        .set(DSL.field("money"), DSL.field("money", Integer.class).add(money))
                        .where(DSL.field("name").eq(playerName))
                        .execute()
        );
    }

    public void setMoney(String playerName, int money) {
        database.getConnection().thenAcceptAsync(connection ->
                DSL.using(connection)
                        .update(DSL.table("users"))
                        .set(DSL.field("money"), money)
                        .where(DSL.field("name").eq(playerName))
                        .execute()
        );
    }

    public void removeMoney(String playerName, int money) {
        database.getConnection().thenAcceptAsync(connection ->
                DSL.using(connection)
                        .update(DSL.table("users"))
                        .set(DSL.field("money", Integer.class),
                                DSL.choose()
                                        .when(DSL.field("money", Integer.class).sub(money).lessThan(0), 0)
                                        .otherwise(DSL.field("money", Integer.class).sub(money)))
                        .where(DSL.field("name").eq(playerName))
                        .execute()
        );
    }

    public List<String> topMoney(int topLimit) {
        return database.getConnection().thenApply(connection ->
                DSL.using(connection)
                        .select(DSL.field("name"), DSL.field("money"))
                        .from(DSL.table("users"))
                        .orderBy(DSL.field("money").desc())
                        .limit(topLimit)
                        .fetch()
                        .map(record -> record.get("name") + ": " + record.get("money"))
        ).join();
    }
}
