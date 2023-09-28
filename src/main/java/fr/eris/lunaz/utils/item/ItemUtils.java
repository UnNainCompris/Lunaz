package fr.eris.lunaz.utils.item;

import fr.eris.lunaz.utils.StringUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ItemUtils {

    /**
     * Use to parse an instance to ItemStack
     * Current available :
     * String, Boolean, Number, Tuple, Material
     * @param object the object to parse in item
     * @return the item parsed
     */
    public static ItemStack typeToItem(Object object) {
        if(object instanceof Boolean) {
            boolean bool = (boolean) object;
            if(bool) return new ItemBuilder().setMaterial(Material.GREEN_WOOL).build();
            else return new ItemBuilder().setMaterial(Material.RED_WOOL).build();
        } else if(object instanceof String) {
            final List<String> lore = new ArrayList<>(StringUtils.prettySplit((String) object, true, 50));
            return new ItemBuilder().setMaterial(Material.BOOK).setLore(lore.toArray(new String[]{})).build();
        } else if(object instanceof Number) {
            Number value = (Number) object;
            Material material = Material.REDSTONE_TORCH;
            if(object instanceof Byte)
                return new ItemBuilder().setMaterial(material).setLore("Value: " + value.byteValue() + " [Byte]").build();
            else if(object instanceof Long)
                return new ItemBuilder().setMaterial(material).setLore("Value: " + value.longValue() + " [Long]").build();
            else if(object instanceof Integer)
                return new ItemBuilder().setMaterial(material).setLore("Value: " + value.intValue() + " [Integer]").build();
            else if(object instanceof Double)
                return new ItemBuilder().setMaterial(material).setLore("Value: " + value.doubleValue() + " [Double]").build();
            else if(object instanceof Float)
                return new ItemBuilder().setMaterial(material).setLore("Value: " + value.floatValue() + " [Float]").build();
            else if(object instanceof Short)
                return new ItemBuilder().setMaterial(material).setLore("Value: " + value.shortValue() + " [Short]").build();
        } else if(object instanceof Material) {
            Material value = (Material) object;
            return new ItemBuilder().setMaterial(value).setLore("Value: " + value.name().toLowerCase() + " [Material]").build();
        }
        return new ItemBuilder().setMaterial(Material.COOKED_BEEF).setLore("Value: " + object.toString() + " [" + object.getClass().getName() + "]").build();
    }

}
