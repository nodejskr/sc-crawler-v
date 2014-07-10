package io.vertx.shopcrawler.infomanager.checker;

public class SchemaChecker implements Checkable{
	private String checkSource;

	@Override
	public void setCompareSource(Object checkSource) {
		// TODO Auto-generated method stub
		this.checkSource = (String) checkSource;
	}

	@Override
	public boolean compareTo(Object checkTarget) {
		// TODO Auto-generated method stub
		return this.checkSource == (String) checkTarget;
	}
}