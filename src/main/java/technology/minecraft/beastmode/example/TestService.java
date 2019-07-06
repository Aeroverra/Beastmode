package technology.minecraft.beastmode.example;

import okhttp3.OkHttpClient;

public class TestService
{
	public OkHttpClient client;
	public TestService(OkHttpClient client)
	{
		this.client = client;
	}

}
