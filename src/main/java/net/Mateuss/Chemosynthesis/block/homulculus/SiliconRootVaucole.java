package net.Mateuss.Chemosynthesis.block.homulculus;

import net.Mateuss.Chemosynthesis.core.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;

public class SiliconRootVaucole extends BaseOrganelleRoot {

    public SiliconRootVaucole(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void spawnOrganelle(ServerLevel level, BlockPos pos) {
        organelle = ModEntities.HOMUNCULUS_VAUCOLE.get().create(level);
        if(organelle != null) {
            organelle.moveTo(pos.getX()+0.5d, pos.getY()+1, pos.getZ()+0.5d);
            level.addFreshEntity(organelle);
            level.playSound(null, pos, SoundEvents.MUD_BREAK, SoundSource.HOSTILE);
        }
    }
}
