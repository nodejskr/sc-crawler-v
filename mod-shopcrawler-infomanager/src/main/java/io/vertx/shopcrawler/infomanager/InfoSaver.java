package io.vertx.shopcrawler.infomanager;

import java.sql.SQLException;

import org.vertx.java.busmods.BusModBase;
import org.vertx.java.core.Handler;
import org.vertx.java.platform.Verticle;
import org.vertx.java.core.eventbus.EventBus;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.json.JsonArray;
import org.vertx.java.core.json.JsonObject;

public class InfoSaver {
	private DbConnect dbConn;

	public InfoSaver(DbConnect dbConn) {
		this.dbConn = dbConn;
	}
}