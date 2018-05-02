package CLI_Module.HerokuUtils.PostgresOperations;

public class HerokuPostgresException
		extends Exception
{

	private String message;

	@Override
	public String getMessage()
	{
		return message;
	}

	public HerokuPostgresException(String message)
	{
		this.message = message;
	}
}
