package net.iirc.simpleutils.blocks.entity;

import net.iirc.simpleutils.blocks.custom.OreAmplifierBlock;
import net.iirc.simpleutils.networking.ModMessages;
import net.iirc.simpleutils.networking.packet.EnergySyncS2CPacket;
import net.iirc.simpleutils.networking.packet.ItemStackSyncS2CPacket;
import net.iirc.simpleutils.screen.OreAmplifierMenu;
import net.iirc.simpleutils.util.ModEnergyStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class OreAmplifierBlockEntity extends BlockEntity implements MenuProvider {
    private final ModEnergyStorage ENERGY_STORAGE = new ModEnergyStorage(5000, 512) {
        @Override
        public void onEnergyChanged() {
            setChanged();
            ModMessages.sendToClients(new EnergySyncS2CPacket(this.energy, getBlockPos()));
        }
    };


    private final ItemStackHandler itemHandler = new ItemStackHandler(3){
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();

            if(!level.isClientSide()){
                ModMessages.sendToClients(new ItemStackSyncS2CPacket(this, worldPosition));
            }
        }
    };


    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();
    private LazyOptional<IEnergyStorage> lazyEnergyHandler = LazyOptional.empty();


    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 80;

    private static final int ENERGY_REQ = 32;


    private final Map<Direction, LazyOptional<WrappedHandler>> directionWrappedHandlerMap =
            Map.of(Direction.DOWN, LazyOptional.of(() -> new WrappedHandler(itemHandler, (i) -> i == 2, (i, s) -> false)),
                    Direction.NORTH, LazyOptional.of(() -> new WrappedHandler(itemHandler, (index) -> index == 1,
                            (index, stack) -> itemHandler.isItemValid(1, stack))),
                    Direction.SOUTH, LazyOptional.of(() -> new WrappedHandler(itemHandler, (i) -> i == 2, (i, s) -> false)),
                    Direction.EAST, LazyOptional.of(() -> new WrappedHandler(itemHandler, (i) -> i == 1,
                            (index, stack) -> itemHandler.isItemValid(1, stack))),
                    Direction.WEST, LazyOptional.of(() -> new WrappedHandler(itemHandler, (index) -> index == 0 || index == 1,
                            (index, stack) -> itemHandler.isItemValid(0, stack) || itemHandler.isItemValid(1, stack))));

    public OreAmplifierBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.ORE_AMPLIFIER.get(), pPos, pBlockState);

        this.data = new ContainerData() {
            @Override
            public int get(int pIndex) {
                return switch (pIndex){
                    case 0 -> OreAmplifierBlockEntity.this.progress;
                    case 1 -> OreAmplifierBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int pIndex, int pValue) {
                switch (pIndex) {
                    case 0 -> OreAmplifierBlockEntity.this.progress = pValue;
                    case 1 -> OreAmplifierBlockEntity.this.maxProgress = pValue;
                }
            }

            @Override
            public int getCount() {
                return 3;
            }
        };
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
        lazyEnergyHandler = LazyOptional.of(() -> ENERGY_STORAGE);
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
    }


    public static void tick(Level level, BlockPos pos, BlockState state, OreAmplifierBlockEntity pEntity){
        if(level.isClientSide()) {
            return;
        }

        //Check for recipe, Check for fuel
        //Increase progress by 1, update state

        if(hasRawOreInFirstSlot(pEntity)){

        }else{
            pEntity.resetProgress();
            setChanged(level, pos, state);
        }
    }

    private static boolean hasRawOreInFirstSlot(OreAmplifierBlockEntity pEntity) {
        return pEntity.itemHandler.getStackInSlot(0).is(Tags.Items.RAW_MATERIALS);
    }

    private void resetProgress() {
        this.progress = 0;
    }


    @Override
    public Component getDisplayName() {
        return Component.literal("Ore Amplifier");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        ModMessages.sendToClients(new EnergySyncS2CPacket(this.getEnergyStorage().getEnergyStored(), getBlockPos()));
        return new OreAmplifierMenu(pContainerId, pPlayerInventory, this, this.data);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == ForgeCapabilities.ENERGY){
            return lazyEnergyHandler.cast();
        }

        if(cap == ForgeCapabilities.ITEM_HANDLER) {
            if(side == null){
                return lazyItemHandler.cast();
            }

            if(directionWrappedHandlerMap.containsKey(side)){
                Direction localDir = this.getBlockState().getValue(OreAmplifierBlock.FACING);

                if(side == Direction.UP || side == Direction.DOWN){
                    return directionWrappedHandlerMap.get(side).cast();
                }

                return switch(localDir){
                    default -> directionWrappedHandlerMap.get(side.getOpposite()).cast();
                    case EAST -> directionWrappedHandlerMap.get(side.getClockWise()).cast();
                    case SOUTH -> directionWrappedHandlerMap.get(side).cast();
                    case WEST -> directionWrappedHandlerMap.get(side.getCounterClockWise()).cast();
                };
            }
        }

        return super.getCapability(cap, side);
    }

    public void setHandler(ItemStackHandler itemStackHandler) {
        for (int i = 0; i < itemStackHandler.getSlots(); i++) {
            itemHandler.setStackInSlot(i, itemStackHandler.getStackInSlot(i));
        }
    }

    private static void extractEnergy(OreAmplifierBlockEntity pEntity) {
        pEntity.ENERGY_STORAGE.extractEnergy(ENERGY_REQ, false);
    }

    private static boolean hasEnoughEnergy(OreAmplifierBlockEntity pEntity) {
        return pEntity.ENERGY_STORAGE.getEnergyStored() >= ENERGY_REQ * pEntity.maxProgress;
    }

    public IEnergyStorage getEnergyStorage() {
        return ENERGY_STORAGE;
    }

    public void setEnergyLevel(int energy) {
        ENERGY_STORAGE.setEnergy(energy);
    }
}
