package io.vertx.shopcrawler.infomanager.checker;

public interface Checkable {
	void setCompareSource(Object checkSource);
	boolean compareTo(Object checkTarget);
}