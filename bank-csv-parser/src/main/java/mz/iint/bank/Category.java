package mz.iint.bank;

import java.util.HashMap;
import java.util.Map;

public class Category {
    private Map<String, Integer> categories = new HashMap<>();

    public Integer get(String key) {
        try {
            /* Si la clave es numerica entonces devuelvo la clave misma */
            final Integer icat = Integer.parseInt(key.trim());
            return icat;
        } catch (NumberFormatException e) { }

        /* Si la clave existe en el mapa, devuelvo su categoria */
        if (categories.containsKey(key)) return categories.get(key);

        /* Sino agrego una nueva categoria al mapa y la devuelvo */
        int newCat = categories.size() + 1;
        categories.put(key, newCat);
        return newCat;
    }

    public static Category[] buildCategories(int size) {
        final Category[] categories = new Category[size];
        for (int i = 0; i < categories.length; i++) categories[i] = new Category();
        return categories;
    }
}
