package technology.minecraft.beastmode;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ServiceCollection
{
	HashMap<Class<?>, Object> singletons = new HashMap<Class<?>, Object>();
	HashMap<Class<?>, ServiceType> services = new HashMap<Class<?>, ServiceType>();

	public <T> T getRequiredService(Class<T> classOfT)
	{
		return getRequiredService(classOfT, null);
	}

	public <T> T getRequiredService(Class<T> classOfT, HashMap<Class<?>, Object> scopes)
	{
		if (scopes == null)
		{
			scopes = new HashMap<Class<?>, Object>();
		}
		T object = null;
		Constructor<?>[] constructors = classOfT.getDeclaredConstructors();
		for (Constructor<?> cons : constructors)
		{
			Class<?>[] params = cons.getParameterTypes();
			Object[] paramValues = new Object[params.length];
			for (int x = 0; x < params.length; x++)
			{
				Class<?> param = params[x];
				if (singletons.containsKey(param))
				{
					paramValues[x] = singletons.get(param);
				}
				else if (scopes.containsKey(param))
				{
					paramValues[x] = scopes.get(param);
				}
				// Must not be a singleton and if its a scoped it has not been created in this
				// request yet
				else if (services.containsKey(param))
				{
					ServiceType serviceType = services.get(param);
					Object instance = getRequiredService(param, scopes);
					if (serviceType == ServiceType.Scoped)
					{
						scopes.put(param, instance);
					}
					paramValues[x] = instance;
				}
				else
				{
					throw new RuntimeException("No services has been added to handle the parameter " + param + " for " + classOfT);
				}
			}
			try
			{
				object = (T) cons.newInstance(paramValues);

			}
			catch (Exception e)
			{
				throw new RuntimeException(e);
			}
			break;
		}
		return object;
	}

	public <T> void addTransient(Class<T> classOfT)
	{
		services.put(classOfT, ServiceType.Transient);
	}

	public <T> void addScoped(Class<T> classOfT)
	{
		services.put(classOfT, ServiceType.Scoped);
	}

	public <T> void addSingleton(Class<T> classOfT)
	{
		try
		{
			T object = classOfT.newInstance();
			singletons.put(classOfT, object);
		}
		catch (Exception e)
		{

			throw new RuntimeException(e);
		}
	}

	enum ServiceType
	{
		Transient, Scoped, Singleton
	}
}
