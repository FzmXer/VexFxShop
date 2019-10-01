package FzmXer.VexFxShop.NBTUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

public class NBTUtils {
	private static Class<?> NMS_ITEM_STACK;
	public static Class<?> NBT_TAG_LIST;
	public static Class<?> NBT_TAG_COMPOUND;
	
	private static String version;
	private static Method asNMSCopyMethod;
	private static Method SAVE_NBT;
	private static Method MOJANGSON_TO_NBT;
	private static Method asBukkitCopyMethod;
	private static Constructor<?> NMS_ITEM_STACK_CONSTRUCTOR_WITH_NBT;
	private static Method NMS_ITEM_STACK_METHOD_WITH_NBT;

	static {
		
		version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
		
		try {
			NMS_ITEM_STACK = getNMSClass("ItemStack");
			NBT_TAG_COMPOUND = getNMSClass("NBTTagCompound");
			NBT_TAG_LIST = getNMSClass("NBTTagList");
			NMS_ITEM_STACK_CONSTRUCTOR_WITH_NBT = getConstructor(NMS_ITEM_STACK, NBT_TAG_COMPOUND);
			NMS_ITEM_STACK_METHOD_WITH_NBT = ReflectionUtils.getMethod(NMS_ITEM_STACK, "createStack", NBT_TAG_COMPOUND);
			
			SAVE_NBT = ReflectionUtils.getMethod(NMS_ITEM_STACK, "save", NBT_TAG_COMPOUND);
			
			asBukkitCopyMethod = ReflectionUtils.getMethod(getOBCClass("inventory.CraftItemStack"), "asBukkitCopy",
					getNMSClass("ItemStack"));
			asNMSCopyMethod = ReflectionUtils.getMethod(getOBCClass("inventory.CraftItemStack"), "asNMSCopy",
					ItemStack.class);
			MOJANGSON_TO_NBT = ReflectionUtils.getMethod(getNMSClass("MojangsonParser"), "parse", String.class);
		} catch (Exception var1) {
			var1.printStackTrace();
		}

	}

	public static Constructor<?> getConstructor(Class<?> clazz, Class<?>... parameterTypes) throws NoSuchMethodException {
		Constructor<?> constructor = null;
		if (hasConstructor(clazz, parameterTypes)) {
			constructor = clazz.getDeclaredConstructor(parameterTypes);
		}

		return constructor;
	}

	public static boolean hasConstructor(Class<?> clazz, Class<?>... parameterTypes) {
		boolean has;
		try {
			clazz.getDeclaredConstructor(parameterTypes);
			has = true;
		} catch (NoSuchMethodException var4) {
			has = false;
		}

		return has;
	}

	public static ItemStack loadItemStackJson(String mojangson) {
		ItemStack itemStack = null;

		try {
			Object nbtTag = ReflectionUtils.invokeMethod(MOJANGSON_TO_NBT, null, mojangson);
			Object nmsItem = newNMSItemStack(nbtTag);
			itemStack = (ItemStack) getBukkitItem(nmsItem);
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return itemStack;
	}

	public static Object newNMSItemStack(Object nbtTagCompound) {
		String version = getVersion();
		int subVersion = Integer.valueOf(version.split("_")[1]);

		try {
			return subVersion >= 11 ? instantiateObject(NMS_ITEM_STACK_CONSTRUCTOR_WITH_NBT, nbtTagCompound)
					: ReflectionUtils.invokeMethod((Method) NMS_ITEM_STACK_METHOD_WITH_NBT, (Object) null,
							nbtTagCompound);
		} catch (Exception var4) {
			var4.printStackTrace();
			return null;
		}
	}

	public static Object getBukkitItem(Object nmsItem) {
		if (asBukkitCopyMethod == null) {
			try {
				asNMSCopyMethod = ReflectionUtils.getMethod(getOBCClass("inventory.CraftItemStack"), "asNMSCopy",
						ItemStack.class);
			} catch (Exception var3) {
				var3.printStackTrace();
			}
		}

		try {
			return ReflectionUtils.invokeMethod((Method) asBukkitCopyMethod, (Object) null, nmsItem);
		} catch (Exception var2) {
			var2.printStackTrace();
			return null;
		}
	}

	public static String getItemStackJson(ItemStack items) {
		Object savedTag = saveItemNBT(items);
		return savedTag != null ? savedTag.toString() : "null";
	}

	public static Object saveItemNBT(Object item) {
		Object pendingItem = item;
		Object savedTag = null;
		if (item instanceof ItemStack) {
			pendingItem = getNMSItem((ItemStack) item);
		}

		try {
			savedTag = ReflectionUtils.invokeMethod(SAVE_NBT, pendingItem, newNBTTagCompound());
		} catch (Exception var4) {
			var4.printStackTrace();
		}

		return savedTag;
	}

	public static Object getNMSItem(ItemStack itemStack) {
		if (asNMSCopyMethod == null) {
			Class<?> craftItemStack = getOBCClass("inventory.CraftItemStack");

			try {
				SAVE_NBT = ReflectionUtils.getMethod(NMS_ITEM_STACK, "save", NBT_TAG_COMPOUND);
				NBT_TAG_COMPOUND = getNMSClass("NBTTagCompound");
				asNMSCopyMethod = ReflectionUtils.getMethod(craftItemStack, "asNMSCopy", ItemStack.class);
			} catch (Exception var4) {
				var4.printStackTrace();
			}
		}

		try {
			return ReflectionUtils.invokeMethod((Method) asNMSCopyMethod, (Object) null, itemStack);
		} catch (Exception var3) {
			var3.printStackTrace();
			return itemStack;
		}
	}

	public static Class<?> getOBCClass(String className) {
		try {
			return Class.forName("org.bukkit.craftbukkit." + getVersion() + "." + className);
		} catch (ClassNotFoundException var2) {
			var2.printStackTrace();
			return null;
		}
	}

	public static String getVersion() {
		return version;
	}

	public static Object newNBTTagCompound() {
		try {
			return instantiateObject(NBT_TAG_COMPOUND.getDeclaredConstructor());
		} catch (Exception var1) {
			var1.printStackTrace();
			return null;
		}
	}

	public static Object newNBTTagList() {
		try {
			return instantiateObject(NBT_TAG_LIST.getDeclaredConstructor());
		} catch (Exception var1) {
			var1.printStackTrace();
			return null;
		}
	}

	public static Class<?> getNMSClass(String className) {
		try {
			return Class.forName("net.minecraft.server." + version + "." + className);
		} catch (Exception var2) {
			System.out.println("错误: " + var2.getMessage());
			return null;
		}
	}

	public static Object instantiateObject(Constructor<?> constructor, Object... arguments)
			throws IllegalAccessException, InvocationTargetException, InstantiationException {
		return constructor.newInstance(arguments);
	}
}
