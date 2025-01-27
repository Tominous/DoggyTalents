package doggytalents.helper;

import java.util.Comparator;
import java.util.Iterator;
import java.util.function.Predicate;

import doggytalents.ModItems;
import doggytalents.entity.EntityDog;
import doggytalents.item.ItemChewStick;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class DogUtil {

	public static void teleportDogToOwner(Entity owner, Entity entity, World world, PathNavigator pathfinder, int radius) {
        teleportDogToPos(owner.posX, owner.getBoundingBox().minY, owner.posZ, entity, world, pathfinder, radius);
    }
	
	public static void teleportDogToOwner(Entity owner, Entity entity, World world, PathNavigator pathfinder) {
        teleportDogToPos(owner.posX, owner.getBoundingBox().minY, owner.posZ, entity, world, pathfinder, 2);
    }
	
	public static void teleportDogToPos(double x, double y, double z, Entity entity, World world, PathNavigator pathfinder, int radius) {
    	int i = MathHelper.floor(x) - radius;
        int j = MathHelper.floor(z) - radius;
        int k = MathHelper.floor(y);

        for(int l = 0; l <= radius * 2; ++l) {
            for(int i1 = 0; i1 <= radius * 2; ++i1) {
                if((l < 1 || i1 < 1 || l > radius * 2 - 1 || i1 > radius * 2 - 1) && isTeleportFriendlyBlock(entity, world, i, j, k, l, i1)) {
                	entity.setLocationAndAngles((double)((float)(i + l) + 0.5F), (double)k, (double)((float)(j + i1) + 0.5F), entity.rotationYaw, entity.rotationPitch);
                    pathfinder.clearPath();
                    return;
                }
            }
        }
    }
	
	public static boolean isTeleportFriendlyBlock(Entity entity, World world, int xBase, int zBase, int y, int xAdd, int zAdd) {
		BlockPos blockpos = new BlockPos(xBase + xAdd, y - 1, zBase + zAdd);
		BlockState iblockstate = world.getBlockState(blockpos);
		return Block.func_220056_d(iblockstate, world, blockpos, Direction.DOWN) && iblockstate.canEntitySpawn(world, blockpos, entity.getType()) && world.isAirBlock(blockpos.up()) && world.isAirBlock(blockpos.up(2));
	}
    
    public static ItemStack feedDog(EntityDog dog, IInventory inventory, int slotIndex) {
        if(!inventory.getStackInSlot(slotIndex).isEmpty()) {
            ItemStack itemstack = inventory.getStackInSlot(slotIndex);
            dog.setDogHunger(dog.getDogHunger() + dog.foodValue(itemstack));
            
            if(itemstack.getItem() == ModItems.CHEW_STICK) { //TODO add player paramater
            	((ItemChewStick)ModItems.CHEW_STICK).addChewStickEffects(dog);
            }

            if(inventory.getStackInSlot(slotIndex).getCount() <= 1) {
                ItemStack itemstack1 = inventory.getStackInSlot(slotIndex);
                inventory.setInventorySlotContents(slotIndex, ItemStack.EMPTY);
                return itemstack1;
            }

            ItemStack itemstack2 = inventory.getStackInSlot(slotIndex).split(1);

            if(inventory.getStackInSlot(slotIndex).isEmpty())
            	inventory.setInventorySlotContents(slotIndex, ItemStack.EMPTY);
            else
            	inventory.markDirty();

            return itemstack2;
        }
        else
            return ItemStack.EMPTY;
    }
    
    public static boolean doesInventoryContainFood(EntityDog dog, IInventory inventory) {
        for(int i = 0; i < inventory.getSizeInventory(); i++) {
            if(dog.foodValue(inventory.getStackInSlot(i)) > 0)
            	return true;
        }

        return false;
    }
    
    public static int getFirstSlotWithFood(EntityDog dog, IInventory inventory) {
    	 for(int i = 0; i < inventory.getSizeInventory(); i++) {
             if(dog.foodValue(inventory.getStackInSlot(i)) > 0)
             	return i;
         }

        return -1;
    }
    
    public static ItemStack addItem(IInventory inventory, ItemStack stack) {
    	if(stack.isEmpty()) return ItemStack.EMPTY;
    	
        ItemStack itemstack = stack.copy();

        for(int i = 0; i < inventory.getSizeInventory(); i++) {
            ItemStack itemstack1 = inventory.getStackInSlot(i);

            if(itemstack1.isEmpty()) {
            	inventory.setInventorySlotContents(i, itemstack);
            	inventory.markDirty();
                return ItemStack.EMPTY;
            }

            if(ItemStack.areItemsEqual(itemstack1, itemstack)) {
                int j = Math.min(inventory.getInventoryStackLimit(), itemstack1.getMaxStackSize());
                int k = Math.min(itemstack.getCount(), j - itemstack1.getCount());

                if(k > 0) {
                	itemstack1.grow(k);
                	itemstack.shrink(k);

                    if(itemstack.isEmpty()) {
                    	inventory.markDirty();
                        return ItemStack.EMPTY;
                    }
                }
            }
        }

        if(itemstack.getCount() != stack.getCount())
        	inventory.markDirty();

        return itemstack;
    }
    
    public static boolean isHolding(Entity entity, Item item, Predicate<CompoundNBT> nbtPredicate) {
		return isHolding(entity, stack -> stack.getItem() == item && stack.hasTag() && nbtPredicate.test(stack.getTag()));
	}
    
    public static boolean isHolding(Entity entity, Item item) {
		return isHolding(entity, stack -> stack.getItem() == item);
	}
    
	public static boolean isHolding(Entity entity, Predicate<ItemStack> matcher) {
		if(entity == null) {
			return false;
		}
		
		Iterator<ItemStack> heldItems = entity.getHeldEquipment().iterator();
		while(heldItems.hasNext()) {
			ItemStack stack = heldItems.next();
			if(matcher.test(stack))
				return true;
		}
		
		return false;
	}
	
	public static float[] rgbIntToFloatArray(int rgbInt) {
		int r = (rgbInt >> 16) & 255;
        int g = (rgbInt >> 8) & 255;
        int b = (rgbInt >> 0) & 255;

        return new float[] {(float)r / 255F, (float)g / 255F, (float)b / 255F};
	}
	
	public static int[] rgbIntToIntArray(int rgbInt) {
		int r = (rgbInt >> 16) & 255;
        int g = (rgbInt >> 8) & 255;
        int b = (rgbInt >> 0) & 255;

        return new int[] {r, g, b};
	}
	
	public static class Sorter implements Comparator<Entity> {
		private final Entity entity;

		public Sorter(Entity entityIn) {
			this.entity = entityIn;
		}

		@Override
		public int compare(Entity entity1, Entity entity2) {
			double d0 = this.entity.getDistanceSq(entity1);
			double d1 = this.entity.getDistanceSq(entity2);
			if(d0 < d1) {
				return -1;
			} else {
				return d0 > d1 ? 1 : 0;
			}
		}
	}
}
