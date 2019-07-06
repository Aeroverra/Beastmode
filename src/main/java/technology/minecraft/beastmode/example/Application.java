package technology.minecraft.beastmode.example;

import technology.minecraft.beastmode.ServiceCollection;

public class Application
{

	public static void main(String[] args)
	{

		test1();
		test2();
	}

	public static void test1()
	{
		System.out.println("--- Test 1 ---");

		ServiceCollection services = new ServiceCollection();
		services.addSingleton(ExampleSingleton.class);
		services.addTransient(TestService.class);
		services.addTransient(FakeDBService.class);
		services.addSingleton(Example2Singleton.class);

		FakeLoggerService test = services.getRequiredService(FakeLoggerService.class);
		System.out.println(test.testService);
		System.out.println(test.fakeDBService.testService);
	}

	public static void test2()
	{
		System.out.println("--- Test 2 ---");

		ServiceCollection services = new ServiceCollection();
		services.addSingleton(ExampleSingleton.class);
		services.addTransient(FakeDBService.class);
		services.addScoped(TestService.class);

		FakeLoggerService test = services.getRequiredService(FakeLoggerService.class);
		System.out.println(test.testService);
		System.out.println(test.fakeDBService.testService);
	}

}
