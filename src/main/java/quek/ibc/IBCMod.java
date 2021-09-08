package quek.ibc;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootTableReference;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fmllegacy.RegistryObject;
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
            .rarity(Rarity.EPIC)
            .tab(CreativeModeTab.TAB_FOOD)
            .food(new FoodProperties.Builder()
                    .nutrition(2)
                    .saturationMod(2.4F)
                    .effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 600, 2), 1.0F)
                    .effect(() -> new MobEffectInstance(MobEffects.DAMAGE_BOOST, 600, 2), 1.0F)
                    .effect(() -> new MobEffectInstance(MobEffects.GLOWING, 600, 0), 1.0F)
                    .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 600, 2), 1.0F)
                    .effect(() -> new MobEffectInstance(MobEffects.JUMP, 600, 2), 1.0F)
                    .effect(() -> new MobEffectInstance(MobEffects.SLOW_FALLING, 600, 0), 1.0F)
                    .meat()
                    .alwaysEat()
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
            event.getTable().addPool(LootPool.lootPool().add(LootTableReference.lootTableReference(new ResourceLocation(MODID, "blocks/ibc_chest")).setWeight(1)).name("ibc_bean_can").build());
        }
    }

    private static class BeanCanItem extends Item {
        public BeanCanItem(Properties properties) {
            super(properties);
        }

        @Override
        public ItemStack finishUsingItem(ItemStack stack, Level worldIn, LivingEntity entityLiving) {
            super.finishUsingItem(stack, worldIn, entityLiving);
            Player player = (Player) entityLiving;
            player.drop(player.getInventory().armor.get(0), true, true);
            player.drop(player.getInventory().armor.get(1), true, true);
            player.drop(player.getInventory().armor.get(2), true, true);
            player.drop(player.getInventory().armor.get(3), true, true);
            player.getInventory().armor.clear();
            return stack;
        }
    }
}
