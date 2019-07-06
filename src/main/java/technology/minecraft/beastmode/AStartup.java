package technology.minecraft.beastmode;

public abstract class AStartup implements Runnable
{
	private ServiceCollection serviceCollection = new ServiceCollection();


	public void run()
	{
		ConfigureServices(serviceCollection);
	}

	abstract void ConfigureServices(ServiceCollection services);
}
