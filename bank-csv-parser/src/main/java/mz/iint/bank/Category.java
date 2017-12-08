package mz.iint.bank;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Category {
    private Map<String, Integer> numberCategories = new LinkedHashMap<>();
    private Map<String, Integer> textCategories = new LinkedHashMap<>();
    private boolean mixed;

    public Category(boolean mixed) {
        this.mixed = mixed;
    }

    public Integer get(String key) {
        key = key == null ? "" : key.trim();

        /* Si la clave existe en alguno de los mapas de categorias, devuelvo el valor */
        if (numberCategories.containsKey(key)) return numberCategories.get(key);
        if (textCategories.containsKey(key)) return textCategories.get(key);

        /* valores yes y no tienen categorias pre asignadas */
        switch (key) {
            case "yes":
                textCategories.put("yes", 1);
                return 1;
            case "no":
                textCategories.put("no", 2);
                return 2;
        }

        if ("gt2".equals(key)) {
            textCategories.put("gt2", 3);
            return 3;
        }

        switch (key) {
            case "primary":
                textCategories.put("primary", 1);
                return 1;
            case "secondary":
                textCategories.put("secondary", 2);
                return 2;
            case "tertiary":
                textCategories.put("tertiary", 3);
                return 3;
        }

        switch (key) {
            case "mon":
                textCategories.put("mon", 1);
                return 1;
            case "tue":
                textCategories.put("tue", 2);
                return 2;
            case "wed":
                textCategories.put("wed", 3);
                return 3;
            case "thu":
                textCategories.put("thu", 4);
                return 4;
            case "fri":
                textCategories.put("fri", 5);
                return 5;
        }

        switch (key) {
            case "autum":
                textCategories.put("autum", 1);
                return 1;
            case "winter":
                textCategories.put("winter", 2);
                return 2;
            case "spring":
                textCategories.put("spring", 3);
                return 3;
            case "summer":
                textCategories.put("summer", 4);
                return 4;
        }

        try {
            /* Si la clave es numerica entonces clave y valor de categoria seran iguales*/
            final Integer icat = Integer.parseInt(key);
            /* Marco la categoria como mixta */
            mixed = true;
            numberCategories.put("" + icat, icat);
            return icat;
        } catch (NumberFormatException e) {
            /* Si la categoria no es numerica entonces es de texto */
            /* Obtengo el valor de la categoria nueva. Si la categoria es mixta (numeros y letras) entonces asigno los valores > 1000 a texto y dejo los numeros como estan */
            int newCat = mixed ? textCategories.size() + 100 : textCategories.size() + 1;
            textCategories.put(key, newCat);
            return newCat;
        }
    }

    private static boolean isMixed(int i, Integer[] mixedCategories) {
        for (int item : mixedCategories) {
            if (i == item) return true;
        }
        return false;
    }

    /**
     * Construye un arreglo de categorias.
     *
     * @param size            Cantidad de categorias.
     * @param mixedCategories Indices de categorias mixtas (numeros y texto) [comienza en 0]
     * @return Arreglo de categorias.
     */
    public static Category[] buildCategories(int size, Integer[] mixedCategories) {
        final Category[] categories = new Category[size];
        for (int i = 0; i < categories.length; i++) {
            categories[i] = new Category(isMixed(i, mixedCategories));
        }
        return categories;
    }

    public Map<String, Integer> getCategories() {
        Map<String, Integer> map = new LinkedHashMap<>();
        if (!numberCategories.isEmpty()) map.putAll(numberCategories);
        if (!textCategories.isEmpty()) map.putAll(textCategories);
        return map;
    }
}
