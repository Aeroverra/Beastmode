package technology.minecraft.beastmode;

import java.lang.reflect.Constructor;
import java.util.HashMap;

/*
 * Uses reflection to keep track of 3 different service types and instantiate other services when called upon to do so.
 */
public class ServiceCollection
{
	HashMap<Class<?>, Object> singletons = new HashMap<Class<?>, Object>();
	
	// All known non singleton services
	HashMap<Class<?>, ServiceType> services = new HashMap<Class<?>, ServiceType>();

	/*
	 * Uses all the known services to instantiate an object based on the class you
	 * pass in. This will never return null and instead will throw a
	 * RuntimeException
	 */
	public <T> T getRequiredService(Class<T> classOfT)
	{
		return getRequiredService(classOfT, null);
	}

	/*
	 * The actual functioning call that can also take a scopes map in order to keep
	 * track of the current instantiations.
	 */
	@SuppressWarnings("unchecked")
	private <T> T getRequiredService(Class<T> classOfT, HashMap<Class<?>, Object> scopes)
	{
		if (scopes == null)
		{
			// If this is not instantiated in must mean we have not done any recursion yet
			scopes = new HashMap<Class<?>, Object>();
		}
		T object = null;
		Constructor<?>[] constructors = classOfT.getDeclaredConstructors();
		// This could be a for loop in the event of multiple constructors but I have no
		// reason to use multiple constructors so this possibility is not yet handled
		Constructor<?> cons = constructors[0];
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

		if (object == null)
		{
			throw new RuntimeException("Return object is some how null Class:  " + classOfT);
		}
		return object;
	}

	/*
	 * Adds a transient service type meaning it will be instantiated multiple times
	 * during the getRequiredService call if other sub services also require this
	 * service
	 */
	public <T> void addTransient(Class<T> classOfT)
	{
		services.put(classOfT, ServiceType.Transient);
	}

	/*
	 * Adds a scoped service type meaning it will only be instantiated once during
	 * the getRequiredService call and the instance will be shared among all sub
	 * services
	 */
	public <T> void addScoped(Class<T> classOfT)
	{
		services.put(classOfT, ServiceType.Scoped);
	}

	/*
	 * This should have no constructor and will be instantiated once for the life of
	 * the service collection
	 */
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

	/*
	 * The Service Types Only used in internally
	 */
	private enum ServiceType
	{
		Transient, Scoped, Singleton
	}
}
