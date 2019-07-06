package technology.minecraft.beastmode;

public abstract class AStartup implements Runnable
{
	private ServiceCollection serviceCollection = new ServiceCollection();

	public AStartup()
	{
		ConfigureServices(serviceCollection);
	}

	public void run()
	{

	}

	public abstract void ConfigureServices(ServiceCollection services);
}
