package main.java;

import java.util.Arrays;

public enum HerokuOptions
{
	HEROKUAPPS, PGINFO, PGCREDENTIALS, PGPULL, PGPUSH, PGRESET, PGKILLQUERY, EXITAPP;


	public static HerokuOptions matcher(ApplicationRunner applicationRunner)
	{
		return Arrays.stream(HerokuOptions.values())
			  .filter(e -> e.name()
							.contains(applicationRunner.optionSelected.toUpperCase()))
			  .findFirst().get();
	}
}
