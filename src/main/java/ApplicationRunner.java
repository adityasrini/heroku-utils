package main.java;

import java.util.Scanner;

import static main.java.HerokuUtils.HerokuCLIOperations.herokuApps;
import static main.java.HerokuUtils.HerokuCLIOperations.herokuPgInfo;
import static main.java.HerokuUtils.InstallUtils.askIfUserWantsToInstallHeroku;
import static main.java.HerokuUtils.InstallUtils.checkToSeeIfHerokuIsInstalled;

public class ApplicationRunner
{

	public static void main(String[] args)
	{
		System.out.println("\n*****WELCOME!*****\n");
		if (!checkToSeeIfHerokuIsInstalled())
		{
			askIfUserWantsToInstallHeroku();
		}


		System.out.println("Here are the apps you have under your account:");
		herokuApps();

		var appName = new Scanner(System.in).nextLine();
		System.out.println(
				"\n****WAITING FOR HEROKU****\n");
		herokuPgInfo(appName);
	}


	

	
}
