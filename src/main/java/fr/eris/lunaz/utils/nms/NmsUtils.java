package fr.eris.lunaz.utils.nms;

import net.minecraft.world.entity.EntityInsentient;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.ai.attributes.GenericAttributes;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.command.CommandMap;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftLivingEntity;
import org.bukkit.entity.LivingEntity;

import java.lang.reflect.Field;
import java.util.UUID;

public class NmsUtils {
    public static CommandMap getCommandMap() {
        try {
            Field commandMapField = commandMapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            return (CommandMap) commandMapField.get(Bukkit.getServer());
        } catch(NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static void changeEntityMovementSpeed(LivingEntity entity, double speedValue) {
        EntityLiving nmsEntity = ((CraftLivingEntity) entity).getHandle();
        AttributeInstance attributes = nmsEntity.craftAttributes.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);
        AttributeModifier modifier = new AttributeModifier(
                UUID.fromString("206a89dc-ae78-4c4d-b42c-3b31db3f5a7c"), "<plugin_name> movement speed multiplier", speedValue, AttributeModifier.Operation.ADD_NUMBER);
        if(attributes == null) {
            System.out.println("Attribute is null");
        }
        attributes.removeModifier(modifier);
        attributes.addModifier(modifier);
    }
}
