package quek.ibc;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.*;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(IBCMod.MODID)
@Mod.EventBusSubscriber(modid = IBCMod.MODID)
public class IBCMod {

    public static final String MODID = "ibc";

    public static final Logger logger = LogManager.getLogger(MODID);

    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);

    private static final RegistryObject<Item> BEAN_CAN = ITEMS.register("inconspicuous_bean_can", () -> new BeanCanItem((new Item.Properties())
            .group(ItemGroup.FOOD)
            .food(new Food.Builder()
                    .hunger(2)
                    .saturation(2.4F)
                    .effect(() -> new EffectInstance(Effects.REGENERATION, 600, 2), 1.0F)
                    .effect(() -> new EffectInstance(Effects.STRENGTH, 600, 2), 1.0F)
                    .effect(() -> new EffectInstance(Effects.GLOWING, 600, 0), 1.0F)
                    .effect(() -> new EffectInstance(Effects.SPEED, 600, 2), 1.0F)
                    .effect(() -> new EffectInstance(Effects.JUMP_BOOST, 600, 2), 1.0F)
                    .effect(() -> new EffectInstance(Effects.SLOW_FALLING, 600, 0), 1.0F)
                    .meat()
                    .setAlwaysEdible()
                    .build()
            )
    ));

    public IBCMod() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ITEMS.register(bus);
    }

    @SubscribeEvent
    public static void lootTableLoad(LootTableLoadEvent event) {
        if(event.getName().getPath().contains("chests")) {
            event.getTable().addPool(LootPool.builder().addEntry(TableLootEntry.builder(new ResourceLocation(MODID, "blocks/ibc_chest")).weight(1)).name("ibc_bean_can").build());
        }
    }

    private static class BeanCanItem extends Item {
        public BeanCanItem(Properties properties) {
            super(properties);
        }

        @Override
        public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {
            super.onItemUseFinish(stack, worldIn, entityLiving);
            PlayerEntity player = (PlayerEntity) entityLiving;
            player.dropItem(player.inventory.armorInventory.get(0), true, true);
            player.dropItem(player.inventory.armorInventory.get(1), true, true);
            player.dropItem(player.inventory.armorInventory.get(2), true, true);
            player.dropItem(player.inventory.armorInventory.get(3), true, true);
            player.inventory.armorInventory.clear();
            return stack;
        }
    }
}
