package technology.minecraft.beastmode.example;

import okhttp3.OkHttpClient;
import technology.minecraft.beastmode.ServiceCollection;

public class Application
{

	public static void main(String[] args)
	{
		ServiceCollection services = new ServiceCollection();

		services.addSingleton(OkHttpClient.class);
	//	services.addTransient(TestService.class);
		services.addTransient(FakeDBService.class);

		FakeLoggerService t = services.getRequiredService(FakeLoggerService.class);
		System.out.println(t.testService);
		System.out.println(t.fakeDBService.testService);
	}

}
