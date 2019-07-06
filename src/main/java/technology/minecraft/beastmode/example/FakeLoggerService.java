package technology.minecraft.beastmode.example;

public class FakeLoggerService
{
	public FakeDBService fakeDBService;
	public TestService testService;
	public FakeLoggerService(FakeDBService fakeDBService,TestService testService)
	{
		this.fakeDBService = fakeDBService;
		this.testService = testService;
	}

}
