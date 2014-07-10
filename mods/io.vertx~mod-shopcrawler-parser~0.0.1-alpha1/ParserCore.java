import org.vertx.java.core.json.JsonArray;

public abstract class ParserCore {
	public abstract JsonArray parse();
	public abstract JsonArray parse(String data);
}
