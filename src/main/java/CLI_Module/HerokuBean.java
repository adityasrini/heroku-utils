package CLI_Module;

import java.util.List;

public class HerokuBean
{
	private String appName;
	private String dbAlias;
	private String localDbName;
	private List<String> pgInfo;
	private List<String> pgCredentials;

	public String getAppName()
	{
		return appName;
	}

	public void setAppName(String appName)
	{
		this.appName = appName;
	}

	public String getLocalDbName()
	{
		return localDbName;
	}

	public void setLocalDbName(String localDbName)
	{
		this.localDbName = localDbName;
	}

	public String getDbAlias()
	{
		return dbAlias;
	}

	public void setDbAlias(String dbAlias)
	{
		this.dbAlias = dbAlias;
	}

	public List<String> getPgCredentials()
	{
		return pgCredentials;
	}

	public void setPgCredentials(List<String> pgCredentials)
	{
		this.pgCredentials = pgCredentials;
	}

	public List<String> getPgInfo()
	{
		return pgInfo;
	}

	public void setPgInfo(List<String> pgInfo)
	{
		this.pgInfo = pgInfo;
	}
}
