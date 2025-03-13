package lunadev.main;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ArmorStandEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.glfw.GLFW;

import java.util.HashSet;
import java.util.Set;

@Mod("ddstands")
public class DDStands {
    private boolean enabled = true;
    private boolean messageShown = false;
    private final Set<Entity> hiddenArmorStands = new HashSet<>();
    private final KeyBinding toggleKey = new KeyBinding("key.ddstands.toggle", GLFW.GLFW_KEY_G, "category.ddstands");

    public DDStands() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onClientSetup(FMLClientSetupEvent event) {
        ClientRegistry.registerKeyBinding(toggleKey);
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (toggleKey.consumeClick()) {
            enabled = !enabled;
            Minecraft.getInstance().player.sendMessage(new StringTextComponent("DDStands " + (enabled ? "enabled" : "disabled")), Minecraft.getInstance().player.getUUID());
            if (enabled) {
                hideArmorStands();
            } else {
                showArmorStands();
            }
        }
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (!messageShown && Minecraft.getInstance().player != null) {
            Minecraft.getInstance().player.sendMessage(new StringTextComponent("Created by @TgLunatik - https://github.com/DDAredW"), Minecraft.getInstance().player.getUUID());
            messageShown = true;
        }
    }

    private void hideArmorStands() {
        hiddenArmorStands.clear();
        for (Entity entity : Minecraft.getInstance().level.entitiesForRendering()) {
            if (entity instanceof ArmorStandEntity) {
                entity.setInvisible(true);
                hiddenArmorStands.add(entity);
            }
        }
    }

    private void showArmorStands() {
        for (Entity entity : hiddenArmorStands) {
            entity.setInvisible(false);
        }
        hiddenArmorStands.clear();
    }
}
