package FzmXer.VexFxShop.NBTUtils;

import java.lang.reflect.Constructor;

public interface ConstructorFilter {
    boolean accept(Constructor<?> constructor);

}
